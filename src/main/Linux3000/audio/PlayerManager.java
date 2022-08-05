package main.Linux3000.audio;

import main.Linux3000.DiscordBot;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

	public ConcurrentHashMap<Long, MusicController> controller;
	
	
	public PlayerManager() {
		
		this.controller = new ConcurrentHashMap<>();
	}
	
	public MusicController getController(long guildId) {
		MusicController mc;
				
				if(this.controller.containsKey(guildId)) {
					mc = this.controller.get(guildId);
				}
				else {
					mc = new MusicController(DiscordBot.INSTANCE.jda.getGuildById(guildId));
					
					this.controller.put(guildId, mc);
				}
				
				return mc;
	}

	public long getGuildByPlayerHash(int hash) {


		for (MusicController controller : this.controller.values()) {
			if(controller.getPlayer().hashCode() == hash) {
				System.out.println("jep");
				System.out.println(controller.getGuild().getIdLong());
				return controller.getGuild().getIdLong();
			}
		}


		return 0;
	}
}
