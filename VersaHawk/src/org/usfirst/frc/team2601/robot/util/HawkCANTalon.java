package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon;

public class HawkCANTalon extends CANTalon implements HawkLoggable {
	public HawkCANTalon(int deviceNumber, String name) {
		super(deviceNumber);
		this.name = name;
		this.canNumber = deviceNumber;
		}
	
	int canNumber;
	String name;
	ArrayList<String> keyList = new ArrayList<String>(6);
	ArrayList<String> dataList = new ArrayList<String>(6);
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setKeys() {
		keyList.add("DeviceID");
		keyList.add("get() value");	
		keyList.add("getTemp");
		keyList.add("getOutputCurrent");
		keyList.add("getOutputVoltage");
		keyList.add("getBusVoltage");
	}
	
	public void setData() {
		dataList.add(Integer.toString(canNumber));
		dataList.add(Double.toString(get()));	
		dataList.add(Double.toString(getTemp()));
		dataList.add(Double.toString(getOutputCurrent()));
		dataList.add(Double.toString(getOutputVoltage()));
		dataList.add(Double.toString(getBusVoltage()));
	}
	
	@Override
	public ArrayList<String> getKeys() {
		setKeys();
		return keyList;
	}

	@Override
	public ArrayList<String> getData() {
		dataList.clear();
		setData();
		System.out.println(dataList);
		return dataList;
	}

}
