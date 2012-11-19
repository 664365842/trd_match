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
			// ��������
			if (arg1.equals("reload")) {
				plugin.reloadConfig();
				plugin.sendtoserver("�������ɿ���̨����.");
				return true;
			}
			if (arg1.equals("debug")) {
				Boolean debug = Trd_match.debug;
				if (debug == false) {
					Trd_match.debug = true;
				} else {
					Trd_match.debug = false;
				}
				plugin.sendtoserver("����״̬����Ϊ: " + Trd_match.debug);
				return true;
			}
			plugin.sendtoserver("����!�����֧�ֿ���̨ʹ��!");
			return true;
		} else {
			Player p = (Player) sender;
			if (args.length != 0) {
				String arg1 = args[0];
				if (arg1.equals("reload")
						&& plugin.getPerm().has(p, "trd.reload")) {
					plugin.reloadConfig();
					plugin.sendtoserver("����������� " + p.getName() + " ����");
					plugin.sendtoplayer(p, "����������");
				}
				if (arg1.equals("debug") && plugin.getPerm().has(p, "trd.debug")) {
					Boolean debug = Trd_match.debug;
					if (debug == false) {
						Trd_match.debug = true;
					} else {
						Trd_match.debug = false;
					}
					plugin.sendtoserver("����״̬�� " + p.getName() + " ����Ϊ: "
							+ Trd_match.debug);
					plugin.sendtoplayer(p, "����״̬�Ѹ�Ϊ: " + Trd_match.debug);
				}
				if (arg1.equals("help")) {
					plugin.sendtoplayer(p, "===== Trddiy���Ĳ������ =====");
					plugin.sendtoplayer(p, "/trd exp ���о���һ�");
					plugin.sendtoplayer(p, "/trd help �򿪰�������");
					plugin.sendtoplayer(p, "/trd xiaodai �յ�С������Ƭ");
				}
			} else {
				plugin.sendtoplayer(p, "===== Trddiy���Ĳ������ =====");
				plugin.sendtoplayer(p, "/trd exp ���о���һ�");
				plugin.sendtoplayer(p, "/trd help �򿪰�������");
				plugin.sendtoplayer(p, "/trd xiaodai �յ�С������Ƭ");
			}
			return true;
		}
	}
}
