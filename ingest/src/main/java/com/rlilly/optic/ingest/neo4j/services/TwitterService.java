package com.rlilly.optic.ingest.neo4j.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import com.rlilly.optic.ingest.neo4j.domain.Tweet;
import com.rlilly.optic.ingest.neo4j.domain.Url;
import com.rlilly.optic.ingest.neo4j.domain.Tag;
import com.rlilly.optic.ingest.neo4j.domain.User;
import com.rlilly.optic.ingest.neo4j.repositories.TagRepository;
import com.rlilly.optic.ingest.neo4j.repositories.TweetRepository;
import com.rlilly.optic.ingest.neo4j.repositories.UrlRepository;
import com.rlilly.optic.ingest.neo4j.repositories.UserRepository;

@Service
public class TwitterService {
	private final Logger _logger = Logger.getLogger(TwitterService.class);
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	TagRepository tagRepository;
	@Autowired
	UrlRepository urlRepository;
	@Autowired
	TweetRepository tweetRepository;
	
	@Transactional
	public void importTweet(Status status) {
		final String screen_name = status.getUser().getScreenName();
		final String display_name = status.getUser().getName();
		User user = userRepository.save(new User(screen_name, display_name));
		final String text = status.getText();
		final Tweet tweet = new Tweet(status.getId(), user, text);
		_logger.info("Imported " + tweet);
		addMentions(tweet, status.getUserMentionEntities());
		addTags(tweet, status.getHashtagEntities());
		addUrls(tweet, status.getURLEntities());
		//TODO addGeo - precise and approximations
		//TODO addPlace
		addOriginalTweet(tweet, status.getInReplyToStatusId());
		tweetRepository.save(tweet);
	}
	
	private void addOriginalTweet(Tweet tweet, Long inReplyToId){
		if (inReplyToId == null) return;
		final Tweet source = tweetRepository.findByTweetId(inReplyToId);
		if (source == null) return;
		tweet.setSource(source);
	}

	private void addUrls(Tweet tweet, URLEntity[] urlEntities) {
		for (URLEntity url : urlEntities) {
			tweet.addUrl(new Url(url.getExpandedURL()));
		}
		
	}

	private void addTags(Tweet tweet, HashtagEntity[] hashtagEntities) {
		for (HashtagEntity hashtag : hashtagEntities) {
			tweet.addTag(tagRepository.save(new Tag(hashtag.getText())));
		}
	}

	private void addMentions(Tweet tweet,
			UserMentionEntity[] userMentionEntities) {
		for (UserMentionEntity mention : userMentionEntities) {
			tweet.addMention(userRepository.save(new User(mention.getScreenName(), mention.getName())));
		}
		
	}
	
	
}
