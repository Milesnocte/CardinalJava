package Listeners;

import Main.Credentials;
import Main.Hikari;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StarBoardListener extends ListenerAdapter {

    private ArrayList<String> UNCCServers = new ArrayList<>(List.of(
            "433825343485247499", // Party Hours
            "433825343485247499", // Woodward Hours
            "935650201291620392", // Charlotte Haven
            "778743841187823636" // Fretwell hours
    ));


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (UNCCServers.contains(event.getGuild().getId())) {
            if (!event.getChannel().getName().equals("star-board")) {
                switch (event.getReaction().getEmoji().getAsReactionCode()) {
                    case "starfroot:991751462302519316" -> {
                        if (!getExists(event.getMessageId())) {
                            addMessage(event);
                        }
                        addStar(event.getMessageId());
                    }
                    case "antifroot:991751461144887337" -> {
                        if (!getExists(event.getMessageId())) {
                            addMessage(event);
                        }
                        revokeStar(event.getMessageId());
                    }
                    case "debugfroot:936344152004755456" -> {
                        if (!getExists(event.getMessageId())) {
                            addMessage(event);
                        }
                        if(getPosted(event.getMessageId())) {
                            return;
                        }
                        if (event.getMember().getId().equals(Credentials.OWNER)) {
                            postMessage(event);
                            setPosted(event.getMessageId());
                        }
                    }
                    default -> {
                        return;
                    }
                }

                int stars = getStars(event.getMessageId());
                
                if (stars >= 5 && !getPosted(event.getMessageId())) {
                    setPosted(event.getMessageId());
                    postMessage(event);
                }
            }
        }
    }


    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if (!event.getChannel().getName().equals("star-board")) {
            switch (event.getReaction().getEmoji().getAsReactionCode()) {
                case "starfroot:991751462302519316" -> revokeStar(event.getMessageId());
                case "antifroot:991751461144887337" -> addStar(event.getMessageId());
            }
        }
    }

    private boolean getExists(String messageID){
        try {
            boolean result = false;
            Connection connect = Hikari.getConnection();
            ResultSet inStarBoard = connect.createStatement().executeQuery("SELECT * FROM starboard WHERE message_id = '" + messageID + "';");
            result = inStarBoard.next();
            connect.close();
            return result;
        } catch (Exception ignored) {}
        return false;
    }

    private int getStars(String messageID) {
        try {
            Connection connect = Hikari.getConnection();
            ResultSet inStarBoard = connect.createStatement().executeQuery("SELECT stars FROM starboard WHERE message_id = '" + messageID + "';");
            int result = 0;
            if(inStarBoard.next()) {
                result = inStarBoard.getInt(1);
            }
            connect.close();
            return result;
        } catch (Exception ignored) {}
        return 0;
    }

    private boolean getPosted(String messageID){
        try {
            boolean result = false;
            Connection connect = Hikari.getConnection();
            ResultSet inStarBoard = connect.createStatement().executeQuery("SELECT posted FROM starboard WHERE message_id = '" + messageID + "';");
            if(inStarBoard.next()) {
                result = inStarBoard.getBoolean(1);
                connect.close();
            }
            connect.close();
            return result;
        } catch (Exception ignored) {}
        return true;
    }

    private void addStar(String messageID){
        try {
            Connection connect = Hikari.getConnection();
            connect.createStatement().execute("UPDATE starboard SET stars = stars + 1 WHERE message_id = '" + messageID + "';");
            connect.close();
        }catch (Exception ignored){}
    }

    private void revokeStar(String messageID){
        try {
            Connection connect = Hikari.getConnection();
            connect.createStatement().execute("UPDATE starboard SET stars = stars - 1 WHERE message_id = '" + messageID + "';");
            connect.close();
        }catch (Exception ignored){}
    }

    private void setPosted(String messageID){
        try {
            Connection connect = Hikari.getConnection();
            connect.createStatement().execute("UPDATE starboard SET posted = 1 WHERE message_id = '" + messageID + "';");
            connect.close();
        }catch (Exception ignored){}
    }

    private void addMessage(MessageReactionAddEvent event) {
        RestAction<Message> action = event.getChannel().retrieveMessageById(event.getMessageId());
        Message message = action.complete();

        try {
            Connection connect = Hikari.getConnection();
            PreparedStatement prepared;
            prepared = connect.prepareStatement("INSERT INTO starboard values(?,?,?,?,?,?);");
            prepared.setString(1,event.getGuild().getId());
            prepared.setString(2, event.getMessageId());
            prepared.setString(3, message.getAuthor().getAsMention());
            prepared.setInt(4, 0);
            prepared.setInt(5, 0);
            prepared.setTimestamp(6, Timestamp.from(Instant.now()));
            prepared.execute();
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postMessage(MessageReactionAddEvent event){
        RestAction<Message> action = event.getChannel().retrieveMessageById(event.getMessageId());
        Message message = action.complete();
        EmbedBuilder embed = new EmbedBuilder();

        embed.setAuthor(message.getAuthor().getAsTag(), message.getJumpUrl(), message.getAuthor().getAvatarUrl());
        if (message.getReferencedMessage() != null) {
            embed.addField("**Reply to " + message.getReferencedMessage().getAuthor().getAsTag() + "**",
                    message.getReferencedMessage().getContentRaw(), false);
        }
        if (!message.getContentDisplay().isBlank()) {
            embed.addField("**Message**", message.getContentRaw(), false);
        }
        if (message.getAttachments().size() > 0) {
            embed.setImage(message.getAttachments().get(0).getUrl());
        }
        embed.setFooter("Sent on: " + message.getTimeCreated().atZoneSameInstant(ZoneId.of("America/New_York"))
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")));

        event.getGuild().getTextChannelsByName("star-board", true).get(0)
                .sendMessageEmbeds(embed.build()).setActionRow(Button.link(message.getJumpUrl(),"Jump to message")).queue();
    }
}