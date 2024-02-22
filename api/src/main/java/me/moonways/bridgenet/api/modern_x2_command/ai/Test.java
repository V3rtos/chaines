package me.moonways.bridgenet.api.modern_x2_command.ai;

import me.moonways.bridgenet.api.modern_command.Help;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.ai.validate.AICommandValidateResult;

public class Test extends AICommandHandler<Help> {

    public Test(Class<Help> annotationType) {
        super(annotationType);
    }

    @Override
    public void load(AICommandContext<Help> context) {
    }

    @Override
    public AICommandValidateResult validate(AICommandValidateRequest<Help> request) {
        return AICommandValidateResult.ok();
    }
}
