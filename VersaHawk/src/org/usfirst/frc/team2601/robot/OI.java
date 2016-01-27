package org.usfirst.frc.team2601.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team2601.robot.commands.Drive;
import org.usfirst.frc.team2601.robot.commands.PIDdrive;
import org.usfirst.frc.team2601.robot.commands.PIDstop;
import org.usfirst.frc.team2601.robot.commands.Shift;

import edu.wpi.first.wpilibj.Joystick;

import org.usfirst.frc.team2601.robot.util.F310;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick driverStick;
	public static Joystick leftStick;
	public static Joystick rightStick;
	public static F310 gamepad;
	Constants constants = Constants.getInstance();
	public static Button shift;
	public static Button PIDstart;
	public static Button PIDstop;
	
	public OI(){
		Constants.Operation_Type drivetype = constants.driverType;
		switch (drivetype){
		case JoystickArcade:
			driverStick = new Joystick(constants.primaryJoystickPort);
			shift = new JoystickButton(driverStick, constants.buttonOneAddress);
			break;
		case JoystickTank: 
			leftStick = new Joystick(constants.primaryJoystickPort);
			rightStick = new Joystick(constants.secondaryJoystickPort);
			shift = new JoystickButton(rightStick, constants.buttonOneAddress);
			break;
		case GamepadTank: 
			gamepad = new F310(constants.primaryJoystickPort);
			shift = new JoystickButton(gamepad, F310.kGamepadButtonShoulderR);
			PIDstart = new JoystickButton(gamepad, F310.kGamepadButtonShoulderL);
			PIDstop = new JoystickButton(gamepad, F310.kGamepadButtonA);
			break;
		}
		
		shift.whenPressed(new Shift());
		PIDstart.whenPressed(new PIDdrive(-500,500));
		PIDstop.whenPressed(new PIDstop());
	}
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

