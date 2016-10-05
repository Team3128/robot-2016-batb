package org.team3128.main;


import org.team3128.common.NarwhalRobot;
import org.team3128.common.util.GenericSendableChooser;
import org.team3128.common.util.Log;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 */
public abstract class MainBatb extends NarwhalRobot
{
	
	@Override
	protected void constructHardware()
	{	
		Log.info("MainBatb", "Loading robot: 2016 Battle at the Border");
	}
	
	@Override
	protected void setupListeners()
	{
		
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

}
