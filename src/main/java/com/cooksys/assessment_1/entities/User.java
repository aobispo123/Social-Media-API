package com.cooksys.assessment_1.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name="user_table")
public class User {

	@Id
	@GeneratedValue
	@Column(unique = true)
	private Long id;
	
	@Embedded
	private Credentials credentials;
	
	@CreationTimestamp
	private Timestamp joined;
	
	private boolean deleted;
	
	@Embedded
	private Profile profile;
	
	@OneToMany(mappedBy = "author", cascade = {CascadeType.ALL})
	private List<Tweet> tweets;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "followers_following",
        joinColumns = { @JoinColumn(name = "follower_id") },
        inverseJoinColumns = { @JoinColumn(name = "following_id") })
	private List<User> followers;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "followers")
	private List<User> following;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_likes",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "tweet_id") })
	private List<Tweet> likedTweets;

	 @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "mentionedUsers")
	private List<Tweet> mentionedTweets;
}
