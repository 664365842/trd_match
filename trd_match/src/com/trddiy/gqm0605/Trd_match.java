package com.trddiy.gqm0605;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;

public class Trd_match extends JavaPlugin {
	public static Boolean debug = false;
	private CommandListener cmd;
	public Heroes hr;
	public static Configuration config;
	public Trd_match plugin;
	private Logger log;
	public static Permission permission = null;
	public static Economy economy = null;

	public void onEnable() {
		checkvault();// ����Ƿ���vault
		setupPermissions();// ����vault Ȩ��
		setupEconomy();// ����vault ����
//		final Boolean a;
//		final Boolean c;
		this.log = getLogger();
		config = getConfig();
		config.options().copyDefaults(true);// ���������ļ�
		saveConfig();
//		sendtoserver("�������޶�: " + a);
//		sendtoserver("��������: " + c);
		setupHeroes();// ����heroes���
		getCommand("trd").setExecutor(cmd);
		sendtoserver(" v" + getDescription().getVersion() + "by:"
				+ getDescription().getAuthors() + "������!");
	}

	public void onDisable() {
		sendtoserver(" v" + getDescription().getVersion() + " by:"
				+ getDescription().getAuthors() + " �ѽ���!");
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null);
	}

	private void checkvault() {
		Plugin p = getServer().getPluginManager().getPlugin("Vault");
		if (p == null) {
			sendtoserver("����:δ�ҵ�Vault���");
			sendtoserver("Trd�������������.");
			setEnabled(false);
		} else {
			return;
		}
	}

	// װ��heroes���
	public void setupHeroes() {
		Heroes hr = (Heroes) getServer().getPluginManager().getPlugin("Heroes");
		if (hr != null) {
			this.hr = hr;
			sendtoserver("Heroes��ع���������");
		}
	}

	public Heroes getheroesplugin() {
		return hr;
	}

	/**
	 * ����ҷ��ʹ�ǰ׺����Ϣ
	 * 
	 * @param p
	 *            �����
	 * @param s
	 *            ��Ҫ���͵��ַ���
	 */
	public void sendtoplayer(Player p, String s) {
		String title = ChatColor.GREEN + "[" + ChatColor.GOLD + "pvpϵͳ"
				+ ChatColor.GREEN + "] " + ChatColor.WHITE;
		p.sendMessage(title + s);
	}
	public void sendtobcast(String s) {
		String title = ChatColor.GREEN + "[" + ChatColor.GOLD + "pvpϵͳ"
				+ ChatColor.GREEN + "] " + ChatColor.WHITE;
		Bukkit.broadcastMessage(title + s);
	}

	/**
	 * �����̨���ʹ�ǰ׺����Ϣ
	 * 
	 * @param s
	 *            ��Ҫ���͵��ַ���
	 */
	public void sendtoserver(String s) {
		String title = "[pvpϵͳ]";
		this.log.info(title + s);
	}

	public Hero getHero(Player p) {
		return getheroesplugin().getCharacterManager().getHero(p);
	}
}

