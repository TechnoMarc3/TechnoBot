package main.Linux3000.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class NowPlayingCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		
		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null) {
            channel.sendMessage("Aktuell wird kein Titel abgespielt").queue();
            return;
        }

        final AudioTrackInfo info = track.getInfo();

        channel.sendMessageFormat("Aktuell spielt  `%s` von `%s` (Link: <%s>)", info.title, info.author, info.uri).queue();
    }

    @Override
    public String help() {
        return "Shows you the current playing track and some other informations";
    }

    @Override
    public String name() {
        return "playing";
    }
}


