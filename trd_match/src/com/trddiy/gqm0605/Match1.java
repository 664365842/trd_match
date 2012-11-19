package com.trddiy.gqm0605;

import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass;

public class Match1 {
	public Trd_match plugin;
	public Arena arena;
	public HeroClass hc;
	public int level;
	public Match1(Trd_match plugin,Arena arena){
		this.plugin = plugin;
		this.arena = arena;
	}
	public void startmatch(){
		final List<Player> ps = arena.getplayers();
		if(ps != null){
			for(Player p : ps){
				p.teleport(arena.getspawn());
				p.setGameMode(GameMode.ADVENTURE);
				plugin.sendtoplayer(p,"15秒倒计时准备");
			}
				plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
					public void run(){
						plugin.sendtobcast("游戏开始!");
						Hero h = plugin.getHero(chooseplayer(ps));
						arena.setbossclass(h.getHeroClass());
						h.setHeroClass(hc, false);
						plugin.sendtobcast("玩家"+h.getPlayer().getDisplayName()+"被选为boss!");
						plugin.sendtobcast("boss等级: "+level);
					}
				},300L);
		}
	}
	public void endmatch(boolean bosssuccess){
		if(bosssuccess){
			Hero h = plugin.getHero(arena.getboss());
			h.setHeroClass(arena.getbossclass(), false);
			h.getPlayer().teleport(h.getPlayer().getWorld().getSpawnLocation());
			plugin.sendtobcast("所有玩家死亡,boss获胜!");
		}else{
			List<Player> ps = arena.getplayers();
			for(Player p :ps){
				p.teleport(p.getWorld().getSpawnLocation());
			}
			plugin.sendtobcast("boss死亡,玩家获胜!");
			arena.clear();
		}
	}
	public Player chooseplayer(List<Player> ps){
		int count = ps.size();
		if(count <=5){
			hc = gethclass("测试1");
			level = 1;
		}else if(count <=10){
			hc = gethclass("测试2");
			level =2;
		}else if (count <= 15){
			hc = gethclass("测试3");
			level = 3;
		}else{
			hc = gethclass("测试4");
			level = 4;
		}
		Random r = new Random();
		int a = r.nextInt(ps.size());
		return ps.get(a);
	}
	public HeroClass gethclass(String s){
		return plugin.getheroesplugin().getClassManager().getClass(s);
	}
}
