package main.Linux3000.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import main.Linux3000.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
	private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public Guild getGuildByPlayer(AudioPlayer player) {
        for (Long llong : musicManagers.keySet()) {
            if(musicManagers.get(llong).getAudioPlayer().equals(player)) {
                return DiscordBot.INSTANCE.getJDA().getGuildById(llong);
            }
        }
        return null;
    }



    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                channel.sendMessage("Zur Playlist hinzugefügt: `")
                        .append(track.getInfo().title)
                        .append("` von `")
                        .append(track.getInfo().author)
                        .append('`')
                        .queue();

                musicManager.scheduler.startOrQueue(track);


            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
            	AudioTrack tracks;
            	if(!isUrl(trackUrl)) {
            	    tracks = playlist.getTracks().get(0);
                    AudioTrackInfo info = tracks.getInfo();

                    channel.sendMessage("Zur Playlist hinzugefügt: `")
                            .append(info.title.toString())
                            .append("` von der Playlist `")
                            .append(playlist.getName())
                            .append('`')
                            .queue();

                    musicManager.scheduler.startOrQueue(tracks);


                }
            	else {
            	    List<AudioTrack> tracks1 = playlist.getTracks();
                    for (int i = 0; i < playlist.getTracks().size(); i++) {

                            musicManager.scheduler.startOrQueue(tracks1.get(i));

                    }
                    channel.sendMessage("Zur Playlist hinzugefügt: `")
                            .append(playlist.getTracks().size() + " Title")
                            .append("` von der Playlist `")
                            .append(playlist.getName())
                            .append('`')
                            .queue();

                }



            }

            @Override
            public void noMatches() {
                channel.sendMessage("no matches").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                //

            }
        });
    }


    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
    public boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}