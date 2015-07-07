package org.usfirst.frc.team2601.robot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class HawkLogger {
	
	private ArrayList<HawkLoggable>componentList = new ArrayList<HawkLoggable>(20);
	public String name;
	private ArrayList<String> keyList = new ArrayList<String>(40);
	private ArrayList<String> dataList = new ArrayList<String>(40);
	private HawkWriter writer;
	
	//Constructor, name HawkLogger object, provide the list of components to be logged
	public HawkLogger(String name, ArrayList<HawkLoggable> loggableList){
		this.componentList = loggableList;
		this.name = name;
		try {
			writer = new HawkWriter(this.name);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	//run only once, takes the keys from a single component
	private void stripKeys(HawkLoggable component){
		ArrayList<String> bareKeys = new ArrayList<String>(component.getKeys().size());
		Iterator<String> i = bareKeys.iterator();
		while (i.hasNext()){
			keyList.add(component.getName()+" "+i.next());
		}
	}
	//run each time data is taken, takes data from a single component
	private void stripData(HawkLoggable component){
		dataList.addAll(component.getData());
	}
	
	//run once, sets headers for the logger file
	public void setup(){
		Iterator<HawkLoggable> i = componentList.iterator();
		while(i.hasNext()){
			stripKeys(i.next());
		}
		writer.writeLine(keyList);
	}
	
	//implements the stipData method on all components
	private void getData(){
		Iterator<HawkLoggable> i = componentList.iterator();
		while(i.hasNext()){
			stripData(i.next());
		}
	}
	
	//this is where the magic happens (ish)
	//call this method to actually log data
	public void log(){
		getData();
		writer.writeLine(dataList);
		dataList.clear();
	}
	
	//last thing you should call before shutting down
	public void stopLog(){
		try{
			writer.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
