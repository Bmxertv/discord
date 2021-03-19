package de.bmxertv.discord.api.command;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * Diese Klasse wird genutzt in der Argumenten Liste von einen Command
 *
 * @version 1.0
 * @author Christopher
 */
@Getter
@Accessors(fluent = true)
public class APICommand {

    /**
     * Gibt das GuildMessageReceivedEvent zurück
     */
    private final GuildMessageReceivedEvent event;
    /**
     * Gibt die Argumente zurück
     */
    private final String[] args;
    /**
     * Gibt denn Member zurück
     */
    private final Member member;
    /**
     * Gibt den TextChannel zurück
     */
    private final TextChannel channel;
    /**
     * Gibt die Guild zurück
     */
    private final Guild guild;
    /**
     * Gibt die Message zurück
     */
    private final Message message;
    /**
     * Gibt die SubCommands zurück
     */
    private final String[] subCommands;

    /**
     * Initialisiert alle Argumente
     * @param event GuildMessageReceivedEvent
     * @param args String[]
     * @param subCommands String[]
     */
    public APICommand(GuildMessageReceivedEvent event, String[] args, String[] subCommands) {
        this.event = event;
        this.args = args;
        this.member = event.getMember();
        this.channel = event.getChannel();
        this.guild = event.getGuild();
        this.message = event.getMessage();
        this.subCommands = subCommands;
    }

    /**
     * Sendet eine Nachricht und Erwähnt den Command Author
     * @param message Nachricht
     */
    public void replay(String message) {
        this.channel.sendMessage(this.member.getAsMention() + ", " + message).queue();
    }

    /**
     * Sendet eine Embed Message und Erwähnt den Command Author
     * @param messageEmbed EmbedMessage
     */
    public void replay(MessageEmbed messageEmbed) {
        this.channel.sendMessage(this.member.getAsMention() + ", ").queue(msg -> {
            msg.editMessage(messageEmbed).queue();
        });
    }

}
