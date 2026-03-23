<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Results - SportsVista</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=2">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
</head>
<body>
    <header>
        <a href="${pageContext.request.contextPath}/matches"><h1>SportsVista</h1></a>
        <nav>
            <form action="${pageContext.request.contextPath}/search" method="GET">
                <input type="text" name="q" placeholder="Search teams or matches..." value="${query}" required>
                <button type="submit">Search</button>
            </form>
        </nav>
    </header>

    <div class="container">
        <h2>Search Results for "${query}"</h2>

        <h3>Teams</h3>
        <div class="match-grid" style="margin-bottom: 3rem;">
            <c:forEach var="team" items="${teams}">
                <div class="match-card">
                    <a href="${pageContext.request.contextPath}/team?id=${team.id}" style="text-decoration: none;">
                        <img src="${not empty team.logo ? team.logo : pageContext.request.contextPath.concat('/css/default-logo.png')}" alt="Logo" style="width: 80px; height: 80px; object-fit: contain; margin-bottom: 1rem;">
                        <h4 style="color: var(--text-bright); margin: 0;">${team.name}</h4>
                    </a>
                </div>
            </c:forEach>
            <c:if test="${empty teams}">
                <p>No teams found.</p>
            </c:if>
        </div>

        <h3>Matches (Past & Upcoming)</h3>
        <div class="match-grid">
            <c:forEach var="match" items="${matches}">
                <div class="match-card">
                    <span class="league">${match.leagueName}</span>
                    <span class="status ${match.status}">${match.status} ${match.status == 'Live' ? '🔴' : (match.status == 'Upcoming' ? '⏳' : '✅')}</span>
                    
                    <div class="teams">
                        <div class="team">
                            <a href="${pageContext.request.contextPath}/team?id=${match.team1Id}">
                                <img src="${not empty match.team1Logo ? match.team1Logo : pageContext.request.contextPath.concat('/css/default-logo.png')}" alt="Logo">
                                <span class="team-name">${match.team1Name}</span>
                            </a>
                        </div>
                        <div class="score">${match.score}</div>
                        <div class="team">
                            <a href="${pageContext.request.contextPath}/team?id=${match.team2Id}">
                                <img src="${not empty match.team2Logo ? match.team2Logo : pageContext.request.contextPath.concat('/css/default-logo.png')}" alt="Logo">
                                <span class="team-name">${match.team2Name}</span>
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty matches}">
                <p>No matches found containing the team.</p>
            </c:if>
        </div>
    </div>
</body>
</html>
