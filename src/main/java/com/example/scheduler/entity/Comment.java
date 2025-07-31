package com.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private String writer;
    private String password;
    @Setter
    private Long schedulerId;


    public Comment(String contents, String writer, String password) {
        this.comment = contents;
        this.writer = writer;
        this.password = password;
    }
}
