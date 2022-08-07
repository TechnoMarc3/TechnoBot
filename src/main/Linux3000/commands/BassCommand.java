package main.Linux3000.commands;

import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class BassCommand implements ServerCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {

        GuildVoiceState state;
        if ((state = m.getVoiceState()) != null) {
            VoiceChannel vc;
            if ((vc = (VoiceChannel) state.getChannel()) != null) {


                final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());

                musicManager.scheduler.bassBoost(100);


                channel.sendMessage("Bass wird nun geboostet :metal: ").queue();

            }
        }
    }

    @Override
    public String help() {
        return "Enables the bass-boost, if the player plays music";
    }

    @Override
    public String name() {
        return "bass";
    }


}
