package com.rlilly.optic.ingest.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.rlilly.optic.ingest.neo4j.domain.Tweet;

public interface TweetRepository extends GraphRepository<Tweet>{
	Tweet findByTweetId(Long id);
	Collection<Tweet> findByTagsTag(String tag);
	Collection<Tweet> findByUrlsUrl(String url);
}
