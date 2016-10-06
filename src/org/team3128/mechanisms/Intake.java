package org.team3128.mechanisms;

import org.team3128.common.hardware.motor.MotorGroup;

public class Intake
{
	MotorGroup wheels;
	
	MotorGroup lifter;
	
	public Intake(MotorGroup wheels, MotorGroup lifter)
	{
		this.wheels = wheels;
		this.lifter = lifter;
	}
	
	

}
