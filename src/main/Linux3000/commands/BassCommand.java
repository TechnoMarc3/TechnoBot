package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.audio.GuildMusicManager;
import main.Linux3000.audio.PlayerManager;
import main.Linux3000.audio.premium.PremiumPlayerManager;
import main.Linux3000.commands.types.AudioCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.*;

public class BassCommand implements AudioCommand {
    @Override
    public void performCommand(Member m, TextChannel channel, Message message) throws InterruptedException {

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
