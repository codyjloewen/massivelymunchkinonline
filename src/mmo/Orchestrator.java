package mmo;

import com.google.gson.Gson;
import lombok.Value;
import mmo.annotations.HasCard;
import mmo.annotations.IsTurn;
import mmo.annotations.NotInCombat;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Value
public class Orchestrator {
    private static final Gson GSON = new Gson();
    private static final Queue<Command> commands = new ConcurrentLinkedQueue<>();

    private final Game game;

    public void orchestrate(final String json) {
        final Command command = GSON.fromJson(json, Command.class);

        commands.add(command);
        execute();
    }

    private synchronized void execute() {
        while (!commands.isEmpty()) {
            final Command command = commands.poll();
            final Object invoker = getInvoker(command);
            final Method method = getMethod(invoker, command.getAction(), command.getParameters());
            final Optional<String> isMethodValid = validateMethod(method, command.getPlayerId());
            if (isMethodValid.isEmpty()) {
                final Object[] parameters = parseRawParameters(method, command.getParameters());
                final Optional<String> isParametersValid =
                        validateParameters(method.getParameters(), parameters, command.getPlayerId());
                if (isParametersValid.isEmpty()) {
                    invoke(method, invoker, parameters);
                } else {
                    System.out.println(isParametersValid.get());
                }
            } else {
                System.out.println(isMethodValid.get());
            }
        }
    }

    private Object getInvoker(final Command command) {
        switch (command.getInvokerType()) {
            case GAME:
                return game;
            case PLAYER:
                final String playerId = command.getPlayerId();
                return game.getPlayerHandler().getPlayers().stream()
                        .filter(player -> player.getId().equals(playerId))
                        .findFirst().get().getMunchkin();
            default:
                throw new IllegalArgumentException();
        }
    }

    private <T> Method getMethod(final T invoker, final String methodName, final Object[] parameters) {
        final List<Method> methods = Arrays.stream(invoker.getClass().getMethods())
                .filter(method -> method.getName().equals(methodName)
                        && method.getParameterCount() == parameters.length)
                .collect(Collectors.toList());
        if (methods.size() != 1) {
            throw new UnsupportedOperationException();
        }
        return methods.get(0);
    }

    private Optional<String> validateMethod(final Method method, final String playerId) {
        final Annotation[] annotations = method.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            final Annotation annotation = annotations[i];
            if (annotation instanceof IsTurn) {
                if (game.getGamePhase() != GamePhase.STARTED && !game.getCurrentPlayer().getId().equals(playerId)) {
                    return Optional.of(((IsTurn) annotation).error());
                }
            } else if (annotation instanceof NotInCombat) {
                if (game.getPlayerHandler().getMunckin(playerId).isInCombat()) {
                    return Optional.of(((NotInCombat) annotation).error());
                }
            }
        }
        return Optional.empty();
    }

    private Object[] parseRawParameters(final Method method, final Object[] rawParameters) {
        final Object[] parameters = new Object[rawParameters.length];
        final Parameter[] methodParameters = method.getParameters();
        for (int i = 0; i < rawParameters.length; i++) {
            final Parameter methodParameter = methodParameters[i];
            final String simpleName = methodParameter.getType().getSimpleName();
            switch (simpleName) {
                case "List":
                    final ParameterizedType parameterizedType = (ParameterizedType) methodParameter.getParameterizedType();
                    final Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    final List<Object> parsedParameterList = new ArrayList();
                    final List rawParameterList = (ArrayList) rawParameters[i];
                    for (final Object object : rawParameterList) {
                        parsedParameterList.add(parseRawParameter(genericClass.getSimpleName(), object));
                    }
                    parameters[i] = parsedParameterList;
                    break;
                default:
                    parameters[i] = parseRawParameter(simpleName, rawParameters[i]);
            }
            return parameters;
        }

        return parameters;
    }

    private Object parseRawParameter(final String simpleTypeName, final Object rawParameter) {
        switch (simpleTypeName) {
            case "String":
                return rawParameter;
            case "IMunchkin":
                return game.getPlayerHandler().getPlayers().stream()
                        .filter(player -> player.getId().equals(rawParameter))
                        .findFirst().get().getMunchkin();
            case "ICard":
                return game.getCard((String) rawParameter);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Optional<String> validateParameters(
            final Parameter[] methodParameters,
            final Object[] parameters,
            final String playerId) {
        for (int i = 0; i < methodParameters.length; i++) {
            final Parameter methodParameter = methodParameters[i];
            final Object parameter = parameters[i];
            final Annotation[] annotations = methodParameter.getAnnotations();
            for (int j = 0; j < annotations.length; j++) {
                final Annotation annotation = annotations[j];
                if (annotation instanceof HasCard) {
                    final ICard card = (ICard) parameter;
                    final IMunchkin munchkin = game.getPlayerHandler().getMunckin(playerId);
                    final CardLocation cardLocation = ((HasCard) annotation).location();
                    if (!munchkin.hasCard(card.getId(), cardLocation)) {
                        return Optional.of("You don't have the card " + card.getName());
                    }
                }
            }
        }
        return Optional.empty();
    }

    private <T> void invoke(final Method method, final T invoker, final Object[] parameters) {
        try {
            method.invoke(invoker, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
