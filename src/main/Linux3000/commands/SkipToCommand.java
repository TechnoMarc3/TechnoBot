package main.Linux3000.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class SkipToCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        String[] split = message.getContentRaw().split(" ");
        int num = Integer.parseInt(split[1]);
        GuildVoiceState state;
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {

                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
                final AudioPlayer audioPlayer = musicManager.audioPlayer;

                if (audioPlayer.getPlayingTrack() == null) {
                    channel.sendMessage("Ich spiele aktuell keine Musik").queue();
                    return;
                }
                System.out.println(musicManager.scheduler.getPlaylist().size());

                    if(musicManager.scheduler.skipTo(num-1)) {
                    channel.sendMessage("Erfolgreich zu #" + num + " gesprungen").queue();
                    }else {
                        channel.sendMessage("Diese Nummer gibt es nicht in der aktuellen Playlist!").queue();
                    }


            }


        }
    }

    @Override
    public String help() {
        return "Skips to {index} in the current playlist / queue";
    }

    @Override
    public String name() {
        return "skipto";
    }
}
