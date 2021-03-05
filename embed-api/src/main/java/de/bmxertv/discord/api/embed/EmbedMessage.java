package de.bmxertv.discord.api.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.function.Consumer;

public class EmbedMessage {

    private final EmbedBuilder EMBED_BUILDER;
    private final TextChannel CHANNEL;

    public EmbedMessage() {
        this.EMBED_BUILDER = new EmbedBuilder();
        this.CHANNEL = null;
    }

    public EmbedMessage(TextChannel channel) {
        this.EMBED_BUILDER = new EmbedBuilder();
        this.CHANNEL = channel;
    }

    public EmbedMessage setFooter(String message) {
        this.EMBED_BUILDER.setFooter(message);
        return this;
    }

    public EmbedMessage setFooter(String message, String iconUrl) {
        this.EMBED_BUILDER.setFooter(message, iconUrl);
        return this;
    }

    public EmbedMessage setThumbnail(String url) {
        this.EMBED_BUILDER.setThumbnail(url);
        return this;
    }

    public EmbedMessage setImage(String url) {
        this.EMBED_BUILDER.setImage(url);
        return this;
    }

    public EmbedMessage setColor(Color color) {
        this.EMBED_BUILDER.setColor(color);
        return this;
    }

    public EmbedMessage setColor(String hexColor) {
        this.EMBED_BUILDER.setColor(Color.decode(hexColor));
        return this;
    }

    public EmbedMessage setDescription(String message) {
        this.EMBED_BUILDER.setDescription(message);
        return this;
    }

    public EmbedMessage setTitle(String title) {
        this.EMBED_BUILDER.setTitle(title);
        return this;
    }

    public EmbedMessage setTitle(String title, String url) {
        this.EMBED_BUILDER.setTitle(title, url);
        return this;
    }

    public EmbedMessage setAuthor(String name) {
        this.EMBED_BUILDER.setAuthor(name);
        return this;
    }

    public EmbedMessage setAuthor(String name, String url) {
        this.EMBED_BUILDER.setAuthor(name, url);
        return this;
    }

    public EmbedMessage setAuthor(String name, String url, String iconUrl) {
        this.EMBED_BUILDER.setAuthor(name, url, iconUrl);
        return this;
    }

    public EmbedMessage appendDescription(String message) {
        this.EMBED_BUILDER.appendDescription(message);
        return this;
    }

    public EmbedMessage addField(MessageEmbed.Field field) {
        this.EMBED_BUILDER.addField(field);
        return this;
    }

    public EmbedMessage addField(String name, String message, boolean inline) {
        this.EMBED_BUILDER.addField(name, message, inline);
        return this;
    }

    public EmbedMessage addBlankField(boolean inline) {
        this.EMBED_BUILDER.addBlankField(inline);
        return this;
    }

    public Message send() {
        return this.CHANNEL.sendMessage(build()).complete();
    }

    public void send(Consumer<Message> action) {
        this.CHANNEL.sendMessage(build()).queue(action);
    }
    public MessageEmbed build() {
        return this.EMBED_BUILDER.build();
    }

}
