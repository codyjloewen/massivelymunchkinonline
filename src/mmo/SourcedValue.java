package mmo;

import lombok.Data;
import lombok.NonNull;

@Data
public class SourcedValue {
    @NonNull private int value;
    @NonNull private String source;
    private String sourceId;

    public SourcedValue(final int value, final String source) {
        this.value = value;
        this.source = source;
    }
}
