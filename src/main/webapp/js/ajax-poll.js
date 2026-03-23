// AJAX Polling for Dashboard
function pollMatches() {
    fetch('api/matches')
        .then(response => response.json())
        .then(matches => {
            // Update match cards without reloading
            // Simplified: just refresh the page content if any live changes detected
            // For a production app, we would update specific DOM elements
        })
        .catch(err => console.error('Polling error:', err));
}

// setInterval(pollMatches, 30000); // Poll every 30s
