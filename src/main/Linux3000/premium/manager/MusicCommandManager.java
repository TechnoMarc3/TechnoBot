package main.Linux3000.premium.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.audio.premium.PremiumPlaylist;
import main.Linux3000.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicCommandManager extends PremiumFeature{



    private Guild guild;
    private TimerTask task;
    private Message message;
    List<ActionRow> rows = new ArrayList<>();

    public MusicCommandManager(Guild guild) {
        this.guild = guild;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void destroy() {
        task.cancel();
    }

    public void sendMusicMessage(TextChannel channel) {
        final GuildMusicManager musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());

        new Thread(() -> {
            channel.sendMessageEmbeds(createEmbed(channel)).queue((message) -> {
                if(getMessage() == null) {
                    this.message = message;
                }
                editMessage(channel);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        if(musicManager.isPlayingMusic) {
                            editMessage(channel);
                        }
                        if(musicManager.isTerminated) {
                            message.delete().queue();
                            this.cancel();
                            Thread.currentThread().stop();

                    }}
                };
                new Timer().scheduleAtFixedRate(task, 3000, 3000);
            });
        }).start();

    }

    public void editMessage(TextChannel channel) {
        final GuildMusicManager musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
        rows.clear();
        if(!musicManager.getPlaylist().getAllTracks().isEmpty()) {
            rows.add(ActionRow.of(Button.primary("previous_track", Emoji.fromUnicode("⏮")),
                    Button.secondary("rewind", Emoji.fromUnicode("⏪")),
                    musicManager.audioPlayer.isPaused() ? Button.success("resume", Emoji.fromUnicode("▶")): Button.success("pause", Emoji.fromUnicode("⏸")),
                    Button.secondary("forward", Emoji.fromUnicode("⏩")),
                    Button.primary("next_track" , Emoji.fromUnicode("⏭"))));
            rows.add(ActionRow.of(createSelectionMenu(musicManager))); }
        if(!musicManager.getPlaylist().getAllTracks().isEmpty()) {
            if(musicManager.getPlaylist().getAllTracks().size() == 1) {
                rows.add(ActionRow.of(Button.secondary("search_track", Emoji.fromUnicode("\uD83D\uDD0D")),
                        Button.danger("bass_boost", Emoji.fromUnicode("\uD83E\uDD18")),
                        Button.secondary("loop_track", Emoji.fromUnicode("\uD83D\uDD02"))
                ));
                rows.add(ActionRow.of(createPlaylistMenu(musicManager, channel.getGuild())));

            }else {
                rows.add(ActionRow.of(Button.secondary("search_track", Emoji.fromUnicode("\uD83D\uDD0D")),
                        Button.danger("bass_boost", Emoji.fromUnicode("\uD83E\uDD18")),
                        Button.primary("loop_queue", Emoji.fromUnicode("\uD83D\uDD01")),
                        Button.secondary("loop_track", Emoji.fromUnicode("\uD83D\uDD02")),
                        Button.primary("shuffle", Emoji.fromUnicode("\uD83D\uDD00"))
                ));
                rows.add(ActionRow.of(Button.primary("register_playlist", Emoji.fromUnicode("\uD83D\uDCBE"))));
                rows.add(ActionRow.of(createPlaylistMenu(musicManager, channel.getGuild())));
            }}else {
            rows.add(ActionRow.of(Button.secondary("search_track", Emoji.fromUnicode("\uD83D\uDD0D")),
                    Button.danger("bass_boost", Emoji.fromUnicode("\uD83E\uDD18"))));
            rows.add(ActionRow.of(createPlaylistMenu(musicManager, channel.getGuild())));
        }

            message.editMessageEmbeds(createEmbed(channel)).setActionRows(rows)
                    .queue();



    }



    private SelectMenu createSelectionMenu(GuildMusicManager musicManager) {
        final List<AudioTrack> trackList = musicManager.getPlaylist().getAllTracks();
         SelectMenu.Builder builder = SelectMenu.create("song_dropdown")
                .setPlaceholder(musicManager.audioPlayer.getPlayingTrack() == null ? "Search a song" : musicManager.audioPlayer.getPlayingTrack().getInfo().title)
                .setRequiredRange(1,1);
        for(AudioTrack track : trackList) {
            builder.addOption(track.getInfo().title , String.valueOf(trackList.indexOf(track)), track.getInfo().author.replace("- Topic", ""), Emoji.fromUnicode("\uD83C\uDFB5"));
        }
        return builder.build();
    }

    private SelectMenu createPlaylistMenu(GuildMusicManager manager, Guild guild) {
        if(!DiscordBot.INSTANCE.getPremiumManager().hasPremium(guild)) {
            return null;
        }
        List<PremiumPlaylist> playlists = PremiumPlaylist.getPlaylists().get(guild);
        if( playlists == null || playlists.isEmpty() ) {
            return null;
        }
        SelectMenu.Builder builder = SelectMenu.create("playlist_dropdown")
                .setPlaceholder("Select a playlist")
                .setRequiredRange(1,1);
        for(PremiumPlaylist playlist : playlists) {
            builder.addOption(playlist.getName().toUpperCase() , playlist.getName());
        }
        return builder.build();
    }

    private MessageEmbed createEmbed(TextChannel channel) {
        final GuildMusicManager musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
        final List<AudioTrack> trackList = musicManager.scheduler.getPlaylist();

        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(Color.cyan);

        builder.setTitle("Audio Player");

        if (trackList.isEmpty()) {
            builder.addField("Playlist", "Die Playlist ist leider leer :sob:", false);
        }
        else {
            final int trackCount = Math.min(trackList.size(), 20);
            StringBuilder messageAction = new StringBuilder();
            StringBuilder footer2 = new StringBuilder();
            int half = trackCount >= 10 ? trackCount/2 : trackCount;
            for (int i = 0; i <  half; i++) {
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
                            .append(info.author.replace("- Topic", ""))
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
                            .append(info.author.replace("- Topic", ""))
                            .append("` [`")
                            .append(formatTime(track.getDuration()))
                            .append("`]\n");
                }else {
                    messageAction.append("    #")
                            .append(String.valueOf(i + 1))
                            .append(" `")
                            .append(String.valueOf(info.title))
                            .append(" von ")
                            .append(info.author.replace("- Topic", ""))
                            .append("` [`")
                            .append(formatTime(track.getDuration()))
                            .append("`]\n"); }
            }
            for (int i = half; i <  trackCount; i++) {
                final AudioTrack track = trackList.get(i);
                final AudioTrackInfo info = track.getInfo();
                AudioTrack currentTrack = musicManager.scheduler.getCurrentTrack();
                int indexOfCurrentTrack = trackList.indexOf(currentTrack);
                if(i < indexOfCurrentTrack) {
                    footer2.append("~~     #")
                            .append(String.valueOf(i + 1))
                            .append(" `")
                            .append(String.valueOf(info.title))
                            .append(" von ")
                            .append(info.author.replace("- Topic", ""))
                            .append("` [`")
                            .append(formatTime(track.getDuration()))
                            .append("`]~~\n");
                }
                else if(track.equals(currentTrack)) {
                    footer2.append("---> #")
                            .append(String.valueOf(i + 1))
                            .append(" `")
                            .append(String.valueOf(info.title))
                            .append(" von ")
                            .append(info.author.replace("- Topic", ""))
                            .append("` [`")
                            .append(formatTime(track.getDuration()))
                            .append("`]\n");
                }else {
                    footer2.append("    #")
                            .append(String.valueOf(i + 1))
                            .append(" `")
                            .append(String.valueOf(info.title))
                            .append(" von ")
                            .append(info.author.replace("- Topic", ""))
                            .append("` [`")
                            .append(formatTime(track.getDuration()))
                            .append("`]\n"); }
            }

            StringBuilder weitere = new StringBuilder();
            if (trackList.size() > trackCount) {
                weitere.append("Und `")
                        .append(String.valueOf(trackList.size() - trackCount))
                        .append("` weitere...");
            }
            builder.addField("Playlist", messageAction.toString(), true);
            if(trackCount >= 10) {
                builder.addField("", footer2.toString(), true);
            }
            builder.addField("", weitere.toString(), false);
        }

        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();



        if (track == null) {
            builder.addField("Aktueller Titel: ", "Aktuell wird kein Titel abgespielt", false);
        }
        else {
            final AudioTrackInfo info = track.getInfo();
            int perc = (int) ((double) track.getPosition() / (double) track.getDuration() * 100);
            builder.addField("Aktueller Titel", "Aktuell spielt  " + info.title +" von " + info.author+" (Link: " + info.uri+") \n \n " + formatTime(track.getPosition()) + "  "  + MusicUtils.getProgressBar(perc)  +" " + formatTime(track.getDuration()),false);
            String url = track.getInfo().uri;
            String id = url.replace("https://www.youtube.com/watch?v=", "");
            builder.setThumbnail("https://img.youtube.com/vi/" + id + "/hqdefault.jpg");}
        return builder.build();
    }




    private String getLength(AudioTrack track) {
        String s = "";
        long duration = track.getDuration();
        long currentPos = track.getPosition();
        long seconds = duration / 1000;
        long currentSeconds = currentPos / 1000;


        for(int i = 0; i< seconds; i++) {
            if(i % 10 == 0) {
                if(currentSeconds <= i) {
                    s += "-  ";
                }else {
                    s+= "# ";
                }
            }
        }
        return s;

    }

    private String formatTime(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        minutes %= 60;
        seconds %=60;



        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}
