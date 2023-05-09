package me.moonways.bnmg.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bnmg.component.function.BnmgComponentLineFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractBnmgComponent implements BnmgComponent {

    @Getter
    @ToString.Include
    protected final String name;

    @Getter
    @ToString.Include
    protected final BnmgComponentLine[] lines;

    @Override
    public void updateLine(BnmgComponentLine bnmgComponentLine) {
        lines[bnmgComponentLine.getIndex() - 1] = bnmgComponentLine;
    }

    private void validateContent(Object content) {
        if (content == null) {
            throw new NullPointerException("content");
        }
    }

    private synchronized BnmgComponentLine wrapComponentLine(int index, @NotNull String content) {
        return new BnmgComponentLine(index, content);
    }

    protected synchronized void setComponentLine(int index, @NotNull Object content) {
        validateContent(content);
        lines[index - 1] = wrapComponentLine(index, content.toString());
    }

    protected synchronized void setComponentLine(int index, @NotNull Collection<?> content) {
        validateContent(content);
        String parsedCollection = content.stream()
                .map(Object::toString)
                .collect(Collectors.joining("; "));

        setComponentLine(index, String.format("[%s]", parsedCollection));
    }

    protected String getComponentContent(int index) {
        BnmgComponentLine line = lines[index - 1];
        if (line == null)
            return null;

        return line.getContent();
    }

    protected Collection<String> getComponentContentList(int index) {
        BnmgComponentLine line = lines[index - 1];
        if (line == null)
            return null;

        return line.wrapCollection();
    }

    protected <T> T getComponentContentMapping(int index, @NotNull Function<String, T> mapper) {
        return mapper.apply(getComponentContent(index));
    }

    protected <T> T getComponentContent(int index, @NotNull BnmgComponentLineFunction<T> function) {
        return function.callFunction(lines[index - 1], getComponentContent(index));
    }
}
