package mmo;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ConsoleDispatcher implements IDispatcher {
    private final List<Client> clients;

    @Override
    public void message(final String message) {
        if (!clients.isEmpty()) {
            clients.get(0).message(message);
        }
    }

    public void message(final String message, final Object... parameters) {
        message(String.format(message, parameters));
    }

    public IDispatcher client(final String id) {
        return clients(id);
    }

    public IDispatcher clients(final String... ids) {
        return new ConsoleDispatcher(clients.stream()
                .filter(client -> Arrays.asList(ids).contains(client.getId()))
                .collect(Collectors.toList()));
    }
}
