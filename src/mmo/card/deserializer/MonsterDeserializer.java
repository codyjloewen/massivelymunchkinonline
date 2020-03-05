package mmo.card.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mmo.card.Monster;

import java.lang.reflect.Type;

public class MonsterDeserializer implements JsonDeserializer<Monster> {
    @Override
    public Monster deserialize(
            final JsonElement jsonElement,
            final Type type,
            final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();
        return Monster.builder().build();
    }
}
