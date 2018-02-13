/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static WPI_VictorSPX victorLeft;
	public static WPI_TalonSRX talonLeft;
	public static WPI_VictorSPX victorRight;
	public static WPI_TalonSRX talonRight;
	public static DifferentialDrive robotDrive;
	
	
	public static AHRS ahrs;
	
	public static SpeedController intakeLeft;
	public static SpeedController intakeRight;
	public static SpeedController liftFront;
	public static SpeedController liftBack;
	
	public static PowerDistributionPanel pdp;
	
	public static final int intakeChannel = 3;
	

	/**
	 * Initializes the speed controllers,
	 * the encoders,
	 * the gyroscope,
	 * and the robot drive.
	 * 
	 */
	public static void init(){
		pdp = new PowerDistributionPanel();
		
		victorLeft = new WPI_VictorSPX(14);
		talonLeft = new WPI_TalonSRX(15);
		victorRight = new WPI_VictorSPX(1);
		talonRight = new WPI_TalonSRX(0);
		
		talonLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		//talonLeft.setSensorPhase(true);
		talonRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		
		victorLeft.follow(talonLeft);
		victorRight.follow(talonRight);
		
		Robot.driveTrain.driveTrainInit();
		
		robotDrive = new DifferentialDrive(talonLeft, talonRight);	
		
		ahrs = new AHRS(SerialPort.Port.kUSB1);
		
		robotDrive.setExpiration(0.1);
		robotDrive.setSafetyEnabled(true);
		robotDrive.setMaxOutput(1.0);
		
		intakeLeft = new Spark(0);
		intakeLeft.setInverted(true);
		intakeRight = new Spark(1);
		intakeRight.setInverted(false);
		
		
		liftFront = new Spark(2);
		liftBack = new Spark(3);
	}
	
}
