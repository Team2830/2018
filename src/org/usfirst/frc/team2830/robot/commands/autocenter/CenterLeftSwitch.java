package org.usfirst.frc.team2830.robot.commands.autocenter;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.commands.DriveBackwards;
import org.usfirst.frc.team2830.robot.commands.DriveDistance;
import org.usfirst.frc.team2830.robot.commands.IntakeInRight;
import org.usfirst.frc.team2830.robot.commands.IntakeOut;
import org.usfirst.frc.team2830.robot.commands.MoveLiftToSetPoint;
import org.usfirst.frc.team2830.robot.commands.Turn;
import org.usfirst.frc.team2830.robot.commands.TurnToAngle;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CenterLeftSwitch extends CommandGroup {

    public CenterLeftSwitch() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	
    	//drive forward and lift
    	addSequential(new CommandGroup(){
    		{
    	    	addParallel(new MoveLiftToSetPoint(Robot.lift.switchHeight/2));
    	    	addSequential(new DriveDistance(14));
    		}
    	});
    	
    	addSequential(new Turn(-50));
    	addSequential(new DriveDistance(4.8*12));
    	addSequential(new Turn(47));
    	//lift to switch height, drives to switch wall
    	addSequential(new CommandGroup(){
    		{
    			addParallel(new MoveLiftToSetPoint(Robot.lift.switchHeight));
    			addSequential(new DriveDistance(12));
    		}
    	});
    	addSequential(new IntakeOut());
    	//drives backwards, brings lift down
    	addSequential(new CommandGroup(){
    		{
    			addParallel(new DriveBackwards(12*3.5));
    			addSequential(new MoveLiftToSetPoint(0));
    		}
    	});
    	
    	addSequential(new Turn(45));
    	//drives to powercube pile, picks up second cube
    	addSequential(new CommandGroup(){
    		{
    			addParallel(new DriveDistance(50));
    			addSequential(new IntakeInRight());
    		}
    	});
    	//lifts lift a bit as robot drives in reverse
    	addSequential(new CommandGroup(){
    		{
    			addParallel(new MoveLiftToSetPoint(Robot.lift.switchHeight/4));
    			addSequential(new DriveBackwards(24));
    		}
    	});
    	addSequential(new TurnToAngle(0));
    	
    	//lifts lift to switch height, drives to switch wall
    	addSequential(new CommandGroup(){
    		{
    			addParallel(new DriveDistance(40));
    			addSequential(new MoveLiftToSetPoint(Robot.lift.switchHeight));
    		}
    	});
    	addSequential(new IntakeOut());
    }
}
