package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HawkDoubleSolenoid extends DoubleSolenoid implements HawkLoggable {
	
	String name;
	int forwardChannel, reverseChannel;
	ArrayList<String> keyList = new ArrayList<String>(3);
	ArrayList<String> dataList = new ArrayList<String>(3);
	
	public HawkDoubleSolenoid(int forwardChannel, int reverseChannel, String name) {
		super(forwardChannel, reverseChannel);
		this.name = name;
		this.forwardChannel = forwardChannel;
		this.reverseChannel = reverseChannel;
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
		keyList.add("get ()");
		keyList.add("ForwardChannel");
		keyList.add("ReverseChannel");
	}

	@Override
	public void setData() {
		dataList.add(get().toString());
		dataList.add(Integer.toString(forwardChannel));
		dataList.add(Integer.toString(reverseChannel));
	}

}
