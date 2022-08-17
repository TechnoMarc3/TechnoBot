package main.Linux3000.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import kotlin.jvm.internal.markers.KMutableSet;
import main.Linux3000.DiscordBot;
import main.Linux3000.audio.AudioPlaylist;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.premium.PlaylistReason;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.audio.premium.PremiumPlaylist;
import main.Linux3000.premium.manager.PremiumManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ButtonClickListener extends ListenerAdapter {


    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(!event.getMessage().equals(DiscordBot.INSTANCE.getPremiumManager().getCommandManager(event.getGuild()).getMessage())) {
            //do else;
            return;
        }
        if(event.getComponent() == null) return;
        if(event.getComponent().getEmoji() == null) return;
        final TextChannel channel = DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(event.getGuild());
        final GuildMusicManager musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        switch (event.getComponent().getEmoji().getName()) {

            case "⏮":
                if (audioPlayer.getPlayingTrack() == null) {
                    channel.sendMessage("Ich spiele aktuell keine Musik").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                    return;
                }
                if(musicManager.scheduler.previousTrack()) {
                    channel.sendMessage("Vorheriger Titel wird jetzt abgespielt :thumbsup:").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);}); }else {
                    channel.sendMessage("Das war der letzte Song in dieser Playlist").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                }
                event.deferEdit().queue();
                break;
            case "⏪":
                musicManager.scheduler.changePosition(-10);
                event.deferEdit().queue();
                break;
            case "⏸":
                musicManager.setPlayingMusic(false);
                musicManager.setupTask();
                musicManager.scheduler.getAudioPlayer().setPaused(true);
                channel.sendMessage("Ich wurde pausiert, Musik zu spielen!").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                event.deferEdit().queue();
                break;
            case "▶":
                if (musicManager.scheduler.getAudioPlayer().isPaused()) {
                    musicManager.setPlayingMusic(true);
                    musicManager.scheduler.getAudioPlayer().setPaused(false);

                    channel.sendMessage("Ich spiele nun weiter Musik für dich ab! :thumbsup:").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});

                } else {
                    channel.sendMessage("Ich bin nicht pausiert!").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                }
                event.deferEdit().queue();
                break;
            case "⏩":
                musicManager.scheduler.changePosition(10);
                event.deferEdit().queue();
                break;
            case "⏭":
                if (audioPlayer.getPlayingTrack() == null) {
                    channel.sendMessage("Ich spiele aktuell keine Musik").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                }
                if(musicManager.scheduler.nextTrack()) {
                    channel.sendMessage("Aktuellen Titel übersprungen").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);}); }else {
                    channel.sendMessage("Das war der letzte Song in dieser Playlist").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                }
                event.deferEdit().queue();
                break;
            case "\uD83D\uDD0D":
                Modal.Builder builder = Modal.create("search_track", "Suche nach einem Song / einer Playlist");
                TextInput input = TextInput.create("track_query", "Track / Playlist", TextInputStyle.SHORT).build();
                    builder.addActionRows(ActionRow.of(input));
                Modal modal = builder.build();
                event.replyModal(modal).queue();
                break;
            case "\uD83E\uDD18":
                musicManager.scheduler.bassBoost(musicManager.scheduler.isBassBoosted() ? 0 : 100);
                channel.sendMessage("Bass Boost wurde " + (musicManager.scheduler.isBassBoosted() ? "**aktiviert**" : "**deaktiviert**")).queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                event.deferEdit().queue();
                break;
            case "\uD83D\uDD01":
                boolean newRepeat = musicManager.scheduler.repeatQueue();
                channel.sendMessageFormat("Die aktuelle Queue wird nun **%s**", newRepeat ? "wiederholt" : "nicht wiederholt").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                event.deferEdit().queue();
                break;
            case "\uD83D\uDD02":
                boolean n = musicManager.scheduler.repeatTrack();

                channel.sendMessageFormat("Das aktuelle Lied wird nun **%s**", n ? "wiederholt" : "nicht wiederholt" ).queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                event.deferEdit().queue();
                break;
            case "\uD83D\uDD00":
                musicManager.scheduler.shufflePlaylist();
                channel.sendMessage("Die aktuelle Playlist wurde nun geshuffelt").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                event.deferEdit().queue();
                break;
            case "\uD83D\uDCBE":
                PlaylistReason reason = DiscordBot.INSTANCE.getPremiumManager().getPlaylistManager(event.getGuild()).check(event.getGuild(), musicManager.getPlaylist().getAllTracks());
                if(reason == PlaylistReason.FINE) {
                Modal.Builder b = Modal.create("reg_playlist", "Playlist erstellen");
                TextInput i = TextInput.create("playlist_name", "Playlist-Name", TextInputStyle.SHORT).build();
                b.addActionRow(i);
                Modal m = b.build();
                event.replyModal(m).queue(); }
                else if(reason == PlaylistReason.ALREADY_CONTAINED) {
                    channel.sendMessage("Die aktuelle Playlist wurde bereits unter dem Namen ```" + reason.getPlaylist().getName() + "``` gespeichert").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                    event.deferEdit().queue();
                }else if(reason == PlaylistReason.FULL) {
                    //TODO -> überlegen ob Premium komplett frei
                }
                break;

        }
    }



    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();
        if(event.getInteraction().getModalId().equalsIgnoreCase("search_track")) {
        String query = event.getInteraction().getValues().get(0).getAsString();

        GuildVoiceState state;
        if ((state = event.getMember().getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {
                Objects.requireNonNull(event.getGuild()).getAudioManager().openAudioConnection(vc);
                PremiumPlaylist audioPlaylist = DiscordBot.INSTANCE.getPremiumManager().getPlaylistManager(event.getGuild()).getAudioPlaylist(query);
                if(audioPlaylist !=null)  {
                    if(DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(event.getGuild()) == null) {
                        DiscordBot.INSTANCE.getManagerController().addChannelToGuild(event.getGuild(), event.getChannel().asTextChannel());
                    }
                    System.out.println("registering playlist: " + audioPlaylist.getTracks().size());
                    PremiumPlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.registerAndStartPlaylist(audioPlaylist);
                    //closing it
                    event.deferEdit().queue();
                }else {
                if (!isUrl(query)) {
                    query = "ytmsearch:" + query;
                }

                System.out.println(isUrl(query) + " : " + query);
                if(DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(event.getGuild()) == null) {
                    DiscordBot.INSTANCE.getManagerController().addChannelToGuild(event.getGuild(), event.getChannel().asTextChannel());
                }
                PremiumPlayerManager.getInstance().loadAndPlay(event.getChannel().asTextChannel(), query);
                //closing it
                event.deferEdit().queue();



                 }
            }
           else {
                    EmbedBuilder bu = new EmbedBuilder();
                    bu.setDescription("Du befindest dich nicht in einem VoiceChannel");
                    bu.setColor(Color.decode("#fc0303"));
                    channel.sendMessageEmbeds(bu.build()).queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
                }


            } else {
                EmbedBuilder bu = new EmbedBuilder();
                bu.setDescription("Du befindest dich nicht in einem VoiceChannel");
                bu.setColor(Color.decode("#fc0303"));
                channel.sendMessageEmbeds(bu.build()).queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
            }}else if(event.getInteraction().getModalId().equalsIgnoreCase("reg_playlist")){
            String name = event.getInteraction().getValues().get(0).getAsString();
            DiscordBot.INSTANCE.getPremiumManager().getPlaylistManager(event.getGuild()).saveCurrentPlaylist(name);

            event.deferEdit().queue();

        }


    }



    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

        if(event.getComponent().getId().equalsIgnoreCase("song_dropdown")) {
            if(!event.getMessage().equals(DiscordBot.INSTANCE.getPremiumManager().getCommandManager(event.getGuild()).getMessage())) {
            System.out.println("do else");
            return;
        }
        int index = Integer.parseInt(event.getInteraction().getSelectedOptions().get(0).getValue());
        final GuildMusicManager musicManager = PremiumPlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
        if(index == musicManager.scheduler.getPlaylist().indexOf(musicManager.scheduler.getCurrentTrack())) {
            DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(event.getGuild()).sendMessage("Der ausgewählte Titel ist der aktuelle Titel, wähle einen anderen aus!").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
            event.deferEdit().queue();
            return;
        }
        if(musicManager.scheduler.skipTo(index)) {
            DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(event.getGuild()).sendMessage("Erfolgreich zu #" + (index+1) + " gesprungen").queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
            event.deferEdit().queue();
        }}else if(event.getComponent().getId().equalsIgnoreCase("tracks")) {
            final GuildMusicManager musicManager = PremiumPlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
            Message message = event.getMessage();
            int index = Integer.parseInt(event.getInteraction().getSelectedOptions().get(0).getValue());
            AudioTrack track = PremiumPlayerManager.getInstance().trackCache.get(index);
            musicManager.scheduler.startOrQueue(track, false);
            message.delete().queue();
        }else if(event.getComponent().getId().equalsIgnoreCase("playlist_dropdown")) {
            GuildVoiceState state;
            if ((state = event.getMember().getVoiceState()) != null) {
                VoiceChannel vc;
                if ((vc = (VoiceChannel) state.getChannel()) != null) {
                    Objects.requireNonNull(event.getGuild()).getAudioManager().openAudioConnection(vc);
            String playlistName = event.getInteraction().getSelectedOptions().get(0).getValue();
            PremiumPlaylist audioPlaylist = DiscordBot.INSTANCE.getPremiumManager().getPlaylistManager(event.getGuild()).getAudioPlaylist(playlistName);
            if(DiscordBot.INSTANCE.getManagerController().getSpecifiedTextChannel(event.getGuild()) == null) {
                DiscordBot.INSTANCE.getManagerController().addChannelToGuild(event.getGuild(), event.getChannel().asTextChannel());
            }
            System.out.println("registering playlist: " + audioPlaylist.getTracks().size());
            PremiumPlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.registerAndStartPlaylist(audioPlaylist);


            }
            else {
                EmbedBuilder bu = new EmbedBuilder();
                bu.setDescription("Du befindest dich nicht in einem VoiceChannel");
                bu.setColor(Color.decode("#fc0303"));
                channel.sendMessageEmbeds(bu.build()).queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
            }


        } else {
            EmbedBuilder bu = new EmbedBuilder();
            bu.setDescription("Du befindest dich nicht in einem VoiceChannel");
            bu.setColor(Color.decode("#fc0303"));
            channel.sendMessageEmbeds(bu.build()).queue(message -> {message.delete().queueAfter(5, TimeUnit.SECONDS);});
        }//closing it
            event.deferEdit().queue();}



    }
    public  boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
