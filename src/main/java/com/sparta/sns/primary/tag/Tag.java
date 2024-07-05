package com.sparta.sns.primary.tag;

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

    private int usageCount;

    public Tag(String name) {
        this.name = name;
        this.usageCount = 0;
    }

    public void increaseUsage() {
        this.usageCount++;
    }

    public void decreaseUsage() {
        this.usageCount--;
    }

}
