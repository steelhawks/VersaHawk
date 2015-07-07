package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Encoder;

public class HawkEncoder extends Encoder implements HawkLoggable {
	String name;
	ArrayList<String> keyList = new ArrayList<String>(3);
	ArrayList<String> dataList = new ArrayList<String>(3);
	int aChannel, bChannel;
	boolean reverseDirection;
	EncodingType encodingType;
	
	public HawkEncoder(int aChannel, int bChannel, boolean reverseDirection,
			EncodingType encodingType, String name) {
		super(aChannel, bChannel, reverseDirection, encodingType);
		this.aChannel = aChannel;
		this.bChannel = bChannel;
		this.reverseDirection = reverseDirection;
		this.encodingType = encodingType;
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
		keyList.add("get");
		keyList.add("getDistance");
		keyList.add("getRate");
	}

	@Override
	public void setData() {
		dataList.add(Double.toString(get()));
		dataList.add(Double.toString(getDistance()));
		dataList.add(Double.toString(getRate()));
	}

}
