package main.Linux3000.commands;

import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager1;
import main.Linux3000.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.*;

public class VolumeCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {

        GuildVoiceState state;
        String[] args = message.getContentRaw().split(" ");
        if(args.length == 2) {
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {
                int volume = Integer.parseInt(args[1]);

                final GuildMusicManager musicManager = PlayerManager1.getInstance().getMusicManager(channel.getGuild());

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
