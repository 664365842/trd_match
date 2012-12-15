package com.trddiy.gqm0605;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.MathHelper;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class Arena {
	
	public static final int STAT_EDIT = 	1; //竞技场状态 编辑模式
	//public static final int STAT_CONFIG =	2; //竞技场状态 配置模式 取消
	public static final int STAT_OPEN = 	3; //竞技场状态 开放加入模式
	public static final int STAT_READY =	4; //竞技场状态 关闭加入 玩家准备中
	public static final int STAT_STARTED = 	5; //竞技场状态 关闭加入 游戏进行中
	public static final int STAT_DISABLED =	6; //竞技场状态 竞技场禁止中
	
	public static final int END_REASON_SUCCESS = 		1; //游戏结束原因 玩家胜利
	public static final int END_REASON_NOPLAYER =		2; //游戏结束原因 玩家全部死亡
	public static final int END_REASON_BOSSLEAVE =		3; //游戏结束原因 BOSS异常离场
	public static final int END_REASON_ARENADISABLE =	4; //游戏结束原因 竞技场被强制关闭
	public static final int END_REASON_ARENAEDIT =		5; //游戏结束原因 竞技场进入编辑模式被强制关闭
	public static final int END_REASON_UNKNOW =			0; //游戏结束原因 未知
	
	private Trd_match plugin;
	private List<Player> players;
	private List<Player> livingplayers;
	private Map<Player,Location> playerslocation;
	private Player boss;
	
	private HeroClass oldclass;
	private HeroClass bossclass;
	
	private String arenaname;
	private int bosslevel;
	private boolean bossliving;
	private int bosskillcount;
	
	private int minplayer;
	private int maxplayer;
	
	private boolean started;
	private int arenastate;
	
	private Location ready;
	private Location playerspawn;
	private Location bossspawn;
	private Location deathspawn;
	
	private PlayerListener playerlistener;
	
	//竞技场构造 基本命令构造方法
	public Arena(Trd_match plugin,String name)
	{
		this.arenaname = name;
		this.plugin = plugin;
		this.minplayer = 3;
		this.maxplayer = 10;
		this.bosslevel = 0;
		playerspawn = null;
		bossspawn = null;
		ready = null;
		deathspawn = null;
		players = new ArrayList<Player>();
		livingplayers = new ArrayList<Player>();
		playerslocation = new HashMap<Player,Location>();
		boss = null;
		arenastate = STAT_EDIT;
		
		playerlistener = new PlayerListener(this);
		plugin.getServer().getPluginManager().registerEvents(playerlistener, plugin);
		
		plugin.addArena(name, this);
	}	
	
	//竞技场构造 带人数上下限设定的命令构造方法
	public Arena(Trd_match plugin,String name,int minplayer ,int maxplayer)
	{
		this.arenaname = name;
		this.plugin = plugin;
		this.minplayer = minplayer;
		this.maxplayer = maxplayer;
		this.bosslevel = 0;
		playerspawn = null;
		bossspawn = null;
		ready = null;
		deathspawn = null;
		players = new ArrayList<Player>();
		livingplayers = new ArrayList<Player>();
		playerslocation = new HashMap<Player,Location>();
		boss = null;
		arenastate = STAT_EDIT;
		plugin.addArena(name, this);
		
		playerlistener = new PlayerListener(this);
		plugin.getServer().getPluginManager().registerEvents(playerlistener, plugin);
	}
	
	//竞技场构造 从竞技场文件中载入的完全属性构造方法
	public Arena(Trd_match plugin,String name,Location ready ,Location playerspawn, Location bossspawn, Location deathspawn, int minplayer ,int maxplayer){
		this.arenaname = name;
		this.plugin = plugin;
		this.ready = ready;
		this.playerspawn = playerspawn;
		this.bossspawn = bossspawn;
		this.deathspawn = deathspawn;
		this.minplayer = minplayer;
		this.maxplayer = maxplayer;
		this.bosslevel = 0;
		players = new ArrayList<Player>();
		livingplayers = new ArrayList<Player>();
		playerslocation = new HashMap<Player,Location>();
		boss = null;
		arenastate = STAT_EDIT;
		plugin.addArena(name, this);
		
		playerlistener = new PlayerListener(this);
		plugin.getServer().getPluginManager().registerEvents(playerlistener, plugin);
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
	
	//设定竞技场的状态
	public void setArenaState(int state)
	{
		this.arenastate = state;
	}
	
	//获取目前竞技场状态
	public int getArenaState()
	{
		return this.arenastate;
	}
	
	//设置竞技场玩家准备室位置
	public void setReadyLocation(Location ready)
	{
		this.ready = ready;
	}
	
	//设置游戏开始玩家出生位置
	public void setPlayerSpawnLocation(Location playerspawn)
	{
		this.playerspawn = playerspawn;
	}
	
	//设置游戏开始BOSS出生位置
	public void setBossSpawnLocation(Location bossspawn)
	{
		this.bossspawn = bossspawn;
	}
	
	//设置游戏开始玩家死亡重生位置
	public void setDeathSpawnLocation(Location deathspawn)
	{
		this.deathspawn = deathspawn;
	}
	
	//游戏开始处理
	public boolean startGame()
	{
		if(getplayernum() < this.minplayer)
		{
			return false;
		}
		
		this.started = true;

		spawnBoss();
		bossliving = true;
		bosskillcount = 0;
		for(Player p : players){
			if(p!=boss)
				livingplayers.add(p);
			p.teleport(playerspawn);
			p.setGameMode(GameMode.ADVENTURE);
			plugin.sendtoplayer(p,"15秒倒计时准备");
		}
		
		final String name = this.arenaname;
		
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
				public void run(){
					Arena arena = plugin.getArena(name);
					Hero h = plugin.getHero(arena.getboss());
					arena.setArenaState(STAT_STARTED);
					arena.arenaBoardcast("游戏开始!");
					arena.setbossoldclass(h.getHeroClass());
					h.setHeroClass(arena.getBossClass(),false);
					arena.getboss().teleport(arena.getBossSpawnLocation());
					arena.arenaBoardcast(arena.getboss().getName() + " 被选为了BOSS!");
					arena.arenaBoardcast("BOSS等级: "+bosslevel);
					
				}
			},300L);
		
		
		return true;
	}
	
	//游戏结束处理
	public void endGame(int reason)
	{
		switch(reason)
		{
		case END_REASON_SUCCESS:
			{
				for(Player p: players)
				{
					playerLeave(p,"玩家胜利,游戏结束");
				}
				arenastate = STAT_OPEN;
				clear();
				break;
			}
		case END_REASON_NOPLAYER:
			{
				for(Player p: players)
				{
					playerLeave(p,"BOSS胜利,游戏结束");
				}	
				arenastate = STAT_OPEN;
				clear();
				break;
			}
		case END_REASON_BOSSLEAVE:			
			{

				arenastate = STAT_OPEN;
				this.arenaBoardcast("BOSS脱离竞技场,游戏重新准备");
				for(Player p: players)
				{
					p.teleport(ready);
				}	
				break;
			}
		case END_REASON_ARENADISABLE:
		{
			for(Player p: players)
			{
				playerLeave(p,"竞技场关闭");
			}	
			arenastate = STAT_DISABLED;
			clear();
			break;
		}
		case END_REASON_ARENAEDIT:
		{
			for(Player p: players)
			{
				playerLeave(p,"竞技场进入编辑模式");
			}	
			arenastate = STAT_EDIT;
			clear();
			break;
		}
		case END_REASON_UNKNOW:
		default:
			{
				for(Player p: players)
				{
					playerLeave(p,"竞技场发生未知错误");
				}					
				arenastate = STAT_OPEN;
				clear();
				break;
			}
		}
		
	}
	
	//生成BOSS 根据玩家数量选择BOSS属性 并随机选择BOSS
	private Player spawnBoss()
	{	
		int count = players.size();
		if(count <=5){
			bossclass = gethclass("测试1");
			bosslevel = 1;
		}else if(count <=10){
			bossclass = gethclass("测试2");
			bosslevel =2;
		}else if (count <= 15){
			bossclass = gethclass("测试3");
			bosslevel = 3;
		}else{
			bossclass = gethclass("测试4");
			bosslevel = 4;
		}
		Random r = new Random();
		int rand = r.nextInt(players.size());
		
		Player boss = players.get(rand);
		setboss(boss);
		return boss;
	}
	
	//获取heroes职业
	public HeroClass gethclass(String s){
		return plugin.getheroesplugin().getClassManager().getClass(s);
	}
	
	//开放竞技场
	public boolean openArena()
	{
		if(this.bossspawn == null || this.playerspawn == null || this.ready == null || this.deathspawn == null)
		{
			return false;
		}
		
		arenastate = STAT_OPEN;
		
		return true;
	}
	
	//竞技场状态清空
	public void clear(){
		playerslocation.clear();
		players.clear();
		livingplayers.clear();
		bossliving = false;
		boss = null;
		oldclass = null;
		bossclass = null;
	}
	
	//将玩家设置为boss
	public void setboss(Player p){
		this.boss = p;
		oldclass = plugin.getHero(p).getHeroClass();
	}
	
	//获取BOSS
	public Player getboss(){
		return boss;
	}
	
	//判断一个玩家是否为BOSS
	public boolean isboss(Player p){
		if(boss != null && p.equals(boss)){
			return true;
		}
		return false;
	}
	
	//
	public Location getBossSpawnLocation()
	{
		return bossspawn;
	}
	
	public HeroClass getBossClass()
	{
		return bossclass;
	}
	
	public HeroClass getbossoldclass(){
		return oldclass;
	}
	
	public void setbossoldclass(HeroClass hc){
		this.oldclass = hc;
	}
	
	public void playerDeath(Player player)
	{
		livingplayers.remove(player);
		if(livingplayers.size() == 0)
			endGame(Arena.END_REASON_NOPLAYER);
	}
	
	public void bossDeath()
	{
		bossliving = false;
		endGame(Arena.END_REASON_SUCCESS);
	}
	
	public void playerLeave(Player player,String reason)
	{
		if(players.contains(player))
		{
			plugin.sendtoplayer(player, reason + " 从竞技场中退出");
			removeplayer(player);
			if(player == boss)
			{
				Hero h = plugin.getHero(player);
				h.setHeroClass(oldclass, false);
				if(bossliving)
				{
					bossliving = false;
					endGame(Arena.END_REASON_BOSSLEAVE);
				}
			}
			if(livingplayers.contains(player))
			{
				playerDeath(player);
			}
			
			return;
		}
	}
	
	public boolean playerJoin(Player player)
	{
		if(arenastate == STAT_OPEN)
		{
			if(players.contains(player))
			{
				plugin.sendtoplayer(player, "你已经在竞技场中了");
				return false;
			}
			plugin.sendtoplayer(player, ChatColor.GREEN+"你加入了竞技场 "+ arenaname +" !");
			
			Location pol = player.getLocation();
			Location pl = new Location(pol.getWorld(),pol.getX(),pol.getY(),pol.getZ(),pol.getYaw(),pol.getPitch());
			playerslocation.put(player, pl);
			player.teleport(ready);
			arenaBoardcast(player.getName() + " 加入了竞技场");
			
			addplayer(player);
		}
		else
		{
			String reason = ChatColor.RED+"你无法加入这个竞技场,";
			if(this.arenastate==this.STAT_DISABLED)
			{
				reason+="竞技场已被禁止";
			}
			else if(this.arenastate==this.STAT_EDIT)
			{
				reason+=",竞技场正在编辑中";
			}
			else if(this.arenastate==this.STAT_READY||this.arenastate==this.STAT_STARTED)
			{
				reason+="游戏已经开始";
			}
			
			plugin.sendtoplayer(player, reason);
			return false;
		}
		
		return false;
	}
	
	public void editArena()
	{
		if(this.arenastate==this.STAT_READY||this.arenastate==this.STAT_STARTED)
			this.endGame(this.END_REASON_ARENAEDIT);
		else if(this.arenastate==this.STAT_OPEN)
		{
			for(Player p:players)
			{
				this.removeplayer(p);
				p.sendMessage(ChatColor.RED+"竞技场进入编辑模式!");
			}
		}
	}
	
	public void disableArena()
	{
		if(this.arenastate==this.STAT_READY||this.arenastate==this.STAT_STARTED)
			this.endGame(this.END_REASON_ARENADISABLE);
		else if(this.arenastate==this.STAT_OPEN)
		{
			for(Player p:players)
			{
				this.removeplayer(p);
				p.sendMessage(ChatColor.RED+"竞技场关闭!");
			}
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
		p.teleport(playerslocation.get(p));
		playerslocation.remove(p);
		players.remove(p);
	}
	
	public int getplayernum(){
		return players.size();
	}
	
	public int getLivingPlayerCount()
	{
		return livingplayers.size();
	}
	
	public boolean isLiving(Player player)
	{
		if(livingplayers.contains(player))
			return true;
		return false;
	}
	
	public List<Player> getplayers(){
		return players;
	}
	
	public Trd_match getPlugin()
	{
		return plugin;
	}
}
