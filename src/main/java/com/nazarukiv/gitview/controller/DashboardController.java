package com.nazarukiv.gitview.controller;

import com.nazarukiv.gitview.dto.GithubStatsDto;
import com.nazarukiv.gitview.service.GitHubApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final GitHubApiService gitHubApiService;

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/dashboard/{username}")
    public String dashboard(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        return "forward:/dashboard.html";
    }


    @GetMapping("/api/stats/{username}")
    @ResponseBody
    public ResponseEntity<GithubStatsDto> getStats(@PathVariable String username) {
        try {
            GithubStatsDto stats = gitHubApiService.fetchStats(username);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching stats for {}: {}", username, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/u/{username}")
    public String publicProfile(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        return "forward:/dashboard.html";
    }

    @GetMapping("/card/{username}")
    public String card(@PathVariable String username) {
        return "forward:/card.html";
    }
}