package mmo;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import mmo.annotations.HasCard;
import mmo.annotations.IsTurn;
import mmo.annotations.NotInCombat;
import mmo.card.Equippable;
import mmo.card.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static mmo.Utility.as;
import static mmo.Utility.ofType;

@Getter
@Setter
public class Munchkin implements IMunchkin {
    private final String name;
    private final Locator locator;
    private final String id;
    private final List<ICard> hand = new ArrayList<>();
    private final List<ICard> inPlay = new ArrayList<>();
    private boolean isTurn = false;
    private boolean isInCombat = false;
    private SourcedValues bigItemsCarriable;
    private EquippableSlots<SourcedValue> equippableSlots;
    private int level = 1;

    public Munchkin(final Locator locator, final String name) {
        this.locator = locator;
        this.name = name;
        this.id = name; //TODO

        bigItemsCarriable = new SourcedValues(new SourcedValue(1, "Puny munchkin strength"));
        equippableSlots = EquippableSlots.<SourcedValue>builder()
                .headgear(new SourcedValue(1, "Your tiny munchkin head"))
                .armor(new SourcedValue(1, "Your weak munchkin physique"))
                .hands(new SourcedValue(2, "Your clammy munchkin hands"))
                .footgear(new SourcedValue(1, "Your cute little munchkin feet"))
                .build();
    }

    public boolean canLevel(final int levels) {
        return level + levels < locator.getConfig().getWinningLevel();
    }

    public void level(final int levels) {
        level(levels, false);
    }

    public void level(final int levels, final boolean canBeWinningLevel) {
        if (!canBeWinningLevel && !canLevel(levels)) {
            throw new RuntimeException("Winning level");
        }
        final int oldLevel = level;
        level = Math.max(1, level + levels);
        locator.getDispatcher().message(
                "%s %s %d level%s",
                name,
                oldLevel < level ? "gained" : "lost",
                Math.abs(oldLevel - level),
                Math.abs(oldLevel - level) == 1 ? "" : "s"
        );
        // TODO: check game over
    }

    @Override
    public <T extends IEvent> ImmutableList<T> getEvents(Class<T> clazz) {
        return null;
    }

    @Override
    public List<IBuff> getBuffs() {
        return null;
    }

    @Override
    public void addBuff(IBuff buff) {

    }

    @Override
    public void removeBuff(IBuff buff) {

    }

    public boolean is(final Identity identity) {
        final List<Identifiable> identities = ofType(Identifiable.class, inPlay);
        return identities.stream().anyMatch(identifiable -> identifiable.getIdentity() == identity);
    }

    public boolean isNot(final Identity identity) {
        return !is(identity);
    }

    public boolean hasCard(final String id, final CardLocation cardLocation) {
        final List<ICard> cards = new ArrayList<>();
        switch (cardLocation) {
            case ANY:
                cards.addAll(hand);
                cards.addAll(inPlay);
                break;
            case HAND:
                cards.addAll(hand);
                break;
            case IN_PLAY:
                cards.addAll(inPlay);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return cards.stream().anyMatch(card -> card.getId().equals(id));
    }

    @NotInCombat
    public void equip(@HasCard final ICard card) {
        final Equippable equippable = as(Equippable.class, card);
        if (Objects.nonNull(equippable)) {
            if (!equippable.isEquipped()) {
                final boolean needsCarry = hand.contains(equippable);
                if (!needsCarry || isTurn() || locator.getGameState().getGamePhase() == GamePhase.STARTED) {
                    if (!needsCarry || canCarry(equippable)) {
                        final Optional<String> canEquip = canEquip(equippable);
                        if (canEquip.isEmpty()) {
                            final Optional<String> canPhysicallyEquip = canPhysicallyEquip(equippable);
                            if (canPhysicallyEquip.isEmpty()) {
                                if (needsCarry) {
                                    hand.remove(equippable);
                                    inPlay.add(equippable);
                                }
                                equippable.setEquipped(true);
                                locator.getDispatcher().message("Equipped %s", equippable.getName());
                            } else {
                                locator.getDispatcher().client(id).message("Cannot equip %s. %s", equippable.getName(),
                                        canPhysicallyEquip.get());
                            }
                        } else {
                            locator.getDispatcher().client(id).message("Cannot equip %s. %s", equippable.getName(),
                                    canEquip.get());
                        }
                    } else {
                        locator.getDispatcher().client(id).message("You can't carry any more big items");
                    }
                } else {
                    locator.getDispatcher().client(id).message("You can't play down cards when it's not your turn");
                }
            } else {
                locator.getDispatcher().client(id).message("%s is already equipped", equippable.getName());
            }
        } else {
            locator.getDispatcher().client(id).message("You can't equip %s", card.getName());
        }
    }

//    @NotInCombat
//    public void equip2(@HasCard final ICard card) {
//        final Equippable equippable = as(Equippable.class, card);
//        verify(Objects.nonNull(equippable), "You can't equip %s", card.getName());
//        verify(!equippable.isEquipped(), "%s is already equipped", equippable.getName());
//        final boolean needsCarry = hand.contains(equippable);
//        verify(!needsCarry || isTurn() || locator.getGameState().getGamePhase() == GamePhase.STARTED,
//                "You can't play down cards when it's not your turn");
//        verify(!needsCarry || canCarry(equippable), "You can't carry any more big items");
//        verify(canEquip(equippable), "Cannot equip %s. %s", equippable.getName());
//        verify(canPhysicallyEquip(equippable), "Cannot equip %s. %s", equippable.getName());
//
//        if (needsCarry) {
//            hand.remove(equippable);
//            inPlay.add(equippable);
//        }
//        equippable.setEquipped(true);
//        locator.getDispatcher().message("Equipped %s", equippable.getName());
//    }

//    private void verify(final boolean condition, final String message) {
//        verify(condition, message, new Object[0]);
//    }
//
//    private void verify(final boolean condition, final String message, final Object... parameters) {
//        if (!condition) {
//            locator.getDispatcher().client(id).message(message, parameters);
//        }
//    }
//
//    private void verify(final Optional<String> optional, final String message) {
//        verify(optional, message, new Object[0]);
//    }
//
//    private void verify(final Optional<String> optional, final String message, final Object... parameters) {
//        if (optional.isPresent()) {
//            locator.getDispatcher().client(id).message(message, Arrays.asList(parameters).add(optional.get()));
//        }
//    }

    @NotInCombat
    public void unequip(@HasCard(location = CardLocation.IN_PLAY) final ICard card) {
        final Equippable equippable = as(Equippable.class, card);
        if (Objects.nonNull(equippable)) {
            if (equippable.isEquipped()) {
                equippable.setEquipped(false);
                locator.getDispatcher().message("Unequipped %s", equippable.getName());
            } else {
                locator.getDispatcher().client(id).message("%s is not equipped", equippable.getName());
            }
        }
        else {
            locator.getDispatcher().client(id).message("%s is not equippable", card.getName());
        }
    }

    @NotInCombat
    @IsTurn
    public void carry(@HasCard(location = CardLocation.HAND) final ICard card) {
        final Item item = as(Item.class, card);
        if (Objects.nonNull(item)) {
            if (canCarry(item)) {
                hand.remove(item);
                inPlay.add(item);
                locator.getDispatcher().message("%s is carrying %s", name, item.getName());
            } else {
                locator.getDispatcher().client(id).message("You can't carry any more big items");
            }
        } else {
            locator.getDispatcher().client(id).message("%s is not an item", card.getName());
        }
    }

    @NotInCombat
    @IsTurn
    // TODO: hascards
    public void sell(final List<ICard> cards) {
        final List<Item> items = new ArrayList<>();
        for (final ICard card : cards) {
            final Item item = as(Item.class, card);
            if (Objects.nonNull(item)) {
                items.add(item);
            }
        }
        if (items.size() == cards.size()) {
            int totalValue = items.stream().mapToInt(Item::getValue).sum();
            final int goldValueForLevel = locator.getConfig().getGoldValueForLevel();
            final int levelsGained = totalValue / goldValueForLevel;
            if (levelsGained != 0) {
                if (canLevel(levelsGained)) {

                    level(levelsGained);
                    locator.getDispatcher().message("%s sold items", name);
                } else {
                    locator.getDispatcher().client(id).message("You can't gain the winning level by selling items");
                }
            } else {
                locator.getDispatcher().client(id).message(
                        "You must sell at least %d gold pieces worth of items to gain a level",
                        goldValueForLevel
                );
            }
        } else {
            locator.getDispatcher().client(id).message("You can only sell items");
        }
    }

    private boolean canCarry(final Item item) {
        if (item.getSize() == ItemSize.BIG) {
            int bigItems = 1;
            for (final Item carriedItem : ofType(Item.class, inPlay)) {
                if (carriedItem.getSize() == ItemSize.BIG) {
                    bigItems++;
                }
            }
            return bigItems <= bigItemsCarriable.sum();
        }
        return true;
    }

    private Optional<String> canEquip(final Equippable equippable) {
        if (!equippable.getUsableBy().isEmpty() && equippable.getUsableBy().stream().noneMatch(this::is)) {
            return Optional.of("You are not a " + Joiner.on(" or ").join(equippable.getUsableBy()));
        }
        if (!equippable.getNotUsableBy().isEmpty()) {
            for (final Identity identity : equippable.getNotUsableBy()) {
                if (is(identity)) {
                    return Optional.of("You are a " + identity.name());
                }
            }
        }
        return Optional.empty();
    }

    private Optional<String> canPhysicallyEquip(final Equippable equippableToCheck) {
        final List<Equippable> equipped = ofType(Equippable.class, inPlay).stream()
                .filter(Equippable::isEquipped)
                .collect(Collectors.toList());
        int headgear = 0;
        int armor = 0;
        int hands = 0;
        int footgear = 0;
        equipped.add(equippableToCheck);
        for (final Equippable equippable : equipped) {
            final EquippableSlots<Integer> slots = equippable.getEquippableSlots();
            headgear += slots.getHeadgear();
            armor += slots.getArmor();
            hands += slots.getHands();
            footgear += slots.getFootgear();
        }

        if (headgear > equippableSlots.getHeadgear().getValue()) {
            return Optional.of("You don't have any more heads");
        }
        if (armor > equippableSlots.getArmor().getValue()) {
            return Optional.of("Can't wear more armor");
        }
        if (hands > equippableSlots.getHands().getValue()) {
            return Optional.of("Not enough hands");
        }
        if (footgear > equippableSlots.getFootgear().getValue()) {
            return Optional.of("Not enough feet");
        }
        return Optional.empty();
    }
}
