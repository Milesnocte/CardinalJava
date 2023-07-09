package CommandManager.SlashCommands;

import CommandManager.ISlashCommand;
import Main.Hikari;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Rank implements ISlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) throws Exception {
        if(event.getOption("user") != null) {
            if(event.getOption("user").getAsUser().isBot()) {
                event.reply("https://tenor.com/view/vegeta-its-over9000-gif-14419267").queue();
                return;
            }
        }

        if(event.getOption("user") == null) {
            Connection connection = Hikari.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT xp FROM levels WHERE member ='" + event.getUser().getId() +"' AND server = '" + event.getGuild().getId() + "';");

            int xp = 0;

            if(resultSet.next())
            {
                xp = resultSet.getInt(1);
            }

            connection.close();

            int level = 0;
            int xpfornext = 0;
            int lastRes = 0;
            for (int i = 0; i < 10000; i++) {
                lastRes = xpfornext;
                xpfornext += (5 * Math.pow(i, 2) + 50 * i + 100);
                if (xpfornext > xp) {
                    level = i+1;
                    break;
                }
            }

            double current = xp - lastRes;
            int xpForNext = xpfornext - lastRes;

            int percentage = (int)((current/xpForNext) * 100);

            int rank = getRankedList(event.getUser().getId(), event.getGuild().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(event.getUser().getAsTag().split("#")[0]);
            if(rank == 0) {
                embedBuilder.setDescription("Level " + level + " - " + (int) current + "/" + xpForNext +
                        "\n" + percentage + "% of the way to level up!");
            }
            else
            {
                embedBuilder.setDescription("Rank " + rank + "\nLevel " + level + " - " + (int) current + "/" + xpForNext +
                        "\n" + percentage + "% of the way to level up!");
            }

            embedBuilder.setFooter("XP total: " + (int) xp);

            event.replyEmbeds(embedBuilder.build()).queue();
        } else {
            Member user = event.getOption("user").getAsMember();
            Connection connection = Hikari.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT xp FROM levels WHERE member ='" + user.getUser().getId() +"' AND server = '" + event.getGuild().getId() + "';");

            int xp = 0;

            if(resultSet.next())
            {
                xp = resultSet.getInt(1);
            }

            connection.close();

            int level = 0;
            int res = 0;
            int lastRes = 0;
            for (int i = 0; i < 100000; i++) {
                lastRes = res;
                res += (5 * Math.pow(i, 2) + 50 * i + 100);
                if (res > xp) {
                    level = i+1;
                    break;
                }
            }

            double current = xp - lastRes;
            int xpForNext = res - lastRes;

            int percentage = (int)((current/xpForNext) * 100);

            EmbedBuilder embedBuilder = new EmbedBuilder();

            int rank = getRankedList(user.getId(), event.getGuild().getId());

            embedBuilder.setTitle(user.getUser().getAsTag().split("#")[0]);
            if(rank == 0) {
                embedBuilder.setDescription("Level " + level + " - " + (int) current + "/" + xpForNext +
                        "\n" + percentage + "% of the way to level up!");
            }
            else
            {
                embedBuilder.setDescription("Rank " + rank + "\nLevel " + level + " - " + (int) current + "/" + xpForNext +
                        "\n" + percentage + "% of the way to level up!");
            }

            event.replyEmbeds(embedBuilder.build()).queue();
            connection.close();
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
        return "rank";
    }

    @Override
    public Boolean enabled() {
        return true;
    }

    @Override
    public String description() {
        return "Get your or someone else's Rank";
    }

    private int getRankedList(String MemberId, String server) throws SQLException {
        Connection connection = Hikari.getConnection();
        Statement prepared = connection.createStatement();
        ResultSet result = prepared.executeQuery(
                "SELECT row_number() over (ORDER BY xp DESC) RowNum, member FROM levels where server = '"+ server + "';"
        );

        HashMap<String, Integer> dict = new HashMap<>();

        while (result.next()) {
            dict.put(result.getString(2), result.getInt(1));
        }

        connection.close();
        if(dict.get(MemberId) == null)
        {
            return 0;
        }
        else
        {
            return dict.get(MemberId);
        }


    }
}
