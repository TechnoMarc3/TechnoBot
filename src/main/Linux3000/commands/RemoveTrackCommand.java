package main.Linux3000.commands;


import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class RemoveTrackCommand implements AudioCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {
        String[] split = message.getContentRaw().split(" ");
        int num = Integer.parseInt(split[1]);
        GuildVoiceState state;
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {


                final GuildMusicManager musicManager;
                if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
                    musicManager = PremiumPlayerManager.getInstance().getMusicManager(channel.getGuild());
                }else {
                    musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
                }
                if(musicManager.scheduler.removeTrack(num-1)) {
                    channel.sendMessage("Song: " + musicManager.scheduler.getPlaylist().get(num-2).getInfo().title + " erfolgreich aus der Playlist gelöscht").queue();
                }else {
                    channel.sendMessage("Diesen Song gibt es nicht in der Playlist").queue();
                }
            }


        }

    }


    @Override
    public String help() {
        return "Removes the song <index> from the current playlist (if it is present)";
    }

    @Override
    public String name() {
        return "removetrack";
    }
}
