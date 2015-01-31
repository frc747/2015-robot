package com.frc869.robot.code2015.endefector;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Tugger implements Runnable{
	
	private CANTalon motor;
	private DigitalInput innerLimit, outterLimit;
	private double[] positions;
	private double direction;
	
	private boolean moveToDest = false;
	private int positionNum = 0;
	private boolean running = false;
	private Thread thread;
	
	public Tugger(CANTalon motor, DigitalInput lowerLimit, DigitalInput upperLimit, double[] positions, double direction){
		this.motor = motor;
		this.innerLimit = lowerLimit;
		this.outterLimit = upperLimit;
		this.direction = direction;
		
		this.positions = positions;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void moveOut(double speed){
		if(!this.outterLimit.get())
			this.motor.set(speed * this.direction);
		this.moveToDest = false;
	}
	
	public void moveIn(double speed){
		if(!this.innerLimit.get())
			this.motor.set(-speed * this.direction);
		this.moveToDest = false;
	}
	
	public void nextOut(){
		if(this.positionNum < this.positions.length - 1){
			this.positionNum++;
			this.moveToDest = true;
		}
	}
	
	public void nextIn(){
		if(this.positionNum > 0){
			this.positionNum--;
			this.moveToDest = true;
		}
	}
	
	public void calibrateOut(){
		this.moveToDest = false;
		while(this.outterLimit.get()){
			this.motor.set(1 * this.direction);
		}
		
		this.positionNum = this.positions.length - 1;
		
		//TODO reset encoder
	}
	
	public void calibrateIn(){
		this.moveToDest = false;
		while(this.innerLimit.get()){
			this.motor.set(-1 * this.direction);
		}
		
		this.positionNum = 0;
		
		//TODO reset encoder
	}

	@Override
	public void run() {
		while(this.running){
			if(this.moveToDest){
				if(!this.innerLimit.get() && this.positions[positionNum] > this.motor.getPosition()){
					this.motor.set(-0.5 * this.direction);
				}else if(!this.outterLimit.get() && this.positions[positionNum] < this.motor.getPosition()){
					this.motor.set(0.5 * this.direction);
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
