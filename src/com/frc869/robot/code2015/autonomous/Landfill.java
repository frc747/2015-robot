package com.frc869.robot.code2015.autonomous;

import com.frc869.robot.code2015.controllable.Tugger;

public class Landfill extends Autonomous {
	@Override
	public void run() {
		switch (getStep()) {
		default:
			stop();
			break;
		case 0:
			if (this.getTuggers().setPosition(Tugger.Position.STRAIGHT)) {
				increaseStep();
			}
			break;
		case 1:
			if (this.getMecanumDrive().autoTurn(-1, 90)) {
				increaseStep();
			}
			break;
		}
	}
}
