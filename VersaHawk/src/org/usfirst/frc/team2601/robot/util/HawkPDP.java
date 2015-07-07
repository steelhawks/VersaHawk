package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class HawkPDP extends PowerDistributionPanel implements HawkLoggable {
	String name = "PDP";
	ArrayList<String> keyList = new ArrayList<String>(5);
	ArrayList<String> dataList = new ArrayList<String>(5);
	
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
		keyList.add("getVoltage");
		keyList.add("getTemperature");
		keyList.add("getTotalCurrent");
		keyList.add("getTotalPower");
		keyList.add("getTotalEnergy");
	}

	@Override
	public void setData() {
		dataList.add(Double.toString(getVoltage()));
		dataList.add(Double.toString(getTemperature()));
		dataList.add(Double.toString(getTotalCurrent()));
		dataList.add(Double.toString(getTotalPower()));
		dataList.add(Double.toString(getTotalEnergy()));
	}

}
