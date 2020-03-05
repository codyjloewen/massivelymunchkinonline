package mmo;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import mmo.card.CardParser;
import mmo.card.CardType;
import mmo.consoleclient.ArgParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    private static Gson GSON = new Gson();
    private static final BiMap<String, String> SHORT_ID_ID_MAP = HashBiMap.create();

    public static void main(final String[] args) throws IOException {
        final SafeScanner scanner = new SafeScanner();

        System.out.println("1. Quick\n2. Custom");
        //final int choice = scanner.nextInt(2);
        final int choice = 1;

        Game game = null;
        if (choice == 1) {
            final Pile doorPile = new Pile();
            final Pile treasurePile = new Pile();

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

            int id = 100;
            for (final ICard card : cards) {
                switch (card.getMetadata().getDeckType()) {
                    case DOOR:
                        doorPile.add(card);
                        break;
                    case TREASURE:
                        treasurePile.add(card);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                SHORT_ID_ID_MAP.put(String.valueOf(id++), card.getId());
            }

            final Deck doors = new Deck(doorPile, new Pile());
            final Deck treasures = new Deck(treasurePile, new Pile());
            final DeckHandler deckHandler = new DeckHandler(doors, treasures);

            final Locator locator = Locator.builder()
                    .dispatcher(new Dispatcher(new ArrayList<>()))
                    .build();
            final List<Player> players = new ArrayList<>();
            players.add(new Player("bob", new Munchkin(locator, "bob")));
            players.add(new Player("fred", new Munchkin(locator, "fred")));
            final List<Client> clients = List.of(
                ConsoleClient.builder()
                .id(players.get(0).getId())
                .build(),
                ConsoleClient.builder()
                .id(players.get(1).getId())
                .build()
            );
            locator.provide(new ConsoleDispatcher(clients));
            locator.provide(new GameState(GamePhase.STARTED));
            locator.provide(GameConfiguration.builder()
                    .isListenAtDoorEnabled(true)
                    .goldValueForLevel(1000)
                    .numberOfDoorsToDeal(4)
                    .numberOfTreasuresToDeal(4)
                    .winningLevel(10)
                    .minimumLevel(1)
                    .startingLevel(1)
                    .build()
            );
            game = new Game(locator, new PlayerHandler(players), deckHandler);
        } else if (choice == 2) {

        }

        final Orchestrator orchestrator = new Orchestrator(game);
        game.start();
        Player player = game.getCurrentPlayer();
        while (true) {
            final String[] command = scanner.nextLine().split(" ");
            if (command.length > 0) {
                final String action = command[0];
                try {
                    switch (action) {
                        case "swap": {
                            final String id = player.getId();
                            player = game.getPlayerHandler().getPlayers().stream().filter(x -> !x.getId().equals(id)).findFirst().get();
                            System.out.println("Playing as " + player.getUsername());
                            break;
                        }
                        case "charity":
                            orchestrate(orchestrator, new Command(player.getId(), Action.CHARITY, InvokerType.GAME, player.getId()));
                            break;
                        case "listen":
                            orchestrate(orchestrator, new Command(player.getId(), Action.LISTEN_AT_THE_DOOR, InvokerType.GAME, player.getId()));
                            break;
                        case "kick":
                            orchestrate(orchestrator, new Command(player.getId(), Action.KICK_DOWN_THE_DOOR, InvokerType.GAME, player.getId()));
                            break;
                        case "look":
                            orchestrate(orchestrator, new Command(player.getId(), Action.LOOK_FOR_TROUBLE, InvokerType.GAME, player.getId()));
                            break;
                        case "loot":
                            orchestrate(orchestrator, new Command(player.getId(), Action.LOOT_THE_ROOM, InvokerType.GAME, player.getId()));
                            break;
                        case "hand":
                            for (final ICard card : player.getMunchkin().getHand()) {
                                System.out.format("%s %-30s%s%n", SHORT_ID_ID_MAP.inverse().get(card.getId()),
                                        card.getName(), card.getClass().getSimpleName());
                            }
                            break;
                        case "inplay": {
                            for (final ICard card : player.getMunchkin().getInPlay()) {
                                System.out.format("%s %-30s%s%n", SHORT_ID_ID_MAP.inverse().get(card.getId()),
                                        card.getName(), card.getClass().getSimpleName());
                            }
                        }
                        case "equip": {
                            final String id = parseArg(command, 1, ArgType.CARD);
                            orchestrate(orchestrator, new Command(player.getId(), Action.EQUIP, InvokerType.PLAYER, id));
                            break;
                        }
                        case "sell": {
                            final List<String> ids = parseArgs(command, 1, Integer.MAX_VALUE, ArgType.CARD);
                            orchestrate(orchestrator, new Command(
                                    player.getId(),
                                    Action.SELL,
                                    InvokerType.PLAYER,
                                    ids
                            ));
                            break;
                        }
                        default:
                            System.out.println("Unrecognized action");
                    }
                } catch (final ArgParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    static List<String> parseArgs(
            final String[] command,
            final int startIndex,
            final int endIndex,
            final ArgType argType) {
        final List<String> args = new ArrayList<>();
        try {
            int index = startIndex;
            do {
                args.add(command[index++]);
            } while (index <= endIndex && index < command.length);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new ArgParseException("No arg at index");
        }

        for (int i = 0; i < args.size(); i++) {
            switch (argType) {
                case CARD:
                    args.set(i, SHORT_ID_ID_MAP.get(args.get(i)));
                    break;
                case PLAYER:
                    throw new UnsupportedOperationException();
                default:
                    throw new IllegalArgumentException();
            }
        }
        if (args.stream().anyMatch(Objects::isNull)) {
            throw new ArgParseException("Arg does not exist");
        }
        return args;
    }

    private static String parseArg(final String[] command, final int index, final ArgType argType) {
        return parseArgs(command, index, index, argType).get(0);
    }

    enum ArgType {
        CARD,
        PLAYER
    }

    static void orchestrate(final Orchestrator orchestrator, final Command command) {
        orchestrator.orchestrate(GSON.toJson(command));
    }
}
