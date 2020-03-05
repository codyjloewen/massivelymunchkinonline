package mmo;

import com.google.common.collect.ImmutableList;

import java.util.List;

public interface IMunchkin {
    //<T extends IEvent> List<T> getEvents(Class<T> clazz);
    List<ICard> getHand();
    List<ICard> getInPlay();
    boolean hasCard(String id, CardLocation cardLocation);
    boolean isInCombat();
    <T extends IEvent> ImmutableList<T> getEvents(Class<T> clazz);
    List<IBuff> getBuffs();
    void addBuff(IBuff buff);
    void removeBuff(IBuff buff);
    int getLevel();
}
