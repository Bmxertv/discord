package de.bmxertv.discord.events;

import de.bmxertv.discord.files.Property;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Map<String, Object> property = Property.getContent();
        if (Long.parseLong(property.get("VERIFY_MESSAGE_ID").toString()) == event.getMessageIdLong()) {
            event.getGuild().addRoleToMember(event.getUserId(), event.getGuild().getRoleById(Long.parseLong(property.get("VERIFY_ROLE_ID").toString()))).queue();
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }

}
