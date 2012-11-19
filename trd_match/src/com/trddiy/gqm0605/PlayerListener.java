package com.trddiy.gqm0605;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerListener implements Listener{
	
	private Arena parentarena;
	
	public PlayerListener(Arena arena)
	{
		this.parentarena = arena;
	}
	
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		
		for(Player p : parentarena.getplayers())
		{
			if((Player)p == player)
			{
				event.setCancelled(true);
				return;
			}
		}
		if(player == parentarena.getboss())
		{
			event.setCancelled(true);
			return;
		}
	}

}
