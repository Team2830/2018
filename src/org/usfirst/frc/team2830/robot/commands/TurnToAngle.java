package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToAngle extends Command {
	
	double m_angle;
    public TurnToAngle(double angle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	m_angle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Timer.delay(.3);
    	Robot.driveTrain.turnToAngle(m_angle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (Math.abs(RobotMap.talonLeft.getSelectedSensorVelocity(0))
				+Math.abs(RobotMap.talonRight.getSelectedSensorVelocity(0))<50){
			if (Robot.driveTrain.onTarget()){
				return true;
			}
		}
		return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
