<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${match.team1Name} vs ${match.team2Name} — SportsVista Match Center</title>
    <link rel="stylesheet" href="../css/main.css?v=2">
    <link rel="stylesheet" href="../css/matchcenter.css?v=2">
    <link rel="stylesheet" href="../css/scorecard.css?v=2">
    <link rel="stylesheet" href="../css/stats.css?v=2">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
</head>
<body>

<header>
    <div class="header-content">
        <div class="logo">SportsVista</div>
        <nav>
            <ul>
                <li><a href="../dashboard">Dashboard</a></li>
            </ul>
        </nav>
    </div>
</header>

<main class="container">
    <div class="match-header">
        <div class="header-meta">${match.competitionName} • ${match.venue}</div>
        <div class="header-main">
            <div class="header-team">
                <img src="${not empty match.team1Logo ? match.team1Logo : 'https://placehold.co/80x80?text=T1'}" class="header-logo" alt="${match.team1Name}">
                <div class="header-team-name">${match.team1Name}</div>
                <div class="header-score">${match.scoreDisplayTeam1}</div>
            </div>
            <div class="header-vs">VS</div>
            <div class="header-team">
                <img src="${not empty match.team2Logo ? match.team2Logo : 'https://placehold.co/80x80?text=T2'}" class="header-logo" alt="${match.team2Name}">
                <div class="header-team-name">${match.team2Name}</div>
                <div class="header-score">${match.scoreDisplayTeam2}</div>
            </div>
        </div>
        <div class="header-status">${match.statusSummaryText}</div>
    </div>

    <div class="match-tabs">
        <div class="match-tab active" data-tab="summary">Summary</div>
        <div class="match-tab" data-tab="lineups">Lineups</div>
        <div class="match-tab" data-tab="info">Match Info</div>
    </div>

    <div class="tab-contents">
        <div id="summary" class="tab-content active">
            <div class="timeline-container">
                <p style="padding: 20px; color: var(--text-3);">Loading match events...</p>
            </div>
        </div>
        <div id="lineups" class="tab-content">
            <div class="stats-container">
                <p style="padding: 20px; color: var(--text-3);">Loading lineup data...</p>
            </div>
        </div>
        <div id="info" class="tab-content">
            <div class="stats-container">
                <div class="stat-row">
                    <div class="stat-name">Match Type</div>
                    <div class="stat-labels"><span>${match.matchType}</span></div>
                </div>
                <div class="stat-row">
                    <div class="stat-name">Venue</div>
                    <div class="stat-labels"><span>${match.venue}${not empty match.venueCity ? ', '.concat(match.venueCity) : ''}${not empty match.venueCountry ? ', '.concat(match.venueCountry) : ''}</span></div>
                </div>
                <div class="stat-row">
                    <div class="stat-name">Date</div>
                    <div class="stat-labels"><span>${match.matchDate}</span></div>
                </div>
                <c:if test="${not empty match.tossText}">
                <div class="stat-row">
                    <div class="stat-name">Toss</div>
                    <div class="stat-labels"><span>${match.tossText}</span></div>
                </div>
                </c:if>
                <c:if test="${not empty match.resultText}">
                <div class="stat-row">
                    <div class="stat-name">Result</div>
                    <div class="stat-labels"><span>${match.resultText}</span></div>
                </div>
                </c:if>
                <div class="stat-row">
                    <div class="stat-name">Status</div>
                    <div class="stat-labels"><span>${match.statusSummaryText}</span></div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    const matchId = ${match.id};
    const sportId = ${match.sportId};
</script>
<script src="../js/match-center.js?v=2"></script>
</body>
</html>
