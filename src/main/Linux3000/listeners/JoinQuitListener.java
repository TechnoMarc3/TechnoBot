package main.Linux3000.listeners;

import main.Linux3000.DiscordBot;
import main.Linux3000.manage.SQLManager;
import main.Linux3000.stats.xp.image.ImageCreator;


import net.dv8tion.jda.api.entities.TextChannel;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class JoinQuitListener extends ListenerAdapter {

    ImageCreator creator = new ImageCreator();
    String channelID;
    String discord_channel_id;
 @Override
public void onReady(ReadyEvent event) {

//        for(Guild guild: event.getJDA().getGuilds()) {
  //          try {
    //            channelID = SQLManager.getYTChannelID(""+guild.getIdLong());
    //            discord_channel_id = SQLManager.getChannelID(""+guild.getIdLong());
//                System.out.println(channelID);
  //              System.out.println(discord_channel_id);
    //            if(channelID != null || discord_channel_id != null) {
//
  //              resulter = new YoutubeSearchResulter();
 //               resulter.setChannelId(channelID);
   //          resulter.setGuild(guild);
   //          resulter.runFirst(); }

   //         SQLManager.setupGuilds(guild.getIdLong());

   //      } catch (IOException | SQLException exception) {
   //          exception.printStackTrace();
   //      }
   //  }
   //  Timer timer = new Timer();
   //  TimerTask task = new TimerTask() {
   //      @Override
   //      public void run() {
   //          try {
   //              System.out.println("run");
   //              resulter.runCircle();
   //              resulter.check();
   //          } catch (IOException ioException) {
   //              ioException.printStackTrace();
   //          }
   //      }
   //     };
   //     timer.scheduleAtFixedRate(task, 300000,300000);
//
    DiscordBot.INSTANCE.getPremiumManager().registerPremium(DiscordBot.INSTANCE.getJDA().getGuildById(868798821092696084L));
    DiscordBot.INSTANCE.getPremiumManager().registerPremium(DiscordBot.INSTANCE.getJDA().getGuildById(967773208981930054L));
     DiscordBot.INSTANCE.getPremiumManager().registerPremium(DiscordBot.INSTANCE.getJDA().getGuildById(1008000645762121781L));

     DiscordBot.INSTANCE.getPremiumManager().getPremiumGuilds().forEach(guild -> {
         try {
             SQLManager.loadPlaylists(guild);
         } catch (SQLException e) {
             e.printStackTrace();
         }
     });




   }
//
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        System.out.println(1);



        try {
            SQLManager.setupGuilds(event.getGuild().getIdLong());
            System.out.println("setup");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        event.getGuild().getTextChannels().get(0).sendMessage("Thanks for inviting me to the server : ```" + event.getGuild().getName() + "```").queue();
        System.out.println("Joined guild: " + event.getGuild().getName());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        TextChannel channel;
       creator.createJoinImage(event.getGuild(), event.getMember());
        if((channel = (TextChannel) event.getGuild().getDefaultChannel()) != null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    channel.sendFile(creator.getOutput()).queue();
                    System.out.println(event.getMember().getEffectiveName() + " joined");
                }
            }
            ;
            timer.schedule(task, 4000);

        }

    }

}
