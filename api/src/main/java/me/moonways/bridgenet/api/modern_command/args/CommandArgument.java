package me.moonways.bridgenet.api.modern_command.args;

import me.moonways.bridgenet.api.util.ExceptionallyFunction;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Данный интерфейс описывает функционал работы
 * с данными массива.
 */
public interface CommandArgument extends Iterable<String> {

    /**
     * Вхождение в стрим массива.
     */
    Stream<String> stream();

    /**
     * Получение массива.
     */
    String[] toStringArray();

    /**
     * Получение элемента из массива.
     *
     * @param position - позиция.
     */
    Optional<String> get(int position);

    /**
     * Получение преобразованной строки массива.
     *
     * @param position - позиция.
     * @param mapper - функция.
     * @param <R> - требуемый тип.
     */
    <R> Optional<R> get(int position, ExceptionallyFunction<String, R> mapper);

    /**
     * Получение первого элемента массива.
     */
    Optional<String> first();

    /**
     * Получение преобразованного первого элемента массива.
     *
     * @param mapper - функция.
     * @return - преобразованный тип первого элемента.
     * @param <R> - требуемый тип.
     */
    <R> Optional<R> first(ExceptionallyFunction<String, R> mapper);

    /**
     * Получение последнего элемента массива.
     */
    Optional<String> last();

    /**
     * Получение преобразованного последнего элемента массива.
     *
     * @param mapper - функция.
     * @param <R> - требуемый тип.
     */
    <R> Optional<R> last(ExceptionallyFunction<String, R> mapper);

    /**
     * Проверка является ли массив пустым.
     */
    boolean isEmpty();

    /**
     * Получение размера массива.
     */
    int size();

    /**
     * Уменьшение массива.
     *
     * @param start - начальная позиция уменьшения.
     */
    void trim(int start);

    /**
     * Валидация размера массива.
     *
     * @param required - запрашиваемый размер.
     */
    void assertSize(int required);

    /**
     * Получить главное имя команды.
     */
    String getCommandName();

    /**
     * Проверка на существование строки массива под индексом.
     *
     * @param requiredSize - запрашиваемый индекс.
     */
    boolean has(int requiredSize);
}
