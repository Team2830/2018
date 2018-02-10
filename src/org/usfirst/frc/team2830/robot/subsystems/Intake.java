package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.OperateIntake;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private boolean isCurrentLimitReached = false;
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new OperateIntake());
    }
    
    public void intakeIn(){
    	RobotMap.intakeLeft.set(.7);
    	RobotMap.intakeRight.set(.7);
    }
    public void intakeOut(){
    	RobotMap.intakeLeft.set(-.7);
    	RobotMap.intakeRight.set(-.7);
    	
    }
    public void intakeInSlow(){
    	RobotMap.intakeLeft.set(.4);
    	RobotMap.intakeRight.set(.4);
    }
    /**
     * 
     */
    public void stopIntake(){
    	RobotMap.intakeLeft.stopMotor();
    	RobotMap.intakeRight.stopMotor();
    }
    /**
     * Checks if certain buttons are pressed and calls methods accordingly.
     * @param operatorStick The joystick on which the checks will be enacted.
     */
    public void operateIntake(Joystick operatorStick){
    	intakeCurrentOverride(Robot.oi.getOperatorJoystick());
    	if(operatorStick.getRawButton(1)){
    		if(!currentLimitReached())
    			intakeIn();
    		else{
    			intakeInSlow();
    		}
    	}else if(operatorStick.getRawButton(2)){
    		isCurrentLimitReached = false;
    		intakeOut();
    	}else{
    		stopIntake();
    	}
    }
    
    public boolean currentLimitReached(){
    	if((RobotMap.pdp.getCurrent(RobotMap.intakeChannel) > 35) || isCurrentLimitReached){
    		isCurrentLimitReached = true;
    	}
    	return isCurrentLimitReached;
    }
    
    public boolean intakeCurrentOverride(Joystick operatorStick){
    	if(operatorStick.getRawButton(1) && operatorStick.getRawButton(3)){
    		isCurrentLimitReached = false;
    	}
    	return isCurrentLimitReached;
    }
}

