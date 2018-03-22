package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeInRight extends Command {
	int checkLoop;
    public IntakeInRight() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	checkLoop = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(checkLoop>70 && checkLoop<75){
    		Robot.intake.stopIntake();
    	}else{
    		Robot.intake.intakeInRight();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(++checkLoop>100){
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.stopIntake();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
