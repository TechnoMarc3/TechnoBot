package main.Linux3000.commands.premium;

import main.Linux3000.DiscordBot;
import main.Linux3000.commands.types.PremiumCommand;
import main.Linux3000.premium.manager.MusicCommandManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;


public class AudioPlayerCommand implements PremiumCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        if(DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(channel.getGuild()) == null) {
            DiscordBot.INSTANCE.getManagerController().addChannelToGuild(channel.getGuild(), channel);
        }
        MusicCommandManager commandManager = DiscordBot.INSTANCE.getPremiumManager().getCommandManager(channel.getGuild());

        commandManager.sendMusicMessage(channel);

    }

    @Override
    public String help() {
        return "Gives you a nice interface of the playlist / current song and some other things";
    }

    @Override
    public String name() {
        return "player";
    }
}
