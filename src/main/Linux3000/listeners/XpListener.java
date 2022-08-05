package main.Linux3000.listeners;

import main.Linux3000.DiscordBot;
import main.Linux3000.stats.xp.XP;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class XpListener extends ListenerAdapter {

    String[] badContent = new String[] {
      "Penis", "ficken", "dick", "fuck", "fucking", "idiot", "GAY", "gaY", "gay", "Keck", "keck", "kek"
    };


    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()|| event.getAuthor().isSystem()) {
            return;
        }
        if(event.getMessage().getContentRaw().startsWith("!")) {
            return;
        }
        String message = event.getMessage().getContentRaw();
        String[] splittedMessage = message.split(" ");

        Random r = new Random();
        Member member = event.getMember();
        XP xp = DiscordBot.INSTANCE.xp;
        if(checkForBadContent(message, splittedMessage)) {
            event.getGuild().timeoutFor(member, 5, TimeUnit.MINUTES).queue();
            return;
        }

        if(xp.isOnCooldown(event.getGuild(), member)) {
            System.out.println("Member: " + member.getEffectiveName() + " is on Cooldown");
            return;
        }
            int add = r.nextInt(3) + 1;
            System.out.println("Adding " + add + " XP to member " + member.getAsMention() + " from Guild : " + event.getGuild().getName() + " [" + event.getGuild().getIdLong() + " ]");
            xp.addXpToMember(event.getGuild(),member, add);
    }

    public boolean checkForBadContent(String  rawMessage, String[] splittedMessage) {
        for(String s : badContent) {
            if(rawMessage.contains(s)) {
                return true;
            }
            for(String f : splittedMessage) {
                if(f.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }


}
