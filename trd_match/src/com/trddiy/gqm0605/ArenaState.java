package com.trddiy.gqm0605;

public enum ArenaState {
	 STAT_EDIT("�༭��"),
	// STAT_CONFIG
	 STAT_OPEN("������"),
	 STAT_READY("׼����Ϸ��"),
	 STAT_STARTED("������Ϸ��"),
	 STAT_DISABLED("��ֹ����");
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
