package com.nazarukiv.gitview.repository;

import com.nazarukiv.gitview.model.GithubStats;
import com.nazarukiv.gitview.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GithubStatsRepository extends JpaRepository<GithubStats, Long> {
    Optional<GithubStats> findByUser(UserProfile user);
    Optional<GithubStats> findByUserGithubUsername(String githubUsername);
}