package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Ultrasonic;

public class HawkUltrasonic extends Ultrasonic implements HawkLoggable {
	
	int pingChannel, echoChannel;
	ArrayList<String> keyList = new ArrayList<String>(4);
	ArrayList<String> dataList = new ArrayList<String>(4);
	String name;
	
	
	public HawkUltrasonic(int pingChannel, int echoChannel, String name) {
		super(pingChannel, echoChannel);
		this.pingChannel = pingChannel;
		this.echoChannel = echoChannel;
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
		// TODO Auto-generated method stub
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
		keyList.add("pingChannel");
		keyList.add("echoChannel");
		keyList.add("range (inches)");
		keyList.add("range (millimeters)");
	}

	@Override
	public void setData() {
		dataList.add(Integer.toString(pingChannel));
		dataList.add(Integer.toString(echoChannel));
		dataList.add(Double.toString(getRangeInches()));
		dataList.add(Double.toString(getRangeMM()));
	}

}
