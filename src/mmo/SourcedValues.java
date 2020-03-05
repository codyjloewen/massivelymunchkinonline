package mmo;

import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
public class SourcedValues {
    private final List<SourcedValue> values;

    public SourcedValues(final SourcedValue... values) {
        this.values = Arrays.asList(values);
    }

    public int sum() {
        return values.stream()
                .mapToInt(value -> value.getValue())
                .sum();
    }
}
