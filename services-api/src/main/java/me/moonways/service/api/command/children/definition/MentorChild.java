package me.moonways.service.api.command.children.definition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.service.api.command.children.CommandChild;

import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class MentorChild implements CommandChild { //недодрочилдрен

    private final Object parent;
    private final Method method;
}
