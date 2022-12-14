package main.Linux3000.commands;

import main.Linux3000.commands.types.BaseCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class KickCommand implements BaseCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        if(!m.hasPermission(Permission.ADMINISTRATOR)) {
            return;
        }
        Guild g = channel.getGuild();

        String[] args = message.getContentRaw().split(" ");
        Member member = message.getMentions().getMembers().get(0);
        if(member == null) {
            return;
        }
        if(args.length >= 2) {
            g.kick(member, message.getContentRaw().replace("!kick " + member.getAsMention(), "")).queue();
        }
        else {
            g.kick(member).queue();
        }
        channel.sendMessage("Kicked " + member.getEffectiveName() + " successfully").queue();
    }

    @Override
    public String help() {
        return "Kicks the specified member of the guild";
    }

    @Override
    public String name() {
        return "kick";
    }
}
