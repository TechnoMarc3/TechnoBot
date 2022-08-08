package main.Linux3000.commands.types;

import main.Linux3000.commands.*;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
	

	private final List<ServerCommand> commands = new ArrayList<>();
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
			cmd.performCommand(m, channel, message);
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

		return null;
	}

	public List<ServerCommand> getCommands() {
		return commands;
	}


}
