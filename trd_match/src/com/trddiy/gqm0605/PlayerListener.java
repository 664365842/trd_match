package com.trddiy.gqm0605;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	private Arena parentarena;

	public PlayerListener(Arena arena) {
		this.parentarena = arena;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		for (Player p : parentarena.getplayers()) {
			if ((Player) p == player) {
				event.setCancelled(true);
				return;
			}
		}
		if (player == parentarena.getboss()) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPlayerSendCommand(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		if (parentarena.getplayers().contains(player)) {
			String commands[] = event.getMessage().split(" ");
			if (!parentarena.getPlugin().getCommandList().contains(commands[0])) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "你不能在竞技场中使用这个命令");
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;

		Player player = event.getPlayer();

		if (this.parentarena.getArenaState() != ArenaState.STAT_READY)
			return;

		if (!this.parentarena.getplayers().contains(player))
			return;

		event.setCancelled(true);

	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;

		if (!(event.getEntity() instanceof Player))
			return;

		if (!(event.getDamager() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();

		if (parentarena.getplayers().contains(player)) {
			if (parentarena.getArenaState() == ArenaState.STAT_READY
					|| parentarena.getArenaState() == ArenaState.STAT_OPEN) {
				event.setCancelled(true);
				return;
			}
			if (parentarena.getArenaState() == ArenaState.STAT_STARTED) {
				if (damager != parentarena.getboss()) {
					event.setCancelled(true);
					return;
				}
			}

		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		if (parentarena.getplayers().contains(player)) {
			if (parentarena.getboss() == player) {
				parentarena.bossDeath();
			} else if (parentarena.isLiving(player)) {
				parentarena.playerDeath(player);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		parentarena.playerLeave(player, "游戏关闭,");
	}

}
