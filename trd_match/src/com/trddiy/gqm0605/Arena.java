package com.trddiy.gqm0605;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.characters.classes.HeroClass;

public class Arena {
	private Trd_match plugin;
	private List<Player> players;
	private Player boss;
	private Location spawn;
	private HeroClass hc;
	public Arena(Trd_match plugin,Location l1){
		this.spawn = l1;
		this.plugin = plugin;
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
	public void clear(){
		players = null;
		boss = null;
		hc = null;
	}
	public void setboss(Player p){
		this.boss = p;
		hc = plugin.getHero(p).getHeroClass();
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
	public HeroClass getbossclass(){
		return hc;
	}
	public void setbossclass(HeroClass hc){
		this.hc = hc;
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
	public Location getspawn(){
		return spawn;
	}
}
