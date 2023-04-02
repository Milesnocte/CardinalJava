package CommandManager;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class SlashCommandData {
    public static final CommandData[] commands = {
            Commands.slash("ping", "Ping the bot"),
            Commands.slash("stats", "Get Cardinal's status"),
            Commands.slash("avatar", "Get user avatar")
                    .addSubcommands(
                    new SubcommandData("server", "server avatar")
                            .addOption(OptionType.USER, "user", "user to get avatar from", false),
                    new SubcommandData("global", "global avatar")
                            .addOption(OptionType.USER, "user", "user to get avatar from", false)
            ),
            Commands.slash("whois", "Get information about user")
                    .addOption(OptionType.USER, "user", "user to get information about", false),
            Commands.slash("purge", "bulk delete")
                    .addOption(OptionType.INTEGER, "num_messages", "number of messages to delete 1-100", true),
            Commands.slash("define", "Get definition of a word")
                    .addOption(
                    OptionType.STRING, "word_to_define", "A word to define", true
            ),
            Commands.slash("latex", "Renders a LaTex formula")
                    .addOption(
                    OptionType.STRING, "content", "LaTex formula", true
            ),
            Commands.slash("eightball", "Shake the 8-ball")
                    .addOption(
                    OptionType.STRING, "question", "Ask the 8-ball a question.", true
            ),
            Commands.slash("shutdown", "shutdown the bot"),
            Commands.slash("restart", "restart the bot"),
    };
    public static final CommandData[] UNCCcommands = {
            Commands.slash("menus", "Role Menus")
                    .addSubcommands(
                    new SubcommandData("yearroles", "create a year role menu"),
                    new SubcommandData("pronounroles", "create a pronoun role menu"),
                    new SubcommandData("collegeroles", "create a college role menu"),
                    new SubcommandData("concentration", "create a concentration role menu"),
                    new SubcommandData("platforms", "create a gaming platform role menu"),
                    new SubcommandData("living", "create a living situation role menu")
            ),
            Commands.slash("uncc", "Role Menus")
                    .addSubcommands(
                            new SubcommandData("sovi", "Get the occupancy of sovi"),
                        new SubcommandData("crown", "Get the occupancy of crown"),
                        new SubcommandData("parking", "Get the occupancy of parking"),
                        new SubcommandData("canvas", "Canvas status"),
                        new SubcommandData("atkins", "Atkins status")
            ),
            Commands.slash("starcheck", "Check the number of stars a user has")
                    .addOption(
                    OptionType.USER, "user", "The user to check", false
            ),
    };
}
