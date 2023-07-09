import java.util.concurrent.TimeUnit;

import Listeners.XpListener;
import Main.Credentials;
import Main.MessageCmd;
import net.dv8tion.jda.api.OnlineStatus;
import Listeners.StarBoardListener;
import Listeners.JoinLeaveListener;
import Listeners.BotEventsListener;
import CommandManager.SlashCommandListener;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDA extends ListenerAdapter implements EventListener
{
    public static void main(String[] args) throws Exception {
        DefaultShardManagerBuilder
                .create(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.MESSAGE_CONTENT)
                .setToken(Credentials.TOKEN)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .addEventListeners(
                        new SlashCommandListener(),
                        new BotEventsListener(),
                        new JoinLeaveListener(),
                        new StarBoardListener(),
                        new MessageCmd(),
                        new XpListener()
                ).setStatus(OnlineStatus.ONLINE).build();
                
        TimeUnit.SECONDS.sleep(3);
        System.out.println("****************************************\n*********    BOT LOGGED IN    *********\n****************************************\n" +
                "\nInvite URL: \nhttps://discord.com/oauth2/authorize?client_id=776643673265012766&permissions=8&scope=bot+applications.commands\n");
    }
}
