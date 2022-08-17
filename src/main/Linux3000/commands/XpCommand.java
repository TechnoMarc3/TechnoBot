package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.commands.types.BaseCommand;
import main.Linux3000.commands.types.ServerCommand;
import main.Linux3000.stats.xp.XP;

import net.dv8tion.jda.api.entities.*;

import javax.annotation.Nullable;


public class XpCommand implements BaseCommand {

    @Override
    public void performCommand(Member m, TextChannel channel, Message message) {
        XP xp = DiscordBot.INSTANCE.xp;
        channel.sendMessage("Du hast " + xp.getXpFromMember(channel.getGuild(), m)  + " XP").queue();


        long i = calculateDifference(channel.getGuild(), message.getMember());

        channel.sendMessage("Dir fehlen noch "  + i + " XP für ein LevelUp").queue();

    }

    @Override
    public String help() {
        return "Shows your xp on this guild";
    }

    @Override
    public String name() {
        return "xp";
    }

    private long calculateDifference(Guild guild, Member member) {
        XP xp = DiscordBot.INSTANCE.xp;


        return xp.getXpFromMember(guild, member);

    }
}
