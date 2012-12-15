package com.trddiy.gqm0605;

public enum ArenaState {
	 STAT_EDIT("编辑中"),
	// STAT_CONFIG
	 STAT_OPEN("开放中"),
	 STAT_READY("准备游戏中"),
	 STAT_STARTED("进行游戏中"),
	 STAT_DISABLED("禁止加入");
	 private String state;
	 private ArenaState(){
	 }
	 private ArenaState(String state){
		 this.state = state;
	 }
	 public String getstatetext(){
		 return state;
	 }
}
