package com.frc869.robot.code2015.endefector;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

public class Lift2 {

	public static final int BTN_UP = 5;// 1;
	public static final int BTN_DOWN = 6;// 2;
	public static final int BTN_OVERRIDE = 3;

	public static final double AUTO_SPEED = 0.5;

	private int position = 0;
	private int encMin, encMax;
	private int[] positions;
	private CANTalon[] talons;
	private boolean goToMode = false;
	private Encoder encoder;
	private DigitalInput liftUp, liftDown;

	public Lift2(CANTalon[] talons, Encoder encoder, DigitalInput liftUp,
			DigitalInput liftDown, int[] positions, int encMin, int encMax) {
		this.talons = talons;
		this.encoder = encoder;
		this.liftUp = liftUp;
		this.liftDown = liftDown;
		this.positions = positions;
		this.encMin = encMin;
		this.encMax = encMax;
	}

	private void up() {
		if (this.goToMode || this.position > this.positions.length) {
			return;
		}
		this.position++;
		new Thread(new AutoDriver(1)).start();
	}

	private void down() {
		if (this.goToMode || this.position < 0) {
			return;
		}
		this.position--;
		new Thread(new AutoDriver(-1)).start();
	}

	public void drive(Joystick cont) {

		if (this.goToMode) {
			return;
		}

		if (cont.getRawButton(BTN_UP)) {
			this.up();
			return;
		} else if (cont.getRawButton(BTN_DOWN)) {
			this.down();
			return;
		}

		double speed = -cont.getY();

		if (speed == 0) {
			return;
		}

		if (this.encoder.get() > this.encMax) {
			return;
		} else if (this.encoder.get() < this.encMin) {
			return;
		}

		double reduction = 0;
		if (this.encoder.get() > .9 * (this.encMax - this.encMin)) {
			reduction = (this.encMax - this.encoder.get())
					/ (.1 * (this.encMax - this.encMin));
		}
		if (this.encoder.get() < .1 * (this.encMax - this.encMin)) {
			reduction = (this.encoder.get())
					/ (.1 * (this.encMax - this.encMin));
		}

		if (!this.liftDown.get() || !this.liftUp.get()) {
			speed /= 4;
		}

		for (int i = 0; i < this.talons.length; i++) {
			this.talons[i].set(speed - reduction);
		}

	}

	private class AutoDriver implements Runnable {
		private int dir = 0;

		public AutoDriver(int dir) {
			this.dir = dir;
		}

		@Override
		public void run() {
			if (this.dir == 0) {
				return;
			}
			goToMode = true;
			while (encoder.get() > positions[position]) {
				for (int i = 0; i < talons.length; i++) {
					talons[i].set(AUTO_SPEED);
				}
			}
		}

	}

}
