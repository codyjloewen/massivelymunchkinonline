package mmo.card;

import com.google.gson.annotations.JsonAdapter;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import mmo.DeckType;
import mmo.Expansion;
import mmo.card.deserializer.CardMetadataDeserializer;

import java.util.List;

@Data
@Builder
@JsonAdapter(CardMetadataDeserializer.class)
public class CardMetadata {
    private final String id;
    @NonNull private final String name;
    @NonNull private final List<Tag> tags;
    @NonNull private final String guid;
    @NonNull private final DeckType deckType;
    @NonNull private final Expansion expansion;

    @Setter
    private String variantId;
}
