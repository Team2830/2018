/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
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
	//public static SpeedControllerGroup speedControllerGroupLeft;
	//public static SpeedControllerGroup speedControllerGroupRight;
	public static DifferentialDrive robotDrive;
	
	public static Encoder leftEncoder;
	public static Encoder rightEncoder;
	
	public static AHRS ahrs;
	
	public static SpeedController intakeLeft;
	public static SpeedController intakeRight;
	public static SpeedController liftFront;
	public static SpeedController liftBack;
	

	/**
	 * Initializes the speed controllers,
	 * the encoders,
	 * the gyroscope,
	 * and the robot drive.
	 * 
	 */
	public static void init(){
		victorLeft = new WPI_VictorSPX(14);
		talonLeft = new WPI_TalonSRX(15);
		victorRight = new WPI_VictorSPX(1);
		talonRight = new WPI_TalonSRX(0);
		
		talonLeft.configPeakCurrentLimit(0, 0);
		talonLeft.configContinuousCurrentLimit(20, 0);
		talonLeft.configMotionCruiseVelocity(Robot.driveTrain.getPulsesFromInches(12), 10);
		talonLeft.configMotionAcceleration(Robot.driveTrain.getPulsesFromInches(3), 10);
		
		talonRight.configPeakCurrentLimit(0, 0);
		talonRight.configContinuousCurrentLimit(20, 0);
		talonRight.configMotionCruiseVelocity(Robot.driveTrain.getPulsesFromInches(12), 10);
		talonRight.configMotionAcceleration(Robot.driveTrain.getPulsesFromInches(3), 10);

		
		victorLeft.follow(talonLeft);
		victorRight.follow(talonRight);
		//speedControllerGroupLeft = new SpeedControllerGroup(speedControllerFrontLeft, speedControllerBackLeft);
		//speedControllerGroupRight = new SpeedControllerGroup(speedControllerFrontRight, speedControllerBackRight);
		
		robotDrive = new DifferentialDrive(talonLeft, talonRight);
		
		leftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(0.052360);
		
		rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		rightEncoder.setDistancePerPulse(-0.052360);		
		
		ahrs = new AHRS(SerialPort.Port.kUSB1);
		
		robotDrive.setExpiration(0.1);
		robotDrive.setSafetyEnabled(true);
		robotDrive.setMaxOutput(1.0);
		
		intakeLeft = new Spark(0);
		intakeRight = new Spark(1);
		liftFront = new Spark(2);
		liftBack = new Spark(3);
	}
	
}
