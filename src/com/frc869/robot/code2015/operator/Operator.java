package com.frc869.robot.code2015.operator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.frc869.robot.code2015.endefector.EndefectorListener;

public class Operator implements Runnable {

	private Socket tablet;
	private final String ip;
	private final int port;

	private Thread thread;

	private HashMap<String, ArrayList<EndefectorListener>> events;

	public Operator() {
		this("10.8.69.5", 869);
	}

	public Operator(String ip, int port) {
		this.ip = ip;
		this.port = port;

		this.thread = new Thread(this);
		this.thread.start();

		this.events = new HashMap<String, ArrayList<EndefectorListener>>();
	}

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

	public void addEvent(String event, EndefectorListener task) {
		ArrayList<EndefectorListener> eventTasks = this.events.get(event);
		if (eventTasks == null) {
			eventTasks = new ArrayList<EndefectorListener>();
			this.events.put(event, eventTasks);
		}

		eventTasks.add(task);
	}

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
