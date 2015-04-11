//MAKE SURE THE ROBOT IS NOT IN TETST MODE BEFORE SWITCHING CODE

package com.frc869.robot.code2015;

import java.util.ArrayList;
import java.util.List;

import com.frc869.robot.code2015.autonomous.Autonomous;
import com.frc869.robot.code2015.autonomous.Corey;
import com.frc869.robot.code2015.autonomous.DirtyCorey;
import com.frc869.robot.code2015.autonomous.Landfill;
import com.frc869.robot.code2015.component.DigitalInput;
import com.frc869.robot.code2015.controllable.Lift;
import com.frc869.robot.code2015.controllable.Controllable;
import com.frc869.robot.code2015.controllable.Mecanum;
import com.frc869.robot.code2015.controllable.Tugger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * Code for the FRC869 2015 robot
 * 
 * The robot is capable of:
 * Moving via the Mecanum drive code
 * "Tugging" totes towards the robot
 * Lifting the totes and placing the totes in a stack
 */

public class Robot extends SampleRobot {
	private Joystick driverController, operatorController;
	private Mecanum mecanumDrive;
	private Tugger tuggers;
	private Lift lift;
	private DigitalInput toteHomeLeft, toteHomeRight, coreySwitch, dirtyCoreySwitch, landfillSwitch;
	private List<Controllable> controllable;

	// limit switch enable with tote contact disables when reaches 9000

	// Creates the object "Robot"
	protected void robotInit() {
		this.driverController = Controllable.Joysticks.DRIVER.getJoystick();
		this.operatorController = Controllable.Joysticks.OPERATOR.getJoystick();
		this.toteHomeLeft = new DigitalInput(Controllable.Digital.TOTE_HOME_LEFT.getValue(),false);
		this.toteHomeRight = new DigitalInput(Controllable.Digital.TOTE_HOME_RIGHT.getValue(),false);
		this.coreySwitch = new DigitalInput(Controllable.Digital.AUTO_COREY.getValue(),false);
		this.dirtyCoreySwitch = new DigitalInput(Controllable.Digital.AUTO_DIRTY_COREY.getValue(),false);
		this.landfillSwitch = new DigitalInput(Controllable.Digital.AUTO_LANDFILL.getValue(),false);
		this.mecanumDrive = Mecanum.getInstance();
		this.lift = Lift.getInstance();
		this.tuggers = Tugger.getInstance();
		this.controllable = new ArrayList<>();
		this.controllable.add(this.mecanumDrive);
		this.controllable.add(this.lift);
		this.controllable.add(this.tuggers);
	}
	
	protected void disabled() {
		for(Controllable component : this.controllable) {
			component.stop();
		}
    }

	public void autonomous() {
		this.mecanumDrive.enable();
//		boolean coreyButton = SmartDashboard.getBoolean("DB/Button 0", false);
//		boolean dirtyCoreyButton = SmartDashboard.getBoolean("DB/Button 1", false);
//		boolean landfillButton = SmartDashboard.getBoolean("DB/Button 2", false);
		Autonomous mode = null;
//		if (coreyButton) {
		if (coreySwitch.get()) {
			mode = new Corey();
//		} else if (dirtyCoreyButton) {
		} else if (dirtyCoreySwitch.get()) {
			mode = new DirtyCorey();
//		} else if (landfillButton) {
		} else if (landfillSwitch.get()) {
			mode = new Landfill();
		} else {
			mode = new Autonomous();
		}
		while (isAutonomous() && isEnabled()) {
			mode.run();
		}
		this.mecanumDrive.disable();
	}

	public void operatorControl() {
		this.mecanumDrive.enable();
		int liftStep = 0;
		while (isOperatorControl() && isEnabled()) {
			// reduce controller input by X% for drive
			double driveMultiplyer = .5;
			if (driverController.getRawButton(5)) {
				driveMultiplyer = .75;
			} else if (driverController.getRawButton(6)) {
				driveMultiplyer = 1;
			}
			this.mecanumDrive.drive(
					(this.driverController.getX() * driveMultiplyer),
					(this.driverController.getY() * driveMultiplyer),
					(this.driverController.getZ() * driveMultiplyer));
			if (this.operatorController.getRawButton(1)) {
				liftStep = 0;
				this.lift.setPosition(Lift.Position.TOTE2);
				controlTuggers();
			} else if (this.operatorController.getRawButton(2)) {
				liftStep = 0;
				this.lift.setPosition(Lift.Position.TOTE3);
				controlTuggers();
			} else if (this.operatorController.getRawButton(3)) {
				liftStep = 0;
				this.lift.setPosition(Lift.Position.PICKUP_BIN_STEP);
				controlTuggers();
			} else if (this.operatorController.getRawButton(4)) {
				liftStep = 0;
				this.lift.setPosition(Lift.Position.TOTE4);
				controlTuggers();
			} else if (this.operatorController.getRawButton(8) && this.operatorController.getRawButton(7)) {
				liftStep = 0;
				this.lift.calibrateDown(-.6);
				controlTuggers();
			} else if (this.operatorController.getRawButton(8)) {
				liftStep = 0;
				this.lift.calibrateDown(-.1);
			} else if (Math.abs(this.operatorController.getY()) >= .5) {
				liftStep = 0;
				this.lift.control(this.operatorController.getY());
				controlTuggers();
			} else if(liftStep==0) {
				lift.move(0);
				controlTuggers();
			} else {
				if (this.operatorController.getRawButton(5) && this.operatorController.getRawButton(6)
						&& !this.toteHomeLeft.get() && !this.toteHomeRight.get()
//						&& this.lift.lowerThan(LiftPosition.TOTE2)
						) {
					liftStep = 1;
				}
				switch(liftStep) {
					default:
						System.out.println("LIFT ROUTINE DISABLED");
						liftStep = 0;
					case 1:
						if (this.lift.setPosition(Lift.Position.TOTE1)) {
							liftStep++;
						}
					case 2:
						System.out.println("LIFT STEP1 DONE");
						if (this.lift.setPosition(Lift.Position.TOTE2)) {
							liftStep++;
						}
					case 3:
						System.out.println("LIFT STEP2 DONE");
						liftStep = 0;
				}
			}
		}
		this.mecanumDrive.disable();
	}
	
	public void controlTuggers() {
		if (this.operatorController.getRawAxis(2) >= (.3)) {
			this.tuggers.controlLeft(-1);
		} else if (this.operatorController.getRawButton(5)) {
			this.tuggers.controlLeft(1);
		} else {
			this.tuggers.controlLeft(0);
		}
		if (this.operatorController.getRawAxis(3) >= (.3)) {
			this.tuggers.controlRight(-1);
		} else if (this.operatorController.getRawButton(6)) {
			this.tuggers.controlRight(1);
		} else {
			this.tuggers.controlRight(0);
		}
	}

	public void test() {
		
	}

}
