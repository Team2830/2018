package org.usfirst.frc.team2830.robot.subsystems;

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
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new OperateIntake());
    }
    
    public void intakeInLeft(){
    	RobotMap.intakeLeft.set(.9);
    	RobotMap.intakeRight.set(.5);
    }
    public void intakeInRight(){
    	RobotMap.intakeLeft.set(.5);
    	RobotMap.intakeRight.set(.9);
    }
    public void intakeOut(){ 
    	RobotMap.intakeLeft.set(-.5);
    	RobotMap.intakeRight.set(-.5);
    }	
    public void stopIntake(){
    	RobotMap.intakeLeft.stopMotor();
    	RobotMap.intakeRight.stopMotor();
    }
    public void intakeOutSlow(){
    	RobotMap.intakeLeft.set(-.4);
    	RobotMap.intakeRight.set(-.4);
    }
    /**
     * Checks if certain buttons are pressed and calls methods accordingly.
     * @param operatorStick The joystick on which the checks will be enacted.
     */
    public void operateIntake(Joystick operatorStick, Joystick driverStick){
    	if(operatorStick.getRawButton(4) || driverStick.getRawButton(4)){
    		intakeOutSlow();
    	}else if(operatorStick.getRawButton(2) || driverStick.getRawButton(2)){
    		intakeOut();
    	}else if(operatorStick.getRawButton(1)){
    		intakeInLeft();
    	}else if(operatorStick.getRawButton(3)){
    		intakeInRight();
    	}else{
    		stopIntake();
    	}
    }
}

