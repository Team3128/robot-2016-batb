package org.team3128.mechanisms;

import org.team3128.common.hardware.motor.MotorGroup;
import org.team3128.common.listener.POVValue;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Class for the intake arm and its two rollers
 * @author Jamie
 *
 */
public class Intake
{
	public enum RollerState
	{
		STOPPED(0),
		INTAKE(1),
		OUTTAKE(-1);
		
		public final double rollerPower;
		
		private RollerState(double rollerPower)
		{
			this.rollerPower = rollerPower;
		}
		

	}
	
	private MotorGroup rollers;
	
	private MotorGroup intakeLifter;
	
	private RollerState rollerState;

	/**
	 * 
	 * @param rollers The rollers that suck in and hold the ball
	 * @param intakeLifter The motor that lifts and lowers the intake
	 */
	public Intake(MotorGroup rollers, MotorGroup intakeLifter)
	{
		this.rollers = rollers;
		this.intakeLifter = intakeLifter;
	}
	
	public void onPOVUpdate(POVValue newValue)
	{
		switch(newValue.getDirectionValue())
		{
		case 0:
			setRollerState(RollerState.STOPPED);
			break;
		case 1:
		case 2:
		case 8:
			setRollerState(RollerState.OUTTAKE);
			break;
		case 4:
		case 5:
		case 6:
			setRollerState(RollerState.INTAKE);
			break;
		}

		
	}
	
	public RollerState getRollerState()
	{
		return rollerState;
	}

	public void setRollerState(RollerState state)
	{
		rollers.setTarget(state.rollerPower);
		
		rollerState = state;
	}
	
	/**
	 *  Set the speed (motor power) of the lifter motor.  Positive 
	 * @param speed
	 */
	public void setLifterSpeed(double speed)
	{
		intakeLifter.setTarget(speed);
	}
	
	
	public void raiseIntake() {
		
	}
	
	
	public class CmdMoveRollers extends Command 
	{

		boolean in;
		
		public CmdMoveRollers(int msec, boolean in)
		{
			super(msec / 1000.0);
			this.in = in;
		}
		
		protected void initialize()
	    {
			setRollerState(in ? RollerState.INTAKE : RollerState.OUTTAKE);
	    }

	    // Called repeatedly when this Command is scheduled to run
	    protected void execute()
	    {
	    }

	    protected boolean isFinished()
	    {
	    	//wait for timeout
	    	return false;
	    }

	    protected void end()
	    {
	    	setRollerState(RollerState.STOPPED);
	    }

	    protected void interrupted()
	    {
	    	end();
	    }
	    
	}
}
