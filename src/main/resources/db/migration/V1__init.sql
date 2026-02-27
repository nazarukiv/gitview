CREATE TABLE user_profile (
                              id BIGSERIAL PRIMARY KEY,
                              github_username VARCHAR(100) NOT NULL UNIQUE,
                              display_name VARCHAR(200),
                              avatar_url TEXT,
                              bio TEXT,
                              location VARCHAR(200),
                              public_repos INTEGER DEFAULT 0,
                              followers INTEGER DEFAULT 0,
                              following INTEGER DEFAULT 0,
                              created_at TIMESTAMP DEFAULT NOW(),
                              updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE github_stats (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT REFERENCES user_profile(id),
                              total_commits INTEGER DEFAULT 0,
                              total_stars INTEGER DEFAULT 0,
                              total_forks INTEGER DEFAULT 0,
                              top_languages TEXT,
                              longest_streak INTEGER DEFAULT 0,
                              current_streak INTEGER DEFAULT 0,
                              fetched_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_user_profile_username ON user_profile(github_username);
CREATE INDEX idx_github_stats_user_id ON github_stats(user_id);