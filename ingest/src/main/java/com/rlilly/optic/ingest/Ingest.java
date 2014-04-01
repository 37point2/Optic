package com.rlilly.optic.ingest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import twitter4j.Status;

import com.rlilly.optic.ingest.config.BaseConfig;
import com.rlilly.optic.ingest.config.ConfigMapper;
import com.rlilly.optic.ingest.neo4j.Neo4j;
import com.rlilly.optic.ingest.stats.Stats;
import com.rlilly.optic.ingest.twitterstream.PrintEvents;
import com.rlilly.optic.ingest.twitterstream.StreamingClient;
import com.twitter.hbc.core.event.Event;

public class Ingest 
{
private static Logger _logger = Logger.getLogger(Ingest.class);
	
	private LinkedBlockingQueue<String> _msgQueue;
	private LinkedBlockingQueue<Event> _eventQueue;
	private LinkedBlockingQueue<Status> _statusQueue;
	
	private ExecutorService _executorService;
	
	public Ingest() {
		new ConfigMapper();
		
		this.initQueues();
		
		this.initThreads();
		
		this.initStats();
	}
	
	private void initQueues() {
		_logger.info("Initializing Queues");
		
		this._msgQueue = new LinkedBlockingQueue<String>(100000);
        this._eventQueue = new LinkedBlockingQueue<Event>(1000);
        this._statusQueue = new LinkedBlockingQueue<Status>(100000);
	}
	
	private void initThreads() {
		_logger.info("Starting Threads");
		
		this._executorService = Executors.newFixedThreadPool(
				BaseConfig.threadTwitterSize
				+BaseConfig.threadNeo4jSize
				+1);
		
		for(int i=0; i<BaseConfig.threadTwitterSize; i++) {
			this._executorService.execute(new StreamingClient(this._msgQueue, this._eventQueue, this._statusQueue));
		}
		
		for(int i=0; i<BaseConfig.threadNeo4jSize; i++) {
			this._executorService.execute(new Neo4j(this._statusQueue));
		}
		
		this._executorService.execute(new PrintEvents(this._eventQueue));
	}
	
	private void initStats() {
		ScheduledThreadPoolExecutor stats = new ScheduledThreadPoolExecutor(1);
		stats.scheduleAtFixedRate(new Stats(this._statusQueue, this._msgQueue, this._eventQueue), 60000, 60000, TimeUnit.MILLISECONDS);
	}
	
    public static void main( String[] args )
    {
        System.out.println( "YOLO!" );
        
        new Ingest();
    }
}
