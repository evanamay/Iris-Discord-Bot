package me.evana.command.commands;


import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline;
import net.rithms.riot.constant.Platform;

public class RiotMain {
    private static RiotApi riotApi;



    public RiotMain(RiotApi riotApi) {
        this.riotApi = riotApi;
    }

    public MatchTimeline getMatchTimeLine(Platform platform, long matchId) throws RiotApiException{
        return riotApi.getTimelineByMatchId(platform,matchId);
    }
    public static RiotApi getRiotApi() {
        return riotApi;
    }


}
