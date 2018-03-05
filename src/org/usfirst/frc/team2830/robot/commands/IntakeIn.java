package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeIn extends Command {
	int checkLoop = 0;
    public IntakeIn() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.intake.intakeOut();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(++checkLoop>50){
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