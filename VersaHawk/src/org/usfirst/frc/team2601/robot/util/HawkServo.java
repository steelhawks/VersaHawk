package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Servo;

public class HawkServo extends Servo implements HawkLoggable {
	
	String name;
	ArrayList<String> keyList = new ArrayList<String>(2);
	ArrayList<String> dataList = new ArrayList<String>(2);
	int channel;
	
	public HawkServo(int channel, String name) {
		super(channel);
		this.name = name;
		this.channel = channel;
}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<String> getKeys() {
		return keyList;
	}

	@Override
	public ArrayList<String> getData() {
		dataList.clear();
		setData();
		return dataList;
	}

	@Override
	public void setKeys() {
		keyList.add("channel");
		keyList.add("getAngle");
	}

	@Override
	public void setData() {
		dataList.add(Integer.toString(channel));
		dataList.add(Double.toString(getAngle()));
	}

}
