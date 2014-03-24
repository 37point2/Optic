package com.rlilly.optic.ingest.neo4j.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.rlilly.optic.ingest.neo4j.domain.User;

public interface UserRepository extends GraphRepository<User>{

}
