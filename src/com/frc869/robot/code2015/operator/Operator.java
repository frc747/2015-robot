package com.frc869.robot.code2015.operator;

//Imports necessary assets

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.frc869.robot.code2015.endefector.EndefectorListener;

/*
 * Code responsible for taking in user input and converting it to commands for the robot
 * 
 * We plan to use an Android tablet as our means of providing input
 */

public class Operator implements Runnable {

	//Establishing all variables to be used in later code
	
	private Socket tablet;
	private final String ip;
	private final int port;

	private Thread thread;

	private HashMap<String, ArrayList<EndefectorListener>> events;

	public Operator() {
		this("10.8.69.5", 5809);
	}

	//Creates object "Operator"
	
	public Operator(String ip, int port) {
		this.ip = ip;
		this.port = port;

		this.thread = new Thread(this);
		this.thread.start();

		this.events = new HashMap<String, ArrayList<EndefectorListener>>();
	}

	//Connects to tablet, reads input and triggers events
	
	public void run() {
		BufferedReader br = null;
		while (true) {
			try {
				this.tablet = new Socket(ip, port);
				br = new BufferedReader(new InputStreamReader(
						this.tablet.getInputStream()));

				String str;
				while ((str = br.readLine()) != null) {
					String[] parts = str.split(" ");

					if (parts.length < 2)
						continue;

					this.triggerEvent(parts[0], parts[1]);

				}
			} catch (IOException e) {

			}

		}
	}

	//Adds new event to be triggered by remote(tablet) 
	
	public void addEvent(String event, EndefectorListener task) {
		ArrayList<EndefectorListener> eventTasks = this.events.get(event);
		if (eventTasks == null) {
			eventTasks = new ArrayList<EndefectorListener>();
			this.events.put(event, eventTasks);
		}

		eventTasks.add(task);
	}

	//Execute stored events for a particular event name
	
	public void triggerEvent(String event, String arg) {
		ArrayList<EndefectorListener> eventTasks = this.events.get(event);

		if (eventTasks == null) {
			return;
		}

		for (EndefectorListener task : eventTasks) {
			task.run(arg);
		}

	}

}
