//MAKE SURE THE ROBOT IS NOT IN TETST MODE BEFORE SWITCHING CODE

package com.frc869.robot.code2015;

//Import necessary assetss

import com.frc869.robot.code2015.drive.Mecanum869;
import com.frc869.robot.code2015.endefector.CleanLift;
import com.frc869.robot.code2015.endefector.Lift;
import com.frc869.robot.code2015.endefector.Lift2;
import com.frc869.robot.code2015.endefector.LiftListener;
import com.frc869.robot.code2015.endefector.Tugger;
import com.frc869.robot.code2015.endefector.TuggerListener;
import com.frc869.robot.code2015.operator.Operator;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
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

	//private final int LiftMax = 4400; // Pre Bag value
	private final int LiftMax = 28000; //Backup Robot value
	private final int LiftMin = -150;
	// Establishing all variables to be used in later code

	private CANTalon rightFront, rightBack, leftBack, leftFront;
	private CANTalon talonLeftTugger, talonRightTugger;
	private CANTalon talonLift1, talonLift2;
	private Mecanum869 drive;
	//private Joystick stick;
	private Joystick cont, oper;
	private Gyro gyro;
	private Operator operator;
	private Encoder encoder;

	private DigitalInput tLeftIn, tLeftOut, tRightIn, tRightOut, liftUp, liftDown;

	private Tugger tuggerLeft, tuggerRight;
	private CleanLift lift;

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

		// Custom Mecanum Drive
		this.drive = new Mecanum869(this.rightFront, this.rightBack,
				this.leftFront, this.leftBack);

		// joystick
	//	this.stick = new Joystick(0);
		this.cont = new Joystick(1);
		this.oper = new Joystick(2);

		// gyro
		this.gyro = new Gyro(0);

		this.encoder = new Encoder(0, 1, false, EncodingType.k1X); // Encoder for lift
		System.out.println("ES: " + this.encoder.getEncodingScale());

		// digital inputs for the limit switches
		this.liftUp = new DigitalInput(3); // lift full up limit switch
		this.liftDown = new DigitalInput(2); // lift full down limit switch

		this.tLeftOut = new DigitalInput(4); // left tugger full out limit
												// switch
		this.tRightOut = new DigitalInput(5); // right tugger full out limit
												// switch
		this.tLeftIn = new DigitalInput(6); // left tugger full in limit switch
		this.tRightIn = new DigitalInput(7); // right tugger full in limit
												// switch
		double[] liftPos = { 1, 100 }; // orders positions for the lift
		
		CANTalon[] liftTalons = {talonLift1, talonLift2};

		this.lift = new CleanLift(talonLift1, talonLift2, encoder, liftDown, liftUp, liftPos, LiftMax, LiftMax);
	}

	// Autonomous period

	public void autonomous() {
		this.rightFront.setPosition(0);
		this.rightBack.setPosition(0);
		this.leftFront.setPosition(0);
		this.leftBack.setPosition(0);
		this.drive.enable();
		while(this.rightFront.getPosition() < 1080)
			this.drive.drive(1, 0, 0, 0);
		while(this.rightFront.getPosition() < 2160)
			this.drive.drive(0, 1, 0, 0);
		while(this.rightFront.getPosition() > 1080)
			this.drive.drive(-1, 0, 0, 0);
		while(this.rightFront.getPosition() > 0){
			System.out.println(this.rightFront.getPosition());
			this.drive.drive(0, -1, 0, 0);
		}
		System.out.println("Done!");
		this.drive.drive(0, 0, 0, 0);
		this.drive.disable();
		
	}

	// Code which speaks to operator control

	public void operatorControl() {
		this.drive.enable();
		this.gyro.reset();
		while (isOperatorControl() && isEnabled()) {

			/*
			 * The following lines up to line 129 are the lines of code for the
			 * two different controllers. //The first one is for the joystick
			 * controller The second one is for the gamepad controller
			 */
			// Standard Joystick
			/*
			 * double y = this.stick.getY(); double x = this.stick.getX();
			 * double z = this.stick.getZ();
			 */

			// Gamepad
			//double x = this.cont.getRawAxis(4);
			//double y = this.cont.getY();
			//double z = this.cont.getRawAxis(3) - this.cont.getRawAxis(2);
			
			double x = this.cont.getX();
			double y = this.cont.getY();
			double z = this.cont.getZ();
			
			if(this.cont.getRawButton(6)){
				if(Math.abs(x) > Math.abs(y)){
					y = 0;
				}else if(Math.abs(x) < Math.abs(y)){
					x = 0;
				}
			}

			double gyroAng = this.gyro.getAngle();

			if (Math.abs(x) < 0.1) {
				x = 0;
			}

			else {
				x -= 0.1 * x / Math.abs(x);
				x /= .9;
				// x *= x * x / Math.abs(x);
			}

			if (Math.abs(y) < 0.1) {
				y = 0;
			}

			else {
				y -= 0.1 * y / Math.abs(y);
				y /= .9;
				// y *= y * y / Math.abs(y);
			}

			if (Math.abs(z) < 0.1) {
				z = 0;
			}

			else {
				z -= 0.1 * z / Math.abs(z);
				z /= .9;
				// z *= z * z / Math.abs(z);
			}
			
			//reduce controller input by X% for drive
			this.drive.drive((x*.65), (y*.65), (z*.65), 0);
			
			if(this.oper.getRawButton(1)){
				System.out.println(this.encoder.get());
			}else if(this.oper.getRawButton(3)){
				this.encoder.reset();
				System.out.println("Clear!");
			}
			
			if(this.oper.getRawButton(6)) {
				this.lift.setPosition(CleanLift.Position.TOTE1);
			} else if(this.oper.getRawButton(7)) {
				this.lift.setPosition(CleanLift.Position.TOTE2);
			} else if(this.oper.getRawButton(8)) {
				this.lift.setPosition(CleanLift.Position.TOTE3);
			} else if(this.oper.getRawButton(9)) {
				this.lift.setPosition(CleanLift.Position.TOTE4);
			} else if (this.oper.getRawButton(4)){
				this.lift.move(.25);
			} else if (this.oper.getRawButton(1)){
				this.lift.move(-.25);
			} else {
				this.lift.move(0);
			}
			
		}
		this.drive.disable();
	}
	
	public void test(){
		while(this.isEnabled()){
			System.out.println(this.cont.getRawAxis(3) - this.cont.getRawAxis(2));
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
