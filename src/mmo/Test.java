package mmo;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mmo.card.CardParser;
import mmo.card.CardType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Test {
    public static void main(final String[] args) throws IOException {
        final Gson gson = new Gson();
        final CardParser cardParser = new CardParser();
        final List<ICard> cards = new ArrayList<>();
        for (final File folder : new File("src/mmo/cards").listFiles()) {
            final CardType cardType = CardType.valueOf(folder.getName());
            for (final File file : folder.listFiles()) {
                System.out.println("Parsing " + file.getName());
                final String json = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                cards.addAll(cardParser.parse(json, cardType));
            }
        }

        for (final ICard card : cards) {
            System.out.println(gson.toJson(card));
        }
    }
}
