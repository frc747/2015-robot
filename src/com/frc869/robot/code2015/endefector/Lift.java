package com.frc869.robot.code2015.endefector;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Lift implements Runnable {
	private CANTalon motor;
	private DigitalInput lowerLimit, upperLimit;
	private double[] positions;
	
	private boolean moveToDest = false;
	private int positionNum = 0;
	private boolean running = false;
	private Thread thread;
	
	public Lift(CANTalon motor, DigitalInput lowerLimit, DigitalInput upperLimit, double[] positions){
		this.motor = motor;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
		this.positions = positions;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void moveUp(double speed){
		if(!this.upperLimit.get())
			this.motor.set(speed);
		this.moveToDest = false;
	}
	
	public void moveDown(double speed){
		if(!this.lowerLimit.get())
			this.motor.set(-speed);
		this.moveToDest = false;
	}
	
	public void nextUp(){
		if(this.positionNum < this.positions.length - 1){
			this.positionNum++;
			this.moveToDest = true;
		}
	}
	
	public void nextDown(){
		if(this.positionNum > 0){
			this.positionNum--;
			this.moveToDest = true;
		}
	}
	
	public void calibrateUp(){
		this.moveToDest = false;
		while(this.upperLimit.get()){
			this.motor.set(1);
		}
		
		this.positionNum = this.positions.length - 1;
		
		//TODO reset encoder
		
	}
	
	public void calibrateDown(){
		this.moveToDest = false;
		while(this.lowerLimit.get()){
			this.motor.set(-1);
		}
		
		this.positionNum = 0;
		
		//TODO reset encoder
	}

	@Override
	public void run() {
		while(this.running){
			if(this.moveToDest){
				if(!this.lowerLimit.get() && this.positions[positionNum] > this.motor.getPosition()){
					this.motor.set(-0.5);
				}else if(!this.upperLimit.get() && this.positions[positionNum] < this.motor.getPosition()){
					this.motor.set(0.5);
				}else{
					this.moveToDest = false;
				}
			}else{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
