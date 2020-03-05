package mmo;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ConsoleClient extends Client {
    @Override
    public void message(final String message) {
        System.out.println(message);
    }
}
