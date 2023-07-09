package Main;

import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageCmd extends ListenerAdapter
{
    private List<String> UNCCServers = List.of(
            "931663140687585290", // Party Hours
            "433825343485247499", // Woodward Hours
            "935650201291620392", // Charlotte Haven
            "778743841187823636", // Fretwell hours
            "869677292610285628" // Test server
    );


    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        if(UNCCServers.contains(event.getGuild().getId())) {
            ActionRow SchoolsRow = ActionRow.of(
                    StringSelectMenu.create("schools").addOptions(
                            SelectOption.of("UNCG", "UNCG"),
                            SelectOption.of("ECU", "ECU"),
                            SelectOption.of("NCSU", "NCSU"),
                            SelectOption.of("WCU", "WCU"),
                            SelectOption.of("UNC", "UNC"),
                            SelectOption.of("UNCC", "UNCC"),
                            SelectOption.of("UNCW", "UNCW"),
                            SelectOption.of("NCA&T", "NCA&T"),
                            SelectOption.of("APP", "APP"),
                            SelectOption.of("UNCA", "UNCA"),
                            SelectOption.of("ECSU", "ECSU"),
                            SelectOption.of("WSSU", "WSSU"),
                            SelectOption.of("FSU", "FSU"),
                            SelectOption.of("NCCU", "NCCU"),
                            SelectOption.of("UNCP", "UNCP"),
                            SelectOption.of("NCSA", "NCSA"),
                            SelectOption.of("NCSSM", "NCSSM"),
                            SelectOption.of("Future Student", "Future_Student")
                    ).setRequiredRange(0, 1).setPlaceholder("Select Your School").build()
            );
            ActionRow YearRow = ActionRow.of(
                    StringSelectMenu.create("years").addOptions(
                            SelectOption.of("Incoming Student", "Year_Incoming"),
                            SelectOption.of("Freshman", "Year_Freshman"),
                            SelectOption.of("Sophomore", "Year_Sophomore"),
                            SelectOption.of("Junior", "Year_Junior"),
                            SelectOption.of("Senior", "Year_Senior"),
                            SelectOption.of("Graduate Student", "Year_Grad"),
                            SelectOption.of("Alumni", "Year_Alumni")
                    ).setRequiredRange(0, 1).setPlaceholder("Select Your Year").build()
            );
            ActionRow PronRow = ActionRow.of(
                    StringSelectMenu.create("pronouns")
                            .addOptions(
                                    SelectOption.of("He/Him", "Pron_He"),
                                    SelectOption.of("She/Her", "Pron_She"),
                                    SelectOption.of("They/Them", "Pron_They"),
                                    SelectOption.of("He/They", "Pron_HeThey"),
                                    SelectOption.of("She/They", "Pron_SheThey"),
                                    SelectOption.of("Ask Me", "Pron_Ask")
                            ).setRequiredRange(0, 6).setPlaceholder("Select Your Pronouns").build()
            );
            ActionRow PingsRow = ActionRow.of(
                    // Button.primary("EventPing","Events"),
                    // Button.primary("VCPing","VC Ping"),
                    // Button.primary("ChatPing","Revive Chat")
                    Button.primary("MeetupPing", "Meetup Pings"),
                    Button.primary("EventPing", "Event Pings")
            );

            ActionRow AlumnRoles = ActionRow.of(
                    Button.primary("Alum_Bachelors", "Bachelors"),
                    Button.primary("Alum_Masters", "Masters"),
                    Button.primary("Alum_PHD", "PHD")
            );

            ActionRow CollegeRow = ActionRow.of(
                    StringSelectMenu.create("college")
                            .addOptions(
                                    SelectOption.of("Data Science", "College_Data"),
                                    SelectOption.of("Liberal Arts and Sciences", "College_Liberal"),
                                    SelectOption.of("Health and Human Services", "College_Health"),
                                    SelectOption.of("Engineering", "College_Engineering"),
                                    SelectOption.of("Education", "College_Education"),
                                    SelectOption.of("Computing and Informatics", "College_Computing"),
                                    SelectOption.of("Arts and Architectures", "College_Arts"),
                                    SelectOption.of("Business", "College_Business"),
                                    SelectOption.of("Undeclared", "College_Undec")
                            ).setRequiredRange(0, 2).setPlaceholder("Select Your Major(s)").build()
            );

            ActionRow ConcentrationRow = ActionRow.of(
                    StringSelectMenu.create("concentration")
                            .addOptions(
                                    SelectOption.of("Software Engineering", "Conc_SE"),
                                    SelectOption.of("Bioinformatics", "Conc_Bioinformatics"),
                                    SelectOption.of("AI, Robotics, and Gaming", "Conc_ARG"),
                                    SelectOption.of("Data Science", "Conc_DataScience"),
                                    SelectOption.of("Information Technology", "Conc_IT"),
                                    SelectOption.of("Web and Mobile", "Conc_WM"),
                                    SelectOption.of("Human-Computer Interaction", "Conc_HCI"),
                                    SelectOption.of("Cybersecurity", "Conc_Cybersecurity"),
                                    SelectOption.of("Software, Systems, and Networks", "Conc_SSN"),
                                    SelectOption.of("Undeclared", "Conc_UD")
                            ).setRequiredRange(0, 1).setPlaceholder("Select Your Concentration").build()
            );


            if (event.getAuthor().getId().equals("225772174336720896") && event.getMessage().getContentRaw().startsWith("$combinedmenu")) {
                event.getMessage().delete().queue();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setImage("https://cdn.discordapp.com/attachments/482254335666028574/991745222637785158/LinkedIn_cover_-_1.png");
                embed.setTitle("Roles Menu");
                embed.setDescription("Below you can select your Year, Major(s), and Concentration.");
                event.getChannel().sendMessageEmbeds(embed.build()).setComponents(YearRow, CollegeRow, ConcentrationRow).queue();
                embed.clear();
                embed.setTitle("Ping Roles");
                embed.setDescription("Click the buttons below to be added to the ping roles. \nIf you have the role clicking it will remove you from it!");
                event.getChannel().sendMessageEmbeds(embed.build()).setComponents(PingsRow).queue();
            }

            if (event.getAuthor().getId().equals("225772174336720896") && event.getMessage().getContentRaw().startsWith("$alum")) {
                event.getMessage().delete().queue();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Alum Roles");
                embed.setDescription("Select your highest completed degree!");
                event.getChannel().sendMessageEmbeds(embed.build()).setComponents(AlumnRoles).queue();
            }

            if (event.getAuthor().getId().equals("225772174336720896") && event.getMessage().getContentRaw().startsWith("$me")) {
                if (event.getMessage().getReferencedMessage() != null) {
                    event.getChannel().sendMessage(event.getMessage().getContentRaw().replaceFirst(Pattern.quote("$me "), "")).setMessageReference(event.getMessage().getReferencedMessage()).queue();
                    event.getMessage().delete().queue();
                } else {
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage(event.getMessage().getContentRaw().replaceFirst(Pattern.quote("$me "), "")).queue();
                }
            }

            if (event.getMessage().getContentRaw().startsWith("$rphroles") && event.getAuthor().getId().equals("225772174336720896")) {
                event.getChannel().sendMessage("Select game ping roles, if im missing any let me know, these are easy to add").setComponents(ActionRow.of(StringSelectMenu.create("rphgames").addOptions(SelectOption.of("CSGO", "CSGO"), SelectOption.of("Valorant", "Valorant"), SelectOption.of("Overcooked 2", "Overcooked 2"), SelectOption.of("TF2", "TF2"), SelectOption.of("League", "League"), SelectOption.of("Overwatch", "Overwatch")).setRequiredRange(1, 5).setPlaceholder("Select games").build())).queue();
            }

            if (event.getGuild().getId().equals("931663140687585290") && event.getMessage().getContentRaw().startsWith("!")) {
                Random rand = new Random();
                int rng = rand.nextInt(10) + 1;
                String person = event.getMessage().getContentRaw().replace("!", "").split(" ")[0].toLowerCase();
                if (rng <= 9) {
                    switch (person) {
                        case "miles" -> event.getChannel().sendMessage("future discord developer").queue();
                        case "yamez" -> event.getChannel().sendMessage("CS:GO Pro").queue();
                        case "michael" -> event.getChannel().sendMessage("Lord of the weebs").queue();
                        case "tokyo" -> event.getChannel().sendMessage("Tokyo drift").queue();
                        case "nicole" -> event.getChannel().sendMessage("currently crying because of linear regression").queue();
                        case "bambi" -> event.getChannel().sendMessage("bank babe").queue();
                        case "kels" -> event.getChannel().sendMessage("Mom of the server").queue();
                        case "carney" -> event.getChannel().sendMessage("looking for: brown girl").queue();
                        case "moe" -> event.getChannel().sendMessage("Chick magnet").queue();
                        case "yosi" -> event.getChannel().sendMessage("https://tenor.com/view/big-bird-angry-door-fall-gif-11404358").queue();
                        case "lefty" -> event.getChannel().sendMessage("ginger giant").queue();
                        case "mitchell" -> event.getChannel().sendMessage("SASS Lord").queue();
                    }
                } else {
                    event.getChannel().sendMessage("https://tenor.com/view/who-dat-snoop-gif-15116696").queue();
                }
            }
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull final StringSelectInteractionEvent event) {
        if(event.getGuild().getId().equals("931663140687585290")) {
            Role Chillin = event.getGuild().getRolesByName("Chillin", true).get(0);
            Role Weeb = event.getGuild().getRolesByName("Weeb", true).get(0);
            Role SASS = event.getGuild().getRolesByName("SASS", true).get(0);
            Role MaterialGirl = event.getGuild().getRolesByName("Material Girl", true).get(0);
            ArrayList<Role> roles = new ArrayList<Role>();
            String rolesString = "";
            if (event.getMember().isBoosting()) {
                roles.add(event.getGuild().getBoostRole());
            }
            if (event.getMember().getRoles().contains(Chillin)) {
                roles.add(Chillin);
            }
            if (event.getMember().getRoles().contains(Weeb)) {
                roles.add(Weeb);
            }
            if (event.getMember().getRoles().contains(SASS)) {
                roles.add(SASS);
            }
            if (event.getMember().getRoles().contains(MaterialGirl)) {
                roles.add(MaterialGirl);
            }
            for (String role : event.getValues()) {
                rolesString = rolesString + ", " + role;
                roles.add(event.getGuild().getRolesByName(role, true).get(0));
            }
            rolesString = rolesString.replaceFirst(",", "");
            event.getGuild().modifyMemberRoles(event.getMember(), roles).queue();
            event.reply(rolesString).setEphemeral(true).queue();
        }
    }
}
