package main.Linux3000.audio.premium;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.premium.manager.MusicCommandManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PremiumPlayerManager {
	private static PremiumPlayerManager INSTANCE;

    public List<AudioTrack> trackCache = new ArrayList<>();

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PremiumPlayerManager() {
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

    public void loadUrisAndCreatePlaylist(Guild guild, String[] uris, String name) {
        final GuildMusicManager musicManager = this.getMusicManager(guild);
        PremiumPlaylist playlist = new PremiumPlaylist(guild, name);
        AudioLoadResultHandler handler = new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
               playlist.addTrack(audioTrack, guild);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        };
        for(String uri : uris) {
            this.audioPlayerManager.loadItemOrdered(musicManager, uri, handler);
        }



    }



    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        System.out.println(musicManager);

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                channel.sendMessage("Zur Playlist hinzugefügt: `")
                        .append(track.getInfo().title)
                        .append("` von `")
                        .append(track.getInfo().author.replace("- Topic", ""))
                        .append('`')
                        .queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});

                musicManager.scheduler.startOrQueue(track, false);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                if(!isUrl(trackUrl)) {
                    List<AudioTrack> tracks = playlist.getTracks();
                    trackCache.clear();
                    trackCache.addAll(tracks);
                    SelectMenu.Builder selectBuilder = SelectMenu.create("tracks");
                    selectBuilder.setPlaceholder("Select your song")
                            .setRequiredRange(1,1);
                    for(AudioTrack track : tracks) {
                        selectBuilder.addOption(track.getInfo().title , String.valueOf(tracks.indexOf(track)), track.getInfo().author.replace("- Topic", ""), Emoji.fromUnicode("\uD83C\uDFB5"));
                    }
                    SelectMenu menu = selectBuilder.build();
                    channel.sendMessage("Ich habe folgende Lieder gefunden").setActionRows(ActionRow.of(menu)).queue();

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


    public static PremiumPlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PremiumPlayerManager();
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