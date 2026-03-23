<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SportsVista — Live Professional Sports Scores</title>
    <link rel="stylesheet" href="css/main.css?v=2">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
</head>
<body>

<header>
    <div class="header-content">
        <div class="logo">SportsVista</div>
        <nav>
            <ul>
                <li><a href="dashboard" class="${empty param.sportId ? 'active' : ''}">All Sports</a></li>
                <c:forEach var="sport" items="${sports_all}">
                    <li><a href="dashboard?sportId=${sport.id}" class="${not empty param.sportId and param.sportId == sport.id ? 'active' : ''}">${sport.sportName}</a></li>
                </c:forEach>
            </ul>
        </nav>
        <div class="user-controls">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span class="user-name">Hi, ${sessionScope.user.fullName}</span>
                    <a href="logout" class="btn-logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="login" class="btn-auth">Sign In</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

<main class="container">
    <c:forEach var="sport" items="${sports}">
        <c:set var="sportMatches" value="${matchesBySport[sport.sportKey]}" />
        <c:if test="${not empty sportMatches}">
            <section class="sport-section">
                <h2 class="section-title">${sport.sportName}</h2>
                <div class="match-grid">
                    <c:forEach var="match" items="${sportMatches}">
                        <a href="match/${match.id}" class="match-card">
                            <div class="match-top">
                                <span>${match.competitionName} • ${match.matchDayLabel}</span>
                                <span class="status-badge status-${match.status}">${match.status}</span>
                            </div>
                            <div class="match-teams">
                                <div class="team-row">
                                    <div class="team-info">
                                        <img src="${not empty match.team1Logo ? match.team1Logo : 'https://placehold.co/24x24?text=T1'}" class="team-logo" alt="${match.team1Name}">
                                        <span class="team-name">${match.team1Name}</span>
                                    </div>
                                    <span class="team-score">${match.scoreDisplayTeam1}</span>
                                </div>
                                <div class="team-row">
                                    <div class="team-info">
                                        <img src="${not empty match.team2Logo ? match.team2Logo : 'https://placehold.co/24x24?text=T2'}" class="team-logo" alt="${match.team2Name}">
                                        <span class="team-name">${match.team2Name}</span>
                                    </div>
                                    <span class="team-score">${match.scoreDisplayTeam2}</span>
                                </div>
                            </div>
                            <div class="match-status-text">${match.statusSummaryText}</div>
                            <div class="match-footer">
                                ${match.venue} ${not empty match.venueCity ? ', '.concat(match.venueCity) : ''}
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </section>
        </c:if>
    </c:forEach>
    
    <c:if test="${empty matchesBySport}">
        <section class="no-matches">
            <p>No live matches currently available. Check back soon!</p>
        </section>
    </c:if>
</main>

<div class="ticker-wrap">
    <div class="ticker">
        <c:forEach var="sport" items="${sports}">
            <c:forEach var="match" items="${matchesBySport[sport.sportKey]}">
                <div class="ticker-item">
                    <span>${sport.sportName}:</span> ${match.team1Name} ${match.scoreDisplayTeam1} vs ${match.team2Name} ${match.scoreDisplayTeam2} • ${match.statusSummaryText}
                </div>
            </c:forEach>
        </c:forEach>
    </div>
</div>

<script src="js/ajax-poll.js"></script>
</body>
</html>
