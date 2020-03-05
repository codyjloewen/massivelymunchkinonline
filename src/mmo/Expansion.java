package mmo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Expansion {
    MUNCHKIN_BASE("Base Game");

    private final String name;
}
