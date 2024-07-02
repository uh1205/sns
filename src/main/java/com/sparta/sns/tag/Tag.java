package com.sparta.sns.tag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private int usage; // 사용량

    public Tag(String name) {
        this.name = name;
        this.usage = 0;
    }

    public void increaseUsage() {
        this.usage++;
    }

    public void decreaseUsage() {
        this.usage--;
    }

}
