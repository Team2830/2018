/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.opencv.core.Mat;
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
	public static final DriveTrain driveTrain
			= new DriveTrain();
	public static OI oi;
	public static Lift lift;
	public static Intake intake;
	
	Thread t;
	
	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	SendableChooser<String> startPlace = new SendableChooser<>();
	SendableChooser<String> plate = new SendableChooser<>();
	Command selectedAuto;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		RobotMap.init();
		oi = new OI();
		lift = new Lift();
		intake = new Intake();
		
		t = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				//	Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
				//		new Scalar(255, 255, 255), 5);
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		t.setDaemon(true);
		t.start();
		
		m_chooser.addDefault("Drive Forward", new DriveForwardAuto());
		
		startPlace.addObject("Left", "left");
		startPlace.addObject("Right", "right");
		startPlace.addObject("Center", "center");
		
		plate.addDefault("None", "none");
		plate.addObject("Switch", "switch");
		plate.addObject("scale", "scale");
		
		SmartDashboard.putData("Auto mode", m_chooser);
		
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

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
					if(gameData.charAt(0) == 'L'){
						selectedAuto = new RightFarSwitch();
					}else if(gameData.charAt(0) == 'R'){
						selectedAuto = new RightCloseSwitch();
					}
				}else if (startPlace.getSelected() == "left"){
					if(gameData.charAt(0) == 'L'){
						selectedAuto = new LeftCloseSwitch();
					}else if(gameData.charAt(0) == 'R'){
						selectedAuto = new LeftFarSwitch();
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
					if(gameData.charAt(1) == 'L'){
						selectedAuto = new RightFarScale();
					}else if(gameData.charAt(1) == 'R'){
						selectedAuto = new RightCloseScale();
					}
				}else if (startPlace.getSelected() == "left"){
					if(gameData.charAt(1) == 'L'){
						selectedAuto = new LeftCloseScale();
					}else if(gameData.charAt(1) == 'R'){
						selectedAuto = new LeftFarScale();
					}
				}else{
					selectedAuto = new DriveForwardAuto();
				}
			}else{
				selectedAuto = new DriveForwardAuto();
			}
		}
		m_autonomousCommand = selectedAuto;
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
		Robot.lift.disable();
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
