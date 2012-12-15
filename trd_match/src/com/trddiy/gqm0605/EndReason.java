package com.trddiy.gqm0605;

public enum EndReason {
	 END_REASON_SUCCESS, //游戏结束原因 玩家胜利
	 END_REASON_NOPLAYER , //游戏结束原因 玩家全部死亡
	 END_REASON_BOSSLEAVE, //游戏结束原因 BOSS异常离场
	 END_REASON_ARENADISABLE, //游戏结束原因 竞技场被强制关闭
	 END_REASON_ARENAEDIT, //游戏结束原因 竞技场进入编辑模式被强制关闭
	 END_REASON_UNKNOW , //游戏结束原因 未知
}
