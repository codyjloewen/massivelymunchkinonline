package mmo.card;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mmo.ICard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

public class CardParser {
    private static final Gson GSON = new Gson();
    private static final Map<CardType, Class<? extends ICard>> CARD_TYPE_TO_CARD_CLASS = Map.ofEntries(
            entry(CardType.CLASS, Clazz.class),
            entry(CardType.CLASS_ENHANCER, ClazzEnhancer.class),
            entry(CardType.EQUIPPABLE, Equippable.class),
            entry(CardType.GO_UP_A_LEVEL, GoUpALevel.class),
            entry(CardType.HIRELING, Hireling.class),
            entry(CardType.ITEM_ENHANCER, ItemEnhancer.class),
            entry(CardType.MONSTER, Monster.class),
            entry(CardType.MONSTER_ENHANCER, MonsterEnhancer.class),
            entry(CardType.MONSTER_ENHANCER_ENHANCER, MonsterEnhancerEnhancer.class),
            entry(CardType.RACE, Race.class),
            entry(CardType.RACE_ENHANCER, RaceEnhancer.class),
            entry(CardType.STEED, Steed.class)
    );

    public List<ICard> parse(final String json, final CardType cardType) {
        final JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        final JsonElement variants = object.get("metadata").getAsJsonObject().get("variants");

        final List<String> variantIds = new ArrayList<>();
        if (Objects.nonNull(variants)) {
            for (final JsonElement jsonElement : variants.getAsJsonArray()) {
                variantIds.add(jsonElement.getAsString());
            }
        }

        final List<ICard> cards = new ArrayList<>();
        if (!variantIds.isEmpty()) {
            for (final String variant : variantIds) {
                final ICard card = GSON.fromJson(json, CARD_TYPE_TO_CARD_CLASS.get(cardType));
                card.getMetadata().setVariantId(variant);
                cards.add(card);
            }
        } else {
            cards.add(GSON.fromJson(json, CARD_TYPE_TO_CARD_CLASS.get(cardType)));
        }
        return cards;
    }
}
