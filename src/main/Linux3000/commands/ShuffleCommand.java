package main.Linux3000.commands;

import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class ShuffleCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {

        GuildVoiceState state;
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {


                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());

                musicManager.scheduler.shufflePlaylist();


                channel.sendMessage("Die aktuelle Playlist wird nun geshuffelt").queue();

            }
        }
    }

    @Override
    public String help() {
        return "Shuffles the current playlist";
    }

    @Override
    public String name() {
        return "shuffle";
    }
}
