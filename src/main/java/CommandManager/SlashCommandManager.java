package CommandManager;

import CommandManager.SlashCommands.*;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Strings;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandManager
{
    private final Map<String, ISlashCommand> commands;
    
    SlashCommandManager() {
        commands = new HashMap<>();
        addCommand(new Ping());
        addCommand(new Menus());
        addCommand(new WhoIs());
        addCommand(new Avatar());
        addCommand(new Purge());
        addCommand(new Stats());
        addCommand(new Define());
        addCommand(new EightBall());
        addCommand(new Restart());
        addCommand(new Shutdown());
        addCommand(new StarCheck());
        addCommand(new UNCC());
        addCommand(new LaTex());
        addCommand(new Rank());
    }
    
    private void addCommand(ISlashCommand c) {
        this.commands.putIfAbsent(c.commandName(), c);
        if (!c.buttons().isEmpty()) {
            for (String k : c.buttons()) {
                this.commands.putIfAbsent(k, c);
            }
        }
    }
    
    void run(SlashCommandInteractionEvent event) throws Exception {
        String name = event.getName();
        if (event.getUser().isBot()) {
            return;
        }
        if (this.commands.containsKey(name)) {
            if (this.commands.get(name).enabled()) {
                this.commands.get(name).run(event);
            }
            else {
                event.reply("This command is disabled!").setEphemeral(true).queue();
            }
        }
    }
    
    void run(ButtonInteractionEvent event) throws Exception {
        String name = event.getComponentId();
        if (event.getUser().isBot()) {
            return;
        }
        if (this.commands.containsKey(name) && this.commands.get(name).enabled()) {
            this.commands.get(name).run(event);
        }
    }
    
    void run(StringSelectInteractionEvent event) throws Exception {
        String name = event.getComponentId();
        if (event.getUser().isBot()) {
            return;
        }
        if (this.commands.containsKey(name) && this.commands.get(name).enabled()) {
            this.commands.get(name).run(event);
        }
    }
}
