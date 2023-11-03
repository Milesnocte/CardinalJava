package Listeners;

import Main.Hikari;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

public class XpListener extends ListenerAdapter {

    HashMap<String, ArrayList<String>> xpDict = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try
        {
            xpDict.putIfAbsent(event.getGuild().getId(), new ArrayList<String>());

            if (xpDict.get(event.getGuild().getId()).contains(Objects.requireNonNull(event.getMember().getId())))
                return;

            ArrayList<String> members = xpDict.get(event.getGuild().getId());
            members.add(event.getMember().getId());
            xpDict.replace(event.getGuild().getId(), members);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

            Timer timer1 = new Timer();
            Timer timer2 = new Timer();

            // Every Minute
            timer1.schedule(new TimerTask() {
                public void run() {
                    try {
                        xpDict.forEach((server, memberList) -> {
                            memberList.forEach((member) -> {
                                try {

                                    Random random = new Random();
                                    int experience = random.nextInt(15) + 10;

                                    boolean exists = false;
                                    Connection connect = Hikari.getConnection();
                                    exists = connect.createStatement().executeQuery("SELECT * FROM levels WHERE member = '" + member + "' AND server = '" + server + "' ;").next();

                                    if (exists) {
                                        connect.createStatement().execute("UPDATE levels SET xp = xp + " + experience + "  WHERE member = '" + member + "' AND server = '" + server + "' ;");
                                    } else {
                                        PreparedStatement prepared = connect.prepareStatement("Insert Into levels values(?,?,?)");
                                        prepared.setString(1, member);
                                        prepared.setString(2, server);
                                        prepared.setInt(3, experience);
                                        prepared.execute();
                                    }
                                    connect.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            });
                        });
                        xpDict.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 60 * 1000);

            // Every 15 Minutes
            timer2.schedule(new TimerTask() {
                public void run() {
                    try {
                        Long start = System.currentTimeMillis();
                        HashMap<String, Integer> membersToUpdate = new HashMap<>();

                        System.out.println("Rounding up members to update.");

                        Connection connect = Hikari.getConnection();
                        ResultSet resultSet = connect.createStatement().executeQuery("SELECT member, Xp FROM levels where server = '776380239961260052';");
                        while (resultSet.next()) {
                            membersToUpdate.put(resultSet.getString(1), resultSet.getInt(2));
                        }
                        connect.close();

                        System.out.println("All members fetched.");

                        Guild BeanZone = event.getJDA().getGuildById("776380239961260052");

                        List<Role> RankRoles = new ArrayList<>();
                        RankRoles.add(BeanZone.getRoleById("1114237294988230808")); // tadpole
                        RankRoles.add(BeanZone.getRoleById("857136982692724756")); // froglet
                        RankRoles.add(BeanZone.getRoleById("857137043355992106")); // Frog
                        RankRoles.add(BeanZone.getRoleById("857137261706346537")); // Bullfrog
                        RankRoles.add(BeanZone.getRoleById("857137344318668810")); // suited frog
                        RankRoles.add(BeanZone.getRoleById("882488807776202762")); // rainbow frog

                        membersToUpdate.forEach((key, value) -> {

                            Member member = BeanZone.getMemberById(key);


                            if (member != null && !member.getUser().isBot()) {
                                int level = 0;
                                int res = 0;

                                iterator:
                                for (int i = 0; i < 10000; i++) {
                                    res += (5 * Math.pow(i, 2) + 50 * i + 100);
                                    if (res > value) {
                                        level = i;
                                        break iterator;
                                    }
                                }

                                level++;

                                List<Role> rolesToAdd = new ArrayList<>();

                                // ROOT
                                rolesToAdd.add(RankRoles.get(0));

                                // RUNG
                                if (level >= 5) {
                                    rolesToAdd.add(RankRoles.get(1));
                                }

                                // SEEDLING
                                if (level >= 15) {
                                    rolesToAdd.add(RankRoles.get(2));
                                }

                                // PLANT
                                if (level >= 30) {
                                    rolesToAdd.add(RankRoles.get(3));
                                }

                                // BEAN
                                if (level >= 50) {
                                    rolesToAdd.add(RankRoles.get(4));
                                }

                                // LEGENDARY BEAN
                                if (level >= 75) {
                                    rolesToAdd.add(RankRoles.get(5));
                                }

                                try {
                                    BeanZone.modifyMemberRoles(member, rolesToAdd, Collections.emptyList()).queue();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Long end = System.currentTimeMillis();
                        System.out.println("Finished updating all users in " + (end - start) + "ms");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 10 * 60 * 1000);
    }
}
