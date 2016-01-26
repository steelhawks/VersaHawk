package org.usfirst.frc.team2601.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	HawkEncoder leftEncoder = new HawkEncoder(constants.leftEncoderPortI,constants.leftEncoderPortII,true, EncodingType.k4X, "leftEncoder");
	HawkEncoder rightEncoder = new HawkEncoder(constants.rightEncoderPortI,constants.rightEncoderPortII,false,EncodingType.k4X, "rightEncoder");
	
	//this is used for the logger
	private ArrayList<HawkLoggable> loggingList = new ArrayList<HawkLoggable>();
	public HawkLogger logger;
	


	//standard RobotDrive from wpilib
	RobotDrive drive = new RobotDrive(frontLeftCANTalon, middleLeftCANTalon, frontRightCANTalon, middleRightCANTalon);
	
	//PID stuff
	public PIDController rightSide;
	public PIDController leftSide;
	
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
		
		//ready logger
		logger = new HawkLogger("drivetrain", loggingList);
		logger.setup();
		
		//ready encoders
		leftEncoder.setDistancePerPulse(constants.distancePerPulse);
		rightEncoder.setDistancePerPulse(constants.distancePerPulse);
		
		//ready shifting gearboxes
		leftShift.set(DoubleSolenoid.Value.kForward);
		matchSolenoids();
		
		//ready PIDControllers
		leftSide = new PIDController(constants.kP, constants.kI, constants.kD, constants.kF, leftEncoder, frontLeftCANTalon);
		rightSide = new PIDController(constants.kP,constants.kI, constants.kD, constants.kF, rightEncoder,frontRightCANTalon );
		leftSide.setPercentTolerance(constants.PIDtolerance);
		rightSide.setPercentTolerance(constants.PIDtolerance);
		
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
    }
    
    //functions to match motors together
    private void matchMotors(CANTalon leader, CANTalon follower){
    	follower.set(leader.get());
    }
    private void matchMotors(CANTalon leader, CANTalon followerI, CANTalon followerII){
    	followerI.set(leader.get());
    	followerII.set(leader.get());
    }
    
    
    //arcade drive method for 6-CIM drivetrain with logging
    public void arcadeDrive(double move, double rotate){
    	drive.arcadeDrive(move, rotate);
    	matchMotors(middleLeftCANTalon, rearLeftCANTalon);
    	matchMotors(middleRightCANTalon, rearRightCANTalon);
    	SmartDashboard.putNumber("leftEncoder", leftEncoder.getDistance());
    	SmartDashboard.putNumber("rightEncoder", rightEncoder.getDistance());
    	logger.log(constants.logging);
    }
    
    //use X-axis for twist
    public void arcadeDriveX(Joystick stick){
    	double move = stick.getY();
    	double rotate = stick.getX();
    	arcadeDrive(move, rotate);
    	System.out.println("arcadeDriveX");
    }
    ;
    //use joystick twist for twist
    public void arcadeDriveTwist(Joystick stick){
    	double move = stick.getY();
    	double rotate = stick.getTwist();
    	arcadeDrive(move, rotate);
    	System.out.println("arcadeDriveTwist");
    }
    
    //tank drive method for 6-CIM drivetrain with logging
    public void tankDrive(double left, double right){
    	if (PIDinitialized && PIDenabled){
	    	//check if we're on target
	    	if(Math.abs(((leftSide.getError()/leftSide.getSetpoint())*100))<=constants.PIDtolerance){
	    		leftSide.disable();
	    	}
	    	if (Math.abs(((rightSide.getError()/rightSide.getSetpoint())*100))<=constants.PIDtolerance){
	    		rightSide.disable();
	    	}
	    	if (Math.abs(((leftSide.getError()/leftSide.getSetpoint())*100))<=constants.PIDtolerance && Math.abs(((rightSide.getError()/rightSide.getSetpoint())*100))<=constants.PIDtolerance){
	    		leftSide.disable();
	    		rightSide.disable();
	    		PIDinitialized = false;
	    		PIDenabled = false;
	    	
	    		}
	    	}
    	drive.tankDrive(left, right);
    	SmartDashboard.putNumber("leftEncoder", leftEncoder.getDistance());
    	SmartDashboard.putNumber("rightEncoder", rightEncoder.getDistance());
    	SmartDashboard.putNumber("frontRight", frontRightCANTalon.get());
    	SmartDashboard.putNumber("middleRight", middleRightCANTalon.get());
    	SmartDashboard.putNumber("rearRight", rearRightCANTalon.get());
    	SmartDashboard.putNumber("frontLeft", frontLeftCANTalon.get());
    	SmartDashboard.putNumber("middleLeft", middleLeftCANTalon.get());
    	SmartDashboard.putNumber("rearLeft", rearLeftCANTalon.get());
    	matchMotors(frontLeftCANTalon, middleLeftCANTalon, rearLeftCANTalon);
    	matchMotors(frontRightCANTalon, middleRightCANTalon, rearRightCANTalon);
    	System.out.println("tankDrive");
    	logger.log(constants.logging);
    }
    
    //use joysticks for tankdrive
    public void joystickTankDrive(Joystick leftStick, Joystick rightStick){
    	double left = leftStick.getY();
    	double right = rightStick.getY();
    	tankDrive(left, right);
    	System.out.println("joysticktankDrive");
    }
    
    //use gamepad for tankdrive
    public void gamepadTankDrive(F310 gamepad){
    	double left = gamepad.getLeftY();
    	double right = gamepad.getRightY();
    	tankDrive(left, right);
    	System.out.println("gamepadtankDrive");
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
    	logger.log(constants.logging);
    	System.out.println("switchSolenoids");
    }
    
    //keep solenoids unified
    private void matchSolenoids(){
    	rightShift.set(leftShift.get());
    }
    
    //BangBang autonomous
    
    //this variable locks in the BangBang method, allows it to set final distance once per successful run.
    private boolean bangBangStarted = false;
    private double finalLeftLocation = 0;//10 I changed these to 10 but reset them to 0
    private double finalRightLocation = 0;//10 
    
    public boolean bangBang(double distance, double threshold, double speed){
    	double currentLeftDistance = leftEncoder.getDistance();
    	double currentRightDistance = rightEncoder.getDistance();
    	double left = 0, right = 0;
    	boolean leftTruth, rightTruth;
    	
    	if(!bangBangStarted){
    		finalRightLocation = distance + currentRightDistance;
    		finalLeftLocation = distance + currentLeftDistance;
    	}
    	
    	if (currentLeftDistance > finalLeftLocation - threshold && currentLeftDistance < finalLeftLocation + threshold){
    		leftTruth = true;
    	}
    	else{
    		left = speed;
    		leftTruth = false;
    	}
    	
    	if (currentRightDistance > finalRightLocation - threshold && currentRightDistance < finalRightLocation + threshold){
    		rightTruth = true;
    	}
    	else{
    		right = speed;
    		rightTruth = false;
    	}
    	
    	if (leftTruth && rightTruth){
    		bangBangStarted = false;
    		finalLeftLocation = 0;
    		finalRightLocation = 0;
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
    
    private boolean PIDinitialized = false;
    private boolean PIDenabled = false;
    
    public void setBothPID(double setpoint){
    	leftSide.setSetpoint(leftEncoder.getDistance()+setpoint);
    	rightSide.setSetpoint(rightEncoder.getDistance()+setpoint);
    	PIDinitialized = true;
    }
    
    public void setIndividualPID(double leftSet, double rightSet){
    	leftSide.setSetpoint(leftEncoder.getDistance()+leftSet);
    	rightSide.setSetpoint(rightEncoder.getDistance()+rightSet);
    	PIDinitialized = true;
    }
    
    public boolean runPID(double left, double right){
    	//make sure we have setpoints
    	SmartDashboard.putBoolean("pidInit", PIDinitialized);
    	if (PIDinitialized){
	    	//check if we're on target
	    	if(leftSide.onTarget()){
	    		leftSide.disable();
	    	}
	    	if (rightSide.onTarget()){
	    		rightSide.disable();
	    	}
	    	if (leftSide.onTarget() && rightSide.onTarget()){
	    		leftSide.disable();
	    		rightSide.disable();
	    		PIDinitialized = false;
	    		return true;
	    	}
	    	//start moving
    		if(!PIDenabled){
    			leftSide.enable();
    			rightSide.enable();
    			PIDenabled = true;
    		}
	    	
    		matchMotors(frontRightCANTalon, middleRightCANTalon, rearRightCANTalon);
	    	matchMotors(frontLeftCANTalon, middleLeftCANTalon, rearLeftCANTalon);
	    	SmartDashboard.putNumber("frontRight", frontRightCANTalon.getSpeed());
	    	SmartDashboard.putNumber("middleRight", middleRightCANTalon.getSpeed());
	    	SmartDashboard.putNumber("rearRight", rearRightCANTalon.getSpeed());
	    	SmartDashboard.putNumber("frontLeft", frontLeftCANTalon.getSpeed());
	    	SmartDashboard.putNumber("middleLeft", middleLeftCANTalon.getSpeed());
	    	SmartDashboard.putNumber("rearLeft", rearLeftCANTalon.getSpeed());
	    	
	    	System.out.println("PID Drive shit");
	    	logger.log(constants.logging);
	    	return false;
    	}
    	//set setpoints if we don't already have them
    	else {
    		setIndividualPID(left, right);
    		return false;
    	}
    }
    
    public boolean runPID(double setpoint){
    	//make sure we have setpoints
    	System.out.println("running PID");
    	if (PIDinitialized){
	    	//check if we're on target
	    	if(leftSide.onTarget()){
	    		leftSide.disable();
	    	}
	    	if (rightSide.onTarget()){
	    		rightSide.disable();
	    	}
	    	if (leftSide.onTarget() && rightSide.onTarget()){
	    		leftSide.disable();
	    		rightSide.disable();
	    		PIDinitialized = false;
	    		return true;
	    	}
	    	//start moving
    		if(!PIDenabled){
    			leftSide.enable();
    			rightSide.enable();
    			PIDenabled = true;
    		}
	    	
    		matchMotors(frontRightCANTalon, middleRightCANTalon, rearRightCANTalon);
	    	matchMotors(frontLeftCANTalon, middleLeftCANTalon, rearLeftCANTalon);
	    	System.out.println("PID Drive shit");
	    	logger.log(constants.logging);
	    	return false;
    	}
    	//set setpoints if we don't already have them
    	else {
    		setBothPID(setpoint);
    		return false;
    	}
    }
    
    public boolean stopPID(){
    	PIDinitialized = false;
    	leftSide.disable();
    	rightSide.disable();
    	PIDenabled = false;
    	return true;
    }
    
}

