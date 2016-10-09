package org.team3128.autonomous.defensecrossers;

import org.team3128.common.util.Log;
import org.team3128.main.MainBatb;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CmdGoAcrossLowBar extends CommandGroup {
	/*
	    *        _
	    *       / \ 
	    *      / _ \
	    *     / | | \
	    *    /  |_|  \
	    *   /    _    \
	    *  /    (_)    \
	    * /_____________\
	    * -----------------------------------------------------
	    * UNTESTED CODE!
	    * This class has never been tried on an actual robot.
	    * It may be non or partially functional.
	    * Do not make any assumptions as to its behavior!
	    * And don't blink.  Not even for a second.
	    * -----------------------------------------------------*/
	 public CmdGoAcrossLowBar(MainBatb robot)
	 {
		 Log.info("CmdGoAcrossLowBar", "Going across Low Bar");
	 }
}
