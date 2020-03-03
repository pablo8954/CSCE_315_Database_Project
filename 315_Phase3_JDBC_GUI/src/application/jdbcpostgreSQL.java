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
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\","
                        + "\"StadiumName\",\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" "
                        + "INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" "
                        + "INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN"
                        + "(SELECT*FROM\"Game\" WHERE EXTRACT(YEAR FROM\"Date\")=%s AND(\"HomeTeamId\"="
                        + "(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')OR\"AwayTeamId\"="
                        + "(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')))AS game_data ON\"TeamId\"="
                        + "\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" ."
                        + "\"StadiumId\"=team2_data.\"StadiumId\";",
                        year.toString(), name, name);
                
            } else if (name.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\","
                        + "\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" "
                        + ".\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS"
                        + "\"HomeTeamName\",*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\" WHERE EXTRACT"
                        + "(YEAR FROM \"Date\")=%s)AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data ON\"Team\" "
                        + ".\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data.\"StadiumId\";",
                        year.toString());
                
            } else if (!name.equals("") && (year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\",\"Attendance\","
                        + "\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\","
                        + "*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\" "
                        + "WHERE (\"HomeTeamId\"=(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%') OR "
                        + "\"AwayTeamId\"=(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')))AS game_data ON"
                        + "\"TeamId\"=\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON"
                        + "\"Stadium_Yearwise\" .\"StadiumId\"=team2_data.\"StadiumId\";",
                        name, name);
            } else if (name.equals("") && (year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\","
                        + "\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" ."
                        + "\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\","
                        + "*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\")AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data "
                        + "ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data"
                        + ".\"StadiumId\";",
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
                    "SELECT DISTINCT "
                    + "\"StadiumName\",\"StadiumSurface\",\"StadiumCity\",\"StadiumState\","
                    + "\"StadiumCapacity\",\"StadiumYearOpened\" FROM \"Stadium_Yearwise\" WHERE"
                    + " \"StadiumId\" = ( SELECT DISTINCT \"StadiumId\" FROM \"Stadium_Yearwise\" WHERE "
                    + "\"StadiumName\" LIKE '%s%%');",
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

    public static String confDataFetch(String name, Connection conn) {
        String result_str = "";
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = String.format(
                    "SELECT DISTINCT \"Subdivision\", \"ConferenceName\" "
                    + "FROM \"Conference_Yearwise\" WHERE \"ConfId\" = "
                    + "( SELECT DISTINCT \"ConfId\" FROM \"Conference_Yearwise\" WHERE "
                    + "\"ConferenceName\" LIKE '%s%%');",
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
                    "SELECT DISTINCT \"TeamName\", \"AverageAttendance\", "
                    + "\"ConferenceName\", \"Subdivision\", \"Conference_Yearwise\".\"Year\" FROM "
                    + "\"Conference_Yearwise\" INNER JOIN (SELECT * FROM \"Team_Yearwise\" "
                    + "INNER JOIN (SELECT * FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') "
                    + "AS Team_Data_Inner ON Team_Data_Inner.\"TeamId\" = \"Team_Yearwise\".\"TeamId\") "
                    + "AS Team_Data ON \"Conference_Yearwise\".\"ConfYearId\"=Team_Data.\"ConfYearId\";",
                    name);

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Team Name: ";
                result_str += result.getString("TeamName");

                result_str += " | Average Attendance: ";
                result_str += result.getString("AverageAttendance");

                result_str += " | Conference Name: ";
                result_str += result.getString("ConferenceName");

                result_str += " | Subdivision: ";
                result_str += result.getString("Subdivision");

                result_str += " | Year: ";
                result_str += result.getString("Year");

                result_str += "\n";
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing General Team Data. Make sure that the Conference name is correct");
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
            	 JOptionPane.showMessageDialog(null, "First");
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
                        year.toString(), team, team, awayteam, awayteam, team);
            }

            else if (!team.equals("") && !awayteam.equals("") && (year < 2005 || year > 2013)) {
            	 JOptionPane.showMessageDialog(null, "second");
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
                                + "\"Team\" INNER JOIN (SELECT * FROM \"Game\" WHERE((\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')OR \"AwayTeamId\" = "
                                + "( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')) AND (\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" "
                                + "LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS "
                                + "team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\".\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" "
                                + "= ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        team, team, awayteam, awayteam, team);
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
                                + "* FROM \"Team\" INNER JOIN (SELECT * FROM \"Game\" WHERE EXTRACT(YEAR FROM \"Date\") = %s AND((\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE "
                                + "\"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')))) AS game_data ON \"TeamId\" = \"HomeTeamId\") "
                                + "AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\".\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" "
                                + "= ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        year.toString(), team, team, team);
            }

            else if (!team.equals("") && awayteam.equals("") && (year < 2005 || year > 2013)) {
            	
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
                                + "SELECT * FROM \"Game\" WHERE(\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = (SELECT "
                                + "DISTINCT \"TeamId\" FROM \"Team\" WHERE\"TeamName\" LIKE '%s%%'))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\" = "
                                + "\"AwayTeamId\") AS team_data2 ON team_data2.\"GameId\"=\"Team_Metrics_Gamewise\".\"GameId\" AND \"Team_Metrics_Gamewise\".\"TeamId\" = (SELECT DISTINCT \"TeamId"
                                + "\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%');",
                        team, team, team);
            }

       
    
            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Home Team Name: ";
                result_str += result.getString("HomeTeamName");

                result_str += " | Away Team Name: ";
                result_str += result.getString("AwayTeamName");

                result_str += String.format(" | Result for %s: ", team);
                result_str += result.getString("Result for Team 1");

                result_str += " | Kickoff Return Yards: ";
                result_str += result.getString("YardsKickoffReturn");

                result_str += " | Punt Return Yards: ";
                result_str += result.getString("YardsPuntReturn");

                result_str += " | Fumble Return Yards: ";
                result_str += result.getString("YardsFumbleReturn");

                result_str += " | Interception Return Yards: ";
                result_str += result.getString("YardsInterceptionReturn");

                result_str += " | Misc Return Yards: ";
                result_str += result.getString("YardsMiscReturn");

                result_str += " | Punt Yards: ";
                result_str += result.getString("YardsPunt");

                result_str += " | Kickoff Yards: ";
                result_str += result.getString("YardsKickoff");

                result_str += " | Tackle Yards: ";
                result_str += result.getString("YardsTackleLoss");

                result_str += " | Tackle Loss Yards: ";
                result_str += result.getString("YardsTackleLoss");

                result_str += " | Sack Yards: ";
                result_str += result.getString("YardsSack");

                result_str += " | Penalty Yards: ";
                result_str += result.getString("YardsPenalty");

                result_str += " | Tackles Solo: ";
                result_str += result.getString("TacklesSolo");

                result_str += " | Tackles Assist: ";
                result_str += result.getString("TacklesAssist");

                result_str += " | Tackles For Loss: ";
                result_str += result.getString("TacklesForLoss");

                result_str += " | Interceptions Pass: ";
                result_str += result.getString("InterceptionsPass");

                result_str += " | Interceptions Return: ";
                result_str += result.getString("InterceptionsReturn");

                result_str += " | Fumbles: ";
                result_str += result.getString("Fumbles");

                result_str += " | Fumbles Lost: ";
                result_str += result.getString("FumblesLost");

                result_str += " | Fumbles Forced: ";
                result_str += result.getString("FumblesForced");

                result_str += " | Rush Attempts: ";
                result_str += result.getString("AttemptsRush");

                result_str += " | Pass Attempts: ";
                result_str += result.getString("AttemptsPass");

                result_str += " | FG Attempts: ";
                result_str += result.getString("AttemptsFG");

                result_str += " | Offense XP Kick Attempts: ";
                result_str += result.getString("AttemptsOffenseXPKick");

                result_str += " | Offense 2XP Attempts: ";
                result_str += result.getString("AttemptsOffense2XP");

                result_str += " | Third Down Attempts: ";
                result_str += result.getString("AttemptsThirdDown");

                result_str += " | Fourth Down Attempts: ";
                result_str += result.getString("AttemptsFourthDown");

                result_str += " | Red Zone Attempts: ";
                result_str += result.getString("AttemptsRedZone");

                result_str += " | TouchDown Rush: ";
                result_str += result.getString("TDRush");

                result_str += " | TouchDown Kickoff Return: ";
                result_str += result.getString("TDKickoffReturn");

                result_str += " | TouchDown Punt Return: ";
                result_str += result.getString("TDPuntReturn");

                result_str += " | TouchDown Fumble Return: ";
                result_str += result.getString("TDFumbleReturn");

                result_str += " | TouchDown Interception Return: ";
                result_str += result.getString("TDInterceptionReturn");

                result_str += " | TouchDown Misc Return: ";
                result_str += result.getString("TDMiscReturn");

                result_str += " | TouchDown Red Zone: ";
                result_str += result.getString("TDRedZone");

                result_str += " | Pass Conversion: ";
                result_str += result.getString("ConvPass");

                result_str += " | Third Down Conversion: ";
                result_str += result.getString("ConvThirdDown");

                result_str += " | Fourth Down Conversion: ";
                result_str += result.getString("ConvFourthDown");

                result_str += " | Pass Completion: ";
                result_str += result.getString("PassCompletion");

                result_str += " | Misc Return: ";
                result_str += result.getString("MiscReturn");

                result_str += " | FG Made: ";
                result_str += result.getString("MadeFG");

                result_str += " | Offensive XP Kick Made: ";
                result_str += result.getString("MadeOffensiveXPKick");

                result_str += " | Offensive 2XP Made: ";
                result_str += result.getString("MadeOffensive2XP");

                result_str += " | Defensive 2XP Made: ";
                result_str += result.getString("MadeDefensive2XP");

                result_str += " | Safety: ";
                result_str += result.getString("Safety");

                result_str += " | Points: ";
                result_str += result.getString("Points");

                result_str += " | Punts: ";
                result_str += result.getString("Punts");

                result_str += " | Kickoffs Return: ";
                result_str += result.getString("KickoffsReturn");

                result_str += " | Kickoffs Out Of Bounds: ";
                result_str += result.getString("KickoffsOutOfBounds");

                result_str += " | Kickoffs Onside: ";
                result_str += result.getString("KickoffsOnside");

                result_str += " | Kickoffs TouchBacks: ";
                result_str += result.getString("KickoffTouchBacks");

                result_str += " | Sacks: ";
                result_str += result.getString("Sacks");

                result_str += " | QBHurry: ";
                result_str += result.getString("QBHurry");

                result_str += " | Pass Broken Up: ";
                result_str += result.getString("PassBrokenUp");

                result_str += " | Kick/Punt Blocked: ";
                result_str += result.getString("KickPuntBlocked");

                result_str += " | First Down Rush: ";
                result_str += result.getString("FirstDownRush");

                result_str += " | First Down Pass: ";
                result_str += result.getString("FirstDownPass");

                result_str += " | First Down Penalty: ";
                result_str += result.getString("FirstDownPenalty");

                result_str += " | Time Of Possession: ";
                result_str += result.getString("TimeOfPossession");

                result_str += " | Penalty: ";
                result_str += result.getString("Penalty");

                result_str += " | Red Zone FG: ";
                result_str += result.getString("RedZoneFG");

                result_str += " | TD Pass: ";
                result_str += result.getString("TDPass");

                result_str += " | Attempts Defense 2XP: ";
                result_str += result.getString("AttemptsDefense2XP");

                result_str += " | Punt Return: ";
                result_str += result.getString("PuntReturn");

                result_str += " | Fumble Return: ";
                result_str += result.getString("FumbleReturn");

                result_str += " | Kickoff: ";
                result_str += result.getString("Kickoff");

                result_str += "\n";
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error accessing Team Data. Make sure that the Team name is correct");
        }
        if (result_str == "") {
            return "Team Game Data non existent\n";
        }
        return result_str;
    }

    public static String teamPlayData(String team, String awayteam, Integer year, Connection conn) {

        String result_str = "";
        String sqlStmt = "";

        try {
            Statement stmt = conn.createStatement();

            if (!team.equals("") && !awayteam.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT \"HomeTeamName\", \"AwayTeamName\", \"Quarter\", \"Clock\", \"OffensePoints\", \"DefensePoints\", \"Down\", \"Distance\", \"Spot\", \"PlayType\", \"DriveNumber\","
                                + "\"DrivePlay\", \"Attempt\", \"Yards\", \"FairCatch\", \"Touchback\", \"Downed\", \"OutOfBounds\", \"Onside\", \"OnsideSuccess\", \"TD\", \"Fumble\", \"FumbleLost\", "
                                + "\"Sack\", \"Safety\", \"Completion\", \"Interception\", \"FirstDown\",\"Dropped\",\"Blocked\", \"Reception\" "

                                + "FROM \"Play_Metrics\" INNER JOIN ( SELECT \"Play\".\"GameId\" AS \"game_id\", * FROM \"Play\" INNER JOIN (SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", * FROM "
                                + "\"Team\" INNER JOIN (SELECT \"TeamName\" AS \"HomeTeamName\",* FROM \"Team\" INNER JOIN ( SELECT* FROM \"Game\" WHERE EXTRACT(YEAR FROM \"Date\") = %s AND((\"HomeTeamId\" "
                                + "= ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')) "
                                + "AND(\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" "
                                + "LIKE '%s%%')))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2. \"GameId\" = \"Play\".\"GameId\") "
                                + "AS inner_play ON \"Play_Metrics\".\"GameId\" = inner_play. \"game_id\""
                                + "AND inner_play. \"PlayNum\" = \"Play_Metrics\".\"PlayNum\";",
                        year.toString(), team, team, awayteam, awayteam);
            }

            else if (!team.equals("") && !awayteam.equals("") && (year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT \"HomeTeamName\", \"AwayTeamName\", \"Quarter\", \"Clock\", \"OffensePoints\", "
                                + "\"DefensePoints\", \"Down\", \"Distance\", \"Spot\", \"PlayType\", \"DriveNumber\", \"DrivePlay\", \"Attempt\", \"Yards\", "
                                + "\"FairCatch\", \"Touchback\", \"Downed\", \"OutOfBounds\", \"Onside\", \"OnsideSuccess\", \"TD\", \"Fumble\", \"FumbleLost\", \"Sack\","
                                + "\"Safety\", \"Completion\", \"Interception\", \"FirstDown\", \"Dropped\", \"Blocked\", \"Reception\""

                                + "FROM \"Play_Metrics\"" + "INNER JOIN ("
                                + "SELECT \"Play\".\"GameId\" AS \"game_id\",* FROM \"Play\" INNER JOIN ( SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", "
                                + "* FROM \"Team\" INNER JOIN ( SELECT \"TeamName\" AS \"HomeTeamName\", * FROM \"Team\" INNER JOIN (SELECT* FROM \"Game\" WHERE "
                                + "(\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT "
                                + "\"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')) AND(\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') "
                                + "OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%'))) AS game_data ON \"TeamId\" = \"HomeTeamId\") "
                                + "AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2. \"GameId\" = \"Play\".\"GameId\") AS inner_play ON \"Play_Metrics\"."
                                + "\"GameId\" = inner_play. \"game_id\" AND inner_play. \"PlayNum\" = \"Play_Metrics\".\"PlayNum\";",
                                
                                team, team, awayteam, awayteam);
            }

            else if (!team.equals("") && awayteam.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format(
                        "SELECT \"HomeTeamName\", \"AwayTeamName\", \"Quarter\", \"Clock\", \"OffensePoints\",\"DefensePoints\", \"Down\",\"Distance\",\"Spot\","
                                + "\"PlayType\", \"DriveNumber\", \"DrivePlay\", \"Attempt\", \"Yards\", \"FairCatch\", \"Touchback\", \"Downed\", \"OutOfBounds\", "
                                + "\"Onside\", \"OnsideSuccess\", \"TD\", \"Fumble\", \"FumbleLost\", \"Sack\", \"Safety\", \"Completion\", \"Interception\", \"FirstDown\", "
                                + "\"Dropped\", \"Blocked\", \"Reception\""

                                + "FROM \"Play_Metrics\" INNER JOIN ( SELECT \"Play\".\"GameId\" AS \"game_id\", * FROM \"Play\" INNER JOIN (SELECT \"Team\".\"TeamName\" AS "
                                + "\"AwayTeamName\", * FROM \"Team\" INNER JOIN (SELECT \"TeamName\" AS \"HomeTeamName\", *FROM \"Team\" "
                                + "INNER JOIN (SELECT * FROM \"Game\" WHERE EXTRACT(YEAR FROM \"Date\") = %s AND((\"HomeTeamId\" = (SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE"
                                + "\"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')))) AS game_data ON "
                                + "\"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2. \"GameId\" = \"Play\".\"GameId\") "
                                + "AS inner_play ON \"Play_Metrics\".\"GameId\" = inner_play. \"game_id\" AND inner_play. \"PlayNum\" = \"Play_Metrics\".\"PlayNum\";",
                        year.toString(), team, team);
            }

            else {
                sqlStmt = String.format(
                        "SELECT \"HomeTeamName\", \"AwayTeamName\", \"Quarter\", \"Clock\", \"OffensePoints\",\"DefensePoints\", \"Down\",\"Distance\",\"Spot\","
                                + "\"PlayType\", \"DriveNumber\", \"DrivePlay\", \"Attempt\", \"Yards\", \"FairCatch\", \"Touchback\", \"Downed\", \"OutOfBounds\", "
                                + "\"Onside\", \"OnsideSuccess\", \"TD\", \"Fumble\", \"FumbleLost\", \"Sack\", \"Safety\", \"Completion\", \"Interception\", \"FirstDown\", "
                                + "\"Dropped\", \"Blocked\", \"Reception\" "

                                + "FROM \"Play_Metrics\" INNER JOIN (SELECT \"Play\".\"GameId\" AS \"game_id\", * FROM \"Play\" INNER JOIN (SELECT \"Team\".\"TeamName\" AS "
                                + "\"AwayTeamName\", * FROM \"Team\" INNER JOIN ( SELECT \"TeamName\" AS \"HomeTeamName\", * FROM \"Team\" INNER JOIN ( SELECT * FROM \"Game\" "
                                + "WHERE (\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT "
                                + "\"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%'))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\""
                                + " = \"AwayTeamId\") AS team_data2 ON team_data2. \"GameId\" = \"Play\".\"GameId\") AS inner_play ON \"Play_Metrics\".\"GameId\" = inner_play. \"game_id\" "
                                + "AND inner_play. \"PlayNum\" = \"Play_Metrics\".\"PlayNum\";",
                        team, team);
            }

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Home Team Name: ";
                result_str += result.getString("HomeTeamName");

                result_str += " | Away Team Name: ";
                result_str += result.getString("AwayTeamName");

                result_str += " | Quarter: ";
                result_str += result.getString("Quarter");

                result_str += " | Clock: ";
                result_str += result.getString("Clock");

                result_str += " | Offense Points: ";
                result_str += result.getString("OffensePoints");

                result_str += " | Defense Points: ";
                result_str += result.getString("DefensePoints");

                result_str += " | Down: ";
                result_str += result.getString("Down");

                result_str += " | Distance: ";
                result_str += result.getString("Distance");

                result_str += " | Spot: ";
                result_str += result.getString("Spot");

                result_str += " | Play Type: ";
                result_str += result.getString("PlayType");

                result_str += " | Drive Number: ";
                result_str += result.getString("DriveNumber");

                result_str += " | Drive Play: ";
                result_str += result.getString("DrivePlay");

                result_str += " | Attempt: ";
                result_str += result.getString("Attempt");

                result_str += " | Yards: ";
                result_str += result.getString("Yards");

                result_str += " | Fair Catch: ";
                result_str += result.getString("FairCatch");

                result_str += " | Touchback: ";
                result_str += result.getString("Touchback");

                result_str += " | Downed: ";
                result_str += result.getString("Downed");

                result_str += " | Out Of Bounds: ";
                result_str += result.getString("OutOfBounds");

                result_str += " | Onside: ";
                result_str += result.getString("Onside");

                result_str += " | Touch Down: ";
                result_str += result.getString("TD");

                result_str += " | Fumble: ";
                result_str += result.getString("Fumble");

                result_str += " | Fumble Lost: ";
                result_str += result.getString("FumbleLost");

                result_str += " | Sack: ";
                result_str += result.getString("Sack");

                result_str += " | Safety: ";
                result_str += result.getString("Safety");

                result_str += " | Completion: ";
                result_str += result.getString("Completion");

                result_str += " | Interception: ";
                result_str += result.getString("Interception");

                result_str += " | First Down: ";
                result_str += result.getString("FirstDown");

                result_str += " | Dropped: ";
                result_str += result.getString("Dropped");

                result_str += " | Blocked: ";
                result_str += result.getString("Blocked");

                result_str += " | Reception: ";
                result_str += result.getString("Reception");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Team Play Data. Make sure that the Team name is correct");
        }

        if (result_str == "") {
            return "Team Play Data non existent\n";
        }
        return result_str;
    }

    public static String generalPlayer(String fname, String lname, Connection conn) {
        String result_str = "";
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            // create an SQL statement
            String sqlStatement = String.format(
                    "SELECT DISTINCT\"FirstName\",\"LastName\",\"TeamName\",\"Uniform\","
                    + "\"Class\",\"Position\",\"Height\",\"Weight\",\"LastSchool\",\"Hometown\",\"HomeState\","
                    + "\"HomeCountry\" "
                    
                    + "FROM\"Team\" INNER JOIN(SELECT*FROM\"Team_Yearwise\" "
                    + "INNER JOIN(SELECT*FROM\"Player_Yearwise\" INNER JOIN(SELECT*FROM\"Player\""
                    + " WHERE\"FirstName\" LIKE'%s%%' AND\"LastName\" LIKE'%s%%')AS player_data ON player_data."
                    + "\"PlayerId\"=\"Player_Yearwise\" .\"PlayerId\")AS player_data2 ON\"Team_Yearwise\""
                    + " .\"TeamYearId\"=player_data2.\"TeamYearId\")AS team_data ON\"Team\" .\"TeamId\"=team_data."
                    + "\"TeamId\";",
                    
                    fname, lname);
            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                result_str += "First Name: ";
                result_str += result.getString("FirstName");
                result_str += " | Last Name: ";
                result_str += result.getString("LastName");
                result_str += " | Team Name: ";
                result_str += result.getString("TeamName");
                result_str += " | Uniform: ";
                result_str += result.getString("Uniform");
                result_str += " | Class: ";
                result_str += result.getString("Class");
                result_str += " | Position: ";
                result_str += result.getString("Position");
                result_str += " | Height: ";
                result_str += result.getString("Height");
                result_str += " | Last School: ";
                result_str += result.getString("LastSchool");
                result_str += " | Home Town: ";
                result_str += result.getString("Hometown");
                result_str += " | Home State: ";
                result_str += result.getString("HomeState");
                result_str += " | Home Country: ";
                result_str += result.getString("HomeCountry");
                result_str += "\n";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Player Data. Make sure that the Player name is correct");
        }
        if (result_str == "") {
            return "Player Data non existent\n";
        }
        return result_str;
    }

    public static String playerMetricsData(String fname, String lname, Integer year, Connection conn) {
        String result_str = "";
        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            String sqlStatement = "";
            // create an SQL statement
            if (!fname.equals("") && !lname.equals("") && !(year < 2005 || year > 2013)) {
                sqlStatement = String.format(
                        "SELECT DISTINCT \"FirstName\", \"LastName\", \"Game\".\"GameId\", \"YardsRush\", \"YardsPass\", \"YardsKickoffReturn\","
                                + "\"YardsPuntReturn\", \"YardsFumbleReturn\", \"YardsInterceptionReturn\", \"YardsMisc\", \"YardsPunt\", \"YardsKickoff\","
                                + "\"YardsTackleLoss\", \"YardsSack\", \"TacklesSolo\", \"TacklesAssist\", \"TacklesForLoss\", \"InterceptionPass\", "
                                + "\"InterceptionReturn\", \"Fumbles\", \"FumblesLost\", \"FumblesForced\", \"AttemptsRush\", \"AttemptsPass\", \"AttemptsFG\","
                                + "\"AttemptsOffenseXPKick\", \"AttemptsOffense2XP\", \"TDRush\", \"TDKickoffReturn\", \"TDPuntReturn\", \"TDFumbleReturn\", "
                                + "\"TDInterceptionReturn\", \"TDMIscReturn\", \"PassCompletion\", \"MiscReturn\", \"MadeFG\", \"MadeOffensiveXPKick\","
                                + "\"MadeOffensive2XP\", \"MadeDefensive2XP\", \"Safety\", \"Points\", \"Punts\", \"Kickoffs\", \"KickoffsOutOfBounds\", "
                                + "\"KickoffsOnside\", \"Sacks\", \"QBHurry\", \"PassBrokenUp\", \"KickPuntBlocked\", \"TDPass\", \"AttemptsDefense2XP\","
                                + "\"ConvPass\", \"Reception\", \"YardsReception\", \"TDReception\", \"KickReturn\", \"PuntReturn\", \"FumbleReturn\", \"Touchbacks\""

                                + "FROM \"Game\" INNER JOIN (SELECT * FROM \"Player_Metrics_Gamewise\" INNER JOIN (SELECT * FROM \"Team\" "
                                + "INNER JOIN (SELECT * FROM \"Team_Yearwise\" INNER JOIN (SELECT * FROM \"Player_Yearwise\" INNER JOIN (SELECT "
                                + "\"PlayerId\" AS \"MainPlayerId\", * FROM \"Player\" WHERE \"FirstName\" LIKE '%s%%' "
                                + "AND \"LastName\" LIKE '%s%%') AS player_data ON player_data. \"PlayerId\" = \"Player_Yearwise\".\"PlayerId\") "
                                + "AS player_data2 ON \"Team_Yearwise\".\"TeamYearId\" = player_data2. \"TeamYearId\") AS team_data ON \"Team\".\"TeamId\" = team_data. "
                                + "\"TeamId\") AS player_data3 ON player_data3. \"MainPlayerId\" = \"Player_Metrics_Gamewise\".\"PlayerId\") AS player_data4 ON "
                                + "\"Game\".\"GameId\" = player_data4. \"GameId\" WHERE EXTRACT(YEAR FROM \"Game\".\"Date\") = %s;",
                        fname, lname, year.toString());
            } else if (!fname.equals("") && !lname.equals("") && (year < 2005) || year > 2013) {
                sqlStatement = String.format(
                        "SELECT DISTINCT \"FirstName\", \"LastName\", \"Game\".\"GameId\", \"YardsRush\", \"YardsPass\", "
                                + "\"YardsKickoffReturn\",\"YardsPuntReturn\", \"YardsFumbleReturn\", \"YardsInterceptionReturn\", "
                                + "\"YardsMisc\", \"YardsPunt\", \"YardsKickoff\", \"YardsTackleLoss\", \"YardsSack\", \"TacklesSolo\", "
                                + "\"TacklesAssist\", \"TacklesForLoss\", \"InterceptionPass\", \"InterceptionReturn\", \"Fumbles\", "
                                + "\"FumblesLost\", \"FumblesForced\", \"AttemptsRush\", \"AttemptsPass\", \"AttemptsFG\", \"AttemptsOffenseXPKick\", "
                                + "\"AttemptsOffense2XP\", \"TDRush\", \"TDKickoffReturn\", \"TDPuntReturn\", \"TDFumbleReturn\", \"TDInterceptionReturn\","
                                + "\"TDMIscReturn\", \"PassCompletion\", \"MiscReturn\", \"MadeFG\", \"MadeOffensiveXPKick\", \"MadeOffensive2XP\", "
                                + "\"MadeDefensive2XP\", \"Safety\", \"Points\", \"Punts\", \"Kickoffs\", \"KickoffsOutOfBounds\", \"KickoffsOnside\", "
                                + "\"Sacks\", \"QBHurry\", \"PassBrokenUp\", \"KickPuntBlocked\", \"TDPass\", \"AttemptsDefense2XP\", \"ConvPass\","
                                + "\"Reception\", \"YardsReception\", \"TDReception\", \"KickReturn\", \"PuntReturn\", \"FumbleReturn\", \"Touchbacks\""

                                + "FROM \"Game\" INNER JOIN (SELECT * FROM \"Player_Metrics_Gamewise\" INNER JOIN (SELECT * FROM \"Team\" INNER JOIN ("
                                + "SELECT * FROM \"Team_Yearwise\" INNER JOIN (SELECT * FROM \"Player_Yearwise\" INNER JOIN (SELECT\"PlayerId\" "
                                + "AS \"MainPlayerId\", * FROM \"Player\" WHERE \"FirstName\" LIKE '%s%%' AND \"LastName\" LIKE '%s%%') "

                                + "AS player_data ON player_data. \"PlayerId\" = \"Player_Yearwise\".\"PlayerId\") AS player_data2 ON \"Team_Yearwise\"."
                                + "\"TeamYearId\" = player_data2. \"TeamYearId\") AS team_data ON \"Team\".\"TeamId\" = team_data. \"TeamId\") "
                                + "AS player_data3 ON player_data3. \"MainPlayerId\" = \"Player_Metrics_Gamewise\".\"PlayerId\") AS player_data4 ON "
                                + "\"Game\".\"GameId\" = player_data4. \"GameId\";",
                        fname, lname);
            }

            // send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                try {
                    result_str += "First Name: ";
                    result_str += result.getString("FirstName");
                } catch (Exception e) {
                    result_str += "First Name: ";
                    result_str += "NULL";
                }

                result_str += " | Last Name: ";
                result_str += result.getString("LastName");

                result_str += " | Game ID: ";
                result_str += result.getString("GameId");

                result_str += " | Yards Rush: ";
                result_str += result.getString("YardsRush");

                result_str += " | Yards Pass: ";
                try {
                    result_str += result.getString("YardsPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Kickoff Return: ";
                try {
                    result_str += result.getString("YardsKickoffReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Fumble Return: ";
                try {
                    result_str += result.getString("YardsFumbleReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Interception Return: ";
                try {
                    result_str += result.getString("YardsInterceptionReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Misc: ";
                try {
                    result_str += result.getString("YardsMisc");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Kickoff: ";
                try {
                    result_str += result.getString("YardsKickoff");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Tackle Loss: ";
                try {
                    result_str += result.getString("YardsTackleLoss");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Sack: ";
                try {
                    result_str += result.getString("YardsSack");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Tackles Solo: ";
                try {
                    result_str += result.getString("TacklesSolo");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Interception Pass: ";
                try {
                    result_str += result.getString("Interception Pass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Interception Return: ";
                try {
                    result_str += result.getString("InterceptionReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fumbles: ";
                try {
                    result_str += result.getString("Fumbles");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fumbles Lost: ";
                try {
                    result_str += result.getString("FumblesLost");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fumbles Forced: ";
                try {
                    result_str += result.getString("FumblesForced");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attempts Rush: ";
                try {
                    result_str += result.getString("AttemptsRush");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attempts Pass: ";
                try {
                    result_str += result.getString("Attempts Pass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attempts Field Goal: ";
                try {
                    result_str += result.getString("AttemptsFG");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attempts Offense XP Kick: ";
                try {
                    result_str += result.getString("AttemptsOffenseXPKick");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attempts Offense 2XP: ";
                try {
                    result_str += result.getString("AttemptsOffense2XP");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Rush: ";
                try {
                    result_str += result.getString("TDRush");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Kickoff Return: ";
                try {
                    result_str += result.getString("TDKickoffReturn: ");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Punt Return: ";
                try {
                    result_str += result.getString("TDPuntReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Fumble Return: ";
                try {
                    result_str += result.getString("TDFumbleReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Interception Return: ";
                try {
                    result_str += result.getString("TD Interception Return");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Misc Return: ";
                try {
                    result_str += result.getString("TDMIscReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Pass Completion: ";
                try {
                    result_str += result.getString("Pass Completion");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | MiscReturn: ";
                try {
                    result_str += result.getString("MiscReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | MadeFG: ";
                try {
                    result_str += result.getString("MadeFG");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Made Offensive XP Kick: ";
                try {
                    result_str += result.getString("MadeOffensiveXPKick");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Made Offensive 2XP: ";
                try {
                    result_str += result.getString("MadeOffensiveXP");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Made Defensive 2XP: ";
                try {
                    result_str += result.getString("MadeDefensive2XP");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Safety: ";
                try {
                    result_str += result.getString("Safety");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Points: ";
                try {
                    result_str += result.getString("Points");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Punts: ";
                try {
                    result_str += result.getString("Punts");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoffs: ";
                try {
                    result_str += result.getString("Kickoffs");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoffs out of Bounds: ";
                try {
                    result_str += result.getString("KickoffsOutOfBounts");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoffs Onside: ";
                try {
                    result_str += result.getString("Kickoffs Onside");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Sacks: ";
                try {
                    result_str += result.getString("Sacks");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | QBHurry: ";
                try {
                    result_str += result.getString("QBHurry");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Pass Broken Up: ";
                try {
                    result_str += result.getString("Pass Broken Up");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kick Punt Blocked: ";
                try {
                    result_str += result.getString("KickPuntBlocked");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Pass: ";
                try {
                    result_str += result.getString("TDPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attempts Defense 2XP: ";
                try {
                    result_str += result.getString("AttemptsDefense2XP");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | ConvPass: ";
                try {
                    result_str += result.getString("ConvPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Reception: ";
                try {
                    result_str += result.getString("Reception");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Yards Reception: ";
                try {
                    result_str += result.getString("YardsReception");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TD Reception: ";
                try {
                    result_str += result.getString("TDReception");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kick Return: ";
                try {
                    result_str += result.getString("KickReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Punt Return: ";
                try {
                    result_str += result.getString("PuntReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fumble Return: ";
                try {
                    result_str += result.getString("FumbleReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Touchbacks: ";
                try {
                    result_str += result.getString("Touchbacks");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += "\n";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing Player Metrics Data. Make sure that the Player name is correct");
        }
        if (result_str == "") {
            return "Player Metrics Data non existent\n";
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
        System.out.println(confDataFetch("Big Sky", conn));
        System.out.println(confDataFetch("Football_Sucks", conn));

        // game data with name/year inputs
        System.out.println(gameDataFetcWithNameYear("Texas A&", 2013, conn));
        System.out.println(gameDataFetcWithNameYear("", 2013, conn));
        System.out.println(gameDataFetcWithNameYear("Texas A&", 0, conn));
        // System.out.println(gameDataFetcWithNameYear("", 0, conn));
        System.out.println(gameDataFetcWithNameYear("Bazinga", 2003, conn));

        System.out.println(teamGameData("Texas A&M", "Clemson", 2005, conn));
        System.out.println(generalPlayer("Bryan", "C", conn));
        System.out.println(generalPlayer("Baby", "Boy", conn));


       // System.out.println(playerMetricsData("Bryan", "C", 2012, conn));
//        System.out.println(generalPlayer("Bryan", "C", conn));


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