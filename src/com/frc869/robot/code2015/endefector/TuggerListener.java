package com.frc869.robot.code2015.endefector;

public class TuggerListener implements EndefectorListener {

	private Tugger tugger;
	
	public TuggerListener(Tugger tugger){
		this.tugger = tugger;
			
	}
	
	@Override
	public void run(String arg) {
		switch(arg){
		case "IN":
			this.tugger.moveIn(-0.5);
			break;
		case "OUT":
			this.tugger.moveOut(0.5);
			break;
		case "NEXTIN":
		case "PREV":
			this.tugger.nextIn();
			break;
		case "NEXTOUT":
		case "NEXT":
			this.tugger.nextOut();
			break;
		default:
			break;
		}
	}
}
