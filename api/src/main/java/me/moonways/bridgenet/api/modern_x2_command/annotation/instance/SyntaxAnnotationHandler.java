package me.moonways.bridgenet.api.modern_x2_command.annotation.instance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.ArgSyntax;
import me.moonways.bridgenet.api.modern_x2_command.ArgSyntaxes;
import me.moonways.bridgenet.api.modern_x2_command.Pattern;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationCommandContext;
import me.moonways.bridgenet.api.modern_x2_command.annotation.AnnotationCommandHandler;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateRequest;
import me.moonways.bridgenet.api.modern_x2_command.annotation.validate.AnnotationCommandValidateResult;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.obj.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.obj.pattern.PatternFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntaxAnnotationHandler extends AnnotationCommandHandler<ArgSyntaxes> {

    private final Map<String, String> formats = new HashMap<>();

    @Override
    public void prepare(AnnotationCommandContext<ArgSyntaxes> context) {
        // empty
    }

    @Override
    public AnnotationCommandValidateResult validate(AnnotationCommandValidateRequest<ArgSyntaxes> request) {
        List<ArgSyntax> syntaxes = Arrays.asList(request.getCommandContext().getAnnotation().value());

        CommandLabelContext labelContext = request.getExecutionContext().getLabel();
        CommandLabelContext.Arguments arguments = labelContext.getArguments();
        EntityCommandSender sender = request.getExecutionContext().getSender();

        for (ArgSyntax syntax : syntaxes) {
            int position = syntax.position();

            Pattern annotationPattern = syntax.pattern();

            if (!hasPosition(arguments, position)) {
                //todo throw info
                return AnnotationCommandValidateResult.fail();
            }

            if (annotationPattern.enumFormat().getValue() != null) {
                PatternFormat patternFormat = annotationPattern.enumFormat();

                String value = patternFormat.getValue();

                if (!matches(arguments, position, value)) {
                    String pattern = patternFormat.name();
                    String exceptionMsg = formats.get(pattern);
                    if (validateExceptionMsg(exceptionMsg)) {
                        return getConditionFormattingError(sender);
                    }

                    return AnnotationCommandValidateResult.fail(sender, exceptionMsg);
                }
            }

            if (annotationPattern.stringFormat() != null) {
                String format = annotationPattern.stringFormat();

                String[] splitMsg = format.split("(?<=})");

                if (splitMsg.length != 2) {
                    return getConditionFormattingError(sender);
                }

                String pattern = splitMsg[0]
                        .replace("{", "")
                        .replace("}", "");

                if (!matches(arguments, position, pattern)) {
                    String exceptionMsg = formats.get(pattern);
                    if (validateExceptionMsg(exceptionMsg)) {
                        return getConditionFormattingError(sender);
                    }

                    return AnnotationCommandValidateResult.fail(sender, exceptionMsg);
                }
            }
        }
        return AnnotationCommandValidateResult.ok();
    }

    private AnnotationCommandValidateResult getConditionFormattingError(EntityCommandSender sender) {
        return AnnotationCommandValidateResult.fail(sender, "§cПроизошла ошибка форматирования условий команды");
    }

    private boolean validateExceptionMsg(String pattern) {
        return formats.containsKey(pattern);
    }

    private boolean hasPosition(CommandLabelContext.Arguments arguments, int position) {
        return arguments.has(position);
    }

    private boolean matches(CommandLabelContext.Arguments arguments, int position, String format) {
        return arguments.get(position).get().matches(format);
    }
}
