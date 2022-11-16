package com.cooksys.assessment_1.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet {
    
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;
    
    @CreationTimestamp
    private Timestamp posted;

    private boolean deleted = false;

    private String content;

    @ManyToOne
    private Tweet inReplyTo;
    
    @OneToMany(mappedBy = "inReplyTo")
    private List<Tweet> replies;

    @ManyToOne
    private Tweet repostOf;
    
    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "tweet_hashtags",
        joinColumns = { @JoinColumn(name = "tweet_id") },
        inverseJoinColumns = { @JoinColumn(name = "hashtag_id") })
    private List<Hashtag> hashtags;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likedTweets")
    private List<User> likedByUsers;
    
    @ManyToMany
    @JoinTable(name = "user_mentions",
        joinColumns = { @JoinColumn(name = "tweet_id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private List<User> mentionedUsers;

}
