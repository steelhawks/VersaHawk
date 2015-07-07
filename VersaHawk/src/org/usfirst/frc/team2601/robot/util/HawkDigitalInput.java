package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DigitalInput;

public class HawkDigitalInput extends DigitalInput implements HawkLoggable {
	int channel;
	String name;
	ArrayList<String> keyList = new ArrayList<String>(2);
	ArrayList<String> dataList = new ArrayList<String>(2);
	
	public HawkDigitalInput(int channel, String name) {
		super(channel);
		this.name = name;
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
		keyList.add("channel #");
		keyList.add("get Value");
	}

	@Override
	public void setData() {
		dataList.add(Integer.toString(channel));
		dataList.add(Boolean.toString(get()));
	}

}
