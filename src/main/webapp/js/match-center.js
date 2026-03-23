// Match Center Tab Handling and Data Fetching
document.querySelectorAll('.match-tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelectorAll('.match-tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
        
        tab.classList.add('active');
        const targetId = tab.getAttribute('data-tab');
        document.getElementById(targetId).classList.add('active');
    });
});

function loadCricketScorecards() {
    const containers = document.querySelectorAll('.scorecard-body');
    containers.forEach(container => {
        const inningsId = container.id.split('-').pop();
        fetch(`../api/scorecard?inningsId=${inningsId}`)
            .then(res => res.json())
            .then(data => {
                renderScorecard(container, data);
            });
    });
}

function renderScorecard(container, data) {
    if (!data.batting || data.batting.length === 0) {
        container.innerHTML = '<p style="padding: 20px; color: var(--text-3);">Scorecard not available yet.</p>';
        return;
    }
    let html = `
        <table class="scorecard-table">
            <thead>
                <tr>
                    <th>Batter</th>
                    <th class="col-runs">R</th>
                    <th class="col-balls">B</th>
                    <th class="col-4s">4s</th>
                    <th class="col-6s">6s</th>
                    <th class="col-sr">SR</th>
                </tr>
            </thead>
            <tbody>
    `;

    data.batting.forEach(b => {
        html += `
            <tr>
                <td>
                    <div class="${b.isBatting ? 'batting-now' : ''}">${b.playerName}</div>
                    <div class="dismissal">${b.dismissalText}</div>
                </td>
                <td class="col-runs">${b.runs}</td>
                <td class="col-balls">${b.ballsFaced}</td>
                <td class="col-4s">${b.fours}</td>
                <td class="col-6s">${b.sixes}</td>
                <td class="col-sr">${b.strikeRate}</td>
            </tr>
        `;
    });

    html += `</tbody></table><div class="bowling-section"><table class="scorecard-table"><thead><tr><th>Bowler</th><th class="col-o">O</th><th class="col-m">M</th><th class="col-r">R</th><th class="col-w">W</th><th class="col-eco">ECO</th></tr></thead><tbody>`;

    data.bowling.forEach(bw => {
        html += `
            <tr>
                <td>${bw.playerName}</td>
                <td class="col-o">${bw.overs}</td>
                <td class="col-m">${bw.maidens}</td>
                <td class="col-r">${bw.runsGiven}</td>
                <td class="col-w">${bw.wickets}</td>
                <td class="col-eco">${bw.economy}</td>
            </tr>
        `;
    });

    html += `</tbody></table></div>`;
    container.innerHTML = html;
}

function loadMatchDetails() {
    console.log(`Fetching details for match ${matchId}...`);
    fetch(`../api/match-details?matchId=${matchId}`)
        .then(res => {
            console.log(`API Response Status: ${res.status}`);
            return res.json();
        })
        .then(data => {
            console.log("Received data:", data);
            renderEvents(data.events);
            renderLineups(data.lineups);
        })
        .catch(err => console.error("AJAX Error:", err));
}

function renderEvents(events) {
    const container = document.querySelector('#summary .timeline-container');
    if (!container) return;
    if (!events || events.length === 0) {
        container.innerHTML = '<p style="padding: 20px; color: var(--text-3);">No events recorded yet.</p>';
        return;
    }
    let html = '';
    events.forEach(e => {
        html += `
            <div class="event-item">
                <div class="event-time">${e.eventMinute}'</div>
                <div class="event-detail">
                    <div class="event-player">${e.playerName}</div>
                    <div class="event-desc">${e.eventType}${e.eventDetail ? ' — ' + e.eventDetail : ''}</div>
                </div>
            </div>
        `;
    });
    container.innerHTML = html;
}

function renderLineups(lineups) {
    const container = document.querySelector('#lineups .stats-container');
    if (!container) return;
    if (!lineups || lineups.length === 0) {
        container.innerHTML = '<p style="padding: 20px; color: var(--text-3);">Lineup data not available yet.</p>';
        return;
    }
    
    container.innerHTML = '<h3>Match Lineups</h3><div class="lineup-grid">' + 
        lineups.map(l => `<div class="lineup-player"><strong>${l.playerName}</strong> <span>${l.position || ''}</span></div>`).join('') + 
        '</div>';
}

console.log("Match Center JS loaded. sportId:", typeof sportId !== 'undefined' ? sportId : 'null');
if (typeof sportId !== 'undefined') {
    loadMatchDetails();
    setInterval(loadMatchDetails, 60000);
}
