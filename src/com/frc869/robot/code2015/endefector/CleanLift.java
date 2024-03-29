package com.frc869.robot.code2015.endefector;

//Import necessary assets

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Code responsible for commanding the lift, which lifts the totes after they've been "Tugged"
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

//Establishing all variables to be used in later code

public class CleanLift implements Runnable {
	private CANTalon talonLift1, talonLift2;
	private Encoder encoder, liftEncoder;
	private DigitalInput lowerLimit, upperLimit;
	private double speed;
	private Joystick operatorController;
	
	
	
//	private boolean moveToDest = false;
//	private boolean running = false;
//	private int positionNum = 0;
	private int encoderMin, encoderMax;
	private double posi1 = 1000;
	private double posi2 = 2000;
	private double posi3 = 3000;
	private double posi4 = 4000;
	public boolean liftIsHome;
	
	
	
	
	//Creates object "Lift"
	
	public CleanLift(CANTalon talonLift1, CANTalon talonLift2, Encoder encoder, DigitalInput lowerLimit, DigitalInput upperLimit, int encMin, int encMax){
		this.talonLift1 = talonLift1;
		this.talonLift2 = talonLift2;
		this.encoder = encoder;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.operatorController = new Joystick(2);
		this.encoderMin = encMin;
		this.encoderMax = encMax;
	}
	
	//Moves lift
	
	public void move(double speed){
		if(speed>0){
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
		} else if(speed < 0) {
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
		}
	}
	
	public void setPosition(double position, Encoder liftEncoder, double slowDownPosi1, double slowDownPosi2) {
		
		double slowSpeed2 = .1;
		double slowSpeed1 = .25;
		double fastSpeed = 1;
		double buffer = 100;
		this.liftEncoder = liftEncoder;
//		this.robot = robot;
	
		
		if (this.liftEncoder.get() < position + buffer
				&& this.liftEncoder.get() > position - buffer) {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
		} else if (this.liftEncoder.get() > position) {
			this.talonLift1.set((fastSpeed * (-1)));
			this.talonLift2.set((fastSpeed * (-1)));
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
		} else if (this.liftEncoder.get() < position) {
			this.talonLift1.set(fastSpeed);
			this.talonLift2.set(fastSpeed);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
		} else if (this.liftEncoder.get() <= 0) {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
		} else if (this.liftEncoder.get() < slowDownPosi2) {
			this.talonLift1.set(slowSpeed1);
			this.talonLift2.set(slowSpeed1);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
		} else if (this.liftEncoder.get() < slowDownPosi1) {
			this.talonLift1.set(slowSpeed2);
			this.talonLift2.set(slowSpeed2);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
		}
		
		
		
	}

	
	
	//Calibrate lower position
	
	public void calibrateDown(DigitalInput liftLowerLimit, Encoder liftEncoder, Double speed){

		
		this.liftEncoder = liftEncoder;
		
		
		if (this.lowerLimit.get()){
			this.talonLift1.set(speed);
			this.talonLift2.set(speed);
			this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 1);
			this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 1);
			System.out.println("LIMIT");
			System.out.println(this.liftEncoder.get());
			liftIsHome = false;
		} else {
			
			if (this.liftEncoder.get() <= (-125) && this.liftEncoder.get() >= (-135)){
				this.talonLift1.set(0);
				this.talonLift2.set(0);
				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
				System.out.println(this.liftEncoder.get());
				System.out.println("third");
				this.liftEncoder.reset();
				liftIsHome = true;
			} else if (this.liftEncoder.get() <= 0 && this.liftEncoder.get() > (-125) && !liftIsHome){
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
				System.out.println(this.liftEncoder.get());
				System.out.println("second");
			} else if (this.liftEncoder.get() > 0 || this.liftEncoder.get() < (-135) && !liftIsHome){
				this.liftEncoder.reset();
				this.operatorController.setRumble(Joystick.RumbleType.kLeftRumble, 0);
				this.operatorController.setRumble(Joystick.RumbleType.kRightRumble, 0);
				this.talonLift1.set(0);
				this.talonLift2.set(0);	
				System.out.println(this.liftEncoder.get());
				System.out.println("first"); 
			}

		}
		
			
				
				
		
		
		//TO DO reset encoder
	}

	
	@Override
	public void run() {

	}
}
