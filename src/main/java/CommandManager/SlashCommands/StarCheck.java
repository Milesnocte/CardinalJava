package CommandManager.SlashCommands;

import CommandManager.ISlashCommand;
import Main.Hikari;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

public class StarCheck implements ISlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) throws Exception {
        String author;
        if (event.getOption("user") != null) {
            author = event.getOption("user").getAsUser().getAsMention();
        } else {
            author = event.getMember().getUser().getAsMention();
        }

        int stars = 0;

        try {
            stars = Integer.parseInt(getStarsSum(event));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String starsFormat = NumberFormat.getNumberInstance().format(stars);

        event.reply(author + " has " + starsFormat + "<:starfroot:991751462302519316>")
                .setAllowedMentions(Collections.emptyList()).queue();
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
        return "starcheck";
    }

    @Override
    public Boolean enabled() {
        return true;
    }

    @Override
    public String description() {
        return null;
    }

    public String getStarsSum(SlashCommandInteractionEvent event) throws ClassNotFoundException, SQLException {
        String author;
        String stars;
        if (event.getOption("user") != null) {
            author = event.getOption("user").getAsUser().getId();
        } else {
            author = event.getMember().getUser().getId();
        }
        Connection connect = Hikari.getConnection();
        ResultSet result = connect.createStatement().executeQuery("SELECT sum(stars) FROM starboard WHERE guild_id ='"+ event.getGuild().getId() +"' AND author ilike '%"+ author +"%';");

        if(result.next()) {
            stars = result.getString(1);
            connect.close();
            return stars;
        }
        else
        {
            return "0";
        }
    }
}