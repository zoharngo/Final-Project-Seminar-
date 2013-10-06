package com.smartphoneWebcam.ui.flowActivities.adpter;

public class Transmitter {
	private int id;
	private String nickName;
	private String ip;

	public Transmitter() {
		super();
	}

	public Transmitter(String nickName, String ip) {
		super();
		this.nickName = nickName;
		this.ip = ip;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
