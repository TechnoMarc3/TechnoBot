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
import java.util.concurrent.TimeUnit;

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
        if(musicManagers.containsKey(guild.getIdLong())) {

            return musicManagers.get(guild.getIdLong());
        }
        else {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            DiscordBot.INSTANCE.getManagerController().addEntry(guild, guildMusicManager);
            this.musicManagers.put(guild.getIdLong(), guildMusicManager);

            return guildMusicManager;
        }
    }

    public void removeMusicManager(Guild guild) {
        this.musicManagers.remove(guild.getIdLong());
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
                        .queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});

                musicManager.scheduler.startOrQueue(track, false);

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
                            .queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});

                    musicManager.scheduler.startOrQueue(tracks, false);


                }
            	else {
            	    List<AudioTrack> tracks1 = playlist.getTracks();
                    for (int i = 0; i < playlist.getTracks().size(); i++) {

                            musicManager.scheduler.startOrQueue(tracks1.get(i), false);

                    }
                    channel.sendMessage("Zur Playlist hinzugefügt: `")
                            .append(playlist.getTracks().size() + " Title")
                            .append("` von der Playlist `")
                            .append(playlist.getName())
                            .append('`')
                            .queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});

                }



            }

            @Override
            public void noMatches() {
                channel.sendMessage("no matches").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();

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