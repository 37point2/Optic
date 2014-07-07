package com.rlilly.optic.ingest.neo4j.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Tweet {
	@GraphId Long id;
	
	@Indexed(unique=true) Long tweetId;
	
	String text;
	String esId;
	
	@Fetch User sender;
	@Fetch @RelatedTo(type="TAG") Collection<Tag> tags = new HashSet<Tag>();
	@Fetch @RelatedTo(type="MENTION") private Set<User> mentions=new HashSet<User>();
	@Fetch @RelatedTo(type="URL") private Set<Url> urls=new HashSet<Url>();
	@Fetch @RelatedTo(type="SOURCE") private Tweet source;
	
	public Tweet() {
		
	}
	
	public Tweet(long tweetId, User sender, String text, String esId){
		this.tweetId = tweetId;
		this.sender = sender;
		this.text = text;
		this.esId = esId;
	}
	
	public void addMention(User mention){
		this.mentions.add(mention);
	}
	
	public void addUrl(Url url) {
		this.urls.add(url);
	}
	
	public Long getId(){
		return id;
	}
	
	public String getEsId() {
		return esId;
	}
	
	public Long getTweetId() {
		return tweetId;
	}
	
	public User getSender() {
		return sender;
	}
	
	@Override
	public String toString() {
		return "Tweet " + tweetId +
				": " + text +
				" by " + sender;
	}
	
	public Set<User> getMentions() {
		return mentions;
	}
	
	public Set<Url> getUrls() {
		return urls;
	}
	
	public Collection<Tag> getTags() {
		return tags;
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	public void setSource(Tweet source) {
		this.source = source;
	}
	
	public Tweet getSource() {
		return source;
	}
}
