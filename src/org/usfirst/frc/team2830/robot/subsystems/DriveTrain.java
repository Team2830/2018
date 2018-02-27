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

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class DriveTrain extends Subsystem {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public double controllerCorrection = 0.35;
	public double joystickDeadband = 0.2;
	
	double maxOutputLeft = 0.0;
	double maxOutputRight = 0.0;
	
	double bearing = 0;
	int drivingStraightCycleCount = 0;
	
	

	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDrive());

	}

	
	/**
	 * Resets the encoders and the gyroscope.
	 */
	public void resetCounters() {
		//RobotMap.leftEncoder.reset();
		//RobotMap.rightEncoder.reset();
		RobotMap.ahrs.zeroYaw();
		RobotMap.talonLeft.setSelectedSensorPosition(0, 0, 10);
		RobotMap.talonRight.setSelectedSensorPosition(0, 0, 10);
		bearing = 0;
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
		double throttle = deadbanded((-1*driverStick.getRawAxis(2))+driverStick.getRawAxis(3), joystickDeadband);
		double steering = deadbanded(driverStick.getRawAxis(0), joystickDeadband);
		

		
		if (steering == 0 && Robot.oi.getDriverJoystick().getRawButton(1)){
			
			if(++drivingStraightCycleCount > 25){
				//code for driving straight logic
				driveCorrection(bearing, throttle);
			}else if(drivingStraightCycleCount == 3){
				//set bearing, set isDrivingStraight to true, drive Straight
				bearing = RobotMap.ahrs.getAngle();
				driveCorrection(bearing, throttle);
			}else{
				driveUncorrected(throttle, steering);
			}
		}else{
			//set isDrivingStraight to false, drive uncorrected
			drivingStraightCycleCount = 0;
			driveUncorrected(throttle, steering);
		}
		
		
		writeToSmartDashboard();
	}


	private void driveUncorrected(double throttle, double steering) {
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
	}
	
	/**
	 * Keeps the robot from driving when the controller values are minuscule.
	 * @param input Controller input value.
	 * @param deadband Lower threshold for controller input value.
	 * @return Returns input if the controller input is greater than the deadband.
	 * Otherwise returns the deadband.
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
		
		SmartDashboard.putNumber("Bearing", bearing);
		SmartDashboard.putNumber("Gyro Error", RobotMap.ahrs.getAngle()-bearing);
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
		
		SmartDashboard.putNumber("Gyro Angle", RobotMap.ahrs.getAngle());
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
	public void driveCorrection(double presetAngle, double throttle){
		if (RobotMap.ahrs.getAngle()-presetAngle > 1){
			RobotMap.talonLeft.set(throttle*.60);
			RobotMap.talonRight.set(throttle);
		}else if(RobotMap.ahrs.getAngle()-presetAngle<-1){
			RobotMap.talonLeft.set(throttle);
			RobotMap.talonRight.set(throttle*.60);
		}else{
			RobotMap.talonLeft.set(throttle);
			RobotMap.talonRight.set(throttle);
		}
	}
	
//	/**
//	 * This method takes the 2 inputs from the game pad and converts them into the Left side throttle
//	 * @param speed
//	 * @param rotation
//	 * @return
//	 */
//	public double getLeftThrottle(double speed, double rotation){
//		speed = deadbanded(speed, joystickDeadband);
//		rotation = deadbanded(rotation, joystickDeadband);
//
//		double maxInput = Math.copySign(Math.max(Math.abs(speed), Math.abs(rotation)), speed);
//		
//		if (speed >= 0){
//			if(rotation >= 0){
//				return maxInput;
//			}else{
//				return speed+rotation;
//			}
//			
//		}else{
//			if(rotation >= 0){
//				return speed+rotation;
//			}else{
//				return maxInput;
//			}
//		}
//	}
//	
//	/**
//	 * This method takes the 2 inputs from the game pad and converts them into the Right side throttle
//	 * @param speed
//	 * @param rotation
//	 * @return
//	 */
//	public double getRightThrottle(double speed, double rotation){
//		speed = deadbanded(speed, joystickDeadband);
//		rotation = deadbanded(rotation, joystickDeadband);
//		
//		double maxInput = Math.copySign(Math.max(Math.abs(speed), Math.abs(rotation)), speed);
//		
//		if (speed >= 0){
//			if(rotation >= 0){
//				return speed-rotation;
//			}else{
//				return maxInput;
//			}
//			
//		}else{
//			if(rotation >= 0){
//				return maxInput;
//			}else{
//				return speed-rotation;
//			}
//		}
//	}
	/**
	 * Uses the gyroscope to ensure that the robot is driving straight.
	 * Calls driveForward to correct the drive when the robot is not driving
	 * within 1 degree of straight.
	 * @param velocity Takes the velocity of the robot to use when calling driveForward for the correction.
	 */
//	public void driveCorrection(double velocity){
//
//       	if(RobotMap.ahrs.getAngle()<-1.00){
//    		driveForward(velocity,.3);
//    		SmartDashboard.putString("Turn Direction", "left");
//    	}
//    	else if(RobotMap.ahrs.getAngle()>1.00){
//    		driveForward(velocity,-.3);
//    		SmartDashboard.putString("Turn Direction", "right");
//    	}
//    	else{
//    		driveForward(velocity,0);
//    		SmartDashboard.putString("Turn Direction", "straight");
//    	}
//	}
//	public double getProfileSpeed(double endDistance, double maxSpeed){
//       	double distanceDriven = getDrivenDistance();
//       	double minSpeed = .6;
//       	double rampDistance = Math.min(18,endDistance/2);
//       	double brakeStartDistance = endDistance-Math.min(18,endDistance/2);
//       	if (distanceDriven>brakeStartDistance)
//       		 return maxSpeed+((minSpeed-maxSpeed)/(endDistance-brakeStartDistance))*(distanceDriven-brakeStartDistance);
//       	else if (distanceDriven<rampDistance)
//       		return minSpeed+((maxSpeed-minSpeed)/rampDistance)*distanceDriven;
//       	else
//      		return maxSpeed;
//	}
	
//	public void controlDriveArcade(double speed, double rotation){
//		
//		double maxInput = Math.copySign(Math.max(Math.abs(speed), Math.abs(rotation)), speed);
//		
//		if (speed >= .02){
//			if(rotation >= 0.02){
//				RobotMap.talonLeft.set(ControlMode.PercentOutput, maxInput);
//				RobotMap.talonRight.set(ControlMode.PercentOutput, speed-rotation);
//			}else if (rotation <=-.02){
//				RobotMap.talonLeft.set(ControlMode.PercentOutput, speed+rotation);
//				RobotMap.talonRight.set(ControlMode.PercentOutput, maxInput);
//			}
//			
//		}else if (rotation <-.02){
//			if(rotation >= 0.02){
//				RobotMap.talonLeft.set(ControlMode.PercentOutput, speed+rotation);
//				RobotMap.talonRight.set(ControlMode.PercentOutput, maxInput);
//			}else if (rotation <= -.02){
//				RobotMap.talonLeft.set(ControlMode.PercentOutput, maxInput);
//				RobotMap.talonRight.set(ControlMode.PercentOutput, speed+rotation);
//			}
//		}
//	}
//	
//	public void driveDistanceStraight(double distance, double speed){
//		driveCorrection(getProfileSpeed(distance, speed));
//		
//	}
//	public void stopDriving(){
//		RobotMap.robotDrive.arcadeDrive(0, 0);
//	}
//	
//	public double getDrivenDistance(){
//		return getInchesFromPulses(RobotMap.talonRight.getSelectedSensorPosition(0));
//	}
//	
//	public int getPulsesFromInches(double inches){
//		return (int)(240/Math.PI*inches);
//	}
//	
//	public double getInchesFromPulses(int pulses){
//		return pulses*Math.PI/240;
//	}
}
