package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.OperateIntake;
import edu.wpi.first.wpilibj.Joystick;
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
    	RobotMap.intakeLeft.set(.8);
    	RobotMap.intakeRight.set(.8);
    }
    public void intakeOut(){
    	RobotMap.intakeLeft.set(-.5);
    	RobotMap.intakeRight.set(-.5); 
    	
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
    	if(operatorStick.getRawButton(1)){
    		intakeIn();
    	}else if(operatorStick.getRawButton(2)){
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

