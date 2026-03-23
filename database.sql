-- SportsVista Full Schema (Multi-Sport with Detailed Scorecards/Stats)

SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS sportsvista;
USE sportsvista;

-- Drop all possible project tables
DROP TABLE IF EXISTS match_info;
DROP TABLE IF EXISTS match_stats;
DROP TABLE IF EXISTS match_lineups;
DROP TABLE IF EXISTS match_events;
DROP TABLE IF EXISTS bowling_scorecards;
DROP TABLE IF EXISTS batting_scorecards;
DROP TABLE IF EXISTS match_innings;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS competitions;
DROP TABLE IF EXISTS sports;

-- 1. Sports Table
CREATE TABLE sports (
  id INT PRIMARY KEY AUTO_INCREMENT,
  sport_key VARCHAR(30) UNIQUE NOT NULL,
  sport_name VARCHAR(50) NOT NULL,
  display_order INT DEFAULT 0
);

-- 2. Competitions Table
CREATE TABLE competitions (
  id INT PRIMARY KEY AUTO_INCREMENT,
  external_id VARCHAR(150) UNIQUE,
  competition_name VARCHAR(200),
  short_name VARCHAR(50),
  sport_id INT,
  competition_type ENUM('international','domestic_league','domestic_cup','tournament'),
  country VARCHAR(80),
  season VARCHAR(20),
  logo_url VARCHAR(255),
  FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE
);

-- 3. Teams Table
CREATE TABLE teams (
  id INT PRIMARY KEY AUTO_INCREMENT,
  external_id VARCHAR(150) UNIQUE,
  team_name VARCHAR(120),
  short_name VARCHAR(20),
  sport_id INT,
  country VARCHAR(80),
  logo_url VARCHAR(255),
  team_type ENUM('national','club') DEFAULT 'club',
  FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE
);

-- 4. Matches Table
CREATE TABLE matches (
  id INT PRIMARY KEY AUTO_INCREMENT,
  external_id VARCHAR(150) UNIQUE,
  sport_id INT,
  competition_id INT,
  team1_id INT,
  team2_id INT,
  match_type VARCHAR(60),
  match_sub_type VARCHAR(60),
  status ENUM('live','upcoming','halftime','innings_break','rain_delay','completed','abandoned','postponed'),
  score_display_team1 VARCHAR(150),
  score_display_team2 VARCHAR(150),
  status_summary_text VARCHAR(400),
  match_date DATETIME,
  venue VARCHAR(200),
  venue_city VARCHAR(100),
  venue_country VARCHAR(80),
  toss_text VARCHAR(250),
  result_text VARCHAR(300),
  match_day_label VARCHAR(100),
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE,
  FOREIGN KEY (competition_id) REFERENCES competitions(id) ON DELETE CASCADE,
  FOREIGN KEY (team1_id) REFERENCES teams(id) ON DELETE CASCADE,
  FOREIGN KEY (team2_id) REFERENCES teams(id) ON DELETE CASCADE
);

-- 5. Match Innings (Primarily for Cricket)
CREATE TABLE match_innings (
  id INT PRIMARY KEY AUTO_INCREMENT,
  match_id INT,
  innings_number INT,
  batting_team_id INT,
  total_runs INT,
  total_wickets INT,
  total_overs VARCHAR(20),
  extras INT DEFAULT 0,
  innings_status ENUM('ongoing','completed','declared','forfeited') DEFAULT 'ongoing',
  FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE,
  FOREIGN KEY (batting_team_id) REFERENCES teams(id) ON DELETE CASCADE
);

-- 6. Batting Scorecards (Primarily for Cricket)
CREATE TABLE batting_scorecards (
  id INT PRIMARY KEY AUTO_INCREMENT,
  innings_id INT,
  player_name VARCHAR(120),
  runs INT DEFAULT 0,
  balls_faced INT DEFAULT 0,
  fours INT DEFAULT 0,
  sixes INT DEFAULT 0,
  strike_rate DECIMAL(6,2) DEFAULT 0,
  dismissal_text VARCHAR(255),
  is_batting BOOLEAN DEFAULT FALSE,
  batting_order INT,
  FOREIGN KEY (innings_id) REFERENCES match_innings(id) ON DELETE CASCADE
);

-- 7. Bowling Scorecards (Primarily for Cricket)
CREATE TABLE bowling_scorecards (
  id INT PRIMARY KEY AUTO_INCREMENT,
  innings_id INT,
  player_name VARCHAR(120),
  overs VARCHAR(10),
  maidens INT DEFAULT 0,
  runs_given INT DEFAULT 0,
  wickets INT DEFAULT 0,
  economy DECIMAL(5,2) DEFAULT 0,
  FOREIGN KEY (innings_id) REFERENCES match_innings(id) ON DELETE CASCADE
);

-- 8. Match Events (Goals, Cards, Wickets, Home Runs, etc.)
CREATE TABLE match_events (
  id INT PRIMARY KEY AUTO_INCREMENT,
  match_id INT,
  event_type VARCHAR(50),
  event_minute VARCHAR(15),
  event_second VARCHAR(5),
  event_detail VARCHAR(300),
  team_id INT,
  player_name VARCHAR(120),
  event_order INT,
  FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE
);

-- 9. Match Lineups
CREATE TABLE match_lineups (
  id INT PRIMARY KEY AUTO_INCREMENT,
  match_id INT,
  team_id INT,
  player_name VARCHAR(120),
  shirt_number INT,
  position VARCHAR(50),
  is_starter BOOLEAN DEFAULT TRUE,
  is_captain BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE,
  FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
);

-- 10. Match Stats (JSON for flexible storage per sport)
CREATE TABLE match_stats (
  id INT PRIMARY KEY AUTO_INCREMENT,
  match_id INT UNIQUE,
  stats_json LONGTEXT,
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE
);

-- 11. Match Info (Generic details)
CREATE TABLE match_info (
  id INT PRIMARY KEY AUTO_INCREMENT,
  match_id INT UNIQUE,
  umpires VARCHAR(255),
  match_referee VARCHAR(120),
  tv_umpire VARCHAR(120),
  attendance INT,
  weather_conditions VARCHAR(150),
  pitch_conditions VARCHAR(150),
  toss_winner VARCHAR(120),
  toss_decision VARCHAR(50),
  series_context VARCHAR(300),
  extra_info_json LONGTEXT,
  FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE
);

-- 12. Users Table
CREATE TABLE IF NOT EXISTS users (
  id INT PRIMARY KEY AUTO_INCREMENT,
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

SET FOREIGN_KEY_CHECKS = 1;

-- Seed Basic Sports
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('cricket','Cricket',1);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('football','Football',2);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('american_football','American Football',3);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('baseball','Baseball',4);
INSERT INTO sports (sport_key, sport_name, display_order) VALUES ('basketball','Basketball',5);
