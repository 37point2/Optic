package com.rlilly.optic.ingest.stats;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import twitter4j.Status;

import com.twitter.hbc.core.event.Event;

public class Stats implements Runnable {

	Logger _logger = Logger.getLogger(Stats.class);
	
	LinkedBlockingQueue<Status> _statusQueue;
	LinkedBlockingQueue<String> _msgQueue;
	LinkedBlockingQueue<Event> _eventQueue;
	
	public Stats(LinkedBlockingQueue<Status> statusQueue, LinkedBlockingQueue<String> msgQueue, LinkedBlockingQueue<Event> eventQueue) {
		_statusQueue = statusQueue;
		_msgQueue = msgQueue;
		_eventQueue = eventQueue;
	}
	
	private void printStats() {
		String msg = "Stats: ";
		if (this._statusQueue.size() > 0) msg += "Tweet Queue: " + this._statusQueue.size() + ", ";
		if (this._msgQueue.size() > 0) msg += "Message Queue: " + this._msgQueue.size() + ", ";
		if (this._eventQueue.size() > 0) msg += "Event Queue: " + this._eventQueue.size() + ", ";
		
		_logger.info(msg);
	}
	
	public void run() {
		printStats();
		
	}

}
