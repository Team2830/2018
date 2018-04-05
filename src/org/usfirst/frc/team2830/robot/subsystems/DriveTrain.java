/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.ArcadeDrive;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveTrain extends Subsystem implements PIDOutput {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public double controllerCorrection = 0.35;
	public double joystickDeadband = 0.2;

	double maxOutputLeft = 0.0;
	double maxOutputRight = 0.0;
	int drivingStraightCycleCount = 0;

	PIDController turnController;
	double rotateToAngleRate;

	/* The following PID Controller coefficients will need to be tuned */
	/* to match the dynamics of your drive system.  Note that the      */
	/* SmartDashboard in Test mode has support for helping you tune    */
	/* controllers by displaying a form where you can enter new P, I,  */
	/* and D constants and test the mechanism.                         */

//	static final double kP = 0.06;
//	static final double kI = 0.0055;
	
	static final double kP = 0.01;
	static final double kI = 0.000;
	static final double kD = 0.0002;
	static final double kF = 0.00;

	/* This tuning parameter indicates how close to "on target" the    */
	/* PID Controller will attempt to get.                             */

	static final double kToleranceDegrees = 2.0f;
 
	AHRS navx;

	public DriveTrain(){
		try{
			navx = new AHRS(SerialPort.Port.kUSB1);
		}
		catch(RuntimeException ex){
			DriverStation.reportError("Error ins\tantiating navX-MXP: "+ ex.getMessage(), true);
		}
		while (navx == null){
			System.out.println("Navx still null");
		}
		turnController = new PIDController(kP, kI, kD, kF, navx, this); 
		turnController.setInputRange(-180.0f,  180.0f);
		turnController.setOutputRange(-.5, .5);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
		//turnController.disable();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDrive());

	}


	/**
	 * Resets the encoders and the gyroscope.
	 */
	public void resetCounters() {
		//RobotMap.leftEncoder.reset();
		//RobotMap.rightEncoder.reset();
		navx.zeroYaw();
		RobotMap.talonLeft.setSelectedSensorPosition(0, 0, 10);
		RobotMap.talonRight.setSelectedSensorPosition(0, 0, 10);
	}

	/**
	 * Calls arcadeDrive with given parameters
	 * @param velocity Takes the speed.
	 * @param rotation Takes the angle.
	 */
	//	public void driveForward(double velocity, double rotation){
	//		RobotMap.robotDrive.arcadeDrive(velocity, rotation);
	//	}

	/**
	 * Takes inputs from driverStick and calls arcadeDrive to move the robot.
	 * @param driverStick Takes input values and activates the drivetrain motors accordingly.
	 * The throttle tells arcadeDrive how fast the robot should move.
	 * Steering tells arcadeDrive how quickly the robot should turn.
	 */
	public void driveArcade(Joystick driverStick){
		/**
		 * TODO try to dampen joystick input
		 */
		if(turnController.isEnabled() && this.onTarget()){
			this.disablePID();
		}
		if(! turnController.isEnabled()){
			double throttle = deadbanded((-1*driverStick.getRawAxis(2))+driverStick.getRawAxis(3), joystickDeadband);
			double steering = 0.6*deadbanded(driverStick.getRawAxis(0), joystickDeadband);
			
			if (Math.abs(throttle) > .8){
				throttle = Math.copySign(.8, throttle);
			}
			
			SmartDashboard.putNumber("Steering", steering);
			double maxInput = Math.copySign(Math.max(Math.abs(throttle), Math.abs(steering)), throttle);
			if (throttle >= 0){
				if(steering >= 0){
					RobotMap.talonLeft.set(maxInput);
					RobotMap.talonRight.set(throttle-steering);
				}else{
					RobotMap.talonLeft.set(throttle+steering);
					RobotMap.talonRight.set(maxInput);
				}

			}else{
				if(steering >= 0){
					RobotMap.talonLeft.set(throttle+steering);
					RobotMap.talonRight.set(maxInput);
				}else{
					RobotMap.talonLeft.set(maxInput);
					RobotMap.talonRight.set(throttle-steering);
				}
			}
			writeToSmartDashboard();
		}
	}

	/**
	 * Keeps the robot from driving when the controller values are minuscule.
	 * @param input Controller input value.
	 * @param deadband Lower threshold for controller input value.
	 * @return Returns input if the controller input is greater than the deadband.
	 * Otherwise returns the 0.
	 */
	public double deadbanded(double input, double deadband){
		if(Math.abs(input)>Math.abs(deadband)){
			return input;
		}else{
			return 0;
		}
	}

	/**
	 * Adds values to the shuffleboard.
	 */
	public void writeToSmartDashboard(){
		SmartDashboard.putNumber("Left Encoder Distance", RobotMap.talonLeft.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Left Encoder Speed", RobotMap.talonLeft.getSelectedSensorVelocity(0));
		RobotMap.talonLeft.setName("DriveTrain", "Left Talon");

		SmartDashboard.putNumber("Left Controller Input", RobotMap.talonLeft.get());
		SmartDashboard.putNumber("Right Controller Input", RobotMap.talonRight.get());

		SmartDashboard.putNumber("Right Encoder Distance", RobotMap.talonRight.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Encoder Speed", RobotMap.talonRight.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Error", RobotMap.talonLeft.getClosedLoopError(0));

		SmartDashboard.putNumber("Driving Straight Cycle Count", drivingStraightCycleCount);
		SmartDashboard.putNumber("steering", deadbanded(Robot.oi.getDriverJoystick().getRawAxis(0), joystickDeadband));

		if(RobotMap.talonLeft.getSelectedSensorVelocity(0)>maxOutputLeft){
			maxOutputLeft = RobotMap.talonLeft.getSelectedSensorVelocity(0);
		}
		if(RobotMap.talonRight.getSelectedSensorVelocity(0)>maxOutputRight){
			maxOutputRight = RobotMap.talonRight.getSelectedSensorVelocity(0);
		}
		SmartDashboard.putNumber("MaxVelocityLeft", maxOutputLeft);
		SmartDashboard.putNumber("MaxVelocityRight", maxOutputRight);

		SmartDashboard.putNumber("Gyro Angle", navx.getAngle());
		//	SmartDashboard.putBoolean("CurrentLimit", RobotMap.talonLeft.)
	}

	/**
	 * Checks the gyro against setAngle.
	 * 
	 * If the difference (gyro angle - set angle) is greater than 1, 
	 * turn the robot slightly to the left.
	 * 
	 * If the difference (gyro angle - set angle) is less than -1,
	 * turn the robot slightly to the right.
	 * 
	 * @param presetAngle The preset angle
	 * @param throttle The default driving speed
	 */
	public void driveStraight(double throttle){
		if(turnController.isEnabled() && this.onTarget()){
			this.disablePID();
		}
		if(!turnController.isEnabled()){
			if(throttle > 0){
				if (navx.getAngle() > 1){
					RobotMap.talonLeft.set(throttle*.60);
					RobotMap.talonRight.set(throttle);
				}else if(navx.getAngle()< -1){
					RobotMap.talonLeft.set(throttle);
					RobotMap.talonRight.set(throttle*.60);
				}else{
					RobotMap.talonLeft.set(throttle);
					RobotMap.talonRight.set(throttle);
				}
			}
			else{
				if (navx.getAngle() > 1){
					RobotMap.talonLeft.set(throttle);
					RobotMap.talonRight.set(throttle*.60);
				}else if(navx.getAngle()< -1){
					RobotMap.talonLeft.set(throttle*.60);
					RobotMap.talonRight.set(throttle);
				}else{
					RobotMap.talonLeft.set(throttle);
					RobotMap.talonRight.set(throttle);
				}
			}
		}
	}



	/**
	 * Uses the gyroscope to ensure that the robot is driving straight.
	 * Calls driveForward to correct the drive when the robot is not driving
	 * within 1 degree of straight.
	 * @param velocity Takes the velocity of the robot to use when calling driveForward for the correction.
	 */
	public int getPulsesFromInches(double inches){
		return (int)(240/Math.PI*inches);
	}

	public double getInchesFromPulses(double d){
		return d*Math.PI/240;
	}
	public double getDistance(){
		return(RobotMap.talonLeft.getSelectedSensorPosition(0) + RobotMap.talonRight.getSelectedSensorPosition(0))/2;
	}

	public double getAngle() {
		return navx.getAngle();
	}
	public void setOpenloopRamp(double rampTime){
		RobotMap.talonLeft.configOpenloopRamp(rampTime, 10);
		RobotMap.talonRight.configOpenloopRamp(rampTime, 10);
	}
	public void turnToAngle(double setAngle){
		turnController.setSetpoint(setAngle);
		turnController.enable();
	}
	public void disablePID(){
		turnController.disable();
	}
	public boolean onTarget(){
		return turnController.onTarget();
	}
	@Override
	/* This function is invoked periodically by the PID Controller, */
	/* based upon navX-MXP yaw angle input and PID Coefficients.    */
	public void pidWrite(double output) {
		/**
		 * TODO add a "deadband" for the output
		 */
		if (!onTarget()){
			
			if (Math.abs(output) < .33){
				RobotMap.talonLeft.set(Math.copySign(.33, output));
				RobotMap.talonRight.set(Math.copySign(.33, -output));
			} else {
				RobotMap.talonLeft.set(output);
				RobotMap.talonRight.set(-output);
			}
		}
		else {
			RobotMap.talonLeft.set(0);
			RobotMap.talonRight.set(0);
		}
	}
	public double getAccelerationX(){
		return navx.getWorldLinearAccelX();
	}
	public double getAccelerationY(){
		return navx.getWorldLinearAccelY();
	}
}
