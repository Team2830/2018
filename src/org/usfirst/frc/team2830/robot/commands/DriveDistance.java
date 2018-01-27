package org.usfirst.frc.team2830.robot.commands;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *@param m_distance is the goal distance of the robot
 *@param m_speed is the speed the robot will drive
 */
public class DriveDistance extends Command {
	private double m_distance;
	private double m_speed;
	private boolean isFirstRun = true;

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
    }
    
    public void resetSensors(){
    	RobotMap.ahrs.zeroYaw();
    	RobotMap.leftEncoder.reset();
    	RobotMap.rightEncoder.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(isFirstRun){
    		resetSensors();
    		isFirstRun = false;
    	}
    	
       	double velocity;
       	double distanceDriven = Robot.driveTrain.getEncoderAverage();
       	double vMin = .6;
       	double xRamp = Math.min(18,m_distance/2);
       	double xBrake = Math.min(18,m_distance/2);
       	if (distanceDriven>xBrake)
       		velocity=m_speed+((vMin-m_speed)/(m_distance-xBrake))*distanceDriven;
       	else if (distanceDriven<xRamp)
       		velocity=vMin+((m_speed-vMin)/xRamp)*distanceDriven;
       	else
      		velocity=m_speed;

       	if(RobotMap.ahrs.getAngle()<-1.00){
    		Robot.driveTrain.driveForward(velocity,.3);
    		SmartDashboard.putString("Turn Direction", "left");
    	}
    	else if(RobotMap.ahrs.getAngle()>1.00){
    		Robot.driveTrain.driveForward(velocity,-.3);
    		SmartDashboard.putString("Turn Direction", "right");
    	}
    	else{
    		Robot.driveTrain.driveForward(velocity,0);
    		SmartDashboard.putString("Turn Direction", "straight");
    	}
       	
       	
    
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
