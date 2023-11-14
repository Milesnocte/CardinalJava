package Main;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.Timer;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ScheduledTask extends ListenerAdapter
{

    public ScheduledTask(ReadyEvent event) {
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                if (minute % 5 == 0)
                {
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

                if(hour == 1 && minute == 0)
                {
                    try {
                        Runtime.getRuntime().exec("sh cardinal.sh");
                        System.exit(0);
                    } catch (IOException e) {
                        event.getJDA().getGuildById("433825343485247499")
                                .getTextChannelById("989301601476943882")
                                .sendMessage("Scheduled restart failed : ```" + e + "```").queue();
                    }
                }
            }
        };
        timer.schedule(tt, 1000L, 60000L);
    }
}
