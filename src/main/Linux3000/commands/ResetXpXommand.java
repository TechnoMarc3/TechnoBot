package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.commands.types.BaseCommand;
import main.Linux3000.commands.types.ServerCommand;
import main.Linux3000.stats.xp.XP;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ResetXpXommand implements BaseCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        if(!m.hasPermission(Permission.ADMINISTRATOR)) {
            return;
        }
        String[] args = message.getContentRaw().split(" ");
        XP xp = DiscordBot.INSTANCE.xp;
        if(args.length == 2) {
            Member member = message.getMentions().getMembers().get(0);
            xp.resetXpFromMember(channel.getGuild(), member);
            channel.sendMessage("Die Xp von " + member.getEffectiveName() + " wurden resettet!" ).queue();
        }
    }

    @Override
    public String help() {
        return "Resets <@MentionenMember>'s xp";
    }

    @Override
    public String name() {
        return "resetxp";
    }
}
