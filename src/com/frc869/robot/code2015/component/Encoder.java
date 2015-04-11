package com.frc869.robot.code2015.component;

public class Encoder extends edu.wpi.first.wpilibj.Encoder {
	private int offset;
	public Encoder(int aChannel, int bChannel, boolean reverseDirection, EncodingType encodingType) {
		super(aChannel, bChannel, reverseDirection, encodingType);
		offset = 0;
	}
	@Override
	public int get() {
		return super.get()+getOffset();
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getOffset() {
		return offset;
	}
}
