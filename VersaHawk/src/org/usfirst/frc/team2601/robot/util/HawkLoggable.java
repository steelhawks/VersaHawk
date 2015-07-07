/**
 * 
 */
package org.usfirst.frc.team2601.robot.util;

import java.util.ArrayList;

/**
 * @author Marcus
 *
 */
public interface HawkLoggable {
	
	public void setName(String name);
	public String getName();
	
	public ArrayList<String> getKeys();
	public ArrayList<String> getData();
	public void setKeys();
	public void setData();
}
