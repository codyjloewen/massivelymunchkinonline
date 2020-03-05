package mmo.card;

import lombok.Value;

@Value
public class ModifierMetadata {
    private final int bonusModifier;
    private final ConditionType conditionType;
}
