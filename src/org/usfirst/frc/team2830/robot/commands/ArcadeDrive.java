package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.OI;
import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArcadeDrive extends Command {
    public ArcadeDrive() {
    	requires(Robot.driveTrain);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis)
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.resetCounters();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.driveArcade(Robot.oi.getDriverJoystick());
    	Robot.driveTrain.writeToSmartDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
