package de.bmxertv.discord;

import de.bmxertv.discord.api.command.APICommand;
import de.bmxertv.discord.api.command.CommandManager;
import de.bmxertv.discord.commands.AdminCommands;
import de.bmxertv.discord.commands.UserCommands;
import de.bmxertv.discord.events.ReactionEvent;
import de.bmxertv.discord.files.Config;
import de.bmxertv.discord.files.Property;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;

public class Application {

    public static Application instance;

    @Getter
    @Accessors(fluent = true)
    private final Config CONFIG;

    @Getter
    @Accessors(fluent = true)
    private final CommandManager COMMAND_MANAGER;

    @Getter
    @Accessors(fluent = true)
    private final JDA jda;

    @Getter
    @Accessors(fluent = true)
    private final String TITLE_PATTERN = "**%s**";

    public Application() throws LoginException, IOException {
        instance = this;
        if (!Files.exists(Paths.get(Application.getJarLocation().toString(), "application.cfg"))) {
            Files.createFile(Paths.get(Application.getJarLocation().toString(), "application.cfg"));
            Map<String, Object> content = Property.getContent();
            content.put("GAST_ROLE_ID", " ");
            Property.write(content);
        }

        this.CONFIG = new Config();
        COMMAND_MANAGER = new CommandManager(this.CONFIG.PREFIX(), defaultCommand(), commandObjects());

        this.jda = JDABuilder.createDefault(this.CONFIG.TOKEN()).build();
        this.jda.addEventListener(COMMAND_MANAGER);
        this.jda.addEventListener(eventObjects());
    }

    public static void main(String[] args) throws LoginException, IOException {
        new Application();
    }

    public Consumer<APICommand> defaultCommand() {
        return command -> {
            command.replay("dieser Command existiert nicht :confused:");
        };
    }

    public Object[] commandObjects() {
        return new Object[]{
                new UserCommands(),
                new AdminCommands()
        };
    }

    public Object[] eventObjects() {
        return new Object[]{
                new ReactionEvent()
        };
    }

    @SneakyThrows
    public static Path getJarLocation() {
        return Paths.get(instance.getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()).getParent();
    }
}
