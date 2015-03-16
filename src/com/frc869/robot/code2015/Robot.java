//MAKE SURE THE ROBOT IS NOT IN TETST MODE BEFORE SWITCHING CODE

package com.frc869.robot.code2015;

//IMPORT 869 CLASSES FOR USE
import com.frc869.robot.code2015.drive.Mecanum869;
import com.frc869.robot.code2015.endefector.CleanLift;
import com.frc869.robot.code2015.endefector.Tugger;

//IMPORT FRC CLASSES FOR USE
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.SampleRobot;

/*
 * Code for the FRC869 2015 robot
 * 
 * The robot is capable of:
 * Moving via the Mecanum drive code
 * "Tugging" totes towards the robot
 * Lifting the totes and placing the totes in a stack
 */

public class Robot extends SampleRobot {

	// private final int LiftMax = 4400; // Pre Bag value
	private final int LiftMax = 28000; // Backup Robot value
	private final int LiftMin = -150;
	// Establishing all variables to be used in later code

	// Setup Talon variables
	private CANTalon rightFront, rightBack, leftBack, leftFront;
	private CANTalon talonLeftTugger, talonRightTugger;
	private CANTalon talonLift1, talonLift2;
	private Mecanum869 mecanumDrive;
	private Joystick driverController, operatorController;
	private Gyro gyro;
	private Encoder liftEncoder, tuggerLeftEncoder, tuggerRightEncoder;
	private DigitalInput tugLeftLim, tugRightLim, liftLimUp, liftLimDown,
			presetLim;
	private Tugger tuggers;
	private CleanLift lift;
	private boolean move1Done, preset, presetSwitch;

	// limit switch enable with tote contact disables when reaches 9000

	// Creates the object "Robot"
	public Robot() {

		// Drive Motors
		this.rightFront = new CANTalon(3);
		this.rightBack = new CANTalon(2);
		this.leftBack = new CANTalon(7);
		this.leftFront = new CANTalon(6);

		this.talonLeftTugger = new CANTalon(8);
		this.talonRightTugger = new CANTalon(1);
		this.talonLift1 = new CANTalon(4);
		this.talonLift2 = new CANTalon(5);

		this.talonRightTugger.setPosition(0);
		this.talonLeftTugger.setPosition(0);

		// Custom Mecanum Drive
		this.mecanumDrive = new Mecanum869(this.rightFront, this.rightBack,
				this.leftFront, this.leftBack);

		// joystick
		this.driverController = new Joystick(1);
		this.operatorController = new Joystick(2);

		// gyro
		this.gyro = new Gyro(0);

		this.liftEncoder = new Encoder(0, 1, false, EncodingType.k1X); // Encoder
																		// for
																		// lift
		this.liftEncoder.reset();
		System.out.println("ES: " + this.liftEncoder.getEncodingScale());

		// digital inputs for the limit switches
		this.liftLimUp = new DigitalInput(3);
		this.liftLimDown = new DigitalInput(2);
		this.tugLeftLim = new DigitalInput(4);
		this.tugRightLim = new DigitalInput(5);
		this.presetLim = new DigitalInput(6);

		CANTalon[] liftTalons = { talonLift1, talonLift2 };

		this.lift = new CleanLift(talonLift1, talonLift2, liftEncoder,
				liftLimDown, liftLimUp, LiftMax, LiftMax);
		this.tuggers = new Tugger(talonLeftTugger, talonRightTugger,
				tugLeftLim, tugRightLim);

	}

	// *********************************************************************
	// ***** Autonomous code begins *******
	// ***** *******
	// ***** *******
	// *********************************************************************

	public void autonomous() {

		this.rightFront.setPosition(0);
		this.rightBack.setPosition(0);
		this.leftFront.setPosition(0);
		this.leftBack.setPosition(0);
		this.mecanumDrive.enable();
		this.move1Done = false;
//<<<<<<< HEAD
//
//		while (isAutonomous() && isEnabled()) {
//			// Autonomous picks up garbage can and moves backwards
//			if (this.rightFront.getPosition() > -100 && !this.move1Done) {
//				this.mecanumDrive.drive(0, -.15, 0, 0);
//				if (this.rightFront.getPosition() <= -100) {
//					this.move1Done = true;
//				}
//			} else if (this.liftEncoder.get() < 10500) {
//				this.lift.move(.7);
//				this.mecanumDrive.drive(0, 0, 0, 0);
//			} else if (this.rightFront.getPosition() < (1000 * 4)
//					&& this.liftEncoder.get() >= 10500) {
//=======
		
		
		while (isAutonomous() && isEnabled()) {

			if (this.rightFront.getPosition() > -100 && !this.move1Done) {
				this.mecanumDrive.drive(0, -.15, 0, 0);
				if (this.rightFront.getPosition() <= -100) {
					this.move1Done = true;
				}
			} else if (this.liftEncoder.get() < 10500) {
				this.lift.move(.7);
				this.mecanumDrive.drive(0, 0, 0, 0);
			} else if (this.rightFront.getPosition() < (1000 * 4)
					&& this.liftEncoder.get() >= 10500) {
				this.lift.move(0);
				this.mecanumDrive.drive(0, .5, 0, 0);
			} else {
				this.lift.move(0);
				this.mecanumDrive.drive(0, 0, 0, 0);
			} // end Autonomous pick up garbage can and move backwards

		}


		this.mecanumDrive.disable();

	}

	// *********************************************************************
	// ***** Autonomous code ends *******
	// *********************************************************************

	// Code which speaks to operator control

	public void operatorControl() {
		this.mecanumDrive.enable();
		this.gyro.reset();
		this.preset = false;
		while (isOperatorControl() && isEnabled()) {

			// Get variables from Driver Controller joysticks. Will be used to
			// set speed for mecanum drive.
			double x = this.driverController.getX();
			double y = this.driverController.getY();
			double z = this.driverController.getZ();

			double lift1Position = 1000;
			double lift2Position = 2000;
			double lift3Position = 3000;
			double lift4Position = 4000;
			double speed = 0;
			double driveMultiplyer;

			//

			// reduce controller input by X% for drive
			if (driverController.getRawButton(5)) {
				driveMultiplyer = .75;
			} else if (driverController.getRawButton(6)) {
				driveMultiplyer = 1;
			} else {
				driveMultiplyer = .5;
			}

			this.mecanumDrive.drive((x * driveMultiplyer),
					(y * driveMultiplyer), (z * driveMultiplyer), 0);

			// if(this.operatorController.getRawButton(1)){
			// System.out.println(this.liftEncoder.get());
			// }else if(this.operatorController.getRawButton(3)){
			// this.liftEncoder.reset();
			// System.out.println("Clear!");
			// }

			// if(this.operatorController.getRawButton(10)) {
			// this.lift.calibrateDown(liftLimDown, liftEncoder);
			// } else if(this.operatorController.getRawButton(6)) {
			// this.lift.setPosition(lift1Position, liftEncoder);
			// } else if(this.operatorController.getRawButton(7)) {
			// this.lift.setPosition(lift2Position, liftEncoder);
			// } else if(this.operatorController.getRawButton(8)) {
			// this.lift.setPosition(lift3Position, liftEncoder);
			// } else if(this.operatorController.getRawButton(9)) {
			// this.lift.setPosition(lift4Position, liftEncoder);
			// } else if (this.operatorController.getRawButton(4)){
			// this.lift.move(.25);
			// } else if (this.operatorController.getRawButton(1)){
			// this.lift.move(-.1);
			// } else {
			// this.lift.move(0);
			// }

			// if (this.operatorController.getRawButton(7)){
			// System.out.println(this.liftEncoder.get());
			// }

			String output1;
			String output2;
			Double left = this.talonLeftTugger.getPosition();
			Double right = this.talonRightTugger.getPosition();

			output1 = String.valueOf(left);
			output2 = String.valueOf(right);

			// if (this.operatorController.getRawButton(9)){
			// System.out.println(output1 + "left");
			// System.out.println(output2 + "right");
			// }

			// COMPETITION ROBOT VALUES
			if (this.operatorController.getRawButton(10)
					&& this.operatorController.getRawButton(9)) {
				speed = (-.6);
				this.lift.calibrateDown(liftLimDown, liftEncoder, speed);
			} else if (this.operatorController.getRawButton(10)) {
				speed = (-.1);
				this.lift.calibrateDown(liftLimDown, liftEncoder, speed);
			} else if (this.operatorController.getRawButton(4)) {
				if (this.liftEncoder.get() < 28500) {
					this.lift.move(.70);
				} else if (this.liftEncoder.get() < 28750
						&& this.liftEncoder.get() > 28500) {
					this.lift.move(.25);
				} else if (this.liftEncoder.get() < 29000) {
					this.lift.move(.1);
				} else {
					this.lift.move(0);
				}
			} else if (this.operatorController.getRawButton(2)) {
				if (this.liftEncoder.get() > 500) {
					this.lift.move(-.70);
				} else if (this.liftEncoder.get() < 500
						&& this.liftEncoder.get() > 250) {
					this.lift.move(-.25);
				} else if (this.liftEncoder.get() > 0) {
					this.lift.move(-.1);
				} else {
					this.lift.move(0);
				}
			} else {
				this.lift.move(0);
			}

			// PRACTICE ROBOT VALUES
			// if (this.operatorController.getRawButton(12)) {
			// this.lift.calibrateDown(liftLimDown, liftEncoder);
			// }if (this.operatorController.getRawButton(4)) {
			// if (this.liftEncoder.get() < 27250) {
			// this.lift.move(.70);
			// } else if (this.liftEncoder.get() < 28500
			// && this.liftEncoder.get() > 27250) {
			// this.lift.move(.25);
			// } else if (this.liftEncoder.get() < 29250) {
			// this.lift.move(.1);
			// } else {
			// this.lift.move(0);
			// }
			// } else if (this.operatorController.getRawButton(2)) {
			// if (this.liftEncoder.get() > 2000) {
			// this.lift.move(-.70);
			// } else if (this.liftEncoder.get() < 2000
			// && this.liftEncoder.get() > 750) {
			// this.lift.move(-.25);
			// } else if (this.liftEncoder.get() > 0) {
			// this.lift.move(-.1);
			// } else {
			// this.lift.move(0);
			// }
			// } else {
			// this.lift.move(0);
			// }

			// if (this.operatorController.getRawButton(6)){
			// System.out.println(this.talonLeftTugger.getPosition());
			// System.out.println(this.talonRightTugger.getPosition());
			// } else if (this.operatorController.getRawButton(8)){
			// this.talonLeftTugger.setPosition(0);
			// this.talonRightTugger.setPosition(0);
			// }

			// TUGGER MOVING CODE

			if (this.operatorController.getRawButton(11)) {
				this.tuggers.calibrate(tugLeftLim, tugRightLim);

			} else if (this.operatorController.getRawButton(7)) {
				if (this.talonLeftTugger.getPosition() < 19812) {
					this.tuggers.moveL(-1);
				} else if (this.talonLeftTugger.getPosition() < 20312
						&& this.talonLeftTugger.getPosition() > 19812) {
					this.tuggers.moveL(-.5);
				} else if (this.talonLeftTugger.getPosition() < 21812) {
					this.tuggers.moveL(-.25);
				} else {
					this.tuggers.moveL(0);
				}
			} else if (this.operatorController.getRawButton(5)) {
				if (this.talonLeftTugger.getPosition() > 2000) {
					this.tuggers.moveL(1);
				} else if (this.talonLeftTugger.getPosition() < 2000
						&& this.talonLeftTugger.getPosition() > 500) {
					this.tuggers.moveL(.5);
				} else if (this.talonLeftTugger.getPosition() > 0) {
					this.tuggers.moveL(.25);
				} else {
					this.tuggers.moveL(0);
				}

			} else {
				this.tuggers.moveL(0);
			}

			// MOVE RIGHT TUGGER

			if (this.operatorController.getRawButton(8)) {
				if (this.talonRightTugger.getPosition() < 19812) {
					this.tuggers.moveR(-1);
				} else if (this.talonRightTugger.getPosition() < 20312
						&& this.talonRightTugger.getPosition() > 19812) {
					this.tuggers.moveR(-.5);
				} else if (this.talonRightTugger.getPosition() < 21812) {
					this.tuggers.moveR(-.25);
				} else {
					this.tuggers.moveR(0);
				}
			} else if (this.operatorController.getRawButton(6)) {
				if (this.talonRightTugger.getPosition() > 2000) {
					this.tuggers.moveR(1);
				} else if (this.talonRightTugger.getPosition() < 2000
						&& this.talonRightTugger.getPosition() > 500) {
					this.tuggers.moveR(.5);
				} else if (this.talonRightTugger.getPosition() > 0) {
					this.tuggers.moveR(.25);
				} else {
					this.tuggers.moveR(0);
				}

			} else {
				this.tuggers.moveR(0);
			}

			// Preset code by Kevin

			
			if (this.operatorController.getRawButton(3)) {
				if (!this.presetLim.get()) {
					this.presetSwitch = true;
				}
				if (this.presetSwitch && this.liftEncoder.get() < 9000
						&& !this.preset) {
					this.lift.move(.7);
				}
				if (this.presetLim.get() && this.liftEncoder.get() >= 9000) {
					this.preset = true;
					this.presetSwitch = false;
				}
				if (!this.presetLim.get() && this.liftEncoder.get() > 0
						&& this.preset) {
					this.lift.move(-.2);
				}
				if (!this.presetLim.get() && this.liftEncoder.get() <= 0) {
					this.preset = false;
				}
			}
			// preset ends
			this.mecanumDrive.disable();
		}
	}

	private boolean getRawbutton(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public void test() {

	}

}
