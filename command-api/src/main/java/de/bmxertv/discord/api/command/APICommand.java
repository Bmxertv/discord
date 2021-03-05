package de.bmxertv.discord.api.command;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@Getter
@Accessors(fluent = true)
public class APICommand {

    private final GuildMessageReceivedEvent event;
    private final String[] args;
    private final Member member;
    private final TextChannel channel;
    private final Guild guild;
    private final Message message;
    private final String[] subCommands;

    public APICommand(GuildMessageReceivedEvent event, String[] args, String[] subCommands) {
        this.event = event;
        this.args = args;
        this.member = event.getMember();
        this.channel = event.getChannel();
        this.guild = event.getGuild();
        this.message = event.getMessage();
        this.subCommands = subCommands;
    }

    public void replay(String message) {
        this.channel.sendMessage(this.member.getAsMention() + ", " + message).queue();
    }

    public void replay(MessageEmbed messageEmbed) {
        this.channel.sendMessage(this.member.getAsMention() + ", ").queue(msg -> {
            msg.editMessage(messageEmbed).queue();
        });
    }

}
