-- SportsVista PostgreSQL Schema (for Render deployment)
-- Run this in your Render PostgreSQL database

-- Drop all tables if they exist
DROP TABLE IF EXISTS match_info CASCADE;
DROP TABLE IF EXISTS match_stats CASCADE;
DROP TABLE IF EXISTS match_lineups CASCADE;
DROP TABLE IF EXISTS match_events CASCADE;
DROP TABLE IF EXISTS bowling_scorecards CASCADE;
DROP TABLE IF EXISTS batting_scorecards CASCADE;
DROP TABLE IF EXISTS match_innings CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS competitions CASCADE;
DROP TABLE IF EXISTS sports CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 1. Sports Table
CREATE TABLE sports (
  id SERIAL PRIMARY KEY,
  sport_key VARCHAR(30) UNIQUE NOT NULL,
  sport_name VARCHAR(50) NOT NULL,
  display_order INT DEFAULT 0
);

-- 2. Competitions Table
CREATE TABLE competitions (
  id SERIAL PRIMARY KEY,
  external_id VARCHAR(150) UNIQUE,
  competition_name VARCHAR(200),
  short_name VARCHAR(50),
  sport_id INT REFERENCES sports(id) ON DELETE CASCADE,
  competition_type VARCHAR(50),
  country VARCHAR(80),
  season VARCHAR(20),
  logo_url VARCHAR(255)
);

-- 3. Teams Table
CREATE TABLE teams (
  id SERIAL PRIMARY KEY,
  external_id VARCHAR(150) UNIQUE,
  team_name VARCHAR(120),
  short_name VARCHAR(20),
  sport_id INT REFERENCES sports(id) ON DELETE CASCADE,
  country VARCHAR(80),
  logo_url VARCHAR(255),
  team_type VARCHAR(20) DEFAULT 'club'
);

-- 4. Matches Table
CREATE TABLE matches (
  id SERIAL PRIMARY KEY,
  external_id VARCHAR(150) UNIQUE,
  sport_id INT REFERENCES sports(id) ON DELETE CASCADE,
  competition_id INT REFERENCES competitions(id) ON DELETE CASCADE,
  team1_id INT REFERENCES teams(id) ON DELETE CASCADE,
  team2_id INT REFERENCES teams(id) ON DELETE CASCADE,
  match_type VARCHAR(60),
  match_sub_type VARCHAR(60),
  status VARCHAR(30),
  score_display_team1 VARCHAR(150),
  score_display_team2 VARCHAR(150),
  status_summary_text VARCHAR(400),
  match_date TIMESTAMP,
  venue VARCHAR(200),
  venue_city VARCHAR(100),
  venue_country VARCHAR(80),
  toss_text VARCHAR(250),
  result_text VARCHAR(300),
  match_day_label VARCHAR(100),
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Match Innings (Cricket)
CREATE TABLE match_innings (
  id SERIAL PRIMARY KEY,
  match_id INT REFERENCES matches(id) ON DELETE CASCADE,
  innings_number INT,
  batting_team_id INT REFERENCES teams(id) ON DELETE CASCADE,
  total_runs INT,
  total_wickets INT,
  total_overs VARCHAR(20),
  extras INT DEFAULT 0,
  innings_status VARCHAR(20) DEFAULT 'ongoing',
  UNIQUE(match_id, innings_number)
);

-- 6. Batting Scorecards (Cricket)
CREATE TABLE batting_scorecards (
  id SERIAL PRIMARY KEY,
  innings_id INT REFERENCES match_innings(id) ON DELETE CASCADE,
  player_name VARCHAR(120),
  runs INT DEFAULT 0,
  balls_faced INT DEFAULT 0,
  fours INT DEFAULT 0,
  sixes INT DEFAULT 0,
  strike_rate DECIMAL(6,2) DEFAULT 0,
  dismissal_text VARCHAR(255),
  is_batting BOOLEAN DEFAULT FALSE,
  batting_order INT
);

-- 7. Bowling Scorecards (Cricket)
CREATE TABLE bowling_scorecards (
  id SERIAL PRIMARY KEY,
  innings_id INT REFERENCES match_innings(id) ON DELETE CASCADE,
  player_name VARCHAR(120),
  overs VARCHAR(10),
  maidens INT DEFAULT 0,
  runs_given INT DEFAULT 0,
  wickets INT DEFAULT 0,
  economy DECIMAL(5,2) DEFAULT 0
);

-- 8. Match Events
CREATE TABLE match_events (
  id SERIAL PRIMARY KEY,
  match_id INT REFERENCES matches(id) ON DELETE CASCADE,
  event_type VARCHAR(50),
  event_minute VARCHAR(15),
  event_second VARCHAR(5),
  event_detail VARCHAR(300),
  team_id INT,
  player_name VARCHAR(120),
  event_order INT
);

-- 9. Match Lineups
CREATE TABLE match_lineups (
  id SERIAL PRIMARY KEY,
  match_id INT REFERENCES matches(id) ON DELETE CASCADE,
  team_id INT REFERENCES teams(id) ON DELETE CASCADE,
  player_name VARCHAR(120),
  shirt_number INT,
  position VARCHAR(50),
  is_starter BOOLEAN DEFAULT TRUE,
  is_captain BOOLEAN DEFAULT FALSE
);

-- 10. Match Stats
CREATE TABLE match_stats (
  id SERIAL PRIMARY KEY,
  match_id INT UNIQUE REFERENCES matches(id) ON DELETE CASCADE,
  stats_json TEXT,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 11. Match Info
CREATE TABLE match_info (
  id SERIAL PRIMARY KEY,
  match_id INT UNIQUE REFERENCES matches(id) ON DELETE CASCADE,
  umpires VARCHAR(255),
  match_referee VARCHAR(120),
  tv_umpire VARCHAR(120),
  attendance INT,
  weather_conditions VARCHAR(150),
  pitch_conditions VARCHAR(150),
  toss_winner VARCHAR(120),
  toss_decision VARCHAR(50),
  series_context VARCHAR(300),
  extra_info_json TEXT
);

-- 12. Users Table
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_match_status     ON matches(status);
CREATE INDEX idx_match_sport      ON matches(sport_id);
CREATE INDEX idx_match_date       ON matches(match_date);
CREATE INDEX idx_match_external   ON matches(external_id);
CREATE INDEX idx_match_comp       ON matches(competition_id);
CREATE INDEX idx_innings_match    ON match_innings(match_id);
CREATE INDEX idx_batting_innings  ON batting_scorecards(innings_id);
CREATE INDEX idx_bowling_innings  ON bowling_scorecards(innings_id);
CREATE INDEX idx_events_match     ON match_events(match_id);
CREATE INDEX idx_lineup_match     ON match_lineups(match_id);

-- Seed Sports
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('cricket','Cricket',1);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('football','Football',2);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('american_football','American Football',3);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('baseball','Baseball',4);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('basketball','Basketball',5);
