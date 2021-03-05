package de.bmxertv.discord.commands;

import de.bmxertv.discord.Application;
import de.bmxertv.discord.annotations.Category;
import de.bmxertv.discord.annotations.NoHelp;
import de.bmxertv.discord.api.command.APICommand;
import de.bmxertv.discord.api.command.annotation.Command;
import de.bmxertv.discord.api.embed.EmbedMessage;
import de.bmxertv.discord.enums.CategoryEnum;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class UserCommands {

    private boolean isAdmin(Member member) {
        return member.getRoles().stream()
                .map(Role::getIdLong)
                .filter(l -> l.equals(Application.instance.CONFIG().ADMIN_ID()))
                .findFirst()
                .isPresent();
    }

    @Category(CategoryEnum.UTILITY)
    @Command(value = "ping", description = "Ein Einfacher Ping Befehl")
    public void ping(APICommand command) {
        command.replay("pong!");
    }

    @Category(CategoryEnum.UTILITY)
    @Command(value = "help", aliases = {"?", "h"}, description = "Der Hilfe Befehl")
    public void help(APICommand command) {
        EmbedMessage embedMessage = new EmbedMessage(command.channel());

        embedMessage.setColor(new Color(86, 109, 255));
        embedMessage.setTitle(String.format(Application.instance.TITLE_PATTERN(), "HILFE"));

        Map<String, Map.Entry<Method, Object>> command_map = Application.instance.COMMAND_MANAGER().getCOMMAND_MAP();
        List<String> usedCommands = new ArrayList<>();
        command_map.forEach((s, methodObjectEntry) -> {
            boolean helpAllowed = true;
            if (methodObjectEntry.getKey().getAnnotation(NoHelp.class) != null)
                helpAllowed = false;

            Category category = methodObjectEntry.getKey().getAnnotation(Category.class);
            Command commandData = methodObjectEntry.getKey().getAnnotation(Command.class);
            if (!usedCommands.contains(commandData.value())) {
                if (helpAllowed) {
                    String subCommands = commandData.subCommands().length > 1 ? "```[" + Arrays.stream(commandData.subCommands()).collect(Collectors.joining(", ")) + "]```" : "";
                    embedMessage.addField(
                            String.format("%s: %s", category.value().name(), commandData.value()),
                            String.format("```%s```%s", commandData.description(), subCommands), false);
                    usedCommands.add(commandData.value());
                }
            }
        });
        embedMessage.send();
    }

    @Category(CategoryEnum.MODERATION)
    @Command(value = "info", description = "Zeigt dir Informationen über unterschiedliche sachen an", subCommands = {"BOT", "SERVER", "USER"})
    public void info(APICommand command) {
        if (command.args().length < 1) {
            command.replay("bitte gib ein Argument an. [BOT, SERVER oder USER]");
            return;
        }
        EmbedMessage embedMessage = new EmbedMessage(command.channel());
        JDA jda = Application.instance.jda();
        switch (command.args()[0].toLowerCase(Locale.ROOT)) {
            case "bot":
                embedMessage.setTitle(String.format(Application.instance.TITLE_PATTERN(), jda.getSelfUser().getName()), Application.instance.CONFIG().WEBSITE())
                        .setColor(new Color(255, 123, 123))
                        .setDescription("Informationen zum Bot")
                        .setThumbnail(jda.getSelfUser().getAvatarUrl())
                        .addField("Name", String.format("```%s```", jda.getSelfUser().getName()), false)
                        .addField("Developer", String.format("```%s```", "Bmxertv_YT"), false)
                        .addField("Version", String.format("```%s```", Application.instance.CONFIG().VERSION()), false)
                        .addField("Prefix", String.format("```%s```", Application.instance.CONFIG().PREFIX()), false);
                break;
            case "server":
                embedMessage.setTitle(String.format(Application.instance.TITLE_PATTERN(), command.guild().getName()), Application.instance.CONFIG().WEBSITE())
                        .setColor(new Color(123, 255, 240))
                        .setDescription("Informationen zum Server")
                        .setThumbnail(command.guild().getIconUrl())
                        .addField("Name", String.format("```%s```", command.guild().getName()), false)
                        .addField("Server Owner", String.format("```%s```",
                                command.guild().getOwner().getEffectiveName()), false)
                        .addField("Member Anzahl", String.format("```%d```",
                                command.guild().getMembers().stream().filter(member -> !member.getUser().isBot()).count()),
                                false)
                        .addField("Bot Anzahl", String.format("```%d```",
                                command.guild().getMembers().stream().filter(member -> member.getUser().isBot()).count()),
                                false)
                        .addField("Boosts", String.format("```%s```",
                                command.guild().getBoostCount()), false);
                break;
            case "user":
                Member target = command.message().getMentionedMembers().get(0);
                embedMessage.setTitle(String.format(Application.instance.TITLE_PATTERN(), target.getEffectiveName()), Application.instance.CONFIG().WEBSITE())
                        .setDescription(String.format("Informationen zum Nutzer %s", target.getEffectiveName()))
                        .setColor(target.getColor())
                        .setThumbnail(target.getUser().getAvatarUrl())
                        .addField("Name", String.format("```%s```", target.getEffectiveName()), false)
                        .addField("Id", String.format("```%s```", target.getId()), false)
                        .addField("Nutzer Tag", String.format("```%s```", target.getUser().getAsTag().split("#")[1]), false)
                        .addField("Aktivier Sprachchannel", String.format("```%s```",
                                target.getVoiceState().inVoiceChannel() ? target.getVoiceState().getChannel().getName() : "Kein Aktivier Sprachchannel"),
                                false)
                        .addField("Beitrittsdatum", String.format("```%s```",
                                target.getTimeJoined().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))), false)
                        .addField("Rollen Anzahl", String.format("```%s```", target.getRoles().size()), false)
                        .addField("Rollen", String.format("```%s```",
                                target.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "))),
                                false);
                break;
            default:
                command.replay("bitte gib ein Argument an. [BOT, SERVER oder USER]");
        }
        embedMessage.send();
    }


}
