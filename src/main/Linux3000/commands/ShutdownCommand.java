package main.Linux3000.commands;

import main.Linux3000.DiscordBot;
import main.Linux3000.commands.types.ServerCommand;
import main.Linux3000.manage.LiteSQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShutdownCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		if(!m.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage("Keine Erlaubniss").queue();
			return;
		}
		channel.sendMessage("Shutdown.exe ausgeführt!").complete();
		JDA jda = DiscordBot.INSTANCE.getJDA();
		
		String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			if(jda != null) { 

                        jda.shutdown();
                        System.out.println("Bot offline");
                    }
                    
					reader.close();
                    System.exit(-1);
                
            }catch(IOException e) {
            e.printStackTrace();
        }
		
        }

	@Override
	public String help() {
		return "Shuts the bot down (temporally)";
	}

	@Override
	public String name() {
		return "sh";
	}
}


