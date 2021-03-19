package de.bmxertv.discord.api.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.function.Consumer;

/**
 * Dies ist ein EmbedMessage Builder
 *
 * @version 1.0
 * @author
 */
public class EmbedMessage {

    private final EmbedBuilder EMBED_BUILDER;
    private final TextChannel CHANNEL;

    /**
     * Initialisiert eine EmbedMessage
     */
    public EmbedMessage() {
        this(new EmbedBuilder(), null);
    }

    /**
     * Initialisiert eine EmbedMessage
     * @param channel TextChannel
     */
    public EmbedMessage(TextChannel channel) {
        this(new EmbedBuilder(), channel);
    }

    private EmbedMessage(EmbedBuilder EMBED_BUILDER, TextChannel CHANNEL) {
        this.EMBED_BUILDER = EMBED_BUILDER;
        this.CHANNEL = CHANNEL;
    }

    /**
     * Setzt denn Footer
     * @param message Nachricht
     * @return EmbedMessage
     */
    public EmbedMessage setFooter(String message) {
        this.EMBED_BUILDER.setFooter(message);
        return this;
    }

    /**
     * Setzt denn Footer mit einem Icon
     * @param message Nachricht
     * @param iconUrl Icon Url
     * @return EmbedMessage
     */
    public EmbedMessage setFooter(String message, String iconUrl) {
        this.EMBED_BUILDER.setFooter(message, iconUrl);
        return this;
    }

    /**
     * Setzt ein Thumbnail in der Nachricht
     * @param url Image Url
     * @return EmbedMessage
     */
    public EmbedMessage setThumbnail(String url) {
        this.EMBED_BUILDER.setThumbnail(url);
        return this;
    }

    /**
     * Setzt ein Image
     * @param url Image Url
     * @return EmbedMessage
     */
    public EmbedMessage setImage(String url) {
        this.EMBED_BUILDER.setImage(url);
        return this;
    }

    /**
     * Setzt die Farbe der EmbedMessage
     * @param color Java Color
     * @return EmbedMessage
     */
    public EmbedMessage setColor(Color color) {
        this.EMBED_BUILDER.setColor(color);
        return this;
    }

    /**
     * Setzt die Farbe der EmbedMessage
     * @param hexColor Hex Color
     * @return EmbedMessage
     */
    public EmbedMessage setColor(String hexColor) {
        this.EMBED_BUILDER.setColor(Color.decode(hexColor));
        return this;
    }

    /**
     * Setzt die Beschreibung der EmbedMessage
     * @param message Nachricht
     * @return EmbedMessage
     */
    public EmbedMessage setDescription(String message) {
        this.EMBED_BUILDER.setDescription(message);
        return this;
    }

    /**
     * Setzt den Titel der EmbedMessage
     * @param title Title
     * @return EmbedMessage
     */
    public EmbedMessage setTitle(String title) {
        this.EMBED_BUILDER.setTitle(title);
        return this;
    }

    /**
     * Setzt den Titel und einem Icon in der EmbedMessage
     * @param title Titel
     * @param url Icon Url
     * @return EmbedMessage
     */
    public EmbedMessage setTitle(String title, String url) {
        this.EMBED_BUILDER.setTitle(title, url);
        return this;
    }

    /**
     * Setzt den Author in der EmbedMessage
     * @param name Author
     * @return EmbedMessage
     */
    public EmbedMessage setAuthor(String name) {
        this.EMBED_BUILDER.setAuthor(name);
        return this;
    }

    /**
     * Setzt den Author mit Icon in der EmbedMessage
     * @param name Author
     * @param url Icon Url
     * @return EmbedMessage
     */
    public EmbedMessage setAuthor(String name, String url) {
        this.EMBED_BUILDER.setAuthor(name, url);
        return this;
    }

    /**
     * Setzt den Author mit Icon und Url in der EmbedMessage
     * @param name Author
     * @param url Url
     * @param iconUrl Icon Url
     * @return EmbedMessage
     */
    public EmbedMessage setAuthor(String name, String url, String iconUrl) {
        this.EMBED_BUILDER.setAuthor(name, url, iconUrl);
        return this;
    }

    /**
     * Fügt an die Beschreibung ein Text an
     * @param message Message
     * @return EmbedMessage
     */
    public EmbedMessage appendDescription(String message) {
        this.EMBED_BUILDER.appendDescription(message);
        return this;
    }

    /**
     * Fügt ein Feld hinzu
     * @param field Field
     * @return EmbedMessage
     */
    public EmbedMessage addField(MessageEmbed.Field field) {
        this.EMBED_BUILDER.addField(field);
        return this;
    }

    /**
     * Fügt ein Feld hinzu
     * @param name Name
     * @param message Message
     * @param inline inline
     * @return EmbedMessage
     */
    public EmbedMessage addField(String name, String message, boolean inline) {
        this.EMBED_BUILDER.addField(name, message, inline);
        return this;
    }

    /**
     * Fügt ein Leeres feld hinzu
     * @param inline inline
     * @return EmbedMessage
     */
    public EmbedMessage addBlankField(boolean inline) {
        this.EMBED_BUILDER.addBlankField(inline);
        return this;
    }

    /**
     * Sendet die Nachricht
     * @return Message
     */
    public Message send() {
        return this.CHANNEL.sendMessage(build()).complete();
    }

    /**
     * Sendet die Nachricht
     * @param action Action nach dem Senden
     */
    public void send(Consumer<Message> action) {
        this.CHANNEL.sendMessage(build()).queue(action);
    }

    /**
     * Baut die EmbedMessage
     * @return MessageEmbed
     */
    public MessageEmbed build() {
        return this.EMBED_BUILDER.build();
    }

}
