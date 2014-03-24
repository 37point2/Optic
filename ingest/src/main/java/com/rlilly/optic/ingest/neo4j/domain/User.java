package com.rlilly.optic.ingest.neo4j.domain;

import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.GraphId;


@NodeEntity
public class User {

	@GraphId
	private Long id;
	
	@Indexed(unique=true)
	private String screen_name;
	
	@Indexed
	private String display_name;
	
	public User() {
		
	}
	
	public User(String screen_name, String display_name) {
		this.screen_name = screen_name;
		this.display_name = display_name;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getUser() {
		return screen_name;
	}
	
	@Override
	public String toString() {
		return "@" + screen_name;
	}
}
