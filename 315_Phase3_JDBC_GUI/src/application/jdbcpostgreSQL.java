package application;
import java.sql.*;
import java.time.Year;

import javax.swing.JOptionPane;

//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQL {

    public static String gameDataFetcWithNameYear(String name, Integer year, Connection conn) {
        String result_str = "";
        try {
            Statement stmt = conn.createStatement();

            String sqlStmt = "";

            /*
             * 4 cases
             * 
             * 1: proper name and year inputs are provided -> return game data for a team in
             * a specific year 2: only year is provided -> return game data pertaining to
             * specific year 3: only name is provided -> return game data pertaining to
             * specific team 4: no name or year inputs are provided -> return all game data
             * available
             * 
             */

            if (!name.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\",\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\" WHERE EXTRACT(YEAR FROM\"Date\")=%s AND(\"HomeTeamId\"=(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')OR\"AwayTeamId\"=(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')))AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data.\"StadiumId\";",
                        year.toString(), name, name);
            } else if (name.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\",\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\" WHERE EXTRACT(YEAR FROM\"Date\")=%s)AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data.\"StadiumId\";",
                        year.toString());
            } else if (!name.equals("") && (year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\",\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\" WHERE (\"HomeTeamId\"=(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')OR\"AwayTeamId\"=(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')))AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data.\"StadiumId\";",
                        name, name);
            } else if (name.equals("") && (year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\",\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\")AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data.\"StadiumId\";",
                        year.toString(), name, name);
            }

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Home Team Name: ";
                result_str += result.getString("HomeTeamName");
                result_str += " | Away Team Name: ";
                result_str += result.getString("AwayTeamName");
                result_str += " | Date: ";
                result_str += result.getString("Date");
                result_str += " | Attendance: ";
                result_str += result.getString("Attendance");
                result_str += " | Duration: ";
                result_str += result.getString("Duration");
                result_str += " | Stadium: ";
                result_str += result.getString("StadiumName");
                result_str += "\n";
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Game Data. Make sure that the Team name and Year is correct");
        }
        if (result_str == "") {
            return "Game Data non existent\n";
        }
        return result_str;
    }

    public static String stadiumDataFetch(String name, Connection conn) {
        String result_str = "";
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = String.format(
                    "SELECT DISTINCT \"StadiumName\",\"StadiumSurface\",\"StadiumCity\",\"StadiumState\",\"StadiumCapacity\",\"StadiumYearOpened\" FROM \"Stadium_Yearwise\" WHERE \"StadiumId\" = ( SELECT DISTINCT \"StadiumId\" FROM \"Stadium_Yearwise\" WHERE \"StadiumName\" LIKE '%s%%');",
                    name);
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                result_str += "Stadium Name: ";
                result_str += result.getString("StadiumName");
                result_str += " | Surface: ";
                result_str += result.getString("StadiumSurface");
                result_str += " | City: ";
                result_str += result.getString("StadiumCity");
                result_str += " | State: ";
                result_str += result.getString("StadiumState");
                result_str += " | Capacity: ";
                result_str += result.getString("StadiumCapacity");
                result_str += " | Year Opened: ";
                result_str += result.getString("StadiumYearOpened");
                result_str += "\n";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Stadium Data. Make sure that the stadium name is correct");
        }
        if (result_str == "") {
            return "Stadium Data non existent\n";
        }
        return result_str;
    }

    public static String ConfDataFetch(String name, Connection conn) {
        String result_str = "";
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = String.format(
                    "SELECT DISTINCT \"Subdivision\", \"ConferenceName\" FROM \"Conference_Yearwise\" WHERE \"ConfId\" = ( SELECT DISTINCT \"ConfId\" FROM \"Conference_Yearwise\" WHERE \"ConferenceName\" LIKE '%s%%');",
                    name);
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                result_str += "Conference Name: ";
                result_str += result.getString("ConferenceName");
                result_str += " | Subdivision: ";
                result_str += result.getString("Subdivision");
                // result_str += " | Year: ";
                // result_str += result.getString("Year");
                result_str += "\n";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Conference Data. Make sure that the Conference name is correct");
        }
        if (result_str == "") {
            return "Conference Data non existent\n";
        }
        return result_str;
    }

    public static String generalTeam(String name, Connection conn) {
        String result_str = "";
        try {
            Statement stmt = conn.createStatement();
            String sqlStmt = "";

            sqlStmt = String.format(
                    "SELECT DISTINCT \"TeamName\", \"AverageAttendance\", \"ConferenceName\", \"Subdivision\", \"Conference_Yearwise\".\"Year\" FROM \"Conference_Yearwise\" INNER JOIN (SELECT * FROM \"Team_Yearwise\" INNER JOIN (SELECT * FROM \"Team\" WHERE \"TeamName\" LIKE �%s%%�) AS Team_Data_Inner ON Team_Data_Inner.\"TeamId\" = \"Team_Yearwise\".\"TeamId\") AS Team_Data ON \"Conference_Yearwise\".\"ConfYearId\"=Team_Data.\"ConfYearId\";",
                    name);

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Team Name: ";
                result_str += result.getString("TeamName");

                result_str += " | Average Attendance: ";
                result_str += result.getString("AverageAttendence");

                result_str += " | Conference Name: ";
                result_str += result.getString("ConferenceName");

                result_str += " | Subdivision: ";
                result_str += result.getString("Subdivision");

                result_str += " |Conference Yearwise: ";
                result_str += result.getString("Confererence_Yearwise");

                result_str += " | Year: ";
                result_str += result.getString("Year");

                result_str += "\n";
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Conference Data. Make sure that the Conference name is correct");
        }
        if (result_str == "") {
            return "Team Data non existent\n";
        }
        return result_str;
    }

    public static String teamGameData(String team, String awayteam, Integer year, Connection conn) {
        // team is the home team

        String result_str = "";
        String sqlStmt = "";

        try {
            Statement stmt = conn.createStatement();

            /*
             * 4 cases 1: Home team, Away Team, and Year provided 2: Home team, Away Team
             * Provided 3: Home team, Year Provided 4: Only home team name provided
             */

            if (!team.equals("") && !awayteam.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\", \"AwayTeamName\", \"Result\" AS \"Result for Team 1\", \"YardsPass\", \"YardsKickoffReturn\", \"YardsPuntReturn\", "
                                + "\"YardsFumbleReturn\", \"YardsInterceptionReturn\",\"YardsMiscReturn\", \"YardsPunt\", \"YardsKickoff\", \"YardsTackleLoss\", \"YardsSack\", \"YardsPenalty\", "
                                + "\"TacklesSolo\", \"TacklesAssist\", \"TacklesForLoss\", \"InterceptionsPass\", \"InterceptionsReturn\", \"Fumbles\", \"FumblesLost\", \"FumblesForced\", \"AttemptsRush\", "
                                + "\"AttemptsPass\", \"AttemptsFG\", \"AttemptsOffenseXPKick\", \"AttemptsOffense2XP\", \"AttemptsThirdDown\", \"AttemptsFourthDown\", \"AttemptsRedZone\", \"TDRush\", "
                                + "\"TDKickoffReturn\", \"TDPuntReturn\", \"TDFumbleReturn\", \"TDInterceptionReturn\", \"TDMiscReturn\", \"TDRedZone\", \"ConvPass\", \"ConvThirdDown\", \"ConvFourthDown\", "
                                + "\"PassCompletion\", \"MiscReturn\", \"MadeFG\", \"MadeOffensiveXPKick\", \"MadeOffensive2XP\", \"MadeDefensive2XP\", \"Safety\", \"Points\", \"Punts\", \"KickoffsReturn\", "
                                + "\"KickoffsOutOfBounds\", \"KickoffsOnside\", \"KickoffTouchBacks\", \"Sacks\", \"QBHurry\", \"PassBrokenUp\", \"KickPuntBlocked\", \"FirstDownRush\", \"FirstDownPass\", "
                                + "\"FirstDownPenalty\", \"TimeOfPossession\", \"Penalty\", \"RedZoneFG\", \"TDPass\", \"AttemptsDefense2XP\", \"PuntReturn\", \"FumbleReturn\", \"Kickoff\" "

                                + "FROM "
                                + "\"Team_Metrics_Gamewise\" INNER JOIN(SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", * FROM \"Team\" INNER JOIN (SELECT \"TeamName\" AS \"HomeTeamName\", "
                                + "* FROM \"Team\" INNER JOIN (SELECT * FROM \"Game\" WHERE EXTRACT(YEAR FROM \"Date\") = %s AND((\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE "
                                + "\"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')) AND (\"HomeTeamId\" = ( SELECT "
                                + "DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT\"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%'"
                                + ")))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\"."
                                + "\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        year.toString(), team, team, awayteam, awayteam, team); // TODO: VERIFY CORRECTNESS - parameter
                                                                                // places in order of appearance in sql
                                                                                // query
            }

            else if (!team.equals("") && !awayteam.equals("") && (year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\", \"AwayTeamName\", \"Result\" AS \"Result for Team 1\", \"YardsPass\", \"YardsKickoffReturn\", \"YardsPuntReturn\", "
                                + "\"YardsFumbleReturn\", \"YardsInterceptionReturn\", \"YardsMiscReturn\", \"YardsPunt\", \"YardsKickoff\", \"YardsTackleLoss\", \"YardsSack\", \"YardsPenalty\", "
                                + "\"TacklesSolo\", \"TacklesAssist\", \"TacklesForLoss\", \"InterceptionsPass\", \"InterceptionsReturn\", \"Fumbles\", \"FumblesLost\", \"FumblesForced\", \"AttemptsRush\","
                                + " \"AttemptsPass\", \"AttemptsFG\", \"AttemptsOffenseXPKick\", \"AttemptsOffense2XP\", \"AttemptsThirdDown\", \"AttemptsFourthDown\", \"AttemptsRedZone\", \"TDRush\", "
                                + "\"TDKickoffReturn\", \"TDPuntReturn\", \"TDFumbleReturn\", \"TDInterceptionReturn\", \"TDMiscReturn\", \"TDRedZone\", \"ConvPass\", \"ConvThirdDown\", \"ConvFourthDown\","
                                + " \"PassCompletion\", \"MiscReturn\", \"MadeFG\", \"MadeOffensiveXPKick\", \"MadeOffensive2XP\", \"MadeDefensive2XP\", \"Safety\", \"Points\", \"Punts\", \"KickoffsReturn\","
                                + " \"KickoffsOutOfBounds\", \"KickoffsOnside\", \"KickoffTouchBacks\", \"Sacks\", \"QBHurry\", \"PassBrokenUp\", \"KickPuntBlocked\", \"FirstDownRush\", \"FirstDownPass\", "
                                + "\"FirstDownPenalty\", \"TimeOfPossession\", \"Penalty\", \"RedZoneFG\", \"TDPass\", \"AttemptsDefense2XP\", \"PuntReturn\", \"FumbleReturn\", \"Kickoff\" FROM "
                                + "\"Team_Metrics_Gamewise\" INNER JOIN( SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", * FROM \"Team\" INNER JOIN ( SELECT \"TeamName\" AS \"HomeTeamName\", * FROM "
                                + "\"Team\" INNER JOIN (SELECT * FROM \"Game\" WHERE((\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE	\"TeamName\" LIKE '%s%%')OR \"AwayTeamId\" = "
                                + "( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')) AND (\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" "
                                + "LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS "
                                + "team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\".\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" "
                                + "= ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        team, team, team, awayteam, team);
            }

            else if (!team.equals("") && awayteam.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\", \"AwayTeamName\", \"Result\" AS \"Result for Team 1\", \"YardsPass\", \"YardsKickoffReturn\", \"YardsPuntReturn\", "
                                + "\"YardsFumbleReturn\", \"YardsInterceptionReturn\", \"YardsMiscReturn\", \"YardsPunt\", \"YardsKickoff\", \"YardsTackleLoss\", \"YardsSack\", \"YardsPenalty\", "
                                + "\"TacklesSolo\", \"TacklesAssist\", \"TacklesForLoss\", \"InterceptionsPass\", \"InterceptionsReturn\", \"Fumbles\", \"FumblesLost\", \"FumblesForced\", \"AttemptsRush\", "
                                + "\"AttemptsPass\", \"AttemptsFG\", \"AttemptsOffenseXPKick\", \"AttemptsOffense2XP\", \"AttemptsThirdDown\", \"AttemptsFourthDown\", \"AttemptsRedZone\", \"TDRush\", "
                                + "\"TDKickoffReturn\", \"TDPuntReturn\", \"TDFumbleReturn\", \"TDInterceptionReturn\", \"TDMiscReturn\", \"TDRedZone\", \"ConvPass\", \"ConvThirdDown\", \"ConvFourthDown\", "
                                + "\"PassCompletion\", \"MiscReturn\", \"MadeFG\", \"MadeOffensiveXPKick\", \"MadeOffensive2XP\", \"MadeDefensive2XP\", \"Safety\", \"Points\", \"Punts\", \"KickoffsReturn\", "
                                + "\"KickoffsOutOfBounds\", \"KickoffsOnside\", \"KickoffTouchBacks\", \"Sacks\", \"QBHurry\", \"PassBrokenUp\", \"KickPuntBlocked\", \"FirstDownRush\", \"FirstDownPass\","
                                + " \"FirstDownPenalty\", \"TimeOfPossession\", \"Penalty\", \"RedZoneFG\", \"TDPass\", \"AttemptsDefense2XP\", \"PuntReturn\", \"FumbleReturn\", \"Kickoff\" "

                                + "FROM \"Team_Metrics_Gamewise\" INNER JOIN(SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", * FROM \"Team\" INNER JOIN ( SELECT \"TeamName\" AS \"HomeTeamName\", "
                                + "* FROM \"Team\" INNER JOIN (SELECT *	FROM \"Game\" WHERE EXTRACT(YEAR FROM \"Date\") = %s AND((\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE "
                                + "\"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')))) AS game_data ON \"TeamId\" = \"HomeTeamId\") "
                                + "AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\".\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" "
                                + "= ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        year.toString(), team, team, team);
            }

            else {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\", \"AwayTeamName\", \"Result\" AS \"Result for Team 1\", \"YardsPass\", \"YardsKickoffReturn\", \"YardsPunt"
                                + "Return\", \"YardsFumbleReturn\", \"YardsInterceptionReturn\", \"YardsMiscReturn\", \"YardsPunt\", \"YardsKickoff\", \"YardsTackleLoss\", \"YardsSack\", "
                                + "\"YardsPenalty\", \"TacklesSolo\", \"TacklesAssist\", \"TacklesForLoss\", \"InterceptionsPass\", \"InterceptionsReturn\", \"Fumbles\","
                                + "\"FumblesLost\", \"FumblesForced\", \"AttemptsRush\", \"AttemptsPass\", \"AttemptsFG\", \"AttemptsOffenseXPKick\","
                                + "\"AttemptsOffense2XP\", \"AttemptsThirdDown\", \"AttemptsFourthDown\", \"AttemptsRedZone\", \"TDRush\","
                                + "\"TDKickoffReturn\", \"TDPuntReturn\", \"TDFumbleReturn\", \"TDInterceptionReturn\", \"TDMiscReturn\", \"TDRedZone\","
                                + "\"ConvPass\", \"ConvThirdDown\", \"ConvFourthDown\", \"PassCompletion\", \"MiscReturn\", \"MadeFG\","
                                + "\"MadeOffensiveXPKick\", \"MadeOffensive2XP\", \"MadeDefensive2XP\", \"Safety\", \"Points\", \"Punts\","
                                + "\"KickoffsReturn\", \"KickoffsOutOfBounds\", \"KickoffsOnside\", \"KickoffTouchBacks\", \"Sacks\", \"QBHurry\","
                                + "\"PassBrokenUp\", \"KickPuntBlocked\", \"FirstDownRush\", \"FirstDownPass\", \"FirstDownPenalty\", \"TimeOfPossession\","
                                + "\"Penalty\", \"RedZoneFG\", \"TDPass\", \"AttemptsDefense2XP\", \"PuntReturn\", \"FumbleReturn\", \"Kickoff\" FROM \"Team_Metrics_Gamewise\" INNER JOIN("

                                + "SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\",* FROM \"Team\" INNER JOIN ( SELECT \"TeamName\" AS \"HomeTeamName\", * FROM \"Team\" INNER JOIN ( "
                                + "SELECT * FROM \"Game\" WHERE(\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT "
                                + "DISTINCT \"TeamId\" FROM \"Team\" WHERE\"TeamName\" LIKE '%s%%'))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\" = "
                                + "\"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\".\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" = ( SELECT DISTINCT \"TeamId"
                                + "\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        team);
            }

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Home Team Name: ";
                result_str += result.getString("HomeTeamName");

                result_str += " Away Team Name: ";
                result_str += result.getString("AwayTeamName");

                result_str += String.format(" Result for %s: ", team);
                result_str += result.getString("Result for Team 1");

                result_str += " Kickoff Return Yards: ";
                result_str += result.getString("YardsKickoffReturn");

                result_str += " Punt Return Yards: ";
                result_str += result.getString("YardsPuntReturn");

                result_str += " Fumble Return Yards: ";
                result_str += result.getString("YardsFumbleReturn");

                result_str += " Interception Return Yards: ";
                result_str += result.getString("YardsInterceptionReturn");

                result_str += " Misc Return Yards: ";
                result_str += result.getString("YardsMiscReturn");

                result_str += " Punt Yards: ";
                result_str += result.getString("YardsPunt");

                result_str += " Kickoff Yards: ";
                result_str += result.getString("YardsKickoff");

                result_str += " Tackle Yards: ";
                result_str += result.getString("YardsTackleLoss");

                result_str += " Tackle Loss Yards: ";
                result_str += result.getString("YardsTackleLoss");

                result_str += " Sack Yards: ";
                result_str += result.getString("YardsSack");

                result_str += " Penalty Yards: ";
                result_str += result.getString("YardsPenalty");

                result_str += " Tackles Solo: ";
                result_str += result.getString("TacklesSolo");

                result_str += " Tackles Assist: ";
                result_str += result.getString("TacklesAssist");

                result_str += " Tackles For Loss: ";
                result_str += result.getString("TacklesForLoss");

                result_str += " Interceptions Pass: ";
                result_str += result.getString("InterceptionsPass");

                result_str += " Interceptions Return: ";
                result_str += result.getString("InterceptionsReturn");

                result_str += " Fumbles: ";
                result_str += result.getString("Fumbles");

                result_str += " Fumbles Lost: ";
                result_str += result.getString("FumblesLost");

                result_str += " Fumbles Forced: ";
                result_str += result.getString("FumblesForced");

                result_str += " Rush Attempts: ";
                result_str += result.getString("AttemptsRush");

                result_str += " Pass Attempts: ";
                result_str += result.getString("AttemptsPass");

                result_str += " FG Attempts: ";
                result_str += result.getString("AttemptsFG");

                result_str += " Offense XP Kick Attempts: ";
                result_str += result.getString("AttemptsOffenseXPKick");

                result_str += " Offense 2XP Attempts: ";
                result_str += result.getString("AttemptsOffense2XP");

                result_str += " Third Down Attempts: ";
                result_str += result.getString("AttemptsThirdDown");

                result_str += " Fourth Down Attempts: ";
                result_str += result.getString("AttemptsFourthDown");

                result_str += " Red Zone Attempts: ";
                result_str += result.getString("AttemptsRedZone");

                result_str += " TouchDown Rush: ";
                result_str += result.getString("TDRush");

                result_str += " TouchDown Kickoff Return: ";
                result_str += result.getString("TDKickoffReturn");

                result_str += " TouchDown Punt Return: ";
                result_str += result.getString("TDPuntReturn");

                result_str += " TouchDown Fumble Return: ";
                result_str += result.getString("TDFumbleReturn");

                result_str += " TouchDown Interception Return: ";
                result_str += result.getString("TDInterceptionReturn");

                result_str += " TouchDown Misc Return: ";
                result_str += result.getString("TDMiscReturn");

                result_str += " TouchDown Red Zone: ";
                result_str += result.getString("TDRedZone");

                result_str += " Pass Conversion: ";
                result_str += result.getString("ConvPass");

                result_str += " Third Down Conversion: ";
                result_str += result.getString("ConvThirdDown");

                result_str += " Fourth Down Conversion: ";
                result_str += result.getString("ConvFourthDown");

                result_str += " Pass Completion: ";
                result_str += result.getString("PassCompletion");

                result_str += " Misc Return: ";
                result_str += result.getString("MiscReturn");

                result_str += " FG Made: ";
                result_str += result.getString("MadeFG");

                result_str += " Offensive XP Kick Made: ";
                result_str += result.getString("MadeOffensiveXPKick");

                result_str += " Offensive 2XP Made: ";
                result_str += result.getString("MadeOffensive2XP");

                result_str += " Defensive 2XP Made: ";
                result_str += result.getString("MadeDefensive2XP");

                result_str += " Safety: ";
                result_str += result.getString("Safety");

                result_str += " Points: ";
                result_str += result.getString("Points");

                result_str += " Punts: ";
                result_str += result.getString("Punts");

                result_str += " Kickoffs Return: ";
                result_str += result.getString("KickoffsReturn");

                result_str += " Kickoffs Out Of Bounds: ";
                result_str += result.getString("KickoffsOutOfBounds");

                result_str += " Kickoffs Onside: ";
                result_str += result.getString("KickoffsOnside");

                result_str += " Kickoffs TouchBacks: ";
                result_str += result.getString("KickoffTouchBacks");

                result_str += " Sacks: ";
                result_str += result.getString("Sacks");

                result_str += " QBHurry: ";
                result_str += result.getString("QBHurry");

                result_str += " Pass Broken Up: ";
                result_str += result.getString("PassBrokenUp");

                result_str += " Kick/Punt Blocked: ";
                result_str += result.getString("KickPuntBlocked");

                result_str += " First Down Rush: ";
                result_str += result.getString("FirstDownRush");

                result_str += " First Down Pass: ";
                result_str += result.getString("FirstDownPass");

                result_str += " First Down Penalty: ";
                result_str += result.getString("FirstDownPenalty");

                result_str += " Time Of Possession: ";
                result_str += result.getString("TimeOfPossession");

                result_str += " Penalty: ";
                result_str += result.getString("Penalty");

                result_str += " Red Zone FG: ";
                result_str += result.getString("RedZoneFG");

                result_str += " TD Pass: ";
                result_str += result.getString("TDPass");

                result_str += " Attempts Defense 2XP: ";
                result_str += result.getString("AttemptsDefense2XP");

                result_str += " Punt Return: ";
                result_str += result.getString("PuntReturn");

                result_str += " Fumble Return: ";
                result_str += result.getString("FumbleReturn");

                result_str += " Kickoff: ";
                result_str += result.getString("Kickoff");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error accessing Team Data. Make sure that the Team name is correct");
        }
        if (result_str == "") {
            return "Team Game Data non existent\n";
        }
        return result_str;
    }

    public static void main(String args[]) {
        dbSetup my = new dbSetup();
        // Building the connection
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/ace_of_spades", my.user,
                    my.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } // end try catch
          // JOptionPane.showMessageDialog(null, "Opened database successfully");

        //
        // MAIN CODE

        // stadium test
        System.out.println(stadiumDataFetch("ASU", conn));
        System.out.println(stadiumDataFetch("Shrey", conn));

        // general conference data
        System.out.println(ConfDataFetch("Big Sky", conn));
        System.out.println(ConfDataFetch("Football_Sucks", conn));

        // game data with name/year inputs
        System.out.println(gameDataFetcWithNameYear("Texas A&", 2013, conn));
        System.out.println(gameDataFetcWithNameYear("", 2013, conn));
        System.out.println(gameDataFetcWithNameYear("Texas A&", 0, conn));
        System.out.println(gameDataFetcWithNameYear("", 0, conn));
        System.out.println(gameDataFetcWithNameYear("Bazinga", 2003, conn));

        System.out.println(teamGameData("Texas A&M", "Clemson", 2005, conn));

        // general team

        // END MAIN CODE
        //

        try {
            conn.close();
            // JOptionPane.showMessageDialog(null, "Connection Closed.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
        }

    }// end backendmain
}// end Class