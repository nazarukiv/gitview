package com.nazarukiv.gitview.service;

import com.nazarukiv.gitview.dto.GithubStatsDto;
import com.nazarukiv.gitview.dto.LanguageDto;
import com.nazarukiv.gitview.dto.ProgressDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubApiService {

    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String GITHUB_API = "https://api.github.com";

    @Cacheable(value = "github-stats", key = "#username")
    public GithubStatsDto fetchStats(String username) {
        sendProgress(username, "Looking up profile...", 10);

        Map<String, Object> user = fetchUser(username);

        sendProgress(username, "Loading repositories...", 30);
        List<Map<String, Object>> repos = fetchRepos(username);

        sendProgress(username, "Calculating languages...", 55);
        List<LanguageDto> languages = calculateLanguages(repos);

        sendProgress(username, "Crunching stats...", 75);
        int totalStars = repos.stream()
                .mapToInt(r -> (Integer) r.getOrDefault("stargazers_count", 0))
                .sum();
        int totalForks = repos.stream()
                .mapToInt(r -> (Integer) r.getOrDefault("forks_count", 0))
                .sum();

        sendProgress(username, "Done!", 100);

        GithubStatsDto dto = new GithubStatsDto();
        dto.setUsername(username);
        dto.setDisplayName((String) user.get("name"));
        dto.setAvatarUrl((String) user.get("avatar_url"));
        dto.setBio((String) user.get("bio"));
        dto.setLocation((String) user.get("location"));
        dto.setPublicRepos((Integer) user.get("public_repos"));
        dto.setFollowers((Integer) user.get("followers"));
        dto.setFollowing((Integer) user.get("following"));
        dto.setTotalStars(totalStars);
        dto.setTotalForks(totalForks);
        dto.setTotalCommits(0);
        dto.setLongestStreak(0);
        dto.setCurrentStreak(0);
        dto.setTopLanguages(languages);
        dto.setPercentile(0);
        return dto;
    }

    private Map<String, Object> fetchUser(String username) {
        String url = GITHUB_API + "/users/" + username;
        return restTemplate.getForObject(url, Map.class);
    }

    private List<Map<String, Object>> fetchRepos(String username) {
        String url = GITHUB_API + "/users/" + username + "/repos?per_page=100&sort=updated";
        Map<String, Object>[] repos = restTemplate.getForObject(url, Map[].class);
        return repos != null ? Arrays.asList(repos) : List.of();
    }

    private List<LanguageDto> calculateLanguages(List<Map<String, Object>> repos) {
        Map<String, Integer> langCount = new HashMap<>();
        for (Map<String, Object> repo : repos) {
            String lang = (String) repo.get("language");
            if (lang != null) {
                langCount.merge(lang, 1, Integer::sum);
            }
        }
        int total = langCount.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return List.of();

        return langCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(e -> new LanguageDto(
                        e.getKey(),
                        Math.round((e.getValue() * 100.0 / total) * 10.0) / 10.0,
                        getLanguageColor(e.getKey())
                ))
                .toList();
    }

    private String getLanguageColor(String language) {
        return switch (language) {
            case "Java" -> "#b07219";
            case "JavaScript" -> "#f1e05a";
            case "TypeScript" -> "#3178c6";
            case "Python" -> "#3572A5";
            case "Kotlin" -> "#A97BFF";
            case "Go" -> "#00ADD8";
            case "Rust" -> "#dea584";
            case "C++" -> "#f34b7d";
            case "CSS" -> "#563d7c";
            case "HTML" -> "#e34c26";
            default -> "#8b949e";
        };
    }

    private void sendProgress(String username, String message, int progress) {
        messagingTemplate.convertAndSend(
                "/topic/progress/" + username,
                new ProgressDto(message, progress)
        );
    }
}
