package com.frc869.robot.code2015;

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

public class Robot extends SampleRobot {
	private CANTalon rightFront, rightBack, leftBack, leftFront;
	private CANTalon talonLeftTugger, talonRightTugger;
	private CANTalon talonLift;
	private Mecanum869 drive;
	private Joystick stick;
	private Gyro gyro;
	private Operator operator;

	private DigitalInput tLeftIn, tLeftOut, tRightIn, tRightOut, liftUp, liftDown;

	private Tugger tuggerLeft, tuggerRight;
	private Lift lift;

	public Robot() {

		// Drive Motors
		this.rightFront = new CANTalon(1);
		this.rightBack = new CANTalon(2);
		this.leftBack = new CANTalon(3);
		this.leftFront = new CANTalon(4);

		// Custom Mechanum Drive
		this.drive = new Mecanum869(this.rightFront, this.rightBack, this.leftFront, this.leftBack);

		// joystick
		this.stick = new Joystick(0);

		// gyro
		this.gyro = new Gyro(0);

		// digital inputs for the limit switches
		this.tLeftOut = new DigitalInput(0); 	// left tugger full out limit switch
		this.tRightOut = new DigitalInput(1);	// right tugger full out limit switch
		this.tLeftIn = new DigitalInput(2); 	// left tugger full in limit switch
		this.tRightIn = new DigitalInput(3); 	// right tugger full in limit switch
		this.liftUp = new DigitalInput(4); 		// lift full up limit switch
		this.liftDown = new DigitalInput(5); 	// lift full down limit switch

		// position lists for endefectors
		double[] tuggerPos = { 1, 1.5, 100 }; 	// order positions for the tuggers
		double[] liftPos = { 1, 1.5, 100 }; 	// orders positions for the lift

		// tugger constructors
		this.tuggerLeft = new Tugger(this.talonLeftTugger, this.tLeftIn, this.tLeftOut, tuggerPos);
		this.tuggerRight = new Tugger(this.talonRightTugger, this.tRightIn, this.tRightOut, tuggerPos);

		// lift constructor
		this.lift = new Lift(this.talonLift, this.liftDown, this.liftUp, liftPos);

		// operator control setup
		this.operator = new Operator(); // setup operator controller (socket connection to tablet)
		this.operator.addEvent("TUGGERLEFT", new TuggerListener(this.tuggerLeft)); // add event for left tugger
		this.operator.addEvent("TUGGERRIGHT", new TuggerListener(this.tuggerRight)); // add event for right tugger
		this.operator.addEvent("LIFT", new LiftListener(this.lift));
	}

	public void autonomous() {
		super.autonomous();
	}

	public void operatorControl() {
		this.drive.enable();
		while (isOperatorControl() && isEnabled()) {
			double y = this.stick.getY();
			double x = this.stick.getX();
			double z = this.stick.getZ();
			double gyroAng = this.gyro.getAngle();

			this.drive.drive(x, y, z, gyroAng);
		}
		this.drive.disable();
	}

	/**
	 * Runs during test mode
	 */
	public void test() {

	}
}
