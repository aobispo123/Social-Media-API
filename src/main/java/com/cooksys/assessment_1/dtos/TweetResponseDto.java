package com.cooksys.assessment_1.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {
        
    private Long id;

    private UserResponseDto author;

    private Timestamp posted;

    private boolean deleted;

    private String content;

    private TweetResponseDto inReplyTo;

    private TweetResponseDto repostOf;

}
