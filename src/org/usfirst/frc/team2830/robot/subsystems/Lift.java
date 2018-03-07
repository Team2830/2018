package org.usfirst.frc.team2830.robot.subsystems;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.RobotMap;
import org.usfirst.frc.team2830.robot.commands.MoveLiftToSetPoint;
import org.usfirst.frc.team2830.robot.commands.OperateLift;
import org.usfirst.frc.team2830.robot.commands.testlift;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Lift extends PIDSubsystem {
	static double kP = .02;
	static double kI = .00001;
	static double kD = 0;
	
    public Lift() {
		super(kP, kI, kD);
    	liftEncoder = RobotMap.liftEncoder;
		setAbsoluteTolerance(10);
		this.setOutputRange(-.7, .7);
		// TODO Auto-generated constructor stub
	}
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
	public double joystickDeadband = .02;
	private Encoder liftEncoder;
	public int liftHeightIndex = 1;
	public double liftGoal;
	
	public final double switchHeight = 1800;
	public final double lowScaleHeight = 5*1440;
	public final double midScaleHeight = 7*1440;
	public final double tallScaleHeight = 10*1440;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
  //  	setDefaultCommand(new MoveLiftToSetPoint(setHeightIndex(Robot.oi.getOperatorJoystick())));
    	//setDefaultCommand(new OperateLift());

    	
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
		SmartDashboard.putNumber("PID Position", this.getPosition());
		SmartDashboard.putNumber("PID Error", this.getSetpoint()-this.getPosition());
	}
    /**
     * Allows the operator to manually move the lift.
     * @param operatorStick The operator joystick.
     */

    public void operateLift(Joystick operatorStick){
    	double speed = -1*deadbanded(operatorStick.getRawAxis(1), joystickDeadband);
    	set(speed);
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
    
    public double getLiftSetHeight(int index){
    	
    	
    	switch (index){
    	
    	case 0: liftGoal = 0;
    	break;

    	case 1: liftGoal = switchHeight;
    	break;

    	case 2: liftGoal = lowScaleHeight;
    	break;

    	case 3: liftGoal = midScaleHeight;
    	break;

    	case 4: liftGoal = tallScaleHeight;
    	break;
    	}
    	return liftGoal;
    }
    public double setHeightIndex(Joystick operatorStick){
    	if (operatorStick.getPOV() == 0 && liftHeightIndex < 4){
    		liftHeightIndex += 1;
    	}else if (operatorStick.getPOV() == 180 && liftHeightIndex > 0){
    		liftHeightIndex -= 1;
    	}
    	return getLiftSetHeight(liftHeightIndex);
    }
    
	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
//		writeToSmartDashboard(Robot.oi.getOperatorJoystick());
		return getLiftEncoderDistance();
	}
	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("PID output", output);
		writeToSmartDashboard();
		set(output);
		
		
	}
}




