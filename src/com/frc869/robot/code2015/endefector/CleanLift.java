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
				encoderMax = encoder.get();
			}
		} else if(speed < 0) {
			if(this.lowerLimit.get()) {
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
			} else {
				encoderMin = encoder.get();
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
//	
//	//Moves Lift up to next set position
//	
//	public void nextUp(){
//		if(this.positionNum < this.positions.length - 1){
//			this.positionNum++;
//			this.moveToDest = true;
//		}
//	}
//	
//	//Moves Lift down to next set position
//	
//	public void nextDown(){
//		if(this.positionNum > 0){
//			this.positionNum--;
//			this.moveToDest = true;
//		}
//	}
	
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
		while(this.lowerLimit.get()){
			this.talonLift1.set(-1);
			this.talonLift2.set(-1);
		}
		
		this.positionNum = 0;
		
		//TODO reset encoder
	}

	//Automated moving control to preset conditions
//	public void _run() {
//		while(this.running){
//			if(this.moveToDest){
//				if(!this.lowerLimit.get() && this.positions[positionNum] > this.encoder.get()){
//					this.talonLift1.set(-0.5);
//					this.talonLift2.set(-0.5);
//				}else if(!this.upperLimit.get() && this.positions[positionNum] < this.encoder.get()){
//					this.talonLift1.set(0.5);
//					this.talonLift2.set(0.5);
//				}else{
//					this.moveToDest = false;
//				}
//			}else{
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			
//		}
//	
//	}
	
	@Override
	public void run() {
//		while(this.running){
//			if(this.moveToDest){
//				if(!this.lowerLimit.get() && this.positions[positionNum] > this.encoder.get()){
//					double reduction = 0;
//
//					if (this.encoder.get() - this.encoderMin > .9*(encoderMax - encoderMin) && speed > 0){
//						reduction = (this.encoder.get() - encoderMin) / (.1 * (encoderMax - encoderMin));
//					}else if((this.encoder.get() - this.positions[positionNum]) < .1 * (encoderMax - encoderMin)){
//						reduction = (this.encoder.get() - this.positions[positionNum]) / (.1 * (encoderMax - encoderMin));
//					}
//					reduction *= -.9;
//					this.talonLift1.set(-1 + reduction);
//					this.talonLift2.set(-1 + reduction);
//				}else if(!this.upperLimit.get() && this.positions[positionNum] < this.encoder.get()){
//					double reduction = 0;
//
//					if(this.encoder.get() - this.encoderMin  < .1 *(encoderMax - encoderMin) && speed < 0){
//						reduction = (this.encoder.get() - encoderMin) / (.1 * (encoderMax - encoderMin));
//					}else if((this.positions[positionNum] - this.encoder.get()) < .1 * (encoderMax - encoderMin)){
//						reduction = (this.positions[positionNum] - this.encoder.get()) / (.1 * (encoderMax - encoderMin));
//					}
//					reduction *= .9;
//					this.talonLift1.set(1 - reduction);
//					this.talonLift2.set(0.5 - reduction);
//				}else{
//					this.moveToDest = false;
//				}
//			}else{
//				double reduction = 0;
//				if (this.encoder.get() -  this.encoderMin > .9*(encoderMax - encoderMin) && speed > 0){
//					reduction = (encoderMax - this.encoder.get()) / (.1 * (encoderMax - encoderMin));
//					reduction *= .9;
//				}else if(this.encoder.get() - this.encoderMin  < .1 *(encoderMax - encoderMin) && speed < 0){
//					reduction = (this.encoder.get() - encoderMin) / (.1 * (encoderMax - encoderMin));
//					reduction *= -.9;
//				}	
//				this.talonLift1.set(this.speed - reduction);
//			}
//			
//		}
	}
}
