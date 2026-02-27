package com.nazarukiv.gitview.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "github_stats")
public class GithubStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @Column(name = "total_commits")
    private Integer totalCommits = 0;

    @Column(name = "total_stars")
    private Integer totalStars = 0;

    @Column(name = "total_forks")
    private Integer totalForks = 0;

    @Column(name = "top_languages")
    private String topLanguages;

    @Column(name = "longest_streak")
    private Integer longestStreak = 0;

    @Column(name = "current_streak")
    private Integer currentStreak = 0;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt = LocalDateTime.now();
}