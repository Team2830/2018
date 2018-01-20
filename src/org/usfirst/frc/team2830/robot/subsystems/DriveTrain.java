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
}
