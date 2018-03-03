package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.OperateLift;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Lift extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public double joystickDeadband = .02;
	private Encoder liftEncoder;
	public int liftHeightIndex = 0;
	
	public double switchHeight;
	public double lowScaleHeight;
	public double midScaleHeight;
	public double tallScaleHeight;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new OperateLift());
    	
    	liftEncoder = RobotMap.liftEncoder;
    	liftEncoder.reset();
    	
    }
    /**
     * Sets the speed of the motor controllers.
     * @param speed The speed from -1 to 1.
     */
    public void set(double speed){
    	RobotMap.liftRight.set(speed);
    	RobotMap.liftLeft.set(speed);
    }
    
	public void writeToSmartDashboard(Joystick operatorStick) {
		SmartDashboard.putNumber("Lift Encoder",getLiftEncoderDistance());
	}
    /**
     * Allows the operator to manually move the lift.
     * @param operatorStick The operator joystick.
     */
    public void operateLift(Joystick operatorStick){
    	double speed = deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
    	set(speed*speed);
    }
    
    public double deadbanded(double input, double deadband){
    	if (Math.abs(input) > Math.abs(deadband)){
    		return input;
    	}
    	else {
    		return 0;
    	}
    }
    
    public double getLiftEncoderDistance(){
    	return liftEncoder.getDistance();
    }
    
    /**
     * Moves the lift to the goal location.
     * @param goal The goal is the desired lift height.
     */
    public void liftCorrection(double goal){
    	double error = goal-getLiftEncoderDistance();
    	if (Math.abs(error) >= 4){
    		set(Math.copySign(.6, error));
    	}else if(Math.abs(error)<4 && Math.abs(error)>=1){
    		set(Math.copySign(.3, error));
    	}else{
    		set(0);
    	}
    }
    public boolean limitSwitchHit(int channel){
    	return true;
    }
}




