package mmo.card;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import mmo.EquippableSlots;
import mmo.Identity;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
public class Equippable extends Item {
    private final EquippableSlots<Integer> equippableSlots;
    private final List<Identity> usableBy;
    private final List<Identity> notUsableBy;
    private boolean isEquipped;
}
