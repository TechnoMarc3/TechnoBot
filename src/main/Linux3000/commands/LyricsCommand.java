package main.Linux3000.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import core.GLA;

import genius.SongSearch;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LyricsCommand implements ServerCommand {
    String save = "";
    private boolean type;
    Timer timer;

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

        GLA gla = new GLA();

        try {

            channel.sendMessage("Searching Lyrics ....").queue();


                save = info.title.replace("(Official Video)", "") + " ";
                save += info.author.replace("-Topic", "");

            List<SongSearch.Hit> hits = gla.search(save).getHits();
            if(hits.isEmpty() || hits.size() == 0) {
                channel.sendMessage("Leider konnte ich keine Lyrics für dieses Lied finden").queue();
                return;
            }
            type = true;
            type(channel);
            String res = (hits.get(0)).fetchLyrics();
            Timer timer = new Timer();
            TimerTask write = new TimerTask() {
                @Override
                public void run() {
                    try {
                        FileUtils.writeStringToFile(DiscordBot.INSTANCE.tempFile, res);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            };

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    type = false;
                    type(channel);
                    channel.sendFile(DiscordBot.INSTANCE.tempFile, save + " Lyrics.txt").queue();
                }
            };


            timer.schedule(task, 3000);
            timer.schedule(write, 2000);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void type(TextChannel channel) {
        if(type) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    channel.sendTyping().queue();
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(task, 100,2000);
        }
        else{
            timer.cancel();
        }

    }

    @Override
    public String help() {
        return "Searches the lyrics of the playing song";
    }

    @Override
    public String name() {
        return "lyrics";
    }
}
