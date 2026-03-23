package com.sportsvista.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sportsvista.model.BattingScorecard;
import com.sportsvista.model.BowlingScorecard;
import com.sportsvista.model.MatchInnings;
import com.sportsvista.util.SafeParser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CricketScorecardParser {

    public List<InningsBundle> parseScorecard(JsonObject response) {
        List<InningsBundle> bundles = new ArrayList<>();
        JsonObject data = SafeParser.obj(response, "data");
        if (data == null) return bundles;

        JsonArray scoreArr = SafeParser.arr(data, "score");
        JsonArray scorecardArr = SafeParser.arr(data, "scorecard");
        
        for (JsonElement sEl : scorecardArr) {
            JsonObject s = sEl.getAsJsonObject();
            InningsBundle bundle = new InningsBundle();
            
            MatchInnings innings = new MatchInnings();
            innings.setInningsNumber(bundles.size() + 1);
            
            String inningName = SafeParser.str(s, "inning");
            bundle.battingTeamName = extractTeamName(inningName);
            
            // Try to get totals from this inn object
            JsonObject tObj = SafeParser.obj(s, "totals");
            int r = SafeParser.Int(tObj, "R");
            int w = SafeParser.Int(tObj, "W");
            String o = SafeParser.str(tObj, "O");

            // Fallback to top-level score array if totals are empty
            if (r == 0 && w == 0 && (o == null || o.isEmpty() || o.equals("0"))) {
                for (JsonElement scEl : scoreArr) {
                    JsonObject sc = scEl.getAsJsonObject();
                    if (inningName.equalsIgnoreCase(SafeParser.str(sc, "inning"))) {
                        r = SafeParser.Int(sc, "r");
                        w = SafeParser.Int(sc, "w");
                        o = String.valueOf(SafeParser.str(sc, "o"));
                        break;
                    }
                }
            }

            innings.setTotalRuns(r);
            innings.setTotalWickets(w);
            innings.setTotalOvers(o);
            bundle.innings = innings;

            // Batting
            JsonArray batting = SafeParser.arr(s, "batting");
            for (JsonElement bEl : batting) {
                JsonObject bObj = bEl.getAsJsonObject();
                BattingScorecard b = new BattingScorecard();
                b.setPlayerName(SafeParser.str(SafeParser.obj(bObj, "batsman"), "name"));
                b.setRuns(SafeParser.Int(bObj, "r"));
                b.setBallsFaced(SafeParser.Int(bObj, "b"));
                b.setFours(SafeParser.Int(bObj, "4s"));
                b.setSixes(SafeParser.Int(bObj, "6s"));
                String srStr = SafeParser.str(bObj, "sr");
                b.setStrikeRate(new BigDecimal(srStr.isEmpty() || srStr.equals("-") ? "0" : srStr));
                b.setDismissalText(SafeParser.str(bObj, "dismissal-text"));
                bundle.batting.add(b);
            }

            // Bowling
            JsonArray bowling = SafeParser.arr(s, "bowling");
            for (JsonElement bwEl : bowling) {
                JsonObject bwObj = bwEl.getAsJsonObject();
                BowlingScorecard bw = new BowlingScorecard();
                bw.setPlayerName(SafeParser.str(SafeParser.obj(bwObj, "bowler"), "name"));
                bw.setOvers(String.valueOf(SafeParser.str(bwObj, "o")));
                bw.setMaidens(SafeParser.Int(bwObj, "m"));
                bw.setRunsGiven(SafeParser.Int(bwObj, "r"));
                bw.setWickets(SafeParser.Int(bwObj, "w"));
                String ecoStr = SafeParser.str(bwObj, "eco");
                bw.setEconomy(new BigDecimal(ecoStr.isEmpty() || ecoStr.equals("-") ? "0" : ecoStr));
                bundle.bowling.add(bw);
            }

            bundles.add(bundle);
        }
        System.out.println("CricketParser: parsed " + bundles.size() + " innings bundles.");
        return bundles;
    }

    private String extractTeamName(String inning) {
        if (inning == null) return "";
        // Usually "Team Name Inning 1" or "Team Name Innings 1"
        return inning.replaceAll("(?i)\\s+Innings?\\s+\\d+$", "").trim();
    }

    public static class InningsBundle {
        public MatchInnings innings;
        public String battingTeamName;
        public List<BattingScorecard> batting = new ArrayList<>();
        public List<BowlingScorecard> bowling = new ArrayList<>();
    }
}
