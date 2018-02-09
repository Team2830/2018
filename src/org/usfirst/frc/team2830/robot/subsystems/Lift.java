package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.OperateLift;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Lift extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public double joystickDeadband = .02;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new OperateLift());
    	
    }
    /**
     * Sets the speed of the motor controllers.
     * @param speed The speed from -1 to 1.
     */
    public void set(double speed){
    	RobotMap.liftBack.set(speed);
    	RobotMap.liftFront.set(speed);
    }
    /**
     * Allows the operator to manually move the lift.
     * @param operatorStick The operator joystick.
     */
    public void operateLift(Joystick operatorStick){
    	double speed = deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
    	set(speed);
    }
    
    public double deadbanded(double input, double deadband){
    	if (Math.abs(input) > Math.abs(deadband)){
    		return input;
    	}
    	else {
    		return 0;
    	}
    }
}

