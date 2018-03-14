package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @param m_distance
 *            is the goal distance of the robot
 * @param m_speed
 *            is the speed the robot will drive
 */
public class DriveBackwards extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_distance;
	private double m_speed;
	private double delay;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private boolean isFirstRun = true;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public DriveBackwards(double distance, double speed, double delaySeconds) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_distance = Robot.driveTrain.getPulsesFromInches(distance);
		m_speed = speed;
		delay = delaySeconds;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveTrain);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	public DriveBackwards(double distance) {
		this(distance, 0.4, 0);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.driveTrain.resetCounters();
		Robot.driveTrain.setOpenloopRamp(0);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
    	if(isFirstRun){
    		Robot.driveTrain.resetCounters();
    		isFirstRun = false;
    	}
    	Robot.driveTrain.driveStraight(-m_speed);
       	
    }

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (-m_distance >= Robot.driveTrain.getDistance()) {
			Robot.driveTrain.driveStraight(0);
			Timer.delay(delay);
			Robot.driveTrain.resetCounters();
			return true;
		}
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveTrain.driveStraight(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.driveTrain.driveStraight(0);
	}
}
