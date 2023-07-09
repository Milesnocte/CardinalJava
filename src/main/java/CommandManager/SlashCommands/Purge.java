package CommandManager.SlashCommands;

import CommandManager.ISlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class Purge implements ISlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) throws Exception {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {

            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

            int num_messages = (int) event.getOption("num_messages").getAsLong();

            if (num_messages <= 100 && num_messages >= 2) {
                List<Message> messages = event.getChannel().getHistory().retrievePast(num_messages).complete();

                messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));

                event.getGuild().getTextChannelById(event.getChannel().getId()).deleteMessages(messages).complete();
                event.reply("Deleted `" + num_messages + "` messages").queue();
                event.getGuild().getTextChannelById("582399042240118814").sendMessage("Deleted `" + num_messages
                        + "` messages in " + event.getGuild().getTextChannelById(event.getChannel().getId()).getAsMention()
                        + " by " + event.getMember().getAsMention()).queue();
            } else {
                event.reply(num_messages + " is not within the range of `2-100`, please try again with a value in that range.").queue();
            }
        } else {
            event.reply("You do not have permission to run this command!").queue();
        }
    }

    @Override
    public void run(ButtonInteractionEvent event) throws Exception {
    }

    @Override
    public void run(StringSelectInteractionEvent event) throws Exception {

    }

    @Override
    public List<String> buttons() {
        return Collections.emptyList();
    }

    @Override
    public String commandName() {
        return "purge";
    }

    @Override
    public Boolean enabled() {
        return true;
    }

    @Override
    public String description() {
        return null;
    }
}