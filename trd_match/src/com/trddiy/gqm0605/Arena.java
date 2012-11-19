package com.trddiy.gqm0605;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MathHelper;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class Arena {
	
	public static final int STAT_EDIT = 	1;
	public static final int STAT_CONFIG =	2;
	public static final int STAT_OPEN = 	3;
	public static final int STAT_READY =	4;
	public static final int STAT_STARTED = 	5; 
	
	private Trd_match plugin;
	private List<Player> players;
	private Player boss;
	private HeroClass oldclass;
	private String arenaname;
	
	private int minplayer;
	private int maxplayer;
	
	private boolean started;
	private int arenastate;
	
	private Location ready;
	private Location playerspawn;
	private Location bossspawn;
	private Location deathspawn;
	
	public Arena(Trd_match plugin,String name)
	{
		this.arenaname = name;
		this.plugin = plugin;
		this.minplayer = 3;
		this.maxplayer = 10;
		playerspawn = null;
		bossspawn = null;
		ready = null;
		deathspawn = null;
		players = new ArrayList<Player>();
		boss = null;
		plugin.addArena(name, this);
	}	
	
	public Arena(Trd_match plugin,String name,int minplayer ,int maxplayer)
	{
		this.arenaname = name;
		this.plugin = plugin;
		this.minplayer = minplayer;
		this.maxplayer = maxplayer;
		playerspawn = null;
		bossspawn = null;
		ready = null;
		deathspawn = null;
		players = new ArrayList<Player>();
		boss = null;
		plugin.addArena(name, this);
	}
	
	public Arena(Trd_match plugin,String name,Location ready ,Location playerspawn, Location bossspawn, Location deathspawn, int minplayer ,int maxplayer){
		this.arenaname = name;
		this.plugin = plugin;
		this.ready = ready;
		this.playerspawn = playerspawn;
		this.bossspawn = bossspawn;
		this.deathspawn = deathspawn;
		this.minplayer = minplayer;
		this.maxplayer = maxplayer;
		players = new ArrayList<Player>();
		boss = null;
		plugin.addArena(name, this);
	}
//	public boolean playerisinarena(Player p){
//		Location pl = p.getLocation();
//		double minx = Math.min(l1.getX(), l2.getX());
//		double maxx = Math.max(l1.getX(), l2.getX());
//		double miny = Math.min(l1.getY(), l2.getY());
//		double maxy = Math.max(l1.getY(), l2.getY());
//		double minz= Math.min(l1.getZ(), l2.getZ());
//		double maxz = Math.max(l1.getZ(), l2.getZ());
//		if(pl.getX()<=maxx&&pl.getX()>=minx&&pl.getY()<=maxy&&pl.getY()>=miny&&pl.getZ()<=maxz&&pl.getZ()>=minz){
//			return true;
//		}
//		return false;
//	}
	public void setArenaState(int state)
	{
		this.arenastate = state;
	}
	
	public int getArenaState()
	{
		return this.arenastate;
	}
	
	public void setReadyLocation(Location ready)
	{
		this.ready = ready;
	}
	
	public void setPlayerSpawnLocation(Location playerspawn)
	{
		this.playerspawn = playerspawn;
	}
	
	public void setBossSpawnLocation(Location bossspawn)
	{
		this.bossspawn = bossspawn;
	}
	
	public void setDeathSpawnLocation(Location deathspawn)
	{
		this.deathspawn = deathspawn;
	}
	
	public int startGame()
	{
		if(getplayernum() < this.minplayer)
		{
			return 1;
		}
		
		this.started = true;

		spawnBoss();
		
		for(Player p : players){
			p.teleport(playerspawn);
			p.setGameMode(GameMode.ADVENTURE);
			plugin.sendtoplayer(p,"15秒倒计时准备");
		}
		
		final String name = this.arenaname;
		
		boss.teleport(bossspawn);
		
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
				public void run(){
					Arena arena = plugin.getArena(name);
					

					Hero h = plugin.getHero(arena.getboss());
					arena.setArenaState(STAT_STARTED);
					arena.arenaBoardcast("游戏开始!");
					arena.setbossoldclass(h.getHeroClass());
					h.setHeroClass(plugin.getBossClass(),false);
					plugin.sendtobcast("boss等级: "+level);
				}
			},300L);
		boss.teleport(bossspawn);
		
		return 0;
	}
	
	private Player spawnBoss()
	{
		Random r = new Random();
		int rand = MathHelper.nextInt(r, 0, getplayernum());
		
		Player boss = players.get(rand);
		setboss(boss);
		return boss;
	}
	
	public void clear(){
		players = null;
		boss = null;
		oldclass = null;
	}
	
	public void setboss(Player p){
		this.boss = p;
		oldclass = plugin.getHero(p).getHeroClass();
	}
	
	public Player getboss(){
		return boss;
	}
	
	public boolean isboss(Player p){
		if(boss != null && p.equals(boss)){
			return true;
		}
		return false;
	}
	
	public HeroClass getbossoldclass(){
		return oldclass;
	}
	
	public void setbossoldclass(HeroClass hc){
		this.oldclass = hc;
	}
	
	public void playerLeave(Player player,String reason)
	{
		for(Player p: players)
		{
			if(p == player)
			{
				plugin.sendtoplayer(player, reason + " 从竞技场中退出");
				removeplayer(player);
				return;
			}
		}
	}
	
	public void playerJoin(Player player)
	{
		if(arenastate == STAT_OPEN)
		{
			for(Player p: players)
			{
				if(p == player)
				{
					plugin.sendtoplayer(player, "你已经在竞技场中了");
					return;
				}
			}
			plugin.sendtoplayer(player, "你加入了竞技场 "+ arenaname +" !");
			
			addplayer(player);
			
			arenaBoardcast(player.getDisplayName() + " 加入了竞技场");
		}
		else
		{
			plugin.sendtoplayer(player, "你无法加入这个竞技场");
		}
	}
	
	public void arenaBoardcast(String message)
	{
		for(Player p: players)
		{
			plugin.sendtoplayer(p, message);
		}
	}
	
	public void addplayer(Player p){
		players.add(p);
	}
	
	public void removeplayer(Player p){
		players.remove(p);
	}
	
	public int getplayernum(){
		return players.size();
	}
	
	public List<Player> getplayers(){
		return players;
	}
}
