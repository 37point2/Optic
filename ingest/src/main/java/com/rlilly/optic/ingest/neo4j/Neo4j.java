package com.rlilly.optic.ingest.neo4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlilly.optic.ingest.config.BaseConfig;
import com.rlilly.optic.ingest.es.Client;
import com.rlilly.optic.ingest.neo4j.services.TwitterService;

import twitter4j.Status;

public class Neo4j implements Runnable{
	
	private final Logger _logger = Logger.getLogger(Neo4j.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	private Client _client = new Client();
	private LinkedBlockingQueue<Status> _statusQueue;
	private TwitterService service;

	public Neo4j(LinkedBlockingQueue<Status> statusQueue){
		_statusQueue = statusQueue;
		init();
	}
	
	private void init() {
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(BaseConfig.classpath);
		addShutdownHook(ctx);
		service = ctx.getBean(TwitterService.class);
	}
	
	private void addShutdownHook(final ClassPathXmlApplicationContext ctx) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				ctx.close();
			}
		});
	}

	private void processTweets() {
		try {
			Status status = _statusQueue.take();
			String esId = "0";
			try {
				String json = mapper.writeValueAsString(status);
				IndexResponse response = this._client.index("twitter", "tweet", json);
				esId = response.getId();
			} catch (JsonProcessingException e) {
				_logger.error(e);
			} catch (Exception e) {
				_logger.error(e);
			}
			service.importTweet(status, esId);
		} catch (InterruptedException e) {
			_logger.error(e);
		}
		
	}
	
	public void run() {
		while(true){
			processTweets();
		}
		
	}
	
}
