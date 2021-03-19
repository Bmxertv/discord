package de.bmxertv.discord.api.embed;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class PreviewMessage {

    final Map<Member, Map.Entry<Message, EmbedMessage>> MAP = new HashMap<>();
    final Member MEMBER;
    final EmbedMessage EMBED_MESSAGE;

    /**
     * Initialisiert eine PreviewMessage
     * @param member Member der sie erstellt hat
     * @param channel Channel in der sie Angezeigt werden soll
     */
    public PreviewMessage(Member member, TextChannel channel) {
        this.EMBED_MESSAGE = getEmbedMessage(member);
        this.MEMBER = member;

        if (!this.MAP.containsKey(member)) {
            Message message = channel.sendMessage(this.EMBED_MESSAGE.build()).complete();
            this.MAP.put(member, new AbstractMap.SimpleEntry<Message, EmbedMessage>(message, this.EMBED_MESSAGE));
        }
    }

    /**
     * Gibt die PreviewMessage zurück
     * @param member Member der sie erstellt hat
     * @return EmbedMessage
     */
    public EmbedMessage getEmbedMessage(Member member) {
        return this.MAP.containsKey(member) ? this.MAP.get(member).getValue() : new EmbedMessage().setTitle("Preview").setColor("#FFFFFF");
    }

    /**
     * Updatet die PreviewMessage
     * @param embedMessage EmbedMessage
     * @return PreviewMessage
     */
    public PreviewMessage update(EmbedMessage embedMessage) {
        Message message = this.MAP.get(this.MEMBER).getKey();
        message.editMessage(embedMessage.build()).complete();
        this.MAP.replace(this.MEMBER, new AbstractMap.SimpleEntry<Message, EmbedMessage>(message, embedMessage));
        return this;
    }

    /**
     * Sendet die Nachricht
     * @param channel TextChannel in der die Nachricht zu sehen sein soll
     */
    public void finish(TextChannel channel) {
        this.MAP.get(this.MEMBER).getKey().delete().queue();
        EmbedMessage embedMessage = this.MAP.get(this.MEMBER).getValue();
        channel.sendMessage(embedMessage.build()).queue();
        this.MAP.remove(this.MEMBER);
    }

}
