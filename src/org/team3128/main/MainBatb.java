package org.team3128.main;


import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.hardware.motor.MotorGroup;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.POV;
import org.team3128.common.util.GenericSendableChooser;
import org.team3128.common.util.Log;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Turret;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 */
public class MainBatb extends NarwhalRobot
{
	CANTalon drvLeft1, drvLeft2;
	
	CANTalon drvRight1, drvRight2;
	
	CANTalon launcherWheel;

	VictorSP intakeSpin1, intakeSpin2;
	MotorGroup holderWheel;
	MotorGroup intakeRaise;
	
	MotorGroup intakeMotors;
	
	ListenerManager lm;
	
	SRXTankDrive tankDrive;
	
	Turret turret;
	
	@Override
	protected void constructHardware()
	{	
		Log.info("MainBatb", "Loading robot: 2016 Battle at the Border");
		
		drvLeft1 = new CANTalon(0);
		drvLeft2 = new CANTalon(1);
		drvRight1 = new CANTalon(2);
		drvRight2 = new CANTalon(3);
		
		
		intakeSpin1 = new VictorSP(0);
		intakeSpin2 = new VictorSP(1);
		holderWheel = new MotorGroup(new VictorSP(2));
		intakeRaise = new MotorGroup(new VictorSP(3));
		
		intakeMotors = new MotorGroup(intakeSpin1, intakeSpin2);
		
		lm = new ListenerManager(new Joystick(0));//, new Joystick(1));
		
		addListenerManager(lm);
		
		tankDrive = new SRXTankDrive(drvLeft1, drvRight1, 6 * Length.in, 1, 20 * Length.in, 15 * Length.in);
		
		// configure the other tnk drive talons as followers
		drvLeft2.changeControlMode(TalonControlMode.Follower);
		drvLeft2.set(drvLeft1.getDeviceID());
		
		drvRight2.changeControlMode(TalonControlMode.Follower);
		drvRight2.set(drvRight1.getDeviceID());
		
		
	}
	
	@Override
	protected void setupListeners()
	{
		lm.nameControl(ControllerExtreme3D.TWIST, "MoveTurn");
		lm.nameControl(ControllerExtreme3D.JOYY, "MoveForwards");
		lm.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");
		
		lm.nameControl(new POV(0),"IntakeControl");
		lm.nameControl(ControllerExtreme3D.TRIGGER, "Launch");
		
		
		lm.addMultiListener(()->
		{
			tankDrive.arcadeDrive(lm.getAxis("MoveTurn"), lm.getAxis("MoveForwards"), lm.getAxis("Throttle"), true);
		}, "MoveTurn", "MoveForwards", "Throttle");
		
	}

	@Override
	protected void disabledInit()
	{

	}

	@Override
	protected void autonomousInit()
	{
		
	}
	
	@Override
	protected void teleopInit()
	{	
		
	}
	
	@Override
	protected void constructAutoPrograms(GenericSendableChooser<CommandGroup> autoChooser)
	{
		
	}

	@Override
	protected void updateDashboard()
	{
		
	}
	
	@Override
	protected void teleopPeriodic()
	{
		if(lm.getButton("Launch"))
		{
			if(turret.getState() == READY_TO_LAUNCH)
			{
				turret.launchBall();
			}
			else if(turret.getState() == STOPPED)
			{
				turret.spinUpLauncher();
			}
			else //SPINNING_UP
			{
				//do nothing
			}
		}
		else if(turret.getState() != STOPPED)
		{
			turret.stopLauncher();
		}
	}

}
