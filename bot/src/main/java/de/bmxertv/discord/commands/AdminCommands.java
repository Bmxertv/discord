package de.bmxertv.discord.commands;

import de.bmxertv.discord.Application;
import de.bmxertv.discord.annotations.Category;
import de.bmxertv.discord.annotations.NoHelp;
import de.bmxertv.discord.api.command.APICommand;
import de.bmxertv.discord.api.command.annotation.Command;
import de.bmxertv.discord.api.embed.EmbedMessage;
import de.bmxertv.discord.api.embed.PreviewMessage;
import de.bmxertv.discord.enums.CategoryEnum;
import de.bmxertv.discord.files.Property;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCommands {

    private final Map<Member, PreviewMessage> newsMap = new HashMap<>();

    private boolean isAdmin(Member member) {
        return member.getRoles().stream()
                .map(Role::getIdLong)
                .filter(l -> l.equals(Application.instance.CONFIG().ADMIN_ID()))
                .findFirst()
                .isPresent();
    }

    @Category(CategoryEnum.MODERATION)
    @Command(value = "nachricht", description = "Command um Nachrichten zu erstellen",
            aliases = "msg",
            subCommands = {
                    "TITLE [TEXT]",
                    "COLOR [HEX]",
                    "DESCRIPTION [TEXT]",
                    "FIELD [NAME;TEXT]",
                    "THUMBNAIL [URL]",
                    "IMAGE [URL]",
                    "FOOTER [TEXT;URL]",
                    "SEND [#CHANNEL]"
            })
    public void nachricht(APICommand command) {
        if (!isAdmin(command.member())) {
            command.replay("für diesen Command hast du nicht die richtige Berechtigung");
            return;
        }

        if (!this.newsMap.containsKey(command.member())) {
            this.newsMap.put(command.member(), new PreviewMessage(command.member(), command.channel()));
        }

        PreviewMessage previewMessage = this.newsMap.get(command.member());
        EmbedMessage embedMessage = previewMessage.getEmbedMessage(command.member());

        if (command.args().length < 1) {
            command.replay(String.format("bitte gebe mindestens ein Argument an! ```[%s]```", Arrays.stream(command.subCommands()).collect(Collectors.joining(", "))));
            return;
        }

        switch (command.args()[0].toLowerCase()) {
            case "title":
                embedMessage.setTitle(Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" ")));
                break;
            case "color":
                embedMessage.setColor(command.args()[1]);
                break;
            case "description":
                embedMessage.setDescription(Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" ")));
                break;
            case "field":
                String nameText = Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" "));
                embedMessage.addField(nameText.split(";")[0], nameText.split(";")[1], false);
                break;
            case "thumbnail":
                embedMessage.setThumbnail(Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" ")));
                break;
            case "image":
                embedMessage.setImage(Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" ")));
                break;
            case "footer":
                if (Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" ")).contains(";")) {
                    String text = Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" "));
                    embedMessage.setFooter(text.split(";")[0], text.split(";")[1]);
                } else {
                    embedMessage.setFooter(Arrays.stream(command.args()).skip(1).collect(Collectors.joining(" ")));
                }
                break;
            default:
                command.replay(String.format("bitte gebe mindestens ein Argument an! ```[%s]```", Arrays.stream(command.subCommands()).collect(Collectors.joining(", "))));
        }
        if (command.args()[0].toLowerCase().equals("send")) {
            previewMessage.finish(command.message().getMentionedChannels().get(0));
        } else {
            previewMessage.update(embedMessage);
            command.message().delete().queue();
        }
    }

    @Category(CategoryEnum.MODERATION)
    @Command(value = "clear", description = "Leere den Channel", aliases = "c", subCommands = {"FULL", "NUMBER"})
    public void clear(APICommand command) {
        if (!isAdmin(command.member())) {
            command.replay("für diesen Command hast du nicht die richtige Berechtigung");
            return;
        }

        if (command.args().length < 1) {
            command.replay(String.format("gebe bitte ein Argument an!```%s```", Arrays.stream(command.subCommands()).collect(Collectors.joining(", "))));
            return;
        }

        if (command.args()[0].toLowerCase(Locale.ROOT).equals("full")) {
            TextChannel channel = command.channel().createCopy().complete();
            channel.getManager().sync(command.channel()).queue();
            command.channel().delete().queue();
        } else {
            int amount = Integer.parseInt(command.args()[0]);
            command.message().delete().queue();
            if (amount < 2 || amount > 100) {
                command.replay("gib eine anzahl zwischen 2 und 100 ein.");
                return;
            }
            List<Message> messages = command.channel().getHistory().retrievePast(amount).complete();
            command.channel().deleteMessages(messages).queue();
        }
    }

    @NoHelp
    @Command("verify")
    public void verify(APICommand command) {
        if (command.args().length < 1) {
            command.replay("gib bitte die Verifizierungs rollen an [@ROLE]");
            return;
        }
        Role role = command.message().getMentionedRoles().get(0);
        command.message().delete().queue();
        EmbedMessage embedMessage = new EmbedMessage(command.channel());
        embedMessage.setTitle(String.format(Application.instance.TITLE_PATTERN(), "Verifizieren"))
                .setColor(new Color(121, 243, 121))
                .setDescription("```Klicke auf den \u2705 um dich zu Verifizieren.```");

        Message message = embedMessage.send();
        message.addReaction("\u2705").queue();

        Map<String, Object> content = Property.getContent();
        content.put("VERIFY_MESSAGE_ID", message.getIdLong());
        content.put("VERIFY_ROLE_ID", role.getIdLong());
        Property.write(content);

    }



}
