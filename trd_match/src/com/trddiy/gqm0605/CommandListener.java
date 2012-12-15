package com.trddiy.gqm0605;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
	private Trd_match plugin;

	public CommandListener(Trd_match plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("rawtypes")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			String arg1 = args[0];
			// 重载命令
			if (arg1.equals("reload")) {
				plugin.reloadConfig();
				plugin.sendtoserver("设置已由控制台重载.");
				return true;
			}
			if (arg1.equals("debug")) {
				Boolean debug = Trd_match.debug;
				if (debug == false) {
					Trd_match.debug = true;
				} else {
					Trd_match.debug = false;
				}
				plugin.sendtoserver("调试状态更改为: " + Trd_match.debug);
				return true;
			}

			plugin.sendtoserver("错误!本命令不支持控制台使用!");
			return true;
		} else {
			Player p = (Player) sender;
			if (args.length != 0) {
				String arg1 = args[0];
				if (arg1.equals("reload")
						&& plugin.getPerm().has(p, "tm.reload")) {
					plugin.reloadConfig();
					plugin.sendtoserver("设置已由玩家 " + p.getName() + " 重载");
					plugin.sendtoplayer(p, "设置已重载");
				}
				if (arg1.equals("debug")
						&& plugin.getPerm().has(p, "trd.debug")) {
					Boolean debug = Trd_match.debug;
					if (debug == false) {
						Trd_match.debug = true;
					} else {
						Trd_match.debug = false;
					}
					plugin.sendtoserver("调试状态由 " + p.getName() + " 更改为: "
							+ Trd_match.debug);
					plugin.sendtoplayer(p, "调试状态已改为: " + Trd_match.debug);
				}
				if (arg1.equals("help")) {
					plugin.sendtoplayer(p, "===== Trddiy Match插件帮助 =====");
				}
				if (args[0].equalsIgnoreCase("create")) {
					if (args.length != 2 || args.length != 4) {
						sender.sendMessage(ChatColor.RED
								+ "使用方法:\"/tm create 竞技场名称 (最小人数) (最大人数)\"");
					} else if (args.length == 4) {
						boolean isfiguremin = args[2].matches("[0-9]+");
						boolean isfiguremax = args[3].matches("[0-9]+");
						if ((!isfiguremin) || (!isfiguremax)) {
							sender.sendMessage(ChatColor.RED
									+ "使用方法:\"/tm create 竞技场名称 (最小人数) (最大人数)\"");
						}
					} else {
						if (plugin.getArena(args[1]) != null) {
							sender.sendMessage(ChatColor.RED + "竞技场" + args[1]
									+ "已存在!");
						} else {
							if (args.length == 2)
								plugin.addArena(args[1], new Arena(plugin,
										args[1]));
							else
								plugin.addArena(args[1], new Arena(plugin,
										args[1], Integer.parseInt(args[2]),
										Integer.parseInt(args[3])));
						}

						sender.sendMessage(ChatColor.GREEN + "竞技场" + args[1]
								+ "创建成功!");
					}
				}
				if (args[0].equalsIgnoreCase("edit")) {
					if (plugin.getPerm().has(sender, "tm.admin")) {
						if (args.length != 2) {
							sender.sendMessage(ChatColor.RED
									+ "/tm edit <竞技场名称>");
						} else {
							Arena arena = plugin.getArena(args[1]);
							if (arena == null) {
								sender.sendMessage(ChatColor.RED + "没有找到该竞技场!");
							} else {
								arena.editArena();
								sender.sendMessage(ChatColor.GREEN
										+ "竞技场进入编辑模式!");
							}
						}
					} else {
						sender.sendMessage(ChatColor.RED + "你没有使用这个命令的权限!");
					}
				}
				if (args[0].equalsIgnoreCase("list")) {

					if (plugin.getPerm().has(sender, "tm.user")) {
						Object[] entrys = plugin.getArenas();
						String arenalist = ChatColor.GREEN + "竞技场列表:";
						if (entrys.length == 0) {
							arenalist = "没有竞技场";
						}
						for (int i = 0; i < entrys.length; i++) {
							if (((Arena) ((Entry) entrys[i]).getValue())
									.getArenaState() == ArenaState.STAT_OPEN)
								arenalist += ChatColor.GREEN;
							else if (((Arena) ((Entry) entrys[i]).getValue())
									.getArenaState() == ArenaState.STAT_READY
									|| ((Arena) ((Entry) entrys[i]).getValue())
											.getArenaState() == ArenaState.STAT_STARTED)
								arenalist += ChatColor.YELLOW;
							else
								arenalist += ChatColor.RED;
							arenalist += ((Entry) entrys[i]).getKey();
							if (i < entrys.length - 1)
								arenalist += ",";
						}
						sender.sendMessage(arenalist);
					} else {
						sender.sendMessage(ChatColor.RED + "你没有使用竞技场的权限!");
					}
				}
				if (args[0].equalsIgnoreCase("join")) {
					if (args.length != 2) {
						sender.sendMessage(ChatColor.RED + "/tm join <竞技场名称>");
					} else {
						if (plugin.getPerm().has(sender, "tm.user")) {
							String arenaname = args[1];
							Arena joinarena = plugin.getArena(arenaname);
							if (joinarena == null) {
								sender.sendMessage(ChatColor.RED + "没有找到该竞技场");
							} else {
								joinarena.playerJoin((Player) sender);
							}
						} else {
							sender.sendMessage(ChatColor.RED + "你没有使用竞技场的权限!");
						}
					}

				}
				if (args[0].equalsIgnoreCase("disable")) {
					if (plugin.getPerm().has(sender, "tm.admin")) {
						Arena arena = plugin.getArena(args[1]);
						if (arena == null) {
							sender.sendMessage(ChatColor.RED + "没有找到该竞技场!");
						} else {
							arena.disableArena();
							sender.sendMessage(ChatColor.GREEN + "竞技场已关闭!");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "你没有使用这个命令的权限");
					}
				}
			} else {
				plugin.sendtoplayer(p, "===== Trddiy Match插件帮助 =====");
			}
			return true;
		}
	}
}
