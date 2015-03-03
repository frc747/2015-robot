package com.frc869.robot.code2015.endefector;

//Import necessary assets

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Code responsible for commanding the lift, which lifts the totes after they've been "Tugged"
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

//Establishing all variables to be used in later code

public class CleanLift implements Runnable {
	private CANTalon talonLift1, talonLift2;
	private Encoder encoder;
	private DigitalInput lowerLimit, upperLimit;
	private double speed;
	public enum Position {
		TOTE1(1000),TOTE2(2000),TOTE3(3000),TOTE4(4000);
		private double encoderValue;
		private Position(double encoderValue) {
			this.encoderValue = encoderValue;
		}
		public double getEncoderValue() {
			return encoderValue;
		}
	};
	
	private boolean moveToDest = false;
	private boolean running = false;
	private int positionNum = 0;
	private int encoderMin, encoderMax;
	
	
	//Creates object "Lift"
	
	public CleanLift(CANTalon talonLift1, CANTalon talonLift2, Encoder encoder, DigitalInput lowerLimit, DigitalInput upperLimit, double[] positions, int encMin, int encMax){
		this.talonLift1 = talonLift1;
		this.talonLift2 = talonLift2;
		this.encoder = encoder;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
		this.encoderMin = encMin;
		this.encoderMax = encMax;
	}
	
	//Moves Lift up
	
	public void move(double speed){
		if(speed>0){
			if(this.upperLimit.get()) {
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
			} else {
				//encoderMax = encoder.get();
				this.talonLift1.set(0);
				this.talonLift2.set(0);
			}
		} else if(speed < 0) {
			if(this.lowerLimit.get()) {
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
			} else {
				//encoderMin = encoder.get();
				this.talonLift1.set(0);
				this.talonLift2.set(0);
			}
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		}
	}
	
	public void setPosition(Position position) {
		if(encoder.get()>position.getEncoderValue()) {
			//5 percent away from the goal make 25% speed instead of full so we dont overshoot
			if((encoder.get()-position.getEncoderValue()/(encoder.get()+position.getEncoderValue())/2)>.05) {
				move(-1);
			} else {
				move(-.25);
			}
		} else if (encoder.get()<position.getEncoderValue()) {
			//5 percent away from the goal make 25% speed instead of full so we dont overshoot
			if((encoder.get()-position.getEncoderValue()/(encoder.get()+position.getEncoderValue())/2)>.05) {
				move(1);
			} else {
				move(.25);
			}
		} else {
			move(0);
		}
	}

	
	
	
	//Calibrate upper position
	
	public void calibrateUp(){
		this.moveToDest = false;
		while(this.upperLimit.get()){
			this.talonLift1.set(1);
			this.talonLift2.set(1);
		}
		
//		this.positionNum = this.positions.length - 1;
		
		//TODO reset encoder
		
	}
	
	//Calibrate lower position
	
	public void calibrateDown(){
		this.moveToDest = false;
		//distance past lower limit for lower home position
		double homePosition = 150;
		
		
		while(this.lowerLimit.get()){
			this.talonLift1.set(-.35);
			this.talonLift2.set(-.35);
		}
		
		//move X distance past lower limit for actual "0" (home position)
		if(homePosition - this.encoder.get() > 0){
			this.talonLift1.set(-.2);
			this.talonLift2.set(-.2);
		}else{
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		}
		this.encoder.reset();
		this.positionNum = 0;
		
		//TODO reset encoder
	}

	
	@Override
	public void run() {

	}
}
