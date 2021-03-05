package de.bmxertv.discord.api.command;

import de.bmxertv.discord.api.command.annotation.Command;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandManager extends ListenerAdapter {

    @Getter
    final Map<String, Map.Entry<Method, Object>> COMMAND_MAP = new HashMap<>();
    final char PREFIX;
    final Consumer<APICommand> DEFAULT_COMMAND;

    public CommandManager(char prefix, Consumer<APICommand> defaultCommand, Object... commandObjects) {
        this.PREFIX = prefix;
        this.registerCommands(commandObjects);
        this.DEFAULT_COMMAND = defaultCommand;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().trim();
        String[] args = Arrays.stream(message.split(" ")).skip(1).toArray(String[]::new);

        // RETURN WHENE MESSAGE FROM A BOT
        if (event.getAuthor().isBot())
            return;

        // RETURN WHEN NO COMMAND
        if (message.charAt(0) != this.PREFIX)
            return;

        String invoke = message.split(" ")[0].replace(Character.toString(this.PREFIX), "");

        try {
            handleCommand(invoke, args, event);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void handleCommand(String invoke, String[] args, GuildMessageReceivedEvent event) throws InvocationTargetException, IllegalAccessException {
        if (this.COMMAND_MAP.containsKey(invoke.toLowerCase())) {
            Method method = this.COMMAND_MAP.get(invoke).getKey();
            Object object = this.COMMAND_MAP.get(invoke).getValue();
            Command commandData = method.getAnnotation(Command.class);
            method.invoke(object, new APICommand(event, args, commandData.subCommands()));
            return;
        }
        defaultCommand(new APICommand(event, args, new String[]{}));
    }

    private void defaultCommand(APICommand apiCommand) {
        this.DEFAULT_COMMAND.accept(apiCommand);
    }

    private void registerCommands(Object... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getMethods()) {
                if (method.getAnnotation(Command.class) != null) {
                    Command command = method.getAnnotation(Command.class);
                    if (method.getParameterTypes().length > 1 || method.getParameterTypes()[0] != APICommand.class) {
                        System.err.println(String.format("[COMMAND-API] %s kann nicht registriert werden", command.value()));
                        continue;
                    }
                    registerCommand(command, method, object);
                }
            }
        }
    }

    private void registerCommand(Command command, Method method, Object object) {
        this.COMMAND_MAP.put(command.value().toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(method, object));
        for (String alias : command.aliases()) {
            this.COMMAND_MAP.put(alias.toLowerCase(), new AbstractMap.SimpleEntry<Method, Object>(method, object));
        }
    }
}
