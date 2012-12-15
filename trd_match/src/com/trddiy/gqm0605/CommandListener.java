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
						&& plugin.getPerm().has(p, "tm.reload")) {
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
					plugin.sendtoplayer(p, "===== Trddiy Match������� =====");
				}			
				if (args[0].equalsIgnoreCase("create"))
				{
					if(args.length != 2 || args.length != 4)
					{
						sender.sendMessage(ChatColor.RED + "ʹ�÷���:\"/tm create ���������� (��С����) (�������)\"");
					}
					else if(args.length == 4)
					{
						boolean isfiguremin = args[2].matches("[0-9]+");
						boolean isfiguremax = args[3].matches("[0-9]+");
						if((!isfiguremin)||(!isfiguremax))
						{
							sender.sendMessage(ChatColor.RED + "ʹ�÷���:\"/tm create ���������� (��С����) (�������)\"");
						}
					}
					else
					{
						if(plugin.getArena(args[1])!=null)
						{
							sender.sendMessage(ChatColor.RED + "������" + args[1] +"�Ѵ���!");
						}
						else
						{
							if(args.length == 2)
								plugin.addArena(args[1], new Arena(plugin,args[1]));
							else
								plugin.addArena(args[1], new Arena(plugin,args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3])));
						}
						
						sender.sendMessage(ChatColor.GREEN + "������" + args[1] +"�����ɹ�!");
					}
				}
				if (args[0].equalsIgnoreCase("edit"))
				{
					if(plugin.getPerm().has(sender, "tm.admin"))
					{
						if(args.length!=2)
						{
							sender.sendMessage(ChatColor.RED+"/tm edit <����������>");
						}
						else
						{
							Arena arena = plugin.getArena(args[1]);
							if(arena==null)
							{
								sender.sendMessage(ChatColor.RED+"û���ҵ��þ�����!");
							}
							else
							{
								arena.editArena();
								sender.sendMessage(ChatColor.GREEN+"����������༭ģʽ!");
							}
						}
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"��û��ʹ����������Ȩ��!");
					}
				}
				if (args[0].equalsIgnoreCase("list"))
				{

					if(plugin.getPerm().has(sender, "tm.user"))
					{
						Object[] entrys = plugin.getArenas();
						String arenalist = ChatColor.GREEN+"�������б�:";
						if(entrys.length==0)
						{
							arenalist="û�о�����";
						}
						for(int i=0;i<entrys.length;i++)
						{
							if(((Arena)((Entry)entrys[i]).getValue()).getArenaState()==Arena.STAT_OPEN)
								arenalist+=ChatColor.GREEN;
							else if(((Arena)((Entry)entrys[i]).getValue()).getArenaState()==Arena.STAT_READY||((Arena)((Entry)entrys[i]).getValue()).getArenaState()==Arena.STAT_STARTED)
								arenalist+=ChatColor.YELLOW;
							else
								arenalist+=ChatColor.RED;
							arenalist+=((Entry)entrys[i]).getKey();
							if(i<entrys.length-1)
								arenalist+=",";
						}
						sender.sendMessage(arenalist);
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"��û��ʹ�þ�������Ȩ��!");
					}
				}
				if (args[0].equalsIgnoreCase("join"))
				{
					if(args.length!=2)
					{
						sender.sendMessage(ChatColor.RED+"/tm join <����������>");
					}
					else
					{
						if(plugin.getPerm().has(sender, "tm.user"))
						{
							String arenaname = args[1];
							Arena joinarena = plugin.getArena(arenaname);
							if(joinarena==null)
							{
								sender.sendMessage(ChatColor.RED+"û���ҵ��þ�����");
							}
							else
							{
								joinarena.playerJoin((Player) sender);
							}
						}
						else
						{
							sender.sendMessage(ChatColor.RED+"��û��ʹ�þ�������Ȩ��!");
						}
					}
					
				}
				if(args[0].equalsIgnoreCase("disable"))
				{
					if(plugin.getPerm().has(sender, "tm.admin"))
					{
						Arena arena = plugin.getArena(args[1]);
						if(arena==null)
						{
							sender.sendMessage(ChatColor.RED+"û���ҵ��þ�����!");
						}
						else
						{
							arena.disableArena();
							sender.sendMessage(ChatColor.GREEN+"�������ѹر�!");
						}
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"��û��ʹ����������Ȩ��");
					}
				}
			} else {
				plugin.sendtoplayer(p, "===== Trddiy Match������� =====");
			}
			return true;
		}
	}
}
