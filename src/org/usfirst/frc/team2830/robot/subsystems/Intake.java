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
    
    public void intakeIn(){
    	RobotMap.intakeLeft.set(1);
    	RobotMap.intakeRight.set(1);
    }
    public void intakeOut(){
    	RobotMap.intakeLeft.set(-1);
    	RobotMap.intakeRight.set(-1);
    	
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
    	if(operatorStick.getRawButtonPressed(1)){
    		intakeIn();
    	}else if(operatorStick.getRawButtonPressed(2)){
    		intakeOut();
    	}else{
    		stopIntake();
    	}
    }
}
