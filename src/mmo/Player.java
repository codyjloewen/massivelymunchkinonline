package mmo;

import lombok.Value;

@Value
public class Player {
    private final String id;
    private final String username;
    private final IMunchkin munchkin;

    public Player(final String username, final IMunchkin munchkin) {
        this.username = username;
        this.munchkin = munchkin;
        this.id = username;
    }
}
