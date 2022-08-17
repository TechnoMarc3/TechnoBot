package main.Linux3000.commands.types;

import main.Linux3000.DiscordBot;
import main.Linux3000.commands.*;

import main.Linux3000.commands.premium.AudioPlayerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	

	private final List<ServerCommand> commands = new ArrayList<>();
	private final List<PremiumCommand> premiumCommands = new ArrayList<>();
	public CommandManager(){

		
		addCommand(new ClearCommand());
		addCommand( new PlayCommand());
		addCommand( new StopCommand());
		addCommand(new PauseCommand());
		addCommand(new ResumeCommand());
		addCommand( new RepeatCommand());
		addCommand(new SkipCommand());
		addCommand(new QueueCommand());
		addCommand(new NowPlayingCommand());
		addCommand( new XpCommand());
		addCommand(new HelpCommand());
		addCommand( new LyricsCommand());
		addCommand( new RepeatQueueCommand());
		addCommand(new ShuffleCommand());
		addCommand( new BassCommand());
		addCommand(new SkipToCommand());
		addCommand(new UnBassCommand());
		addCommand( new VolumeCommand());
		addCommand(new KickCommand());
		addCommand(new RemoveTrackCommand());
		addCommand(new BanCommand());

		addPremiumCommand(new AudioPlayerCommand());



	}

	private void addPremiumCommand(PremiumCommand cmd) {
		boolean nameFound = this.premiumCommands.stream().anyMatch((it) -> it.name().equalsIgnoreCase(cmd.name()));

		if (nameFound) {
			throw new IllegalArgumentException("A command with this name is already present");
		}

		premiumCommands.add(cmd);
	}


	private void addCommand(ServerCommand cmd) {
		boolean nameFound = this.commands.stream().anyMatch((it) -> it.name().equalsIgnoreCase(cmd.name()));

		if (nameFound) {
			throw new IllegalArgumentException("A command with this name is already present");
		}

		commands.add(cmd);
	}

	public boolean perform(String command, Member m, TextChannel channel, Message message) throws InterruptedException {
		ServerCommand cmd;
		if ((cmd = this.getCommand(command)) != null) {
			if(cmd instanceof PremiumCommand) {
				if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(channel.getGuild())) {
					cmd.performCommand(m, channel, message);
				}else {
					channel.sendMessage("Dieser Command ist nur für Premium-Nutzer verfügbar").queue();
				}
			}else {
			cmd.performCommand(m, channel, message); }
			return true;
		}
		return false;
	}

	public String getHelp(String command) {
		ServerCommand cmd;
		if ((cmd = this.getCommand(command)) != null) {
			return cmd.help();
		}
		else {
			return "";
		}
	}
	@Nullable
	public ServerCommand getCommand(String search) {
		String searchLower = search.toLowerCase();

		for (ServerCommand cmd : this.commands) {
			if (cmd.name().equals(searchLower)) {
				return cmd;
			}
		}
		for(PremiumCommand command : this.premiumCommands) {
			if (command.name().equals(searchLower)) {
				return command;
			}
		}

		return null;
	}

	public List<ServerCommand> getCommands(Guild guild) {
		if(DiscordBot.INSTANCE.getPremiumManager().hasPremium(guild)) {
			List<ServerCommand> allCommands = new ArrayList<>(commands);
			allCommands.addAll(premiumCommands);
			return allCommands;
		}else {
			return commands;
		}
	}


}
