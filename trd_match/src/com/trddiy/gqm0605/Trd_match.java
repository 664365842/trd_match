package com.trddiy.gqm0605;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class Trd_match extends JavaPlugin {

	public static Boolean debug = false;
	private CommandListener cmd;
	private Heroes hr;
	@SuppressWarnings("unused")
	private Trd_match plugin;
	private Logger log;
	private static Permission permission = null;
	private static Economy economy = null;
	@SuppressWarnings("unused")
	private PluginManager pm;
	private ConfigManager configmanager;

	private List<String> accesscommands;

	private HeroClass bossclass;

	private Map<String, Arena> arenas = null;

	public void onEnable() {
		checkvault();// 检查是否有vault
		setupPermissions();// 启动vault 权限
		setupEconomy();// 启动vault 经济
		this.log = getLogger();

		pm = this.getServer().getPluginManager();

		configmanager = new ConfigManager(this);

		saveConfig();
		loadAccessCommands(); // 从配置文件中获取允许在竞技场中使用的命令

		setupHeroes();// 加载heroes相关
		arenas = new HashMap<String, Arena>();

		getCommand("tm").setExecutor(cmd);
		sendtoserver(" v" + getDescription().getVersion() + "by:"
				+ getDescription().getAuthors() + "已启用!");

	}

	public void onDisable() {
		sendtoserver(" v" + getDescription().getVersion() + " by:"
				+ getDescription().getAuthors() + " 已禁用!");
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
			sendtoserver("错误:未找到Vault插件");
			sendtoserver("Trd竞技插件禁用中.");
			setEnabled(false);
		} else {
			return;
		}
	}

	// 装载heroes相关
	public void setupHeroes() {
		Heroes hr = (Heroes) getServer().getPluginManager().getPlugin("Heroes");
		if (hr != null) {
			this.hr = hr;
			sendtoserver("Heroes相关功能已启用");
		}
	}

	public Heroes getheroesplugin() {
		return hr;
	}

	/**
	 * 向玩家发送带前缀的信息
	 * 
	 * @param p
	 *            是玩家
	 * @param s
	 *            是要发送的字符串
	 */
	public void sendtoplayer(Player p, String s) {
		String title = ChatColor.GREEN + "[" + ChatColor.GOLD + "pvp系统"
				+ ChatColor.GREEN + "] " + ChatColor.WHITE;
		p.sendMessage(title + s);
	}

	public void sendtobcast(String s) {
		String title = ChatColor.GREEN + "[" + ChatColor.GOLD + "pvp系统"
				+ ChatColor.GREEN + "] " + ChatColor.WHITE;
		Bukkit.broadcastMessage(title + s);
	}

	/**
	 * 向控制台发送带前缀的信息
	 * 
	 * @param s
	 *            是要发送的字符串
	 */
	public void sendtoserver(String s) {
		String title = "[pvp系统]";
		this.log.info(title + s);
	}

	public Hero getHero(Player p) {
		return getheroesplugin().getCharacterManager().getHero(p);
	}

	public void addArena(String arenaname, Arena arena) {
		arenas.put(arenaname, arena);
	}

	public void delArena(String name) {
		arenas.remove(name);
	}

	public boolean setBossClass(String classname) {
		HeroClass hc = hr.getClassManager().getClass(classname);
		if (hc == null) {
			return false;
		}

		this.bossclass = hc;
		return true;
	}

	public HeroClass getBossClass() {
		return bossclass;
	}

	public Object[] getArenas() {
		return arenas.entrySet().toArray();
	}

	public Arena getArena(String name) {
		return arenas.get(name);
	}

	public Permission getPerm() {
		return permission;
	}

	private void loadAccessCommands() {
		accesscommands = configmanager.getPermissionCommand();
	}

	public List<String> getCommandList() {
		return accesscommands;
	}
}
