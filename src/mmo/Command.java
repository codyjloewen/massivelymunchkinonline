package mmo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class Command {
    @NonNull final String playerId;
    @NonNull final String action;
    @NonNull final InvokerType invokerType;
    @NonNull final Object[] parameters;

    public Command(final String playerId, final String action, final InvokerType invokerType) {
        this(playerId, action, invokerType, new Object[0]);
    }

    public Command(final String playerId, final String action, final InvokerType invokerType, final Object... objects) {
        this.playerId = playerId;
        this.action = action;
        this.invokerType = invokerType;
        this.parameters = objects;
    }
}
