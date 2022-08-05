package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.commands.types.ServerCommand;
import main.Linux3000.events.MemberLevelUpEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetChannelCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        String[] args = message.getContentRaw().split(" ");
        if(args.length == 2) {
            TextChannel setChannel = message.getMentionedChannels().get(0);
            if(setChannel!=null) {
                MemberLevelUpEvent event = DiscordBot.INSTANCE.event;
                event.setGuild(channel.getGuild());
                event.setLevelUpChannel(channel.getGuild(), setChannel);
                channel.sendMessage(setChannel.getAsMention() + " ist nun der Level-Up-Channel").queue();

            }
        }
        else {
            channel.sendMessage("Bitte verwende !channel #{channel-name}!").queue();
        }

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }
}
