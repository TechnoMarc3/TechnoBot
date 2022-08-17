package main.Linux3000.audio.premium;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import main.Linux3000.audio.AudioPlaylist;
import main.Linux3000.manage.SQLManager;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PremiumPlaylist{

    private Guild guild;
    private static HashMap<Guild, List<PremiumPlaylist>> playlists = new HashMap<>();
    private String name;
    private List<AudioTrack> tracks = new ArrayList<>();

    public PremiumPlaylist(Guild guild, String name) {

        System.out.println("new instance: " + this);
        this.guild = guild;
        this.name = name;
        List<PremiumPlaylist> playlist = playlists.get(guild);
        if(playlist == null) {
            playlist = new ArrayList<>();
        }
        playlist.add(this);
        playlists.put(guild, playlist);
    }



    public PremiumPlaylist(String name, List<AudioTrack> audioTracks, Guild guild) {
        System.out.println("new instance: " + this);
        this.name = name;
        this.tracks.addAll(audioTracks);
        this.guild = guild;
        List<PremiumPlaylist> playlist = playlists.get(guild);
        if(playlist == null) {
            playlist = new ArrayList<>();
        }
        playlist.add(this);
        playlists.put(guild, playlist);
        try {
            SQLManager.savePlaylist(guild, this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PremiumPlaylist getPlaylistByTracks(Guild guild, List<AudioTrack> tracks) {
        for(PremiumPlaylist playlist : getPlaylists(guild)) {
            if(playlist.getTracks().containsAll(tracks)) {
                return playlist;

            }
        }
        return null;
    }

    public void addTrack(AudioTrack track, Guild guild) {
        this.tracks.add(track);
        playlists.get(guild).remove(this);
        playlists.get(guild).add(this);

    }

    public static List<PremiumPlaylist> getPlaylists(Guild guild) {
        return playlists.get(guild);
    }

    public String getName() {
        return name;
    }

    public List<AudioTrack> getTracks() {
        return tracks;
    }

    public String toString() {
        String g = "";
        for(AudioTrack track : getTracks()) {
            if(getTracks().indexOf(track) != getTracks().size()-1) {
                g+= track.getInfo().uri + ",";
                continue;
            }
            g+= track.getInfo().uri;
        }
        return g;
    }

    public static HashMap<Guild, List<PremiumPlaylist>> getPlaylists() {
        return playlists;
    }
}
