package mmo;

import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class PlayerHandler {
    private final List<Player> players;

    public List<IMunchkin> getMunchkins() {
        return players.stream().map(player -> player.getMunchkin()).collect(Collectors.toList());
    }

    public Player getPlayer(final String playerId) {
        return players.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst().get();
    }

    public IMunchkin getMunckin(final String playerId) {
        return getPlayer(playerId).getMunchkin();
    }
}
