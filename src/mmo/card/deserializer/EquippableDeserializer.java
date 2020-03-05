package mmo.card.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mmo.EquippableSlots;
import mmo.SourcedValue;
import mmo.card.CardMetadata;
import mmo.card.Equippable;
import mmo.card.MonsterEnhancerEnhancer;

import java.lang.reflect.Type;

public class EquippableDeserializer implements JsonDeserializer<Equippable> {
    @Override
    public Equippable deserialize(
            final JsonElement jsonElement,
            final Type type,
            final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();
        return null;
    }
//    private deserializeEquippableSlots() {
//        return EquippableSlots.builder()
//                .headgear(new SourcedValue(json.get("headgear").getAsInt(), "Your tiny munchkin head"))
//                .armor(new SourcedValue(json.get("armor").getAsInt(), "Your weak munchkin physique"))
//                .hands(new SourcedValue(json.get("hands").getAsInt(), "Your clammy munchkin hands"))
//                .footgear(new SourcedValue(json.get("footgear").getAsInt(), "Your cute little munchkin feet"))
//                .build();
//    }
}
