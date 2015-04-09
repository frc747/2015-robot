package com.frc869.robot.code2015.endefector;

//Import necesary assets

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Code responsible for commanding the tuggers (component which grabs and pulls the totes
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

public class Tugger implements Runnable{
	
	//Establishing all variables that will be used in later code
	
	private CANTalon tuggerLeft, tuggerRight;
	private DigitalInput tugLeftLimit, tugRightLimit;
	private double[] positions;
	private double direction;
	private double speed;
	
	private boolean moveToDest = false;
	private boolean running = false;
	private int positionNum = 0;
	private int encoderMin = 0;
	private int encoderMax = 0;
	private boolean leftTuggerIsHome, rightTuggerIsHome;
	
	//Creates object "Tugger"
	
	public Tugger(CANTalon motorLeft, CANTalon motorRight, DigitalInput leftLimit, DigitalInput rightLimit){
		this.tuggerLeft = motorLeft;
		this.tuggerRight = motorRight;
		this.tugLeftLimit = leftLimit;
		this.tugRightLimit = rightLimit;


		
		this.speed = 0;
	
	}
	
	//Moving out at a given speed
	
	public void moveL(double speed){

		if(speed>0){
			this.tuggerLeft.set(speed);
		} else if(speed < 0) {
			this.tuggerLeft.set(speed);
		} else {
			this.tuggerLeft.set(0);
		}
	}
		
	public void moveR(double speed){

		if(speed>0){
			this.tuggerRight.set(speed);
		} else if(speed < 0) {
			this.tuggerRight.set(speed);
		} else {
			this.tuggerRight.set(0);
		}
		
		
		
	}
	
	
	public void calibrate(DigitalInput tugLeftLim, DigitalInput tugRightLim){

		///////////////////////////////////////////////////////////////////////////////////
//		this.tugLeftLimit = tugLeftLim;
//		this.tugRightLimit = tugRightLim;
//		
//		
//				////HOME LEFT TUGGER
//		if (this.tugLeftLimit.get()){
//			this.tuggerLeft.set(-.1);
//			System.out.println(this.tuggerLeft.getPosition());
//			leftTuggerIsHome = false;
//		}else{
//			
//			if (this.tuggerLeft.getPosition() <= (-100) && this.tuggerLeft.getPosition() >= (-110)){
//				this.tuggerLeft.set(0);
//				System.out.println(this.tuggerLeft.getPosition());
//				System.out.println("third");
//				this.tuggerLeft.setPosition(0);
//				leftTuggerIsHome = true;
//			} else if (this.tuggerLeft.getPosition() <= 0 && this.tuggerLeft.getPosition() > (-100) && !leftTuggerIsHome){
//				this.tuggerLeft.set(-.1);
//				System.out.println(this.tuggerLeft.getPosition());
//				System.out.println("second");
//			} else if (this.tuggerLeft.getPosition() > 0 || this.tuggerLeft.getPosition() < (-110) && !leftTuggerIsHome){
//				this.tuggerLeft.setPosition(0);
//				this.tuggerLeft.set(0);
//				System.out.println(this.tuggerLeft.getPosition());
//				System.out.println("first"); 
//			}
//
//		}		
		//////////////////////////////////////////////////////////////////////////////////////////////////////

	}
	
	
	@Override
	public void run() {

	}
	

}
