package CommandManager.SlashCommands;

import CommandManager.ISlashCommand;
import Main.Credentials;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import java.util.Collections;
import java.util.List;

public class Shutdown implements ISlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) throws Exception {
        if (event.getMember().getId().equals(Credentials.OWNER)) {
            event.reply("Shutting down...").queue();
            Thread.sleep(1000);
            System.exit(0);
        } else {
            event.reply("Why would you even try this command? (Not Bot Owner)").queue();
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
        return "shutdown";
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