package mmo;

public interface IDispatcher extends IClientMethod {
    void message(final String message, final Object... parameters);
    IDispatcher client(final String id);
    IDispatcher clients(final String... ids);
}
