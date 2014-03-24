package com.rlilly.optic.ingest.neo4j.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.rlilly.optic.ingest.neo4j.domain.Url;

public interface UrlRepository extends GraphRepository<Url>{

}
