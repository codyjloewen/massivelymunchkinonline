package mmo;

public interface IEventListener<T> {
    T handleEvent(T event);
}
