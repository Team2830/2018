package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.OperateLift;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Lift extends PIDSubsystem {
//	static double kP = .02;
//	static double kI = .00001;
//	static double kD = 0;
	
	static double kP = .018;
	static double kI = .00001;
	static double kD = 0;
	
	double lastCycle = RobotMap.liftEncoder.getDistance();
	double maxCycleDifference = 0;
	
    public Lift() {
		super(kP, kI, kD);
    	liftEncoder = RobotMap.liftEncoder;
		setAbsoluteTolerance(50);
		this.setOutputRange(-.9, .9);
		setSetpoint(0);
		enable();
	}
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
	public double joystickDeadband = .1;
	private Encoder liftEncoder;
	public int liftHeightIndex = 1;
	public double liftGoal;
	
	public final double switchHeight = 2500;
	public final double lowScaleHeight = 5*1440;
	public final double midScaleHeight = 7*1440;
	public final double tallScaleHeight = 4480; //3360
	
	//DigitalInput upperLimitSwitch = new DigitalInput(1);
    //DigitalInput lowerLimitSwitch = new DigitalInput(2);
    
    private final double maxLiftHeight = 999999999;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
  //  	setDefaultCommand(new MoveLiftToSetPoint(setHeightIndex(Robot.oi.getOperatorJoystick())));
    	setDefaultCommand(new OperateLift());

    	
    }
    
    /**
     * Sets the speed of the motor controllers.
     * @param speed The speed from -1 to 1.
     */
    public void set(double speed){
    	writeToSmartDashboard();
    	RobotMap.liftRight.set(speed);
      	RobotMap.liftLeft.set(speed);
      	
    }
    
	public void writeToSmartDashboard() {
		SmartDashboard.putNumber("Lift Encoder",getLiftEncoderDistance());
		SmartDashboard.putNumber("LIFT PID Position", this.getPosition());
		SmartDashboard.putNumber("LIFT PID Error", this.getSetpoint()-this.getPosition());
	}
	
    /**
     * Allows the operator to manually move the lift.
     * @param operatorStick The operator joystick.
     */
    public void operateLift(Joystick operatorStick){
    	
/**
 *  TODO Change factor to max speed (units/20ms)	
 */
//    	if(Math.abs(liftEncoder.getDistance()-lastCycle) > maxCycleDifference ){
//    		maxCycleDifference = Math.abs(liftEncoder.getDistance()-lastCycle);
//    	}
    	double newSetPoint;
    	if(deadbanded(operatorStick.getRawAxis(1), joystickDeadband)<0){
    		newSetPoint = getSetpoint()-31*deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
    	}else{
    		newSetPoint = getSetpoint()-33*deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
    	}
    	moveToSetPoint(newSetPoint);
    	SmartDashboard.putNumber("Lift Setpoint", getSetpoint());
//    	double controllerInput = deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
//    	set(-controllerInput);
    	
//    	lastCycle = liftEncoder.getDistance();
//    	SmartDashboard.putNumber("Max Lift Speed", maxCycleDifference);
    	
//    	else{
//    		double newSetPoint = liftEncoder.getDistance();
//    		moveToSetPoint(newSetPoint);
//    	}
    }
    public void manualOperateLift(Joystick operatorStick){
    	//disable();
    	double controllerInput = deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
    	set(-controllerInput); //limitSwitch(-controllerInput));
    }
    
    
    public void moveToSetPoint(double setPoint){
    	Robot.lift.setSetpoint(setPoint);
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
    public void resetEncoder(){
    	liftEncoder.reset();
    }

    public boolean limitSwitchHit(int channel){
    	return true;
    }
    
    public void updateOutputRange(double lower, double upper){
    	setOutputRange(lower, upper);
    }
    
	@Override
	protected double returnPIDInput() {
//		writeToSmartDashboard(Robot.oi.getOperatorJoystick());
		return getLiftEncoderDistance();
	}
	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("PID output", output);
		writeToSmartDashboard();
		set(output); //limitSwitch(output));
	}
	
//	private double limitSwitch(double output){
//		 if (upperLimitSwitch.get() || this.getLiftEncoderDistance() > maxLiftHeight){
//			 output = Math.min(output, 0);  // If the forward limit switch is pressed, we want to keep the values between -1 and 0
//		 }
//	     else if(lowerLimitSwitch.get()){
//	    	 liftEncoder.reset();
//	    	 output = Math.max(output, 0); // If the reversed limit switch is pressed, we want to keep the values between 0 and 1
//	     }
//		 
//		 return output;
//	}
	
}




