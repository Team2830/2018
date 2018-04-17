/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2830.robot.commands.DriveForwardAuto;
import org.usfirst.frc.team2830.robot.commands.autocenter.CenterLeftSwitch;
import org.usfirst.frc.team2830.robot.commands.autocenter.CenterRightSwitch;
import org.usfirst.frc.team2830.robot.commands.autoleft.LeftCloseScale;
import org.usfirst.frc.team2830.robot.commands.autoleft.LeftCloseSwitch;
import org.usfirst.frc.team2830.robot.commands.autoleft.LeftFarScale;
import org.usfirst.frc.team2830.robot.commands.autoleft.LeftFarSwitch;
import org.usfirst.frc.team2830.robot.commands.autoright.RightCloseScale;
import org.usfirst.frc.team2830.robot.commands.autoright.RightCloseSwitch;
import org.usfirst.frc.team2830.robot.commands.autoright.RightFarScale;
import org.usfirst.frc.team2830.robot.commands.autoright.RightFarSwitch;

import org.usfirst.frc.team2830.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2830.robot.subsystems.Intake;
import org.usfirst.frc.team2830.robot.subsystems.Lift;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	public static DriveTrain driveTrain;
	public static OI oi;
	public static Lift lift;
	public static Intake intake;
	
	
	Command m_autonomousCommand;
	SendableChooser<String> startPlace = new SendableChooser<>();
	SendableChooser<String> plate = new SendableChooser<>();
	SendableChooser<Boolean> crossCenter = new SendableChooser<>();
	Command selectedAuto;
	UsbCamera camera;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		RobotMap.init();
		driveTrain = new DriveTrain();
		lift = new Lift();
		intake = new Intake();
		oi = new OI();
		

		// Get the UsbCamera from CameraServer
		camera = CameraServer.getInstance().startAutomaticCapture();
		// Set the resolution
		camera.setResolution(320, 240);
		camera.setFPS(15);
		
		startPlace.setName("Start Place");
		startPlace.addDefault("right", "right");
		startPlace.addObject("left", "left");
		startPlace.addObject("center", "center");
		
		plate.setName("Plate Selection");
		plate.addDefault("scale", "scale");
		plate.addObject("none", "none");
		plate.addObject("switch", "switch");	
		
		crossCenter.setName("Cross?");
		crossCenter.addDefault("No", false);
		crossCenter.addObject("Yes", true);
		
		SmartDashboard.putData(startPlace.getName(), startPlace);
		SmartDashboard.putData(plate.getName(), plate);
		SmartDashboard.putData(crossCenter.getName(), crossCenter);		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		driveTrain.disablePID();
		lift.disable();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		//m_chooser.addDefault("Drive Forward", new DriveForwardAuto());

//		m_autonomousCommand = m_chooser.getSelected();
		
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.length() > 0){
			if (plate.getSelected() == "switch"){
				if (startPlace.getSelected() == "right"){
					if(gameData.charAt(0) == 'L' && crossCenter.getSelected()){
						selectedAuto = new RightFarSwitch();
					}else if(gameData.charAt(0) == 'R'){
						selectedAuto = new RightCloseSwitch();
					}else{
						selectedAuto = new DriveForwardAuto();
					}
				}else if (startPlace.getSelected() == "left"){
					if(gameData.charAt(0) == 'L'){
						selectedAuto = new LeftCloseSwitch();
					}else if(gameData.charAt(0) == 'R' && crossCenter.getSelected()){
						selectedAuto = new LeftFarSwitch();
					}else{
						selectedAuto = new DriveForwardAuto();
					}
				}else if (startPlace.getSelected() == "center"){
					if(gameData.charAt(0) == 'L'){
						selectedAuto = new CenterLeftSwitch();
					}else if(gameData.charAt(0) == 'R'){
						selectedAuto = new CenterRightSwitch();
					}
				}
			}else if (plate.getSelected() == "scale"){
				if (startPlace.getSelected() == "right"){
					if(gameData.charAt(1) == 'L' && crossCenter.getSelected()){
						selectedAuto = new RightFarScale();
					}else if(gameData.charAt(1) == 'R'){
						selectedAuto = new RightCloseScale();
					}else{
						if (gameData.charAt(0) == 'R'){
							selectedAuto = new RightCloseSwitch();
						}else{
							selectedAuto = new DriveForwardAuto();
						}
					}
				}else if (startPlace.getSelected() == "left"){
					if(gameData.charAt(1) == 'L'){
						selectedAuto = new LeftCloseScale();
					}else if(gameData.charAt(1) == 'R' && crossCenter.getSelected()){
						selectedAuto = new LeftFarScale();
					}else{
						if (gameData.charAt(0) == 'L'){
							selectedAuto = new LeftCloseSwitch();
						}else{
							selectedAuto = new DriveForwardAuto();
						}
					}
				}else{
					if(gameData.charAt(0) == 'L'){
						selectedAuto = new CenterLeftSwitch();
					}else{
						selectedAuto = new CenterRightSwitch();
					}
				}
			}else{
				selectedAuto = new DriveForwardAuto();
			}
		}
		//m_autonomousCommand = selectedAuto;
		m_autonomousCommand = new DriveForwardAuto();
		SmartDashboard.putString("Chosen Auto", selectedAuto.getName());
		Robot.driveTrain.resetCounters();
		Robot.lift.enable();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		Robot.driveTrain.writeToSmartDashboard();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		//Robot.lift.disable();
		//Robot.lift.setSetpoint(0);
		Robot.lift.disable();
		//Robot.lift.updateOutputRange(-.6, .6);
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		Robot.driveTrain.writeToSmartDashboard();
		Robot.lift.writeToSmartDashboard();
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
