package com.rlilly.optic.ingest.twitterstream;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.hbc.core.event.Event;

public class PrintEvents implements Runnable{

	private static Logger _logger = Logger.getLogger(PrintEvents.class);
	private LinkedBlockingQueue<Event> _eventQueue;
	
	ObjectMapper mapper = new ObjectMapper();
	
	public PrintEvents(LinkedBlockingQueue<Event> eventQueue){
		_eventQueue = eventQueue;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Event event = _eventQueue.take();
				_logger.info(mapper.writeValueAsString(event));
			} catch (InterruptedException e) {
				_logger.error(e);
			} catch (JsonProcessingException e) {
				_logger.error(e);
			}
		}
	}

}
