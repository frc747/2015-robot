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

	//private final int LiftMax = 4400; // Pre Bag value
	private final int LiftMax = 28000; //Backup Robot value
	private final int LiftMin = -150;
	// Establishing all variables to be used in later code

	//Setup Talon variables
	private CANTalon rightFront, rightBack, leftBack, leftFront;
	private CANTalon talonLeftTugger, talonRightTugger;
	private CANTalon talonLift1, talonLift2;
	private Mecanum869 mecanumDrive;
	private Joystick driverController, operatorController;
	private Gyro gyro;
	private Encoder liftEncoder, tuggerLeftEncoder, tuggerRightEncoder;
	private DigitalInput tugLeftLim, tugRightLim, liftLimUp, liftLimDown;
	private Tugger tuggers;
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

		this.liftEncoder = new Encoder(0, 1, false, EncodingType.k1X); // Encoder for lift
		this.liftEncoder.reset();
		System.out.println("ES: " + this.liftEncoder.getEncodingScale());

		// digital inputs for the limit switches
		this.liftLimUp = new DigitalInput(3);
		this.liftLimDown = new DigitalInput(2);
		this.tugLeftLim = new DigitalInput(4);
		this.tugRightLim = new DigitalInput(5);
		
		
			
		CANTalon[] liftTalons = {talonLift1, talonLift2};

		this.lift = new CleanLift(talonLift1, talonLift2, liftEncoder, liftLimDown, liftLimUp, LiftMax, LiftMax);
		this.tuggers = new Tugger(talonLeftTugger, talonRightTugger, tugLeftLim, tugRightLim);
		
	}
	

	
	
	//*********************************************************************
	//***** Autonomous code begins                                  *******
	//*****															*******
	//*****															*******
	//*********************************************************************	

	public void autonomous() {
		this.rightFront.setPosition(0);
		this.rightBack.setPosition(0);
		this.leftFront.setPosition(0);
		this.leftBack.setPosition(0);
		this.mecanumDrive.enable();
		
		if (this.rightFront.getPosition() < (250*3.3333)){
			this.mecanumDrive.drive(0, .5, 0, 0);
		} else {
			this.mecanumDrive.drive(0, 0, 0, 0);
		}
		
//		while(this.rightFront.getPosition() < 1080)
//			this.mecanumDrive.drive(1, 0, 0, 0);
//		while(this.rightFront.getPosition() < 2160)
//			this.mecanumDrive.drive(0, 1, 0, 0);
//		while(this.rightFront.getPosition() > 1080)
//			this.mecanumDrive.drive(-1, 0, 0, 0);
//		while(this.rightFront.getPosition() > 0){
//			System.out.println(this.rightFront.getPosition());
//			this.mecanumDrive.drive(0, -1, 0, 0);
//		}
//		System.out.println("Done!");
//		this.mecanumDrive.drive(0, 0, 0, 0);
//		this.leftBack.set(0);
//		this.leftFront.set(0);
//		this.rightBack.set(0);
//		this.rightFront.set(0);
//		
		this.mecanumDrive.disable();
		
	}
	//*********************************************************************
	//***** Autonomous code ends                                   *******
	//*********************************************************************	

	
	
	// Code which speaks to operator control

	public void operatorControl() {
		this.mecanumDrive.enable();
		this.gyro.reset();
		while (isOperatorControl() && isEnabled()) {

		
			//Get variables from Driver Controller joysticks. Will be used to set speed for mecanum drive.
			double x = this.driverController.getX();
			double y = this.driverController.getY();
			double z = this.driverController.getZ();
			
			double lift1Position = 1000;
			double lift2Position = 2000;
			double lift3Position = 3000;
			double lift4Position = 4000;
			
		
//			
			
			//reduce controller input by X% for drive
			this.mecanumDrive.drive((x*.65), (y*.65), (z*.65), 0);
			
//			if(this.operatorController.getRawButton(1)){
//				System.out.println(this.liftEncoder.get());
//			}else if(this.operatorController.getRawButton(3)){
//				this.liftEncoder.reset();
//				System.out.println("Clear!");
//			}

		
					
			
//			if(this.operatorController.getRawButton(10)) {
//				this.lift.calibrateDown(liftLimDown, liftEncoder);
//			} else if(this.operatorController.getRawButton(6)) {
//				this.lift.setPosition(lift1Position, liftEncoder);
//			} else if(this.operatorController.getRawButton(7)) {
//				this.lift.setPosition(lift2Position, liftEncoder);
//			} else if(this.operatorController.getRawButton(8)) {
//				this.lift.setPosition(lift3Position, liftEncoder);
//			} else if(this.operatorController.getRawButton(9)) {
//				this.lift.setPosition(lift4Position, liftEncoder);
//			} else if (this.operatorController.getRawButton(4)){
//				this.lift.move(.25);
//			} else if (this.operatorController.getRawButton(1)){
//				this.lift.move(-.1);
//			} else {
//				this.lift.move(0);
//			}
			
			if (this.operatorController.getRawButton(7)){
				System.out.println(this.liftEncoder.get());
			}
			
			
			//COMPETITION ROBOT VALUES
			if(this.operatorController.getRawButton(10)) {
				this.lift.calibrateDown(liftLimDown, liftEncoder);
			} else if (this.operatorController.getRawButton(4)){
				if (this.liftEncoder.get() < 28500){
					this.lift.move(.70);
				} else if (this.liftEncoder.get() < 28750 && this.liftEncoder.get() > 28500){
					this.lift.move(.25);
				} else if (this.liftEncoder.get() < 29000){
					this.lift.move(.1);
				} else {
					this.lift.move(0);
				}
			} else if (this.operatorController.getRawButton(2)){
				if (this.liftEncoder.get() > 500){
					this.lift.move(-.70);
				} else if (this.liftEncoder.get() < 500 && this.liftEncoder.get() > 250){
					this.lift.move(-.25);
				} else if (this.liftEncoder.get() > 0){
					this.lift.move(-.1);
				} else {
					this.lift.move(0);
				}
			} else {
				this.lift.move(0);
			}
			
			
			
			
//			PRACTICE ROBOT VALUES
//			if(this.operatorController.getRawButton(12)) {
//				this.lift.calibrateDown(liftLimDown, liftEncoder);
//			} else if (this.operatorController.getRawButton(4)){
//				if (this.liftEncoder.get() < 27250){
//					this.lift.move(.70);
//				} else if (this.liftEncoder.get() < 28500 && this.liftEncoder.get() > 27250){
//					this.lift.move(.25);
//				} else if (this.liftEncoder.get() < 29250){
//					this.lift.move(.1);
//				} else {
//					this.lift.move(0);
//				}
//			} else if (this.operatorController.getRawButton(2)){
//				if (this.liftEncoder.get() > 2000){
//					this.lift.move(-.70);
//				} else if (this.liftEncoder.get() < 2000 && this.liftEncoder.get() > 750){
//					this.lift.move(-.25);
//				} else if (this.liftEncoder.get() > 0){
//					this.lift.move(-.1);
//				} else {
//					this.lift.move(0);
//				}
//			} else {
//				this.lift.move(0);
//			}
			
			if (this.operatorController.getRawButton(6)){
				System.out.println(this.talonLeftTugger.getPosition());
				System.out.println(this.talonRightTugger.getPosition());
			} else if (this.operatorController.getRawButton(8)){
				this.talonLeftTugger.setPosition(0);
				this.talonRightTugger.setPosition(0);
			}
			
			//TUGGER MOVING CODE
			
			if(this.operatorController.getRawButton(11)) {
				this.tuggers.calibrate(tugLeftLim, tugRightLim);
				
			} else if (this.operatorController.getRawButton(5)){
					//tuggers in
//				this.tuggers.move(.5);
				
				if (this.tuggerLeftEncoder.get() < 5078){
					this.tuggers.move(.5);
				} else if (this.tuggerLeftEncoder.get() < 5203 && this.tuggerLeftEncoder.get() > 5078){
					this.tuggers.move(.25);
				} else if (this.tuggerLeftEncoder.get() < 5453){
					this.tuggers.move(.1);
				} else {
					this.tuggers.move(0);
				}
			} else if (this.operatorController.getRawButton(7)){
				
				//tuggers out
//				this.tuggers.move(-.5);
								
				if (this.tuggerLeftEncoder.get() > 250){
					this.tuggers.move(-.5);
				} else if (this.tuggerLeftEncoder.get() < 250 && this.tuggerLeftEncoder.get() > 125){
					this.tuggers.move(-.25);
				} else if (this.tuggerLeftEncoder.get() > 0){
					this.tuggers.move(-.1);
				}
				
			} else {
				this.tuggers.move(0);
			}
			
			
		}
		this.mecanumDrive.disable();
	}
	
	public void test(){

	}

}
