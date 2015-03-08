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
	
	//Moving to next outer position
	
//	public void nextOut(){
//		if(this.positionNum < this.positions.length - 1){
//			this.positionNum++;
//			this.moveToDest = true;
//		}
//	}
	
	//Moving to next inner position
	
//	public void nextIn(){
//		if(this.positionNum > 0){
//			this.positionNum--;
//			this.moveToDest = true;
//		}
//	}
	
	//Calibrate outer position
	
	public void calibrate(DigitalInput tugLeftLim, DigitalInput tugRightLim){
//		this.moveToDest = false;
//		while(this.outterLimit.get()){
//			this.motor.set(1 * this.direction);
//		}
//		
//		this.positionNum = this.positions.length - 1;
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
//		
//		/////HOME RIGHT TUGGER
//		if (this.tugRightLimit.get()){
//			this.tuggerRight.set(-.1);
//			System.out.println(this.tuggerRight.getPosition());
//			rightTuggerIsHome = false;
//		}else{
//			
//			if (this.tuggerRight.getPosition() <= (-100) && this.tuggerRight.getPosition() >= (-110)){
//				this.tuggerRight.set(0);
//				System.out.println(this.tuggerRight.getPosition());
//				System.out.println("third");
//				this.tuggerRight.setPosition(0);
//				rightTuggerIsHome = true;
//			} else if (this.tuggerRight.getPosition() <= 0 && this.tuggerRight.getPosition() > (-100) && !rightTuggerIsHome){
//				this.tuggerRight.set(-.1);
//				System.out.println(this.tuggerRight.getPosition());
//				System.out.println("second");
//			} else if (this.tuggerRight.getPosition() > 0 || this.tuggerRight.getPosition() < (-110) && !rightTuggerIsHome){
//				this.tuggerRight.setPosition(0);
//				this.tuggerRight.set(0);
//				System.out.println(this.tuggerRight.getPosition());
//				System.out.println("first"); 
//			}
//
//		}
//		
		
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////

	}
	
	//Calibrate inner position
	
//	public void calibrateIn(){
//		this.moveToDest = false;
//		while(this.innerLimit.get()){
//			this.motor.set(-1 * this.direction);
//		}
//		
//		//Gives calibrating process a starting point/default position
//		
//		this.positionNum = 0;
//		
//		//TODO reset encoder
//	}

	//Automated moving control, sets to preset positions
	
	@Override
	public void run() {
//		while(this.running){
//			if(this.moveToDest){
//				if(!this.innerLimit.get() && this.positions[positionNum] > this.motor.getPosition()){
//					double reduction = 0;
//
//					if (this.motor.getPosition() - this.encoderMin > .9*(encoderMax - encoderMin) && speed > 0){
//						reduction = (this.motor.getPosition() - encoderMin) / (.1 * (encoderMax - encoderMin));
//					}else if((this.motor.getPosition() - this.positions[positionNum]) < .1 * (encoderMax - encoderMin)){
//						reduction = (this.motor.getPosition() - this.positions[positionNum]) / (.1 * (encoderMax - encoderMin));
//					}
//					reduction *= -.9;
//					this.motor.set(-0.5 * this.direction - reduction);
//				}else if(!this.outterLimit.get() && this.positions[positionNum] < this.motor.getPosition()){
//					double reduction = 0;
//
//					if(this.motor.getPosition() - this.encoderMin  < .1 *(encoderMax - encoderMin) && speed < 0){
//						reduction = (this.motor.getPosition() - encoderMin) / (.1 * (encoderMax - encoderMin));
//					}else if((this.positions[positionNum] - this.motor.getPosition()) < .1 * (encoderMax - encoderMin)){
//						reduction = (this.positions[positionNum] - this.motor.getPosition()) / (.1 * (encoderMax - encoderMin));
//					}
//					reduction *= .9;
//					this.motor.set(0.5 * this.direction - reduction);
//				}else{
//					this.moveToDest = false;
//				}
//			}else{
//				double reduction = 0;
//				if (this.motor.getPosition() -  this.encoderMin > .9*(encoderMax - encoderMin) && speed > 0){
//					reduction = (encoderMax - this.motor.getPosition()) / (.1 * (encoderMax - encoderMin));
//					reduction *= .9;
//				}else if(this.motor.getPosition() - this.encoderMin  < .1 *(encoderMax - encoderMin) && speed < 0){
//					reduction = (this.motor.getPosition() - encoderMin) / (.1 * (encoderMax - encoderMin));
//					reduction *= -.9;
//				}	
//				this.motor.set(this.speed - reduction);
//			}
//			
//		}
	}
	
//	public void on(){
//		this.running = true;
//		this.motor.enableControl();
//	}
//
//	public void off() {
//		this.running = false;
//		this.motor.disable();
//	}

}
