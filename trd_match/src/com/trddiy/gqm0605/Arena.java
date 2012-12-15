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
	private ArenaState arenastate;
	
	private Location ready;
	private Location playerspawn;
	private Location bossspawn;
	private Location deathspawn;
	
	private PlayerListener playerlistener;
	
	//���������� ��������췽��
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
		arenastate = ArenaState.STAT_EDIT;
		
		playerlistener = new PlayerListener(this);
		plugin.getServer().getPluginManager().registerEvents(playerlistener, plugin);
		
		plugin.addArena(name, this);
	}	
	
	//���������� �������������趨������췽��
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
		arenastate = ArenaState.STAT_EDIT;
		plugin.addArena(name, this);
		
		playerlistener = new PlayerListener(this);
		plugin.getServer().getPluginManager().registerEvents(playerlistener, plugin);
	}
	
	//���������� �Ӿ������ļ����������ȫ���Թ��췽��
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
		arenastate = ArenaState.STAT_EDIT;
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
	
	//�趨��������״̬
	public void setArenaState(ArenaState state)
	{
		this.arenastate = state;
	}
	
	//��ȡĿǰ������״̬
	public ArenaState getArenaState()
	{
		return this.arenastate;
	}
	
	//���þ��������׼����λ��
	public void setReadyLocation(Location ready)
	{
		this.ready = ready;
	}
	
	//������Ϸ��ʼ��ҳ���λ��
	public void setPlayerSpawnLocation(Location playerspawn)
	{
		this.playerspawn = playerspawn;
	}
	
	//������Ϸ��ʼBOSS����λ��
	public void setBossSpawnLocation(Location bossspawn)
	{
		this.bossspawn = bossspawn;
	}
	
	//������Ϸ��ʼ�����������λ��
	public void setDeathSpawnLocation(Location deathspawn)
	{
		this.deathspawn = deathspawn;
	}
	
	//��Ϸ��ʼ����
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
			plugin.sendtoplayer(p,"15�뵹��ʱ׼��");
		}
		
		final String name = this.arenaname;
		
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
				public void run(){
					Arena arena = plugin.getArena(name);
					Hero h = plugin.getHero(arena.getboss());
					arena.setArenaState(ArenaState.STAT_STARTED);
					arena.arenaBoardcast("��Ϸ��ʼ!");
					arena.setbossoldclass(h.getHeroClass());
					h.setHeroClass(arena.getBossClass(),false);
					arena.getboss().teleport(arena.getBossSpawnLocation());
					arena.arenaBoardcast(arena.getboss().getName() + " ��ѡΪ��BOSS!");
					arena.arenaBoardcast("BOSS�ȼ�: "+bosslevel);
					
				}
			},300L);
		
		
		return true;
	}
	
	//��Ϸ��������
	public void endGame(EndReason reason)
	{
		switch(reason)
		{
		case END_REASON_SUCCESS:
			{
				for(Player p: players)
				{
					playerLeave(p,"���ʤ��,��Ϸ����");
				}
				arenastate = ArenaState.STAT_OPEN;
				clear();
				break;
			}
		case END_REASON_NOPLAYER:
			{
				for(Player p: players)
				{
					playerLeave(p,"BOSSʤ��,��Ϸ����");
				}	
				arenastate = ArenaState.STAT_OPEN;
				clear();
				break;
			}
		case END_REASON_BOSSLEAVE:			
			{

				arenastate = ArenaState.STAT_OPEN;
				this.arenaBoardcast("BOSS���뾺����,��Ϸ����׼��");
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
				playerLeave(p,"�������ر�");
			}	
			arenastate = ArenaState.STAT_DISABLED;
			clear();
			break;
		}
		case END_REASON_ARENAEDIT:
		{
			for(Player p: players)
			{
				playerLeave(p,"����������༭ģʽ");
			}	
			arenastate = ArenaState.STAT_EDIT;
			clear();
			break;
		}
		case END_REASON_UNKNOW:
		default:
			{
				for(Player p: players)
				{
					playerLeave(p,"����������δ֪����");
				}					
				arenastate = ArenaState.STAT_OPEN;
				clear();
				break;
			}
		}
		
	}
	
	//����BOSS �����������ѡ��BOSS���� �����ѡ��BOSS
	private Player spawnBoss()
	{	
		int count = players.size();
		if(count <=5){
			bossclass = gethclass("����1");
			bosslevel = 1;
		}else if(count <=10){
			bossclass = gethclass("����2");
			bosslevel =2;
		}else if (count <= 15){
			bossclass = gethclass("����3");
			bosslevel = 3;
		}else{
			bossclass = gethclass("����4");
			bosslevel = 4;
		}
		Random r = new Random();
		int rand = r.nextInt(players.size());
		
		Player boss = players.get(rand);
		setboss(boss);
		return boss;
	}
	
	//��ȡheroesְҵ
	public HeroClass gethclass(String s){
		return plugin.getheroesplugin().getClassManager().getClass(s);
	}
	
	//���ž�����
	public boolean openArena()
	{
		if(this.bossspawn == null || this.playerspawn == null || this.ready == null || this.deathspawn == null)
		{
			return false;
		}
		
		arenastate = ArenaState.STAT_OPEN;
		
		return true;
	}
	
	//������״̬���
	public void clear(){
		playerslocation.clear();
		players.clear();
		livingplayers.clear();
		bossliving = false;
		boss = null;
		oldclass = null;
		bossclass = null;
	}
	
	//���������Ϊboss
	public void setboss(Player p){
		this.boss = p;
		oldclass = plugin.getHero(p).getHeroClass();
	}
	
	//��ȡBOSS
	public Player getboss(){
		return boss;
	}
	
	//�ж�һ������Ƿ�ΪBOSS
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
			endGame(EndReason.END_REASON_NOPLAYER);
	}
	
	public void bossDeath()
	{
		bossliving = false;
		endGame(EndReason.END_REASON_SUCCESS);
	}
	
	public void playerLeave(Player player,String reason)
	{
		if(players.contains(player))
		{
			plugin.sendtoplayer(player, reason + " �Ӿ��������˳�");
			removeplayer(player);
			if(player == boss)
			{
				Hero h = plugin.getHero(player);
				h.setHeroClass(oldclass, false);
				if(bossliving)
				{
					bossliving = false;
					endGame(EndReason.END_REASON_BOSSLEAVE);
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
		if(arenastate == ArenaState.STAT_OPEN)
		{
			if(players.contains(player))
			{
				plugin.sendtoplayer(player, "���Ѿ��ھ���������");
				return false;
			}
			plugin.sendtoplayer(player, ChatColor.GREEN+"������˾����� "+ arenaname +" !");
			
			Location pol = player.getLocation();
			Location pl = new Location(pol.getWorld(),pol.getX(),pol.getY(),pol.getZ(),pol.getYaw(),pol.getPitch());
			playerslocation.put(player, pl);
			player.teleport(ready);
			arenaBoardcast(player.getName() + " �����˾�����");
			
			addplayer(player);
		}
		else
		{
			String reason = ChatColor.RED+"���޷��������������,";
			if(this.arenastate==ArenaState.STAT_DISABLED)
			{
				reason+="�������ѱ���ֹ";
			}
			else if(this.arenastate==ArenaState.STAT_EDIT)
			{
				reason+=",���������ڱ༭��";
			}
			else if(this.arenastate==ArenaState.STAT_READY||this.arenastate==ArenaState.STAT_STARTED)
			{
				reason+="��Ϸ�Ѿ���ʼ";
			}
			
			plugin.sendtoplayer(player, reason);
			return false;
		}
		
		return false;
	}
	
	public void editArena()
	{
		if(this.arenastate==ArenaState.STAT_READY||this.arenastate==ArenaState.STAT_STARTED)
			this.endGame(EndReason.END_REASON_ARENAEDIT);
		else if(this.arenastate==ArenaState.STAT_OPEN)
		{
			for(Player p:players)
			{
				this.removeplayer(p);
				p.sendMessage(ChatColor.RED+"����������༭ģʽ!");
			}
		}
	}
	
	public void disableArena()
	{
		if(this.arenastate==ArenaState.STAT_READY||this.arenastate==ArenaState.STAT_STARTED)
			this.endGame(EndReason.END_REASON_ARENADISABLE);
		else if(this.arenastate==ArenaState.STAT_OPEN)
		{
			for(Player p:players)
			{
				this.removeplayer(p);
				p.sendMessage(ChatColor.RED+"�������ر�!");
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
