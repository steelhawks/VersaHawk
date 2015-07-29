package org.usfirst.frc.team2601.robot.util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class HawkWriter extends FileWriter{
	public String filename;
	private static String delim = ",";
	private static String lineEnder = "\n";
	
	private static String filenamePrep(String origFilename){
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String preppedFilename = "/logs/"+origFilename+"_"+timeStamp+".csv";
		return preppedFilename;
	}
	public HawkWriter(String filename) throws IOException{
		super(filenamePrep(filename), true);
		this.filename = filenamePrep(filename);
	}
	
	public void writeLine(ArrayList<String> line){
		Iterator<String> i = line.iterator();
		try{
			while(i.hasNext()){
				write(i.next());
				write(HawkWriter.delim);
			}
			write(HawkWriter.lineEnder);
			//flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
