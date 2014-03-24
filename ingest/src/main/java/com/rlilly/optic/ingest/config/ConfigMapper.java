package com.rlilly.optic.ingest.config;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigMapper {

	private static Logger _logger = Logger.getLogger(ConfigMapper.class);
	
	public ConfigMapper() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final IngestConfig ingestConfig = mapper.readValue(new File(Base.FILE_CONFIG), IngestConfig.class);
			
			//Twitter
			BaseConfig.apiKey = ingestConfig.twitter.apiKey;
			BaseConfig.apiSecret = ingestConfig.twitter.apiSecret;
			BaseConfig.accessToken = ingestConfig.twitter.accessToken;
			BaseConfig.accessTokenSecret = ingestConfig.twitter.accessTokenSecret;
			
			//Neo4j
			BaseConfig.dbpath = ingestConfig.neo4j.dbpath;
			BaseConfig.classpath = ingestConfig.neo4j.classpath;
			
			_logger.info("Config loadded successfully");
			
		} catch (JsonParseException e) {
			_logger.error(e);
			_logger.fatal("Malformed configuration file: " + Base.FILE_CONFIG);
			System.exit(1);
		} catch (JsonMappingException e) {
			_logger.error(e);
		} catch (IOException e) {
			_logger.error(e);
		}
	}
	
}
