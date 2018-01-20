/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Encoder.IndexingType;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static SpeedController speedControllerFrontLeft;
	public static SpeedController speedControllerBackLeft;
	public static SpeedController speedControllerBackRight;
	public static SpeedController speedControllerFrontRight;
	public static SpeedControllerGroup speedControllerGroupLeft;
	public static SpeedControllerGroup speedControllerGroupRight;
	public static DifferentialDrive robotDrive;
	
	public static Encoder leftEncoder;
	public static Encoder rightEncoder;
	
	public static AHRS ahrs;

	
	public static void init(){
		speedControllerFrontLeft = new Spark(0);
		speedControllerBackLeft = new Spark(1);
		speedControllerBackRight = new Spark(2);
		speedControllerFrontRight = new Spark(3);
		speedControllerGroupLeft = new SpeedControllerGroup(speedControllerFrontLeft, speedControllerBackLeft);
		speedControllerGroupRight = new SpeedControllerGroup(speedControllerFrontRight, speedControllerBackRight);
		
		robotDrive = new DifferentialDrive(speedControllerGroupLeft, speedControllerGroupRight);
		
		leftEncoder = new Encoder(0, 1);
		leftEncoder.setDistancePerPulse(-0.052360);
		leftEncoder.setPIDSourceType(PIDSourceType.kRate);
		leftEncoder.setIndexSource(4, IndexingType.kResetOnRisingEdge);
		
		rightEncoder = new Encoder(2, 3);
		rightEncoder.setDistancePerPulse(-0.052360);
		rightEncoder.setPIDSourceType(PIDSourceType.kRate);
		rightEncoder.setIndexSource(5, IndexingType.kResetOnRisingEdge);
		
		
		ahrs = new AHRS(SerialPort.Port.kUSB1);
		
		robotDrive.setExpiration(0.1);
		robotDrive.setSafetyEnabled(true);
		robotDrive.setMaxOutput(1.0);		
	}
	
}
