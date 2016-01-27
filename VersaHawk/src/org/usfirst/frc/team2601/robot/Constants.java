package org.usfirst.frc.team2601.robot;

public class Constants {
	
	private static Constants instance = null;
	
	//Singleton design pattern
	public static Constants getInstance(){
		if (instance == null){
			instance = new Constants();
		}
		return instance;
	}
	
	//Different control system configs
	public enum System_Type  {Alpha, Beta};
	public System_Type system = System_Type.Alpha;
	
	public enum Operation_Type {JoystickTank, JoystickArcade, GamepadTank}
	public Operation_Type driverType = Operation_Type.GamepadTank;
	
	//CAN addresses HERE
	public int frontLeftTalonAddress;
	public int middleLeftTalonAddress;
	public int rearLeftTalonAddress;
	
	public int frontRightTalonAddress;
	public int middleRightTalonAddress;
	public int rearRightTalonAddress;
	
	//DigitalInputs HERE
	public int leftEncoderPortI;
	public int rightEncoderPortI;
	public int leftEncoderPortII;
	public int rightEncoderPortII;
	
	//USB stuff
	public int primaryJoystickPort = 1;
	public int secondaryJoystickPort;
	
	//Controller Buttons
	public int buttonOneAddress = 6;
	public int buttonTwoAddress = 7;
	public int buttonThreeAddress = 8;
	
	//pneumatics
	public int leftSolenoidForwardChannel = 0;
	public int leftSolenoidReverseChannel = 1;
	public int rightSolenoidForwardChannel = 2;
	public int rightSolenoidReverseChannel = 3;
	
	//miscellaneous constants
	public final double distancePerPulse = 0.045;
	public final double defaultBangBangSpeed = 0.5;
	
	public final boolean logging = true;
	       
	public double kP = 0.0;
	public double kI = 0.0;
	public double kD = 0.1;
	public double kF = 0.0;
	public double PIDtolerance = 5.0;
	
	private Constants(){
		if (system == System_Type.Alpha){
			frontLeftTalonAddress = 1;
			middleLeftTalonAddress = 12;
			rearLeftTalonAddress = 14;
			
			frontRightTalonAddress = 4;
			middleRightTalonAddress = 2;
			rearRightTalonAddress = 3;
			
			leftEncoderPortI = 0;
			leftEncoderPortII = 1;
			rightEncoderPortI = 2;
			rightEncoderPortII = 3;
		}
		
		else if (system == System_Type.Beta){
			frontLeftTalonAddress = 7;
			middleLeftTalonAddress = 8;
			rearLeftTalonAddress = 9;
			
			frontRightTalonAddress = 10;
			middleRightTalonAddress = 11;
			rearRightTalonAddress = 12;
			
			leftEncoderPortI = 3;
			leftEncoderPortII = 5;
			rightEncoderPortI = 4;
			rightEncoderPortII = 6;
		}
		
		if (driverType == Operation_Type.JoystickTank){
			secondaryJoystickPort = 2;
		}
		
	}
	
}
