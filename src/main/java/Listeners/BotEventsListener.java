package Listeners;

import CommandManager.SlashCommandData;
import Main.FetchUNCC;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import Main.ScheduledTask;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.text.NumberFormat;
import java.util.HashMap;

public class BotEventsListener extends ListenerAdapter
{
    private HashMap<String, String> messageLog = new HashMap<>();

    private String[] UNCCServers = {
            "931663140687585290", // Party Hours
            "433825343485247499", // Woodward Hours
            "869677292610285628" // Test server
    };

    @Override
    public void onReady(ReadyEvent event) {

        HashMap<String, String> messageLog = new HashMap<>();

        new ScheduledTask(event);
        event.getJDA().updateCommands().addCommands(
                SlashCommandData.commands
        ).queue();

        for(String server : UNCCServers) {
            event.getJDA().getGuildById(server).updateCommands().addCommands(
                    SlashCommandData.UNCCcommands
            ).queue();
        }

        try {
            new FetchUNCC().screenshot();
            int users = 0;
            for (Guild guild : event.getJDA().getGuilds()) {
                users += guild.getMemberCount();
            }
            String userCount = NumberFormat.getNumberInstance().format(users);
            event.getJDA().getPresence().setActivity(Activity.watching( userCount + " Users"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if(event.getGuild().getId().equals("931663140687585290") && !event.getAuthor().isBot()) {
            String[] messageArray = messageLog.get(event.getMessageId()).split(";", 2);
            Member member = event.getGuild().getMemberById(messageArray[0]);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("@" + member.getUser().getAsTag().split("#")[0], member.getEffectiveAvatarUrl());
            embedBuilder.setDescription("Message edited in " + event.getChannel().getAsMention());
            embedBuilder.addField("Old Message", messageArray[1], false);
            embedBuilder.addField("Message", event.getMessage().getContentRaw(), false);

            event.getGuild().getTextChannelById("1124396190050558102").sendMessageEmbeds(embedBuilder.build()).queue();

            messageLog.replace(event.getMessageId(), event.getAuthor().getId() + ";" + event.getMessage().getContentRaw());
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if(event.getGuild().getId().equals("931663140687585290")) {
            String[] messageArray = messageLog.get(event.getMessageId()).split(";", 2);

            Member member = event.getGuild().getMemberById(messageArray[0]);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("@" + member.getUser().getAsTag().split("#")[0], member.getEffectiveAvatarUrl());
            embedBuilder.setDescription("Message deleted in " + event.getChannel().getAsMention());
            embedBuilder.addField("Message",messageArray[1], false);

            event.getGuild().getTextChannelById("1124396190050558102").sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(event.getGuild().getId().equals("931663140687585290") && !event.getAuthor().isBot())
        {
            messageLog.put(event.getMessageId(), event.getAuthor().getId() + ";" + event.getMessage().getContentRaw());
            if(messageLog.size() == 100)
            {
                messageLog.remove(messageLog.keySet().toArray()[messageLog.size()-1]);
            }
        }

        String message = event.getMessage().getContentRaw();
        String regex = ".*(?<![0-9])1984(?![0-9]).*";

        if (message.matches(regex)) {
            try {
                event.getMessage().addReaction(event.getJDA().getGuildById("433825343485247499").getEmojiById("874821152650965022")).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
