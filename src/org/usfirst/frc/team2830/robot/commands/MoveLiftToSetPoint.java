package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveLiftToSetPoint extends Command {
	private double liftGoalHeight;
	
    public MoveLiftToSetPoint(double goalHeight) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	liftGoalHeight = goalHeight;
    	requires(Robot.lift);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.lift.moveToSetPoint(liftGoalHeight);
    	SmartDashboard.putBoolean("Lift Finished", false);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.lift.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	SmartDashboard.putBoolean("Lift Finished", true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
