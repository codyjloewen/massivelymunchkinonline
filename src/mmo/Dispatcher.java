package mmo;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Dispatcher implements IDispatcher, IClientMethod {
    private final List<Client> clients;

    @Override
    public void message(final String message) {
        for (final Client client : clients) {
            client.message(message);
        }
    }

    public void message(final String message, final Object... parameters) {
        message(String.format(message, parameters));
    }

    public IDispatcher client(final String id) {
        return clients(id);
    }

    public IDispatcher clients(final String... ids) {
        return new Dispatcher(clients.stream()
                .filter(client -> Arrays.asList(ids).contains(client.getId()))
                .collect(Collectors.toList()));
    }
}
