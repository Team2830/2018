/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
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
	
	
	//public static AHRS ahrs;
	public static Encoder liftEncoder;
	
	public static SpeedController intakeLeft;
	public static SpeedController intakeRight;
	public static SpeedController liftLeft;
	public static SpeedController liftRight;
	
	
	public static final int intakeChannel = 3;
	

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
		victorRight = new WPI_VictorSPX(21);
		talonRight = new WPI_TalonSRX(20);
		
		talonRight.setInverted(true);
		victorRight.setInverted(true);
		talonLeft.setInverted(false);
		victorLeft.setInverted(false);
		
		talonLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		talonRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		
		victorLeft.follow(talonLeft);
		victorRight.follow(talonRight);

		//Set the open loop ramp to prevent quick starts and stops

		talonLeft.configOpenloopRamp(.01, 10);
		talonRight.configOpenloopRamp(.01, 10);
		
//		talonLeft.configPeakCurrentLimit(35, 10);
//		talonLeft.configPeakCurrentDuration(200, 10);
//		talonLeft.configContinuousCurrentLimit(30, 10);
//		talonLeft.enableCurrentLimit(false);
		talonLeft.setSensorPhase(true);
		
//		talonLeft.configNominalOutputForward(0, 10);
//		talonLeft.configNominalOutputReverse(0, 10);
		talonLeft.configPeakOutputForward(1, 10);
		talonLeft.configPeakOutputReverse(-1, 10);
//		
//		talonLeft.selectProfileSlot(0, 0);
//		talonLeft.config_kF(0, .3, 10);
//		talonLeft.config_kP(0, .2, 10);
//		talonLeft.config_kI(0, 0, 10);
//		talonLeft.config_kD(0, 20, 10);
//		talonLeft.config_IntegralZone(0, 100, 10);
//		talonLeft.configMotionAcceleration(1, 10);
		
		//talonRight.configPeakCurrentLimit(35, 10);
		//talonRight.configPeakCurrentDuration(200, 10);
		//talonRight.configContinuousCurrentLimit(30, 10);
//		talonRight.enableCurrentLimit(false);
		talonRight.setSensorPhase(true);
		
//		talonRight.configNominalOutputForward(0, 10);
//		talonRight.configNominalOutputReverse(0, 10);
		talonRight.configPeakOutputForward(1, 10);
		talonRight.configPeakOutputReverse(-1, 0);
		
//		talonRight.selectProfileSlot(0, 0);
//		talonRight.config_kF(0, .3, 10);
//		talonRight.config_kP(0, .2, 10);
//		talonRight.config_kI(0, 0, 10);
//		talonRight.config_kD(0, 20, 10);
//		talonRight.config_IntegralZone(0, 100, 10);
//		talonRight.configMotionCruiseVelocity(getPulsesFromInches(3), 10);
//		

		
//		robotDrive = new DifferentialDrive(talonLeft, talonRight);	
		


		liftEncoder = new Encoder(0, 1, false);
		

//		robotDrive.setExpiration(0.1);
//		robotDrive.setSafetyEnabled(true);
//		robotDrive.setMaxOutput(1.0);
		
		intakeLeft = new Spark(0);
		intakeLeft.setInverted(true);
		intakeRight = new Spark(1);
		intakeRight.setInverted(false);
		
		
		liftLeft = new Spark(2);
		liftLeft.setInverted(true);
		liftRight = new Spark(3);
		liftRight.setInverted(true);
	}
	
}
