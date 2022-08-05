package main.Linux3000.stats.xp;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.Linux3000.manage.LiteSQL;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class XP {

    public  HashMap<String, Integer> cooldown;
    public  HashMap<Guild, List<Member>> onCooldown;

    public XP() {
        cooldown = new HashMap<>();
        onCooldown = new HashMap<>();

    }



    public void resetCooldown(Member member) {
        cooldown.put(member.getId(), 0);
    }

    public int getCooldownFromMember(Member member) {
        return cooldown.get(member.getId());
    }

    public void setCooldownToMember(Guild guild, Member member, int time) {
        cooldown.put(member.getId(), time);
        addMemberToCooldown(guild, member);
    }

    public static void resetEverthing() {
        String stmt2 = "DELETE FROM expsystem ;";
        LiteSQL.onUpdate(stmt2);
    }

    public void resetXpFromMember(Guild guild, Member member) {
        String stmt2 = "DELETE FROM expsystem WHERE guildid = " + guild.getIdLong() + " AND memberid = " + member.getIdLong() + ";";
        try {
        Connection connection = LiteSQL.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(stmt2);
        statement.close();
        connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void setXpToMember(Guild guild, Member member, int toSet)  {
        String stmt1 = "SELECT * FROM expsystem WHERE guildid = '" + guild.getIdLong() + "' AND memberid = '" + member.getIdLong() +"';";
        ResultSet set = null;
        String stmt = "INSERT INTO expsystem(guildid, memberid, xp) VALUES ('" + guild.getIdLong() + "','" + member.getIdLong() + "','" + toSet + "');";

        String stmt2 = "DELETE FROM expsystem WHERE guildid = '" + guild.getIdLong() + "' AND memberid = '" + member.getIdLong() +"';";
        try {
            Connection connection = LiteSQL.getConnection();
            Statement statement = connection.createStatement();
            set=statement.executeQuery(stmt1);
            if(set.next()) {
                statement.execute(stmt2);
            }
            statement.execute(stmt);
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }







    }

    public boolean isOnCooldown(Guild guild, Member member) {
        List<Member> list = onCooldown.get(guild);
        if(list == null) {
            return false;
        }
        return list.contains(member);
    }
    public int getXpFromMember(Guild guild, Member member) {
        String stmt = "SELECT xp FROM expsystem WHERE guildid = '" + guild.getIdLong() + "' AND memberid = '" + member.getIdLong() + "';";
        int value = 0;
        ResultSet set = null;
        try {
            Connection connection = LiteSQL.getConnection();

            Statement statement = connection.createStatement();

            set = statement.executeQuery(stmt);
            if(set.next()) {
                value = set.getInt(1);
            }

            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }

        return value;


    }

    public void addXpToMember(Guild guild,Member member, int toAdd) {
            int add = getXpFromMember(guild, member) + toAdd;
            setXpToMember(guild, member, add);
        setCooldownToMember(guild, member, 10);
        checkForLevelUp(guild, member);

    }

    public void addMemberToCooldown(Guild guild, Member member) {
        List<Member> list = onCooldown.get(guild);
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(member);
        onCooldown.put(guild, list);
        task(guild, member);
    }
    public void removeMemberFromCooldown(Guild guild, Member member) {
        resetCooldown(member);
        List<Member> list = onCooldown.get(guild);
        if(list == null) {
            list = new ArrayList<>();
        }
        list.remove(member);
        onCooldown.put(guild, list);
    }
    public void task(Guild guild, Member member) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                removeMemberFromCooldown(guild, member);
                System.out.println("back");
            }
        };
        timer.schedule( task, (10 *1000));

    }

    public void checkForLevelUp(Guild guild, Member member) {

    }
    public void getEffectiveRole(Guild guild, Member member) {

    }

}
