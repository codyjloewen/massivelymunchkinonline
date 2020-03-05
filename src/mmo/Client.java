package mmo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class Client implements IClientMethod {
    private final String id;

    public abstract void message(final String message);
}
