package com.trddiy.gqm0605;

import java.util.List;

import org.bukkit.configuration.Configuration;

public class ConfigManager {
	
	@SuppressWarnings("unused")
	private Trd_match plugin;
	
	private Configuration config; 
	
	public ConfigManager(Trd_match plugin)
	{
		this.plugin = plugin;
		
		config = plugin.getConfig();
		config.options().copyDefaults(true);// ���������ļ�
	}
	
	//��ȡ�����ھ�������ʹ�õ�����
	public List<String> getPermissionCommand()
	{
		return config.getStringList("ArenaOption.PermissionCommands");
	}
	
	//��ȡ������������ľ�����
	public int getMaxArena()
	{
		return config.getInt("CommonOption.MaxArena",5);
	}
	
	//��ȡBOSS��ְҵ��
	public String[] getBossClasses()
	{
		String []classes = {"","","",""};

		classes[0] = config.getString("BossOption.ClassLevel1");
		classes[1] = config.getString("BossOption.ClassLevel2");
		classes[2] = config.getString("BossOption.ClassLevel3");
		classes[3] = config.getString("BossOption.ClassLevel4");
		
		return classes;
	}

}
