package main.Linux3000.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.List;

public class QueueCommand implements AudioCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {

        final GuildMusicManager musicManager;
        if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
            musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
        }else {
            musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
        }

        final List<AudioTrack> trackList = musicManager.getPlaylist().getAllTracks();

        if (trackList.isEmpty()) {
            channel.sendMessage("Die Playlist ist leer :sob:").queue();
            return;
        }

        final int trackCount = Math.min(trackList.size(), 20);

        final MessageAction messageAction = channel.sendMessage("**Aktuelle Playlist:**\n");

        for (int i = 0; i <  trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();
            AudioTrack currentTrack = musicManager.scheduler.getCurrentTrack();
            int indexOfCurrentTrack = trackList.indexOf(currentTrack);
            if(i < indexOfCurrentTrack) {
                messageAction.append("~~     #")
                        .append(String.valueOf(i + 1))
                        .append(" `")
                        .append(String.valueOf(info.title))
                        .append(" von ")
                        .append(info.author)
                        .append("` [`")
                        .append(formatTime(track.getDuration()))
                        .append("`]~~\n");
            }
           else if(track.equals(currentTrack)) {
                messageAction.append("---> #")
                        .append(String.valueOf(i + 1))
                        .append(" `")
                        .append(String.valueOf(info.title))
                        .append(" von ")
                        .append(info.author)
                        .append("` [`")
                        .append(formatTime(track.getDuration()))
                        .append("`]\n");
            }else {
            messageAction.append("    #")
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(String.valueOf(info.title))
                    .append(" von ")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]\n"); }
        }

        if (trackList.size() > trackCount) {
            messageAction.append("Und `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` weitere...");
        }

        messageAction.queue();
    }

    @Override
    public String help() {
        return "Shows you the playlist";
    }

    @Override
    public String name() {
        return "playlist";
    }

    private String formatTime(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        minutes %= 60;
        seconds %=60;
        
        

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }}
