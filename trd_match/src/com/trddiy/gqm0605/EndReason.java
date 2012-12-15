package com.trddiy.gqm0605;

public enum EndReason {//结束原因
	 END_REASON_SUCCESS("BOSS死亡"),
	 END_REASON_NOPLAYER("玩家全部死亡") ,
	 END_REASON_BOSSLEAVE("BOSS异常离场"),
	 END_REASON_ARENADISABLE("竞技场被强制关闭"),
	 END_REASON_ARENAEDIT("竞技场进入编辑模式"),
	 END_REASON_UNKNOW ("未知原因");
	 private String reason;
	 //无参枚举
	 private EndReason(){
	 }
	 //有参枚举
	 private EndReason(String reason){
		 this.reason = reason;
	 }
	 //获得说明
	 public String getreasontext(){
		 return reason;
	 }
}
