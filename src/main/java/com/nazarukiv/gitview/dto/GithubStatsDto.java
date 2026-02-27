package com.nazarukiv.gitview.dto;

import lombok.Data;
import java.util.List;

@Data
public class GithubStatsDto {
    private String username;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private String location;
    private Integer publicRepos;
    private Integer followers;
    private Integer following;
    private Integer totalCommits;
    private Integer totalStars;
    private Integer totalForks;
    private Integer longestStreak;
    private Integer currentStreak;
    private List<LanguageDto> topLanguages;
    private Integer percentile;
}