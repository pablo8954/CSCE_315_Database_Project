package application;

public final class Attributes {
	
	static final String[] confAttr = {"ConfYearId", "ConfId", "Year", "ConferenceName", 
			"Subdivision"};
	static final String[] driveAttr = {"DriveId", "GameId", "TeamId", "DriveNum", 
			"StartQuarter", "StartClock", "StartSpot", "StartReason", "EndQuarter",
			"EndClock", "EndSpot", "Plays", "Yards", "RedZoneAttempt", "EndReason"};
	static final String[] gameAttr = {"GameId", "HomeTeamId", "AwayTeamId", "Date",
			"Attendance", "Duration", "StadiumId", "StadiumType"};
	static final String[] playAttr = {};
	static final String[] playMetricsAttr = {"PlayMetricsId","GameId","PlayNum","Attempt","Yards","FairCatch","Touchback","Downed","OutOfBounds","Onside","OnsideSuccess","TD","Fumble","FumbleLost","Safety","PasserId","RecieverId","PlayerId","Completion","Interception","FirstDown","Dropped","Blocked","Reception","Sack"}
	

}
