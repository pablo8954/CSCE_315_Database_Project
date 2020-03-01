package application;

public final class Attributes {

	static final String[] confAttr = { "ConfYearId", "ConfId", "Year", "ConferenceName", "Subdivision" };
	static final String[] driveAttr = { "DriveId", "GameId", "TeamId", "DriveNum", "StartQuarter", "StartClock",
			"StartSpot", "StartReason", "EndQuarter", "EndClock", "EndSpot", "Plays", "Yards", "RedZoneAttempt",
			"EndReason" };
	static final String[] gameAttr = { "GameId", "HomeTeamId", "AwayTeamId", "Date", "Attendance", "Duration",
			"StadiumId", "StadiumType" };
	static final String[] playAttr = { "PlayId", "GameId", "OffenseTeamId", "DefenseTeamId", "Quarter", "Clock",
			"OffensePoints", "DefensePoints", "Down", "Distance", "Spot", "PlayType", "DriveNumber", "DrivePlay",
			"PlayNum" };
	static final String[] playMetricsAttr = { "PlayMetricsId", "GameId", "PlayNum", "Attempt", "Yards", "FairCatch",
			"Touchback", "Downed", "OutOfBounds", "Onside", "OnsideSuccess", "TD", "Fumble", "FumbleLost", "Safety",
			"PasserId", "RecieverId", "PlayerId", "Completion", "Interception", "FirstDown", "Dropped", "Blocked",
			"Reception", "Sack" };
	static final String[] playerAttr = { "PlayerId", "FirstName", "LastName" };
	static final String[] playerMetricsGamewiseAttr = { "PlayerMetricsGameId", "GameId", "PlayerId", "YardsRush",
			"YardsPass", "YardsKickoffReturn", "YardsPuntReturn", "YardsFumbleReturn", "YardsInterceptionReturn",
			"YardsMisc", "YardsPunt", "YardsKickoff", "YardsTackleLoss", "YardsSack", "TacklesSolo", "TacklesAssist",
			"TacklesForLoss", "InterceptionPass", "InterceptionReturn", "Fumbles", "FumblesLost", "FumblesForced",
			"AttemptsRush", "AttemptsPass", "AttemptsFG", "AttemptsOffenseXPKick", "AttemptsOffense2XP", "TDRush",
			"TDKickoffReturn", "TDPuntReturn", "TDFumbleReturn", "TDInterceptionReturn", "TDMIscReturn",
			"PassCompletion", "MiscReturn", "MadeFG", "MadeOffensiveXPKick", "MadeOffensive2XP", "MadeDefensive2XP",
			"Safety", "Points", "Punts", "Kickoffs", "KickoffsOutOfBounds", "KickoffsOnside", "Sacks", "QBHurry",
			"PassBrokenUp", "KickPuntBlocked", "TDPass", "AttemptsDefense2XP", "ConvPass", "Reception",
			"YardsReception", "TDReception", "KickReturn", "PuntReturn", "FumbleReturn", "Touchbacks" };
	static final String[] playerYearwiseAttr = { "PlayerYearId", "PlayerId", "TeamYearId", "Uniform", "Class",
			"Position", "Height", "Weight", "LastSchool", "Hometown", "HomeState", "HomeCountry", "Year" };
	static final String[] stadiumYearwiseAttr = { "StadiumYearId", "StadiumId", "StadiumName", "StadiumCity", "StadiumState",
			"StadiumCapacity", "StadiumSurface", "StadiumYearOpened", "TeamYearId", "Year" };
	static final String[] teamAttr = { "TeamId", "TeamName" };
	static final String[] teamYearwiseAttr = { "TeamYearId", "TeamId", "ConfYearId", "AverageAttendance", "ConfId",
			"Year" };
	static final String[] teamMetricsGamewiseAttr = { "TeamMetricsGameId", "GameId", "TeamId", "Result", "YardsRush",
			"YardsPass", "YardsKickoffReturn", "YardsPuntReturn", "YardsFumbleReturn", "YardsInterceptionReturn",
			"YardsMiscReturn", "YardsPunt", "YardsKickoff", "YardsTackleLoss", "YardsSack", "YardsPenalty",
			"TacklesSolo", "TacklesAssist", "TacklesForLoss", "InterceptionsPass", "InterceptionsReturn", "Fumbles",
			"FumblesLost", "FumblesForced", "AttemptsRush", "AttemptsPass", "AttemptsFG", "AttemptsOffenseXPKick",
			"AttemptsOffense2XP", "AttemptsThirdDown", "AttemptsFourthDown", "AttemptsRedZone", "TDRush",
			"TDKickoffReturn", "TDPuntReturn", "TDFumbleReturn", "TDInterceptionReturn", "TDMiscReturn", "TDRedZone",
			"ConvPass", "ConvThirdDown", "ConvFourthDown", "PassCompletion", "MiscReturn", "MadeFG",
			"MadeOffensiveXPKick", "MadeOffensive2XP", "MadeDefensive2XP", "Safety", "Points", "Punts",
			"KickoffsReturn", "KickoffsOutOfBounds", "KickoffsOnside", "KickoffTouchBacks", "Sacks", "QBHurry",
			"PassBrokenUp", "KickPuntBlocked", "FirstDownRush", "FirstDownPass", "FirstDownPenalty", "TimeOfPossession",
			"Penalty", "RedZoneFG", "TDPass", "AttemptsDefense2XP", "PuntReturn", "FumbleReturn", "Kickoff",
			"OpposingTeamId" };
}
