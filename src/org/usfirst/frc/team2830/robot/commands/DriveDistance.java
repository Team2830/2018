package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *@param m_distance is the goal distance of the robot
 *@param m_speed is the speed the robot will drive
 */
public class DriveDistance extends Command {
	private double m_distance;
	private double m_speed;

    public DriveDistance(double distance, double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	m_distance = distance;
    	m_speed = speed;
    	requires(Robot.driveTrain);
    }
    
    public DriveDistance(double distance){
    	this(distance, .7);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.resetCounters();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       	//Robot.driveTrain.driveDistanceStraight(m_distance, m_speed);
    
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//return Robot.driveTrain.getDrivenDistance() >= m_distance;
    	return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Robot.driveTrain.stopDriving();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    
}
