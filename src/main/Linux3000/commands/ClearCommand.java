package main.Linux3000.commands;



import main.Linux3000.commands.types.BaseCommand;
import main.Linux3000.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand implements BaseCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {

		if (m.hasPermission(channel, Permission.MESSAGE_MANAGE)) {

			message.delete().queue();
			String[] args = message.getContentRaw().split(" ");

			if (args.length == 2) {

				try {

					int amount = Integer.parseInt(args[1]);
					channel.deleteMessages(get(channel, amount)).queue();
					channel.sendMessage(amount + " Nachrichten gelöscht").complete().delete().queueAfter(5,
							TimeUnit.SECONDS);

					return;

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

			}

		}

		else {
			channel.sendMessage("Du hast nicht die Erlaubnis dafür!!!!!!!!").queue();
		}
	}

	@Override
	public String help() {
		return "Clears <amount> of messages in the channel";
	}

	@Override
	public String name() {
		return "clear";
	}

	public List<Message> get(MessageChannel channel, int amount) {

		List<Message> messages = new ArrayList<>();

		int i = amount + 1;

		for (Message message : channel.getIterableHistory().cache(false)) {
			if (!message.isPinned()) {
				messages.add(message);
				if (--i <= 0)
					break;
			}
		}

		return messages;
	}

}
