package mmo.card.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mmo.card.CardMetadata;
import mmo.card.MonsterEnhancerEnhancer;

import java.lang.reflect.Type;

public class MonsterEnhancerEnhancerDeserializer implements JsonDeserializer<MonsterEnhancerEnhancer> {
    @Override
    public MonsterEnhancerEnhancer deserialize(
            final JsonElement jsonElement,
            final Type type,
            final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();
        return MonsterEnhancerEnhancer.builder()
                .metadata(context.deserialize(json.get("metadata"), CardMetadata.class))
                .bonusModifier(json.get("bonusModifier").getAsInt())
                .treasureModifier(json.get("treasureModifier").getAsInt())
                .build();
    }
}
