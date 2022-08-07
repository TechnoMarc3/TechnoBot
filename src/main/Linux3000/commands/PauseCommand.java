package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class PauseCommand implements ServerCommand {


    @Override
    public void performCommand(Member m, TextChannel channel, Message message) {

        GuildVoiceState state;
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {


                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());

                musicManager.scheduler.getAudioPlayer().setPaused(true);


                channel.sendMessage("Ich wurde pausiert, Musik zu spielen!").queue();
                DiscordBot.INSTANCE.playerManager.getMusicManager(channel.getGuild()).changeCooldown();

            }
        }
    }

    @Override
    public String help() {
        return "Pauses the player -> !resume";
    }

    @Override
    public String name() {
        return "pause";
    }
}
