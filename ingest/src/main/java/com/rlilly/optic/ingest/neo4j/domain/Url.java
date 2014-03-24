package com.rlilly.optic.ingest.neo4j.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Url {
	@GraphId Long id;
	@Indexed(unique=true) String url;
	
	public Url() {
		
	}
	
	public Url(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	@Override
	public String toString() {
		return url;
	}
}
