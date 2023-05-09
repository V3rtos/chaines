package me.moonways.bridgenet.message;

import lombok.Getter;
import lombok.Setter;

public class MessageParameter {

    @Getter
    @Setter
    private boolean callback = false;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private final MessageParameter messageParameter = new MessageParameter();

        public Builder setCallback(boolean flag) {
            messageParameter.setCallback(flag);
            return this;
        }

        public MessageParameter build() {
            return messageParameter;
        }
    }
}
