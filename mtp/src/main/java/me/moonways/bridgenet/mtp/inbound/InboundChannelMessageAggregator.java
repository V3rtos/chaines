package me.moonways.bridgenet.mtp.inbound;

import io.netty.buffer.*;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

import io.netty.buffer.ByteBuf;

public class InboundChannelMessageAggregator extends MessageAggregator<ByteBuf, ByteBuf, ByteBufHolder, ByteBufHolder> {

    public InboundChannelMessageAggregator(int maxContentLength) {
        super(maxContentLength);
    }

    @Override
    protected boolean isStartMessage(ByteBuf msg) throws Exception {
        // Определение, является ли сообщение началом агрегируемого сообщения
        return msg.readableBytes() > 0;
    }

    @Override
    protected boolean isContentMessage(ByteBuf msg) throws Exception {
        // Определение, является ли сообщение частью агрегируемого сообщения
        return !isLastContentMessage((ByteBufHolder) msg);
    }

    @Override
    protected boolean isLastContentMessage(ByteBufHolder msg) throws Exception {
        // Определение, является ли сообщение последней частью агрегируемого сообщения
        return msg.content().readableBytes() > 0;
    }

    @Override
    protected boolean isAggregated(ByteBuf msg) throws Exception {
        // Определение, является ли сообщение уже агрегированным
        return isStartMessage(msg) && isLastContentMessage((ByteBufHolder) msg);
    }

    @Override
    protected boolean isContentLengthInvalid(ByteBuf start, int maxContentLength) throws Exception {
        // Проверка допустимой длины контента
        return start.readableBytes() > maxContentLength;
    }

    @Override
    protected Object newContinueResponse(ByteBuf start, int maxContentLength, ChannelPipeline pipeline) throws Exception {
        // Возвращает null, так как продолжение агрегации не требует отдельного ответа
        return null;
    }

    @Override
    protected boolean closeAfterContinueResponse(Object msg) throws Exception {
        // Определяет, нужно ли закрывать соединение после ответа на продолжение
        return false;
    }

    @Override
    protected boolean ignoreContentAfterContinueResponse(Object msg) throws Exception {
        // Определяет, нужно ли игнорировать содержимое после ответа на продолжение
        return false;
    }

    @Override
    protected ByteBufHolder beginAggregation(ByteBuf start, ByteBuf content) throws Exception {
        // Начало агрегации
        CompositeByteBuf aggregated = start.alloc().compositeBuffer();
        aggregated.addComponent(true, start.retain());
        aggregated.addComponent(true, content.retain());
        return new DefaultByteBufHolder(aggregated);
    }

    @Override
    protected void aggregate(ByteBufHolder aggregated, ByteBufHolder content) throws Exception {
        // Агрегация сообщений
        CompositeByteBuf compositeBuf = (CompositeByteBuf) aggregated.content();
        compositeBuf.addComponent(true, content.content().retain());
    }

    @Override
    protected void finishAggregation(ByteBufHolder aggregated) throws Exception {
        // Завершение агрегации, если требуется
        // Можно добавлять любые дополнительные шаги по завершению агрегации
    }
}