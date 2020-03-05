package mmo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Edition {
    MUNCHKIN("Munchkin");

    private final String name;
}
