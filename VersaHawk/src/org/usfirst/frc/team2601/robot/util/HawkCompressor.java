package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Compressor;

public class HawkCompressor extends Compressor implements HawkLoggable {
	ArrayList<String> keyList = new ArrayList<String>(3);
	ArrayList<String> dataList = new ArrayList<String>(3);
	String name = "compressor";
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
		keyList.add("PressureSwitch Value");
		keyList.add("enabled Value");
		keyList.add("CompressorCurrent");
	}

	@Override
	public void setData() {
		dataList.add(Boolean.toString(getPressureSwitchValue()));
		dataList.add(Boolean.toString(enabled()));
		dataList.add(Double.toString(getCompressorCurrent()));
	}

}
