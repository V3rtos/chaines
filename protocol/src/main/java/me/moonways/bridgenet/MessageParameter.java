package me.moonways.bridgenet;

import lombok.Getter;
import lombok.Setter;

public class MessageParameter {

    @Getter
    @Setter
    private boolean callback = false;

    public static class Builder {

        private final MessageParameter messageParameter = new MessageParameter();

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setCallback(boolean flag) {
            messageParameter.setCallback(flag);
            return this;
        }

        public MessageParameter build() {
            return messageParameter;
        }
    }
}
