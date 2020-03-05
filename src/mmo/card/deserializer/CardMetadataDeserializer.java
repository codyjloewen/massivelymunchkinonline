package mmo.card.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mmo.DeckType;
import mmo.Expansion;
import mmo.card.CardMetadata;
import mmo.card.Tag;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardMetadataDeserializer implements JsonDeserializer<CardMetadata> {

    @Override
    public CardMetadata deserialize(
            final JsonElement jsonElement,
            final Type type,
            final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();
        final List<Tag> tags = new ArrayList<>();
        for (final JsonElement element : json.getAsJsonArray("tags")) {
            tags.add(context.deserialize(element, Tag.class));
        }
        return CardMetadata.builder()
                .id(UUID.randomUUID().toString())
                .name(json.get("name").getAsString())
                .tags(tags)
                .guid(json.get("guid").getAsString())
                .deckType(context.deserialize(json.get("deckType"), DeckType.class))
                .expansion(context.deserialize(json.get("expansion"), Expansion.class))
                .build();
    }
}
