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
	private DigitalInput tugLeftLim, tugRightLim, liftLimUp, liftLimDown, toteHomeLeft, toteHomeRight;
	private Tugger tuggers;
	private CleanLift lift;
	private boolean move1Done;

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
//		this.liftLimUp = new DigitalInput(3);
		this.liftLimDown = new DigitalInput(2);
//		this.tugLeftLim = new DigitalInput(4);
//		this.tugRightLim = new DigitalInput(5);
		this.toteHomeLeft = new DigitalInput(3);
		this.toteHomeRight = new DigitalInput(4);	

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
//		this.preset = false;
		boolean liftRoutine = false;
		boolean liftStep1 = false;
		boolean liftStep2 = false;
		boolean disableTugger = false;
		
		
		while (isOperatorControl() && isEnabled()) {

			// Get variables from Driver Controller joysticks. Will be used to
			// set speed for mecanum drive.
			double x = this.driverController.getX();
			double y = this.driverController.getY();
			double z = this.driverController.getZ();
			
			
			//TUGGER/LIFT SOFT LIMIT VALUES
			double liftSlowDownPosi1 = 300;
			double liftSlowDownPosi2 = 150;
			double tugSlowDownPosi1 = 1000;
			double tugSlowDownPosi2 = 500;
			double tugSlowDownPosi3 = 100;
			
			double tugDistanceOutL = 20812;
			double tugDistanceOutR = 20812;
			
			//PRACTICE ROBOT LIFT VALUES
//			double liftDistanceUp = 29250;					
			
			//COMPETITION ROBOT LIFT VALUES
			double liftDistanceUp = 29000;
			
			double liftPosition1 = 0;
			double liftPosition2 = 10863;
			double liftPosition3 = 7897;
			double liftPosition4 = 22416;
			
			double speed = 0;
			double driveMultiplyer;
			
			if (liftRoutine){
				System.out.println("LIFT ROUTINE ENABLED");
				if (liftStep1){
					System.out.println("LIFT STEP1 DONE");
				}
				if (liftStep2){
					System.out.println("LIFT STEP2 DONE");
				}
			} else if (!liftRoutine){
				System.out.println("LIFT ROUTINE DISABLED");
			}
			


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



			String output1;
			String output2;
			Double left = this.talonLeftTugger.getPosition();
			Double right = this.talonRightTugger.getPosition();

			output1 = String.valueOf(left);
			output2 = String.valueOf(right);

			
			
			
//			if ( this.operatorController.getRawButton(1)){
//				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
//				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
//			} else if ( this.operatorController.getRawButton(2)){
//				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
//				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
//			} else {
//				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
//				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
//			}
//			
			
//		///////////////////////////START HERE MOFO///////////////////////////
			
			if (this.operatorController.getRawButton(1)){
				//position 2
				this.lift.setPosition(liftPosition2, liftEncoder, liftSlowDownPosi1, liftSlowDownPosi2);
				liftRoutine = false;
//				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
			} else if (this.operatorController.getRawButton(2)){
				//position 3
				liftRoutine = false;
				this.lift.setPosition(liftPosition3, liftEncoder, liftSlowDownPosi1, liftSlowDownPosi2);
			} else if (this.operatorController.getRawButton(4)){
				//position 4
				liftRoutine = false;
				this.lift.setPosition(liftPosition4, liftEncoder, liftSlowDownPosi1, liftSlowDownPosi2);
			} else if (this.operatorController.getRawButton(8)
					&& this.operatorController.getRawButton(7)) {
				speed = (-.6);
				liftRoutine = false;
				this.lift.calibrateDown(liftLimDown, liftEncoder, speed);
			} else if (this.operatorController.getRawButton(8)) {
				liftRoutine = false;
				speed = (-.1);
				this.lift.calibrateDown(liftLimDown, liftEncoder, speed);
			} else if (this.operatorController.getY() <= -.5) {
				liftRoutine = false;
				System.out.println("lift moved up");
				if (this.liftEncoder.get() < (liftDistanceUp - liftSlowDownPosi1)) {
					this.lift.move(.70);
				} else if (this.liftEncoder.get() < (liftDistanceUp - liftSlowDownPosi2)
						&& this.liftEncoder.get() > (liftDistanceUp - liftSlowDownPosi1)) {
					this.lift.move(.25);
				} else if (this.liftEncoder.get() < liftDistanceUp) {
					this.lift.move(.1);
				} else {
					this.lift.move(0);
				}
			} else if (this.operatorController.getY() >= .5) {
				liftRoutine = false;
				System.out.println("Lift moved down");
				if (this.liftEncoder.get() > liftSlowDownPosi1) {
					this.lift.move(-.70);
				} else if (this.liftEncoder.get() < liftSlowDownPosi1
						&& this.liftEncoder.get() > liftSlowDownPosi2) {
					this.lift.move(-.25);
				} else if (this.liftEncoder.get() > 0) {
					this.lift.move(-.1);
				} else {
					this.lift.move(0);
				}
			} else if (liftRoutine) {

				if (!liftStep1) {
					if (this.liftEncoder.get() <= 0) {
						liftStep1 = true;
						lift.move(0);
					} else {
						this.lift.setPosition(liftPosition1, liftEncoder,
								liftSlowDownPosi1, liftSlowDownPosi2);
					}
				} else if (!liftStep2 && liftStep1) {
					if ((this.liftEncoder.get() < liftPosition2 + 100 && this.liftEncoder
							.get() > liftPosition2 - 100)) {
						liftStep2 = true;
						lift.move(0);
					} else {
						this.lift.setPosition(liftPosition2, liftEncoder,
								liftSlowDownPosi1, liftSlowDownPosi2);
					}
				} else if (liftStep1 && liftStep2) {
					liftRoutine = false;
					liftStep1 = false;
					liftStep2 = false;
				}

			} else {
				liftRoutine = false;
				lift.move(0);
			}
			
			if (!liftRoutine){
				liftStep1 = false;
				liftStep2 = false;
				disableTugger = false;
			}
			
////////////////////////end here nmofo////////
			
			//TUGGER DISABLEMENT
			
			if (liftRoutine && liftStep1 && this.liftEncoder.get() >= 2000 ){
				disableTugger = false;
			} else if (liftRoutine && !liftStep1){
				disableTugger = true;
			} else {
				disableTugger = false;
			}
			

			// TUGGER MOVING CODE

			if (this.operatorController.getRawButton(9)){
				System.out.println(this.liftEncoder.get());
				System.out.println("LIFT HEIGHT");
			}
			
			if ((this.operatorController.getRawButton(5))
					&& (this.operatorController.getRawButton(6))
					&& (!this.toteHomeLeft.get())
					&& (!this.toteHomeRight.get()) && (!liftRoutine)
					&& (this.liftEncoder.get() < liftPosition2 + 1000)) {
				liftRoutine = true;
				liftStep1 = false;
				liftStep2 = false;
			} else if (this.operatorController.getRawAxis(2) >= (.3)
					&& !disableTugger) {

				if (this.talonLeftTugger.getPosition() >= tugDistanceOutL) {
					this.tuggers.moveL(0);
				} else if (this.talonLeftTugger.getPosition() < (tugDistanceOutL - tugSlowDownPosi1)) {
					this.tuggers.moveL(-1);
				} else if (this.talonLeftTugger.getPosition() < (tugDistanceOutL - tugSlowDownPosi2)
						&& this.talonLeftTugger.getPosition() > (tugDistanceOutL - tugSlowDownPosi1)) {
					this.tuggers.moveL(-.5);
				} else if (this.talonLeftTugger.getPosition() < tugDistanceOutL) {
					this.tuggers.moveL(-.25);
				} else {
					this.tuggers.moveL(0);
				}
			} else if (this.operatorController.getRawButton(5) && !disableTugger) {

				if (this.talonLeftTugger.getPosition() <= 0) {
					this.tuggers.moveL(0);
				} else if (this.talonLeftTugger.getPosition() > (tugDistanceOutL - tugSlowDownPosi3)
						&& this.talonLeftTugger.getPosition() < tugDistanceOutL) {
					this.tuggers.moveL(.25);
					System.out.println("5stop1");
				} else if (this.talonLeftTugger.getPosition() > (tugSlowDownPosi1)) {
					this.tuggers.moveL(.7);
					System.out.println("5stop2");
				} else if (this.talonLeftTugger.getPosition() < (tugSlowDownPosi2)) {
					this.tuggers.moveL(.5);
					System.out.println("5stop3");
				} else if (this.talonLeftTugger.getPosition() < tugSlowDownPosi1
						&& this.talonLeftTugger.getPosition() > tugSlowDownPosi2) {
					this.tuggers.moveL(.5);
					System.out.println("5stop4");
				} else if (this.talonLeftTugger.getPosition() > 0) {
					this.tuggers.moveL(.25);
					System.out.println("5stop5");
				} else {
					this.tuggers.moveL(0);
				}
			} else {
				this.tuggers.moveL(0);
			}

			// MOVE RIGHT TUGGER

			if (this.operatorController.getRawAxis(3) >= (.3) && !disableTugger) {
				
				if (this.talonRightTugger.getPosition() >= tugDistanceOutR) {
					this.tuggers.moveR(0);
				} else if (this.talonRightTugger.getPosition() < (tugDistanceOutR - tugSlowDownPosi1)) {
					this.tuggers.moveR(-1);
				} else if (this.talonRightTugger.getPosition() < (tugDistanceOutR - tugSlowDownPosi2)
						&& this.talonRightTugger.getPosition() > (tugDistanceOutR - tugSlowDownPosi1)) {
					this.tuggers.moveR(-.5);
				} else if (this.talonRightTugger.getPosition() < tugDistanceOutR) {
					this.tuggers.moveR(-.25);
				} else {
					this.tuggers.moveR(0);
				}
			} else if (this.operatorController.getRawButton(6) && !disableTugger) {
				if (this.talonRightTugger.getPosition() <= 0) {
					this.tuggers.moveR(0);
				} else if (this.talonRightTugger.getPosition() > (tugDistanceOutR - tugSlowDownPosi3)
						&& this.talonRightTugger.getPosition() < tugDistanceOutR) {
					this.tuggers.moveR(.25);
				} else if (this.talonRightTugger.getPosition() > (tugSlowDownPosi1)) {
					this.tuggers.moveR(.7);
				}  else if (this.talonRightTugger.getPosition() < (tugSlowDownPosi2)) {
					this.tuggers.moveR(.5);
				}  else if (this.talonRightTugger.getPosition() < tugSlowDownPosi1
						&& this.talonRightTugger.getPosition() > tugSlowDownPosi2) {
					this.tuggers.moveR(.5);
				} else if (this.talonRightTugger.getPosition() > 0) {
					this.tuggers.moveR(.25);
				} else {
					this.tuggers.moveR(0);
				}

			} else {
				this.tuggers.moveR(0);
			}


			
			
			
			
			
			
			

		}
		
		this.mecanumDrive.disable();
	}
	
	public void rumbleController (float rumbleSpeed){
		this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, rumbleSpeed);
		this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, rumbleSpeed);
		
	}

	private boolean getRawbutton(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public void test() {

	}

}
