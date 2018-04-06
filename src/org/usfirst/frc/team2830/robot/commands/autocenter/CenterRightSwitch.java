package org.usfirst.frc.team2830.robot.commands.autocenter;

import org.usfirst.frc.team2830.robot.Robot;
import org.usfirst.frc.team2830.robot.commands.DriveBackwards;
import org.usfirst.frc.team2830.robot.commands.DriveDistance;
import org.usfirst.frc.team2830.robot.commands.IntakeInLeft;
import org.usfirst.frc.team2830.robot.commands.IntakeInRight;
import org.usfirst.frc.team2830.robot.commands.IntakeOut;
import org.usfirst.frc.team2830.robot.commands.MoveLiftToSetPoint;
import org.usfirst.frc.team2830.robot.commands.Turn;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CenterRightSwitch extends CommandGroup {

    public CenterRightSwitch() {
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
    	addParallel(new MoveLiftToSetPoint(Robot.lift.switchHeight/2));
    	addSequential(new DriveDistance(15));
    	addSequential(new Turn(45));
    	addSequential(new DriveDistance(4*12));
    	addSequential(new Turn(-53));
    	addSequential(new MoveLiftToSetPoint(Robot.lift.switchHeight));
    	addSequential(new DriveDistance(12));
    	addSequential(new IntakeOut());
    	addSequential(new DriveBackwards(12*3.5));
    	addSequential(new MoveLiftToSetPoint(0));
    	
    	addSequential(new Turn(-45));
    	addParallel(new DriveDistance(50));
    	addSequential(new IntakeInLeft());
    	addSequential(new DriveBackwards(24));
    }
}
