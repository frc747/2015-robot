package com.frc869.robot.code2015.controllable;

//Import necessary assets

import com.frc869.robot.code2015.component.DigitalInput;
import com.frc869.robot.code2015.component.Encoder;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

/**
 * Code responsible for commanding the lift, which lifts the totes after they've been "Tugged"
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

//Establishing all variables to be used in later code

public class Lift extends Controllable {
	private CANTalon talonLift1, talonLift2;
	private Encoder encoder;
	private DigitalInput lowerLimit;
	private Joystick operatorController;
	
	private final int buffer = 100;
	
	public boolean liftIsHome;
	
	private static Lift instance;
	
	public enum Position {
		MIN(-600),
		MAX(116000),
		MAX_PRACTICE(117000),
		SLOW1(600),
		SLOW2(1200),
		TOTE1(0),
		TOTE2(43452),
		TOTE3(31588),
		TOTE4(89664),
		PICKUP_BIN(42000),
		DROP_BIN(39856),
		LITTER_BIN(112000),
		PICKUP_BIN_STEP(70000);
		private int value;
		private Position(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	//Creates object "Lift"
	
	private Lift(CAN talonLift1, CAN talonLift2, Digital encoderA, Digital encoderB, Digital lowerLimit){
		this.talonLift1 = new CANTalon(talonLift1.getValue());
		this.talonLift2 = new CANTalon(talonLift2.getValue());
		this.encoder = new Encoder(encoderA.getValue(),encoderB.getValue(),false,EncodingType.k4X);
		this.lowerLimit = new DigitalInput(lowerLimit.getValue(),false);
		this.operatorController = Joysticks.OPERATOR.getJoystick();
	}
	
	public static Lift getInstance() {
		if(null==instance) {
			instance = new Lift(
					CAN.LIFT_LEFT, 
					CAN.LIFT_RIGHT,
					Digital.LIFT_A, 
					Digital.LIFT_B, 
					Digital.LIFT_LOWER_LIMIT
				);
		}
		return instance;
	}
	
	//Moves lift
	public void control(double speed) {
		int encoderPosition = encoder.get();
		if(speed < 0) {
			System.out.println("lift moved up");
			if(encoderPosition <= Position.MIN.getValue()) {
				this.move(0);
			} else if(encoderPosition < Position.SLOW1.getValue()) {
				this.move(.1);
			} else if(encoderPosition < Position.SLOW2.getValue()) {
				this.move(.25);
			} else {
				this.move(1);
			}
			
		} else if(speed > 0) {
			System.out.println("Lift moved down");
			if(encoderPosition >= Position.MAX.getValue()) {
				this.move(0);
			} else if(encoderPosition > (Position.MAX.getValue() - Position.SLOW1.getValue())) {
				this.move(-.1);
			} else if(encoderPosition > (Position.MAX.getValue() - Position.SLOW2.getValue())) {
				this.move(-.25);
			} else {
				this.move(-1);
			}
		} else {
			move(0);
		}
	}
	
	public void move(double speed){
		if((speed > 0 && encoder.get() < Position.MAX.getValue()) || (speed < 0 && encoder.get() > Position.MIN.getValue())) {
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
	
	public boolean setPosition(Position position) {
		double slowSpeed2 = .1;
		double slowSpeed1 = .25;
		double fastSpeed = 1;
		boolean inPosition = false;
		if (encoder.get() > position.getValue() + buffer) {
			this.move(-fastSpeed);
		} else if (encoder.get() < position.getValue() - buffer) {
			if (encoder.get() < Position.SLOW2.getValue()) {
				this.move(slowSpeed2);
			} else if (encoder.get() < Position.SLOW1.getValue()) {
				this.move(slowSpeed1);
			} else {
				this.move(fastSpeed);
			}
		} else {
			inPosition = true;
		}
		return inPosition;
	}
	
	public void calibrateDown(Double speed){
		if (this.lowerLimit.get()){
			System.out.println("LIMIT");
			this.move(0);
			encoder.reset();
			encoder.setOffset(Position.MIN.getValue());
		} else {
			System.out.println("lowering");
			this.move(speed);
		}
		System.out.println(encoder.get());
	}

	@Override
	public void stop() {
		move(0);
	}
}
