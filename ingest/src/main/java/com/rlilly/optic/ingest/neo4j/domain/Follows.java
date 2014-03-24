package com.rlilly.optic.ingest.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
public class Follows {
	@GraphId Long id;
	@StartNode User follower;
	@EndNode User user;
}
