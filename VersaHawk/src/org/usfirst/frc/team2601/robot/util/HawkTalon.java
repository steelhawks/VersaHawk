package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Talon;

public class HawkTalon extends Talon implements HawkLoggable{
	String name;
	int channel;
	ArrayList<String> keyList = new ArrayList<String>(2);
	ArrayList<String> dataList = new ArrayList<String>(2);
	
	public HawkTalon(int channel, String name) {
		super(channel);
		this.channel = channel;
		this.name = name;
		setKeys();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setData() {
		dataList.add(Integer.toString(channel));
		dataList.add(Double.toString(this.get()));
		
	}


	public void setKeys() {
		keyList.add("channel #");
		keyList.add("get() value");	
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

}
