/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.ArcadeDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;

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
	
	public void driveTrainInit(){
		RobotMap.talonLeft.configPeakCurrentLimit(35, 10);
		RobotMap.talonLeft.configPeakCurrentDuration(200, 10);
		RobotMap.talonLeft.configContinuousCurrentLimit(30, 10);
		RobotMap.talonLeft.enableCurrentLimit(true);
		
		RobotMap.talonLeft.configNominalOutputForward(0, 10);
		RobotMap.talonLeft.configNominalOutputReverse(0, 10);
		RobotMap.talonLeft.configPeakOutputForward(1, 10);
		RobotMap.talonLeft.configPeakOutputReverse(-1, 0);
		
		RobotMap.talonLeft.selectProfileSlot(0, 0);
		RobotMap.talonLeft.config_kF(0, 0, 10);
		RobotMap.talonLeft.config_kP(0, .2, 10);
		RobotMap.talonLeft.config_kI(0, 0, 10);
		RobotMap.talonLeft.config_kD(0, 0, 10);
		RobotMap.talonLeft.config_IntegralZone(0, 100, 10);
		
		RobotMap.talonRight.configPeakCurrentLimit(35, 10);
		RobotMap.talonRight.configPeakCurrentDuration(200, 10);
		RobotMap.talonRight.configContinuousCurrentLimit(30, 10);
		RobotMap.talonRight.enableCurrentLimit(true);
		
		RobotMap.talonRight.configNominalOutputForward(0, 10);
		RobotMap.talonRight.configNominalOutputReverse(0, 10);
		RobotMap.talonRight.configPeakOutputForward(1, 10);
		RobotMap.talonRight.configPeakOutputReverse(-1, 0);
		
		RobotMap.talonRight.selectProfileSlot(0, 0);
		RobotMap.talonRight.config_kF(0, .1, 10);
		RobotMap.talonRight.config_kP(0, .2, 10);
		RobotMap.talonRight.config_kI(0, 0, 10);
		RobotMap.talonRight.config_kD(0, 0, 10);
		RobotMap.talonRight.config_IntegralZone(0, 100, 10);
		
		
	}
	
	/**
	 * Resets the encoders and the gyroscope.
	 */
	public void resetCounters() {
		//RobotMap.leftEncoder.reset();
		//RobotMap.rightEncoder.reset();
		RobotMap.ahrs.zeroYaw();
	}
	
	/**
	 * Calls arcadeDrive with given parameters
	 * @param velocity Takes the speed.
	 * @param rotation Takes the angle.
	 */
	public void driveForward(double velocity, double rotation){
		RobotMap.robotDrive.arcadeDrive(velocity, rotation);
	}
	
	/**
	 * Takes inputs from driverStick and calls arcadeDrive to move the robot.
	 * @param driverStick Takes input values and activates the drivetrain motors accordingly.
	 * The throttle tells arcadeDrive how fast the robot should move.
	 * Steering tells arcadeDrive how quickly the robot should turn.
	 */
	public void driveArcade(Joystick driverStick){
		double throttle = deadbanded((-1*driverStick.getRawAxis(2))+driverStick.getRawAxis(3), joystickDeadband);
		double steering = deadbanded(driverStick.getRawAxis(0), joystickDeadband);
		RobotMap.robotDrive.arcadeDrive(throttle, steering, true);
		
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
			return deadband;
		}
	}
	
	/**
	 * Adds values to the shuffleboard.
	 */
	public void writeToSmartDashboard(){
		SmartDashboard.putNumber("Left Encoder", getInchesFromPulses(RobotMap.talonLeft.getSelectedSensorPosition(0)));
		SmartDashboard.putNumber("Right Encoder", getInchesFromPulses(RobotMap.talonRight.getSelectedSensorPosition(0)));
		SmartDashboard.putNumber("Error", RobotMap.talonLeft.getClosedLoopError(0));
		
		SmartDashboard.putNumber("Gyro Angle", RobotMap.ahrs.getAngle());
	}
	
	/**
	 * Uses the gyroscope to ensure that the robot is driving straight.
	 * Calls driveForward to correct the drive when the robot is not driving
	 * within 1 degree of straight.
	 * @param velocity Takes the velocity of the robot to use when calling driveForward for the correction.
	 */
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
	
	public void controlDriveArcade(double speed, double rotation){
		
		double maxInput = Math.copySign(Math.max(Math.abs(speed), Math.abs(rotation)), speed);
		
		if (speed >= 0){
			if(rotation >= 0){
				RobotMap.talonLeft.set(ControlMode.PercentOutput, maxInput);
				RobotMap.talonRight.set(ControlMode.PercentOutput, speed-rotation);
			}else{
				RobotMap.talonLeft.set(ControlMode.PercentOutput, speed+rotation);
				RobotMap.talonRight.set(ControlMode.PercentOutput, maxInput);
			}
			
		}else{
			if(rotation >= 0){
				RobotMap.talonLeft.set(ControlMode.PercentOutput, speed+rotation);
				RobotMap.talonRight.set(ControlMode.PercentOutput, maxInput);
			}else{
				RobotMap.talonLeft.set(ControlMode.PercentOutput, maxInput);
				RobotMap.talonRight.set(ControlMode.PercentOutput, speed+rotation);
			}
		}
	}
	
	public void driveDistanceStraight(double distance, double speed){
		driveCorrection(getProfileSpeed(distance, speed));
		
	}
	public void stopDriving(){
		RobotMap.robotDrive.arcadeDrive(0, 0);
	}
	
	public double getDrivenDistance(){
		return getInchesFromPulses(RobotMap.talonRight.getSelectedSensorPosition(0));
	}
	
	public int getPulsesFromInches(double inches){
		return (int)(240/Math.PI*inches);
	}
	
	public double getInchesFromPulses(int pulses){
		return pulses*Math.PI/240;
	}
}
