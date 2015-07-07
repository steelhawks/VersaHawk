package org.usfirst.frc.team2601.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team2601.robot.commands.Drive;
import org.usfirst.frc.team2601.robot.Constants;
import org.usfirst.frc.team2601.robot.util.*;

/**
 *
 */
public class Drivetrain extends Subsystem {
    //get a single instance of the constants, refer ONLY TO THIS for constant vars
	Constants constants = Constants.getInstance();
	
	//Declare HawkCANTalons
	HawkCANTalon frontLeftCANTalon = new HawkCANTalon(constants.frontLeftTalonAddress, "frontLeftCANTalon");
	HawkCANTalon middleLeftCANTalon = new HawkCANTalon(constants.middleLeftTalonAddress, "middleLeftCANTalon");
	HawkCANTalon rearLeftCANTalon = new HawkCANTalon(constants.rearLeftTalonAddress, "rearLeftCANTalon");
	HawkCANTalon frontRightCANTalon = new HawkCANTalon(constants.frontRightTalonAddress, "frontRightCANTalon");
	HawkCANTalon middleRightCANTalon = new HawkCANTalon(constants.middleRightTalonAddress, "middleRightCANTalon");
	HawkCANTalon rearRightCANTalon = new HawkCANTalon(constants.rearRightTalonAddress, "rearRightCANTalon");

	//Declare pneumatics
	HawkCompressor compressor = new HawkCompressor();
	HawkDoubleSolenoid leftShift = new HawkDoubleSolenoid(constants.leftSolenoidForwardChannel,constants.leftSolenoidReverseChannel, "leftShiftSolenoid");
	HawkDoubleSolenoid rightShift = new HawkDoubleSolenoid(constants.rightSolenoidForwardChannel,constants.rightSolenoidReverseChannel, "rightShiftSolenoid");
	
	//Declare Encoders
	HawkEncoder leftEncoder = new HawkEncoder(constants.leftEncoderPortI,constants.leftEncoderPortII,false, EncodingType.k4X, "leftEncoder");
	HawkEncoder rightEncoder = new HawkEncoder(constants.rightEncoderPortI,constants.rightEncoderPortII,true,EncodingType.k4X, "rightEncoder");
	
	//this is used for the logger
	private ArrayList<HawkLoggable> loggingList = new ArrayList<HawkLoggable>();
	public HawkLogger logger;
	
	//standard RobotDrive from wpilib
	RobotDrive drive = new RobotDrive(frontLeftCANTalon, middleLeftCANTalon, frontRightCANTalon, middleRightCANTalon);
	
	public Drivetrain(){
		//add all loggables to a list, then assign the logger
		loggingList.add(frontLeftCANTalon);
		loggingList.add(middleLeftCANTalon);
		loggingList.add(rearLeftCANTalon);
		loggingList.add(frontRightCANTalon);
		loggingList.add(middleRightCANTalon);
		loggingList.add(rearRightCANTalon);
		loggingList.add(compressor);
		loggingList.add(leftShift);
		loggingList.add(rightShift);
		loggingList.add(leftEncoder);
		loggingList.add(rightEncoder);
		
		logger = new HawkLogger("drivetrain", loggingList);
		logger.setup();
		
		leftEncoder.setDistancePerPulse(constants.distancePerPulse);
		rightEncoder.setDistancePerPulse(constants.distancePerPulse);
		leftShift.set(DoubleSolenoid.Value.kForward);
		matchSolenoids();
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
    }
    
    private void driveMotorsMatched(CANTalon leader, CANTalon follower){
    	follower.set(leader.get());
    }
    
    //arcade drive method for 6-CIM drivetrain with logging
    public void arcadeDrive(double move, double rotate){
    	drive.arcadeDrive(move, rotate);
    	driveMotorsMatched(middleLeftCANTalon, rearLeftCANTalon);
    	driveMotorsMatched(middleRightCANTalon, rearRightCANTalon);
    	logger.log();
    }
    
    //use X-axis for twist
    public void arcadeDriveX(Joystick stick){
    	double move = stick.getY();
    	double rotate = stick.getX();
    	arcadeDrive(move, rotate);
    }
    
    //use joystick twist for twist
    public void arcadeDriveTwist(Joystick stick){
    	double move = stick.getY();
    	double rotate = stick.getTwist();
    	arcadeDrive(move, rotate);
    }
    
    //tank drive method for 6-CIM drivetrain with logging
    public void tankDrive(double leftSide, double rightSide){
    	drive.tankDrive(leftSide, rightSide);
    	driveMotorsMatched(middleLeftCANTalon, rearLeftCANTalon);
    	driveMotorsMatched(middleRightCANTalon, rearRightCANTalon);
    	logger.log();
    }
    
    //use joysticks for tankdrive
    public void joystickTankDrive(Joystick leftStick, Joystick rightStick){
    	double left = leftStick.getY();
    	double right = rightStick.getY();
    	tankDrive(left, right);
    }
    
    //use gamepad for tankdrive
    public void gamepadTankDrive(F310 gamepad){
    	double left = gamepad.getLeftY();
    	double right = gamepad.getRightY();
    	tankDrive(left, right);
    }
    
    //toggle solenoids
    public void switchSolenoids(){
    	if (leftShift.get() == DoubleSolenoid.Value.kForward){
    		leftShift.set(DoubleSolenoid.Value.kReverse);
    	}
    	else {
    		leftShift.set(DoubleSolenoid.Value.kForward);
    	}
    	matchSolenoids();
    	logger.log();
    }
    
    //keep solenoids unified
    private void matchSolenoids(){
    	rightShift.set(leftShift.get());
    }
    
    //BangBang autonomous
    
    //this variable locks in the BangBang method, allows it to set final distance once per successful run.
    private boolean bangBangStarted = false;

    public boolean bangBang(double distance, double threshold, double speed){
    	double finalLocation;
    	double currentLeftDistance = leftEncoder.getDistance();
    	double currentRightDistance = rightEncoder.getDistance();
    	double left = 0, right = 0;
    	boolean leftTruth, rightTruth;
    	
    	//TODO switch finalLocation to 2 different variables for left and right
    	if(!bangBangStarted){
    		finalLocation = distance + ((currentLeftDistance + currentRightDistance)/2);
    	}
    	
    	if (currentLeftDistance > distance - threshold && currentLeftDistance < distance + threshold){
    		leftTruth = true;
    	}
    	else{
    		left = speed;
    		leftTruth = false;
    	}
    	
    	if (currentRightDistance > distance - threshold && currentRightDistance < distance + threshold){
    		rightTruth = true;
    	}
    	else{
    		right = speed;
    		rightTruth = false;
    	}
    	
    	if (leftTruth && rightTruth){
    		bangBangStarted = false;
    		return true;
    	}
    	tankDrive(left, right);
    	bangBangStarted = true;
    	return false;
  
    }
    //double variable bangbang
    public boolean bangBang(double distance, double threshold){
    	return bangBang(distance, threshold, constants.defaultBangBangSpeed);
    }
    
    //End bangBang autonomous
    
}

