package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.*;

public class VolumeCommand implements AudioCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {

        GuildVoiceState state;
        String[] args = message.getContentRaw().split(" ");
        if(args.length == 2) {
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {
                int volume = Integer.parseInt(args[1]);

                final GuildMusicManager musicManager;
                if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
                    musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
                }else {
                    musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
                }
                musicManager.scheduler.setVolume(volume);


                channel.sendMessage("Die Musik spielt nun mit Volume: " + volume).queue();
            }
            }
        }
    }

    @Override
    public String help() {
        return "Sets the volume to <amount>";
    }

    @Override
    public String name() {
        return "volume";
    }
}
