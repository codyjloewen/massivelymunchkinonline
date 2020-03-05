package mmo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EquippableSlots<T> {
    private T headgear;
    private T armor;
    private T hands;
    private T footgear;
}
