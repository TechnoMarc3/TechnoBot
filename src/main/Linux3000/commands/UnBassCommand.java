package main.Linux3000.commands;

import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager1;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class UnBassCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {

        GuildVoiceState state;
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {


                final GuildMusicManager musicManager = PlayerManager1.getInstance().getMusicManager(channel.getGuild());

                musicManager.scheduler.bassBoost(0);


                channel.sendMessage("Bass boost wurde zurückgesetzt").queue();

            }
        }
    }


    @Override
    public String help() {
        return "Resets the bass boost";
    }

    @Override
    public String name() {
        return "unbass";
    }
}
