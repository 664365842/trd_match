package com.trddiy.gqm0605;

public enum EndReason {//����ԭ��
	 END_REASON_SUCCESS("BOSS����"),
	 END_REASON_NOPLAYER("���ȫ������") ,
	 END_REASON_BOSSLEAVE("BOSS�쳣�볡"),
	 END_REASON_ARENADISABLE("��������ǿ�ƹر�"),
	 END_REASON_ARENAEDIT("����������༭ģʽ"),
	 END_REASON_UNKNOW ("δ֪ԭ��");
	 private String reason;
	 //�޲�ö��
	 private EndReason(){
	 }
	 //�в�ö��
	 private EndReason(String reason){
		 this.reason = reason;
	 }
	 //���˵��
	 public String getreasontext(){
		 return reason;
	 }
}
