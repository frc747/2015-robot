package com.frc869.robot.code2015.component;

public class DigitalInput extends edu.wpi.first.wpilibj.DigitalInput {
	boolean normallyClosed;

	public DigitalInput(int channel,boolean normallyClosed) {
		super(channel);
		this.normallyClosed = normallyClosed;
	}

	@Override
	public boolean get() {
		if(normallyClosed) {
			return !super.get();
		} else {
			return super.get();
		}
	}

}
