/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot.subsystems;

import java.awt.Robot;

import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.ArcadeDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveTrain extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	
	public double controllerCorrection = 0.35;
	public double joystickDeadband = 0.02;
	

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDrive());
		
		resetCounters();
		
	}

	public void resetCounters() {
		RobotMap.leftEncoder.reset();
		RobotMap.rightEncoder.reset();
		RobotMap.ahrs.zeroYaw();
	}
	
	public void driveForward(double velocity, double rotation){
		RobotMap.robotDrive.arcadeDrive(velocity, rotation);
	}
	
	public void driveArcade(Joystick driverStick){
		double throttle = deadbanded((-1*driverStick.getRawAxis(2))+driverStick.getRawAxis(3), joystickDeadband);
		double steering = deadbanded(driverStick.getRawAxis(0), joystickDeadband);
		RobotMap.robotDrive.arcadeDrive(throttle, steering, true);
	}
	
	public double deadbanded(double input, double deadband){
		if(Math.abs(input)>Math.abs(deadband)){
			return input;
		}else{
			return deadband;
		}
	}
	public void writeToSmartDashboard(){
		SmartDashboard.putNumber("Left Encoder", RobotMap.leftEncoder.getDistance());
		SmartDashboard.putNumber("Right Encoder", RobotMap.rightEncoder.getDistance());
		SmartDashboard.putNumber("Gyro Angle", RobotMap.ahrs.getAngle());
	}
	
	public double getEncoderAverage(){
		return (RobotMap.leftEncoder.getDistance()+
				RobotMap.rightEncoder.getDistance())/2;
	}
	
	public void driveCorrection(double velocity){

       	if(RobotMap.ahrs.getAngle()<-1.00){
    		driveForward(velocity,.3);
    		SmartDashboard.putString("Turn Direction", "left");
    	}
    	else if(RobotMap.ahrs.getAngle()>1.00){
    		driveForward(velocity,-.3);
    		SmartDashboard.putString("Turn Direction", "right");
    	}
    	else{
    		driveForward(velocity,0);
    		SmartDashboard.putString("Turn Direction", "straight");
    	}
	}
	public double getProfileSpeed(double endDistance, double maxSpeed){
       	double distanceDriven = getDrivenDistance();
       	double minSpeed = .6;
       	double rampDistance = Math.min(18,endDistance/2);
       	double brakeStartDistance = endDistance-Math.min(18,endDistance/2);
       	if (distanceDriven>brakeStartDistance)
       		 return maxSpeed+((minSpeed-maxSpeed)/(endDistance-brakeStartDistance))*(distanceDriven-brakeStartDistance);
       	else if (distanceDriven<rampDistance)
       		return minSpeed+((maxSpeed-minSpeed)/rampDistance)*distanceDriven;
       	else
      		return maxSpeed;
	}
	
	public void driveDistanceStraight(double distance, double speed){
		driveCorrection(getProfileSpeed(distance, speed));
		
	}
	public void stopDriving(){
		RobotMap.robotDrive.arcadeDrive(0, 0);
	}
	
	public double getDrivenDistance(){
		return RobotMap.rightEncoder.getDistance();
	}
}
