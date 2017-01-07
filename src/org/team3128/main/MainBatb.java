package org.team3128.main;


import org.team3128.autonomous.defensecrossers.CmdGoAcrossLowBar;
import org.team3128.autonomous.defensecrossers.CmdGoAcrossRockWall;
import org.team3128.autonomous.defensecrossers.CmdGoAcrossRoughTerrain;
import org.team3128.common.NarwhalRobot;
import org.team3128.common.drive.SRXTankDrive;
import org.team3128.common.hardware.motor.MotorGroup;
import org.team3128.common.listener.ListenerManager;
import org.team3128.common.listener.controllers.ControllerExtreme3D;
import org.team3128.common.listener.controltypes.Button;
import org.team3128.common.listener.controltypes.POV;
import org.team3128.common.util.GenericSendableChooser;
import org.team3128.common.util.Log;
import org.team3128.common.util.units.Length;
import org.team3128.mechanisms.Intake;
import org.team3128.mechanisms.Turret;
import org.team3128.narwhalvision.NarwhalVisionReceiver;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 */
public class MainBatb extends NarwhalRobot
{
	// Drivetrain
	SRXTankDrive tankDrive;
	
	CANTalon drvLeft1, drvLeft2;	
	CANTalon drvRight1, drvRight2;
	
	PowerDistributionPanel powerDistPanel;
	
	// Intake
	Intake intake;
	
	VictorSP intakeSpin1, intakeSpin2;
	MotorGroup intakeMotors;
	
	MotorGroup intakeRaise;
	DigitalInput intakeDownLimSwitch;
	
	// Turret
	Turret turret;
	
	MotorGroup turretSpin;
	DigitalInput turretMaxHallSensor;
	
	CANTalon launcherWheel;
	Servo hoodServo;
	
	// Input Devices
	Joystick leftJoy, rightJoy;
	ListenerManager lmRight, lmLeft;
	
	//vision
	NarwhalVisionReceiver visionReceiver;
		
	@Override
	protected void constructHardware()
	{	
		Log.info("MainBatb", "Loading robot: 2016 Battle at the Border");
		
		drvLeft1 = new CANTalon(0);
		drvLeft2 = new CANTalon(1);
		drvRight1 = new CANTalon(2);
		drvRight2 = new CANTalon(3);

		launcherWheel = new CANTalon(4);
		
		intakeSpin1 = new VictorSP(8);
		intakeSpin2 = new VictorSP(9);
		
		intakeRaise = new MotorGroup(new VictorSP(5));
		turretSpin = new MotorGroup(new VictorSP(4));
		
		intakeDownLimSwitch = new DigitalInput(0);
		turretMaxHallSensor = new DigitalInput(1);
		
		hoodServo = new Servo(7);
		
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
		
		intake = new Intake(intakeMotors, intakeRaise, intakeDownLimSwitch);
		turret = new Turret(launcherWheel, turretSpin, intakeMotors, hoodServo, 0, turretMaxHallSensor);
		
		powerDistPanel = new PowerDistributionPanel();
				
		CameraServer camera = CameraServer.getInstance();
		camera.startAutomaticCapture(0).setResolution(480, 320);
		
		visionReceiver = new NarwhalVisionReceiver();
		
		Log.info("MainBatb", "Hardware Construction for 2016 Battle at the Border robot finished");
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
		
		lmLeft.nameControl(new Button(5), "RaiseIntake");
		lmLeft.nameControl(new Button(6), "LowerIntake");
		
		// ------------------------------------------------------------------
				
		lmRight.addMultiListener(()->
		{
			tankDrive.arcadeDrive(lmRight.getAxis("MoveTurn"), lmRight.getAxis("MoveForwards"), lmRight.getAxis("Throttle"), lmRight.getButton("FullSpeed"));
		}, "MoveTurn", "MoveForwards", "Throttle", "FullSpeed");
		
		lmRight.addListener("IntakePOV", intake::onPOVUpdate);
		
		// ------------------------------------------------------------------
		
		lmLeft.addButtonDownListener("Fire", () -> turret.launch());
		
		lmLeft.addListener("MoveHood", turret::changeHoodPositionBy);
		
		lmLeft.addButtonDownListener("RaiseIntake", () ->
		{
			intake.setIntake(1.0);
		});
		
		lmLeft.addButtonUpListener("RaiseIntake", () ->
		{
			intake.setIntake(0.0);
		});
		
		lmLeft.addButtonDownListener("LowerIntake", () ->
		{
			intake.setIntake(-1.0);
		});
		
		lmLeft.addButtonDownListener("LowerIntake", () ->
		{
			intake.setIntake(0.0);
		});
		
	}

	@Override
	protected void disabledInit()
	{
		//stop the wheels if they were running
		tankDrive.tankDrive(0, 0);
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
		autoChooser.addDefault("Low Bar", new CmdGoAcrossLowBar(this));
		// autoChooser.addObject("Portcullis", new CmdGoAcrossPortcullis(this));
		// autoChooser.addObject("Shovel Fries", new CmdGoAcrossShovelFries(this));
		// autoChooser.addObject("Moat", new CmdGoAcrossMoat(this));
		autoChooser.addObject("Rock Wall", new CmdGoAcrossRockWall(this));
		autoChooser.addObject("Rough Terrain", new CmdGoAcrossRoughTerrain(this));
		// autoChooser.addObject("Ramparts", new CmdGoAcrossRamparts(this));
		autoChooser.addObject("No Crossing", null);
	}

	@Override
	protected void updateDashboard()
	{
		SmartDashboard.putString("Turret State", turret.getState().toString());
		SmartDashboard.putNumber("Hood Angle", turret.getHoodAngle());
		SmartDashboard.putString("Turret Turning Direction", turret.getTurnDirection().toString());
		SmartDashboard.putNumber("PDP Current Output", powerDistPanel.getTotalCurrent());
		
		if(visionReceiver.getLastPacketReceivedTime() > 0)
		{
			SmartDashboard.putString("Vision Status", "Target seen " + ((System.currentTimeMillis() - visionReceiver.getLastPacketReceivedTime()) / 1000.0)
					+ " s ago, at " + visionReceiver.getMostRecentTarget().getHorizontalAngle() + " degrees from straight ahead");
		}
		else
		{
			SmartDashboard.putString("Vision Status", "No Target Seen");
		}
	}
	
	@Override
	protected void teleopPeriodic()
	{
		turret.changeHoodPositionBy(leftJoy.getY());
		turret.spinTurret(leftJoy.getZ());
	}

}
