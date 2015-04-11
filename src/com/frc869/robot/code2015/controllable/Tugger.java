package com.frc869.robot.code2015.controllable;

//Import necesary assets

import edu.wpi.first.wpilibj.CANTalon;

/**
 * Code responsible for commanding the tuggers (component which grabs and pulls the totes
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

public class Tugger extends Controllable {
	private CANTalon tuggerLeft, tuggerRight;
	private double buffer = 100;
	
	private static Tugger instance;
	
	public enum Position {
		MIN(0),
		SLOW_DOWN_1(1000),
		SLOW_DOWN_2(500),
		SLOW_DOWN_3(100),
		STRAIGHT(4500),
		OPEN(5300),
		MAX(20812);
		private int value;
		private Position(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	private Tugger(CAN motorLeft, CAN motorRight){
		this.tuggerLeft = new CANTalon(motorLeft.getValue());
		this.tuggerLeft.setForwardSoftLimit(Position.MAX.getValue());
		this.tuggerLeft.enableForwardSoftLimit(true);
		this.tuggerLeft.enableBrakeMode(true);
		this.tuggerRight =  new CANTalon(motorRight.getValue());
		this.tuggerRight.setForwardSoftLimit(Position.MAX.getValue());
		this.tuggerRight.enableForwardSoftLimit(true);
		this.tuggerRight.enableBrakeMode(true);
//		this.tugLeftLimit = leftLimit;
//		this.tugRightLimit = rightLimit;
	}
	
	public static Tugger getInstance() {
		if(null==instance) {
			instance = new Tugger(
					CAN.TUGGER_LEFT,
					CAN.TUGGER_RIGHT
				);
		}
		return instance;
		
	}
	
	public void moveRight(double speed) {
		this.tuggerRight.set(speed);
	}
	
	public void moveLeft(double speed) {
		this.tuggerLeft.set(speed);
	}
	
	public boolean setPosition(Position position) {
		return setPosition(position, true, true);
	}
	
	public boolean setPosition(Position position, boolean left, boolean right) {
		double rightPosition = this.tuggerRight.getPosition();
		double leftPosition = this.tuggerLeft.getPosition();
		boolean inPosition = true;
		if(left) {
			if(leftPosition > position.getValue() + buffer) {
				this.moveLeft(-.5);
				inPosition = false;
			} else if(leftPosition < position.getValue() - buffer) {
				this.moveLeft(.5);
				inPosition = false;
			} else {
				this.moveLeft(0);
			}
		}
		if(right) {
			if(rightPosition > position.getValue() + buffer) {
				this.moveRight(-.5);
				inPosition = false;
			} else if(rightPosition < position.getValue() - buffer) {
				this.moveRight(.5);
				inPosition = false;
			} else {
				this.moveRight(0);
			}
		}
		return inPosition;
	}
	
	public void controlRight(double speed) {
		this.moveRight(controlPower(speed,this.tuggerRight.getPosition()));
	}
	
	public void controlLeft(double speed) {
		this.moveLeft(controlPower(speed,this.tuggerLeft.getPosition()));
	}
	
	private double controlPower(double speed, double position) {
		if (speed > 0) {
			if (position >= Position.MAX.getValue()) {
				return 0;
			} else if (position > (Position.MAX.getValue() - Position.SLOW_DOWN_1.getValue())) {
				return .25;
			} else if (position > (Position.MAX.getValue() - Position.SLOW_DOWN_2.getValue()) && position < (Position.MAX.getValue() - Position.SLOW_DOWN_1.getValue())) {
				return .5;
			} else if (position > (Position.MAX.getValue() - Position.SLOW_DOWN_3.getValue()) && position < (Position.MAX.getValue() - Position.SLOW_DOWN_2.getValue())) {
				return .75;
			} else {
				return 1;
			}
		} else if (speed < 0) {
			if (position <= Position.MIN.getValue()) {
				return 0;
			} else if (position < Position.SLOW_DOWN_1.getValue()) {
				return -.25;
			} else if (position < Position.SLOW_DOWN_2.getValue()) {
				return -.5;
			} else if (position < Position.SLOW_DOWN_3.getValue()) {
				return -.75;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}

	@Override
	public void stop() {
		moveRight(0);
		moveLeft(0);
	}
}
