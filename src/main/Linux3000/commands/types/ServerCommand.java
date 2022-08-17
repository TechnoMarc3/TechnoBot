package main.Linux3000.commands.types;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand{

	void performCommand(Member m , TextChannel channel, Message message) throws InterruptedException;

	String help();

	String name();

	String prefix();
}
