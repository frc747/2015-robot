//MAKE SURE THE ROBOT IS NOT IN TETST MODE BEFORE SWITCHING CODE

package com.frc869.robot.code2015;

//Import necessary assets

import com.frc869.robot.code2015.drive.Mecanum869;
import com.frc869.robot.code2015.endefector.Lift;
import com.frc869.robot.code2015.endefector.LiftListener;
import com.frc869.robot.code2015.endefector.Tugger;
import com.frc869.robot.code2015.endefector.TuggerListener;
import com.frc869.robot.code2015.operator.Operator;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
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

	// Establishing all variables to be used in later code

	private CANTalon rightFront, rightBack, leftBack, leftFront;
	private CANTalon talonLeftTugger, talonRightTugger;
	private CANTalon talonLift;
	private Mecanum869 drive;
	private Joystick stick;
	private Joystick cont;
	private Gyro gyro;
	private Operator operator;

	private DigitalInput tLeftIn, tLeftOut, tRightIn, tRightOut, liftUp,
			liftDown;

	private Tugger tuggerLeft, tuggerRight;
	private Lift lift;

	// Creates the object "Robot"

	public Robot() {

		// Drive Motors
		this.rightFront = new CANTalon(1);
		this.rightBack = new CANTalon(2);
		this.leftBack = new CANTalon(3);
		this.leftFront = new CANTalon(4);

		// Custom Mechanum Drive
		this.drive = new Mecanum869(this.rightFront, this.rightBack,
				this.leftFront, this.leftBack);

		// joystick
		this.stick = new Joystick(0);
		this.cont = new Joystick(1);

		// gyro
		this.gyro = new Gyro(0);

		// digital inputs for the limit switches
		this.tLeftOut = new DigitalInput(0); // left tugger full out limit
												// switch
		this.tRightOut = new DigitalInput(1); // right tugger full out limit
												// switch
		this.tLeftIn = new DigitalInput(2); // left tugger full in limit switch
		this.tRightIn = new DigitalInput(3); // right tugger full in limit
												// switch
		this.liftUp = new DigitalInput(4); // lift full up limit switch
		this.liftDown = new DigitalInput(5); // lift full down limit switch

		// position lists for endefectors
		double[] tuggerPos = { 1, 1.5, 100 }; // order positions for the tuggers
		double[] liftPos = { 1, 1.5, 100 }; // orders positions for the lift

		// tugger constructors
		this.tuggerLeft = new Tugger(this.talonLeftTugger, this.tLeftIn,
				this.tLeftOut, tuggerPos, 1);
		this.tuggerRight = new Tugger(this.talonRightTugger, this.tRightIn,
				this.tRightOut, tuggerPos, -1);

		// lift constructor
		this.lift = new Lift(this.talonLift, this.liftDown, this.liftUp,
				liftPos);

		// operator control setup
		this.operator = new Operator(); // setup operator controller (socket
										// connection to tablet)
		this.operator.addEvent("TUGGERLEFT",
				new TuggerListener(this.tuggerLeft)); // add event for left
														// tugger
		this.operator.addEvent("TUGGERRIGHT", new TuggerListener(
				this.tuggerRight)); // add event for right tugger
		this.operator.addEvent("LIFT", new LiftListener(this.lift));
	}

	// Autonomous period

	public void autonomous() {
		super.autonomous();
	}

	// Code which speaks to operator control

	public void operatorControl() {
		this.drive.enable();
		this.gyro.reset();
		while (isOperatorControl() && isEnabled()) {
			
			
			//The following lines up to line 129 are the lines of code for the two different controllers.
			//The first one is for the joystick controller
			//The second one is for the gamepad controller      
			
			//Standard Joystick
			/*double y = this.stick.getY();
			double x = this.stick.getX();
			double z = this.stick.getZ();
			*/
			
			//Gamepad
			double x = this.cont.getX();
			double y = this.cont.getY();
			double z = this.cont.getTwist();
			
			
			double gyroAng = this.gyro.getAngle();

			if (Math.abs(x) < 0.1) {
				x = 0;
			}

			else {
				x -= 0.1 * x / Math.abs(x);
				x /= .9;
				//x *= x * x / Math.abs(x);
			}

			if (Math.abs(y) < 0.1) {
				y = 0;
			}

			else {
				y -= 0.1 * y / Math.abs(y);
				y /= .9;
				//y *= y * y / Math.abs(y);
			}

			if (Math.abs(z) < 0.1) {
				z = 0;
			}

			else {
				z -= 0.1 * z / Math.abs(z);
				z /= .9;
				//z *= z * z / Math.abs(z);
			}
			
			System.out.println("X:" + x + " Y:" + y + " R:" + z + " G:" + gyroAng);
			System.out.println("RF:" + this.rightFront.getPosition() + " RB:" + this.rightBack.getPosition());
			System.out.println("LF:" + this.leftFront.getPosition() + " LB:" + this.leftBack.getPosition());
 
			//this.drive.drive(x, y, z, gyroAng); //Disabled gyro for testing
			this.drive.drive(x, y, z, 0);
		}
		this.drive.disable();
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		/*
		 //encoder test
		this.drive.enable();
		while (isTest() && isEnabled()) {
			double x = this.stick.getThrottle();
			System.out.println(x);
			this.leftBack.set(x);
			this.rightBack.set(x);
			this.leftFront.set(x);
			this.rightFront.set(x);
			System.out.println("\nTotal:");
			System.out.println("RF:" + this.rightFront.getPosition() + " RB:" + this.rightBack.getPosition());
			System.out.println("LF:" + this.leftFront.getPosition() + " LB:" + this.leftBack.getPosition());
			System.out.println("Speed:");
			System.out.println("RF:" + this.rightFront.getSpeed() + " RB:" + this.rightBack.getSpeed());
			System.out.println("LF:" + this.leftFront.getSpeed() + " LB:" + this.leftBack.getSpeed());
		}
		this.drive.disable();
		
		//*/
		double sense = 0.005;
		this.gyro.reset();
		this.gyro.setSensitivity(sense);
		while (isTest() && isEnabled()) {
			System.out.println(this.gyro.getAngle() + "CALIB: "+ sense);
			
			if(this.cont.getRawButton(1)){
				sense += 0.001;
				this.gyro.setSensitivity(sense);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		
	}
}
