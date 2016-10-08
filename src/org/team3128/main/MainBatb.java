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
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Turret;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 */
public class MainBatb extends NarwhalRobot
{
	CANTalon drvLeft1, drvLeft2;
	
	CANTalon drvRight1, drvRight2;
	
	CANTalon launcherWheel;

	VictorSP intakeSpin1, intakeSpin2;

	MotorGroup intakeRaise;
	
	MotorGroup intakeMotors;
	
	MotorGroup turretSpin;
	
	Servo hoodServo;
	
	Joystick leftJoy, rightJoy;
	ListenerManager lmRight, lmLeft;
	
	SRXTankDrive tankDrive;
	
	Turret turret;
	
	Intake intake;
	
	@Override
	protected void constructHardware()
	{	
		Log.info("MainBatb", "Loading robot: 2016 Battle at the Border");
		
		drvLeft1 = new CANTalon(0);
		drvLeft2 = new CANTalon(1);
		drvRight1 = new CANTalon(2);
		drvRight2 = new CANTalon(3);
		
		launcherWheel = new CANTalon(4);
		
		intakeSpin1 = new VictorSP(0);
		intakeSpin2 = new VictorSP(1);
		
		intakeRaise = new MotorGroup(new VictorSP(3));
		turretSpin = new MotorGroup(new VictorSP(4));
		
		hoodServo = new Servo(9);
		
		intakeMotors = new MotorGroup(intakeSpin1, intakeSpin2);
		
		leftJoy = new Joystick(0);
		rightJoy = new Joystick(1);
		
		lmRight = new ListenerManager(rightJoy);
		lmLeft = new ListenerManager(leftJoy);

		addListenerManager(lmRight);
		addListenerManager(lmLeft);
		
		tankDrive = new SRXTankDrive(drvLeft1, drvRight1, 6 * Length.in, 1, 20 * Length.in, 15 * Length.in);
		
		// configure the other tank drive talons as followers
		drvLeft2.changeControlMode(TalonControlMode.Follower);
		drvLeft2.set(drvLeft1.getDeviceID());
		
		drvRight2.changeControlMode(TalonControlMode.Follower);
		drvRight2.set(drvRight1.getDeviceID());
		
		intake = new Intake(intakeMotors, intakeRaise);
		turret = new Turret(launcherWheel, turretSpin, intakeMotors, hoodServo, 0);
		
		
	}
	
	@Override
	protected void setupListeners()
	{
		lmRight.nameControl(ControllerExtreme3D.TWIST, "MoveTurn");
		lmRight.nameControl(ControllerExtreme3D.JOYY, "MoveForwards");
		lmRight.nameControl(ControllerExtreme3D.THROTTLE, "Throttle");
		
		lmRight.nameControl(new POV(0),"IntakePOV");
		lmRight.nameControl(ControllerExtreme3D.TRIGGER, "FullSpeed");
		
		lmLeft.nameControl(ControllerExtreme3D.TWIST, "SpinTurret");
		lmLeft.nameControl(ControllerExtreme3D.JOYY, "MoveHood");
		
		lmLeft.nameControl(ControllerExtreme3D.TRIGGER, "Fire");
		
		// ------------------------------------------------------------------
				
		lmRight.addMultiListener(()->
		{
			tankDrive.arcadeDrive(lmRight.getAxis("MoveTurn"), lmRight.getAxis("MoveForwards"), lmRight.getAxis("Throttle"), lmRight.getButton("FullSpeed"));
		}, "MoveTurn", "MoveForwards", "Throttle", "FullSpeed");
		
		lmRight.addListener("IntakePOV", intake::onPOVUpdate);
		
		// ------------------------------------------------------------------
		
		lmLeft.addButtonDownListener("Fire", () -> turret.launch());
		
		lmLeft.addListener("SpinTurret", turret::spinTurret);
		
	}

	@Override
	protected void disabledInit()
	{
		Log.info("BatB", "Servo angle: " + hoodServo.getAngle());
		hoodServo.setAngle(45.0);
		Log.info("BatB", "Servo angle: " + hoodServo.getAngle());

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
		SmartDashboard.putString("Turret State", turret.getState().toString());
	}
	
	@Override
	protected void teleopPeriodic()
	{
		turret.changeHoodPositionBy(leftJoy.getY());
	}

}
