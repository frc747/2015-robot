package com.frc869.robot.code2015.controllable;

import edu.wpi.first.wpilibj.Joystick;

public abstract class Controllable {
	public enum CAN {
		TUGGER_RIGHT(1),
		MECANUM_RIGHT_REAR(2),
		MECANUM_RIGHT_FORWARD(3),
		LIFT_LEFT(4),
		LIFT_RIGHT(5),
		MECANUM_LEFT_FORWARD(6),
		MECANUM_LEFT_REAR(7),
		TUGGER_LEFT(8);
		private int value;
		private CAN(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	public enum Analog {
		GYRO(0);
		private int value;
		private Analog(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	public enum Digital {
		LIFT_A(0),
		LIFT_B(1),
		LIFT_LOWER_LIMIT(2),
		TOTE_HOME_LEFT(3),
		TOTE_HOME_RIGHT(4),
		
		AUTO_COREY(6),
		AUTO_DIRTY_COREY(7),
		AUTO_LANDFILL(8);
		private int value;
		private Digital(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	public enum Joysticks {
		DRIVER(1),
		OPERATOR(2);
		private Joystick joystick;
		private Joysticks(int slot) {
			this.joystick = new Joystick(slot);
		}
		public Joystick getJoystick() {
			return joystick;
		}
	}
	public abstract void stop();
}
