package com.trddiy.gqm0605;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
	private Trd_match plugin;

	public CommandListener(Trd_match plugin) {
		this.plugin = plugin;
	}

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
						&& Trd_match.permission.has(p, "trd.reload")) {
					plugin.reloadConfig();
					plugin.sendtoserver("设置已由玩家 " + p.getName() + " 重载");
					plugin.sendtoplayer(p, "设置已重载");
				}
				if (arg1.equals("debug") && Trd_match.permission.has(p, "trd.debug")) {
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
					plugin.sendtoplayer(p, "===== Trddiy核心插件帮助 =====");
					plugin.sendtoplayer(p, "/trd exp 进行经验兑换");
					plugin.sendtoplayer(p, "/trd help 打开帮助界面");
					plugin.sendtoplayer(p, "/trd xiaodai 烧掉小呆的照片");
				}
			} else {
				plugin.sendtoplayer(p, "===== Trddiy核心插件帮助 =====");
				plugin.sendtoplayer(p, "/trd exp 进行经验兑换");
				plugin.sendtoplayer(p, "/trd help 打开帮助界面");
				plugin.sendtoplayer(p, "/trd xiaodai 烧掉小呆的照片");
			}
			return true;
		}
	}
}
