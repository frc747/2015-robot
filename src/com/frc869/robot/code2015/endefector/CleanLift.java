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
	private Encoder encoder, liftEncoder;
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
		
		this.encoderMin = encMin;
		this.encoderMax = encMax;
	}
	
	//Moves lift
	
	public void move(double speed){
		if(speed>0){
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
		} else if(speed < 0) {
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		}
	}
	
	public void setPosition(double position, Encoder liftEncoder) {
		
//		if(encoder.get()>position.getEncoderValue()) {
//			//5 percent away from the goal make 25% speed instead of full so we dont overshoot
//			if((encoder.get()-position.getEncoderValue()/(encoder.get()+position.getEncoderValue())/2)>.05) {
//				move(-1);
//			} else {
//				move(-.25);
//			}
//		} else if (encoder.get()<position.getEncoderValue()) {
//			//5 percent away from the goal make 25% speed instead of full so we dont overshoot
//			if((encoder.get()-position.getEncoderValue()/(encoder.get()+position.getEncoderValue())/2)>.05) {
//				move(1);
//			} else {
//				move(.25);
//			}
//		} else {
//			move(0);
//		}
		double currentLocation = liftEncoder.get();
		
		
		
		
	
		if (position > currentLocation){
			if (position - currentLocation < 100){
				this.talonLift1.set(.2);
				this.talonLift2.set(.2);
			} else {
				this.talonLift1.set(.5);
				this.talonLift2.set(.5);
			}
		} else if (position < currentLocation){
			if (currentLocation - position < 100){
				this.talonLift1.set(-.2);
				this.talonLift2.set(-.2);
			} else {
				this.talonLift1.set(-.5);
				this.talonLift2.set(-.5);
			}
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		}
		
	}

	
	
	
	//Calibrate upper position
	
//	public void calibrateUp(){
//		this.moveToDest = false;
//		while(this.upperLimit.get()){
//			this.talonLift1.set(1);
//			this.talonLift2.set(1);
//		}
//		
////		this.positionNum = this.positions.length - 1;
//		
//		//TODO reset encoder
//		
//	}
	
	//Calibrate lower position
	
	public void calibrateDown(DigitalInput liftLowerLimit, Encoder liftEncoder){
//		this.moveToDest = false;
//		//distance past lower limit for lower home position
//		double homePosition = 150;
//		
//		
//		while(this.lowerLimit.get()){
//			this.talonLift1.set(-.35);
//			this.talonLift2.set(-.35);
//		}
//		
//		//move X distance past lower limit for actual "0" (home position)
//		if(homePosition - this.encoder.get() > 0){
//			this.talonLift1.set(-.2);
//			this.talonLift2.set(-.2);
//		}else{
//			this.talonLift1.set(0);
//			this.talonLift2.set(0);
//		}
//		this.encoder.reset();
//		this.positionNum = 0;
		this.liftEncoder = liftEncoder;
		
		
		
		
		if (this.lowerLimit.get()){
			this.talonLift1.set(-.1);
			this.talonLift2.set(-.1);
			System.out.println("LIMIT");
			System.out.println(this.liftEncoder.get());
			liftIsHome = false;
		}else{
			
			if (this.liftEncoder.get() <= (-100) && this.liftEncoder.get() >= (-110)){
				this.talonLift1.set(0);
				this.talonLift2.set(0);
				System.out.println(this.liftEncoder.get());
				System.out.println("third");
				this.liftEncoder.reset();
				liftIsHome = true;
			} else if (this.liftEncoder.get() <= 0 && this.liftEncoder.get() > (-100) && !liftIsHome){
				this.talonLift1.set(-.1);
				this.talonLift2.set(-.1);
				System.out.println(this.liftEncoder.get());
				System.out.println("second");
			} else if (this.liftEncoder.get() > 0 || this.liftEncoder.get() < (-110) && !liftIsHome){
				this.liftEncoder.reset();
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
