package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQL extends Application {

    private static String dataSelection;
    private static String teamName;
    private static String playerFirstName;
    private static String playerLastName;
    private static String conferenceName;
    private static String opposingTeam;
    private static String stadiumName;
    public static Connection conn;
    private static int year;
    public static boolean resultsRequested = false;
    public static boolean questionOneResultsRequested = false;
    public static boolean questionTwoResultsRequested = false;
    public static boolean questionThreeResultsRequested = false;

    private static WindowController controller;
    public static Stage mainStage;

    public void start(Stage primaryStage) throws IOException {
        // Launch database connection
        dbSetup my = new dbSetup();
        // Building the connection
        conn = null;
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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Window.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 800);
        controller = loader.getController();

        primaryStage.setScene(scene);
        primaryStage.setTitle("College Football Data");
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.show();

        new Thread() {
            public void run() {
                // Keep waking up thread for duration of program
                while (true) {

                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Platform.runLater(new Runnable() {

                        public void run() {

                            resultsRequested = controller.getResultsRequested();
                            //questionOneResultsRequested = controller.getQuestionOneResultsRequested();
                            //questionTwoResultsRequested = controller.getQuestionTwoResultsRequested();
                            //questionThreeResultsRequested = controller.getQuestionThreeResultsRequested();

                            // If get results pushed
                            if (resultsRequested) {
                            	 questionOneResultsRequested = controller.getQuestionOneResultsRequested();
                            	 questionTwoResultsRequested = controller.getQuestionTwoResultsRequested();
                            	 questionThreeResultsRequested = controller.getQuestionThreeResultsRequested();
                                dataSelection = controller.getDataSelection();
                                teamName = controller.getTeamName();
                                conferenceName = controller.getConferenceName();
                                playerFirstName = controller.getPlayerFirstName();
                                playerLastName = controller.getPlayerLastName();
                                opposingTeam = controller.getOpposingTeamName();
                                stadiumName = controller.getStadiumName();
                                year = controller.getYear();

                                System.out.println(dataSelection + " selected");
                                String result = "";
                                if (dataSelection.equals("Conference")) {
                                    result = confDataFetch(conferenceName, conn);
                                } else if (dataSelection.equals("Game")) {
                                    result = gameDataFetcWithNameYear(teamName, year, conn);
                                } else if (dataSelection.equals("Player")) {
                                    System.out.println(controller.getPlayerFirstName());
                                    if (controller.getYear() == 0) {
                                        result = generalPlayer(controller.getPlayerFirstName(),
                                                controller.getPlayerLastName(), conn);
                                    } else {
                                        result = playerMetricsData(controller.getPlayerFirstName(),
                                                controller.getPlayerLastName(), controller.getYear(), conn);
                                    }
                                } else if (dataSelection.equals("Stadium")) {
                                    result = stadiumDataFetch(stadiumName, conn);
                                } else if (dataSelection.equals("Team")) {
                                    if (controller.getTeamType().equals("General")) {
                                        result = generalTeam(controller.getTeamName(), conn);
                                    } else if (controller.getTeamType().equals("Game")) {
                                        result = teamGameData(controller.getTeamName(),
                                                controller.getOpposingTeamName(), controller.getYear(), conn);
                                    } else if (controller.getTeamType().equals("Play")) {
                                        result = teamPlayData(controller.getTeamName(),
                                                controller.getOpposingTeamName(), controller.getYear(), conn);
                                    }
                                } else if(questionOneResultsRequested) {
                                	result = "Question One Pressed";
                                	result += "\n" + questionOne(controller.getTeamName(), controller.getOpposingTeamName(), conn);
                                	controller.updateOutputTextArea(result);
                            	} else if(questionTwoResultsRequested) {
                            		result = "Question Two Pressed";
                            		result += "\n" + questionThree(controller.getTeamName(), controller.getYear(), conn);
                            		controller.updateOutputTextArea(result);
                            	} else if(questionThreeResultsRequested) {
                            		result = "Question Three Pressed";
                            		result += "\n" + questionFive(controller.getTeamName(), controller.getYear(), conn);
                            		controller.updateOutputTextArea(result);
                            	}
                                

                                controller.updateOutputTextArea(result);
                                System.out.println(result);

                                if (controller.generateTextFile()) {
                                    // write to file
                                    File generatedFile = new File("results.txt");

                                    try {
                                        generatedFile.createNewFile();
                                        FileWriter writer = new FileWriter(generatedFile);
                                        writer.write(result);
                                        writer.close();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                         
                        }
                    });
                }
            }
        }.start();

    }

    public static String gameDataFetcWithNameYear(String name, Integer year, Connection conn) {
        String result_str = "";
        try {
            Statement stmt = conn.createStatement();
            String sqlStmt = "";

            // SQL Query 4 Cases
            // - Name and Year Provided
            // - Only Year provided
            // - Only Name provided
            // - No input provided -> fetch all game data available
            if (!name.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format("SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\","
                        + "\"StadiumName\",\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" "
                        + "INNER JOIN(SELECT\"Team\" .\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" "
                        + "INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\",*FROM\"Team\" INNER JOIN"
                        + "(SELECT*FROM\"Game\" WHERE EXTRACT(YEAR FROM\"Date\")=%s AND(\"HomeTeamId\"="
                        + "(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')OR\"AwayTeamId\"="
                        + "(SELECT DISTINCT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%')))AS game_data ON\"TeamId\"="
                        + "\"HomeTeamId\")AS team1_data ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" ."
                        + "\"StadiumId\"=team2_data.\"StadiumId\";", year.toString(), name, name);

            } else if (name.equals("") && !(year < 2005 || year > 2013)) {
                sqlStmt = String.format("SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\","
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
                sqlStmt = String.format("SELECT DISTINCT \"HomeTeamName\",\"AwayTeamName\",\"Date\",\"StadiumName\","
                        + "\"Attendance\",\"Duration\" FROM\"Stadium_Yearwise\" INNER JOIN(SELECT\"Team\" ."
                        + "\"TeamName\" AS\"AwayTeamName\",*FROM\"Team\" INNER JOIN(SELECT\"TeamName\" AS\"HomeTeamName\","
                        + "*FROM\"Team\" INNER JOIN(SELECT*FROM\"Game\")AS game_data ON\"TeamId\"=\"HomeTeamId\")AS team1_data "
                        + "ON\"Team\" .\"TeamId\"=\"AwayTeamId\")AS team2_data ON\"Stadium_Yearwise\" .\"StadiumId\"=team2_data"
                        + ".\"StadiumId\";", year.toString(), name, name);
            }

            ResultSet result = stmt.executeQuery(sqlStmt);

            while (result.next()) {
                result_str += "Home Team Name: ";
                try {
                    result_str += result.getString("HomeTeamName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Away Team Name: ";
                try {
                    result_str += result.getString("AwayTeamName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Date: ";
                try {
                    result_str += result.getString("Date");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Attendance: ";
                try {
                    result_str += result.getString("Attendance");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Duration: ";
                try {
                    result_str += result.getString("Duration");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Stadium: ";
                try {
                    result_str += result.getString("StadiumName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

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
            Statement stmt = conn.createStatement();
            String sqlStatement = String
                    .format("SELECT DISTINCT " + "\"StadiumName\",\"StadiumSurface\",\"StadiumCity\",\"StadiumState\","
                            + "\"StadiumCapacity\",\"StadiumYearOpened\" FROM \"Stadium_Yearwise\" WHERE"
                            + " \"StadiumId\" = ( SELECT DISTINCT \"StadiumId\" FROM \"Stadium_Yearwise\" WHERE "
                            + "\"StadiumName\" LIKE '%s%%');", name);

            // Fetch SQL Results for output in DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                result_str += "Stadium Name: ";
                try {
                    result_str += result.getString("StadiumName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Surface: ";
                try {
                    result_str += result.getString("StadiumSurface");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | City: ";
                try {
                    result_str += result.getString("StadiumCity");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | State: ";
                try {
                    result_str += result.getString("StadiumState");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Capacity: ";
                try {
                    result_str += result.getString("StadiumCapacity");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Year Opened: ";
                try {
                    result_str += result.getString("StadiumYearOpened");
                } catch (Exception e) {
                    result_str += "NULL";
                }

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
            Statement stmt = conn.createStatement();

            String sqlStatement = String.format("SELECT DISTINCT \"Subdivision\", \"ConferenceName\" "
                    + "FROM \"Conference_Yearwise\" WHERE \"ConfId\" = "
                    + "( SELECT DISTINCT \"ConfId\" FROM \"Conference_Yearwise\" WHERE "
                    + "\"ConferenceName\" LIKE '%s%%');", name);

            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                result_str += "Conference Name: ";
                try {
                    result_str += result.getString("ConferenceName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Subdivision: ";
                try {
                    result_str += result.getString("Subdivision");
                } catch (Exception e) {
                    result_str += "NULL";
                }

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

            sqlStmt = String.format("SELECT DISTINCT \"TeamName\", \"AverageAttendance\", "
                    + "\"ConferenceName\", \"Subdivision\", \"Conference_Yearwise\".\"Year\" FROM "
                    + "\"Conference_Yearwise\" INNER JOIN (SELECT * FROM \"Team_Yearwise\" "
                    + "INNER JOIN (SELECT * FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') "
                    + "AS Team_Data_Inner ON Team_Data_Inner.\"TeamId\" = \"Team_Yearwise\".\"TeamId\") "
                    + "AS Team_Data ON \"Conference_Yearwise\".\"ConfYearId\"=Team_Data.\"ConfYearId\";", name);

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += "Team Name: ";
                try {
                    result_str += result.getString("TeamName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Average Attendance: ";
                try {
                    result_str += result.getString("AverageAttendance");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Conference Name: ";
                try {
                    result_str += result.getString("ConferenceName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Subdivision: ";
                try {
                    result_str += result.getString("Subdivision");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Year: ";
                try {
                    result_str += result.getString("Year");
                } catch (Exception e) {
                    result_str += "NULL";
                }

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
        String result_str = "";
        String sqlStmt = "";

        try {
            Statement stmt = conn.createStatement();

            // SQL Queries - 4 cases:
            // - Home Team, Away Team, Year provided
            // - Home Team, Away Team provided
            // - Home Team, Year Provided
            // - Only Home Team Provided
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
                try {
                    result_str += result.getString("HomeTeamName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Away Team Name: ";
                try {
                    result_str += result.getString("AwayTeamName");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += String.format(" | Result for %s: ", team);
                try {
                    result_str += result.getString("Result for Team 1");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoff Return Yards: ";
                try {
                    result_str += result.getString("YardsKickoffReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Punt Return Yards: ";
                try {
                    result_str += result.getString("YardsPuntReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fumble Return Yards: ";
                try {
                    result_str += result.getString("YardsFumbleReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Interception Return Yards: ";
                try {
                    result_str += result.getString("YardsInterceptionReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Misc Return Yards: ";
                try {
                    result_str += result.getString("YardsMiscReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Punt Yards: ";
                try {
                    result_str += result.getString("YardsPunt");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoff Yards: ";
                try {
                    result_str += result.getString("YardsKickoff");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Tackle Yards: ";
                try {
                    result_str += result.getString("YardsTackleLoss");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Tackle Loss Yards: ";
                try {
                    result_str += result.getString("YardsTackleLoss");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Sack Yards: ";
                try {
                    result_str += result.getString("YardsSack");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Penalty Yards: ";
                try {
                    result_str += result.getString("YardsPenalty");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Tackles Solo: ";
                try {
                    result_str += result.getString("TacklesSolo");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Tackles Assist: ";
                try {
                    result_str += result.getString("TacklesAssist");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Tackles For Loss: ";
                try {
                    result_str += result.getString("TacklesForLoss");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Interceptions Pass: ";
                try {
                    result_str += result.getString("InterceptionsPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Interceptions Return: ";
                try {
                    result_str += result.getString("InterceptionsReturn");
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

                result_str += " | Rush Attempts: ";
                try {
                    result_str += result.getString("AttemptsRush");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Pass Attempts: ";
                try {
                    result_str += result.getString("AttemptsPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | FG Attempts: ";
                try {
                    result_str += result.getString("AttemptsFG");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Offense XP Kick Attempts: ";
                try {
                    result_str += result.getString("AttemptsOffenseXPKick");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Offense 2XP Attempts: ";
                try {
                    result_str += result.getString("AttemptsOffense2XP");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Third Down Attempts: ";
                try {
                    result_str += result.getString("AttemptsThirdDown");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fourth Down Attempts: ";
                try {
                    result_str += result.getString("AttemptsFourthDown");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Red Zone Attempts: ";
                try {
                    result_str += result.getString("AttemptsRedZone");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Rush: ";
                try {
                    result_str += result.getString("TDRush");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Kickoff Return: ";
                try {
                    result_str += result.getString("TDKickoffReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Punt Return: ";
                try {
                    result_str += result.getString("TDPuntReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Fumble Return: ";
                try {
                    result_str += result.getString("TDFumbleReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Interception Return: ";
                try {
                    result_str += result.getString("TDInterceptionReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Misc Return: ";
                try {
                    result_str += result.getString("TDMiscReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | TouchDown Red Zone: ";
                try {
                    result_str += result.getString("TDRedZone");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Pass Conversion: ";
                try {
                    result_str += result.getString("ConvPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Third Down Conversion: ";
                try {
                    result_str += result.getString("ConvThirdDown");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Fourth Down Conversion: ";
                try {
                    result_str += result.getString("ConvFourthDown");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Pass Completion: ";
                try {
                    result_str += result.getString("PassCompletion");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Misc Return: ";
                try {
                    result_str += result.getString("MiscReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | FG Made: ";
                try {
                    result_str += result.getString("MadeFG");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Offensive XP Kick Made: ";
                try {
                    result_str += result.getString("MadeOffensiveXPKick");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Offensive 2XP Made: ";
                try {
                    result_str += result.getString("MadeOffensive2XP");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Defensive 2XP Made: ";
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

                result_str += " | Kickoffs Return: ";
                try {
                    result_str += result.getString("KickoffsReturn");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoffs Out Of Bounds: ";
                try {
                    result_str += result.getString("KickoffsOutOfBounds");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoffs Onside: ";
                try {
                    result_str += result.getString("KickoffsOnside");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kickoffs TouchBacks: ";
                try {
                    result_str += result.getString("KickoffTouchBacks");
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
                    result_str += result.getString("PassBrokenUp");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Kick/Punt Blocked: ";
                try {
                    result_str += result.getString("KickPuntBlocked");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | First Down Rush: ";
                try {
                    result_str += result.getString("FirstDownRush");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | First Down Pass: ";
                try {
                    result_str += result.getString("FirstDownPass");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | First Down Penalty: ";
                try {
                    result_str += result.getString("FirstDownPenalty");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Time Of Possession: ";
                try {
                    result_str += result.getString("TimeOfPossession");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Penalty: ";
                try {
                    result_str += result.getString("Penalty");
                } catch (Exception e) {
                    result_str += "NULL";
                }

                result_str += " | Red Zone FG: ";
                try {
                    result_str += result.getString("RedZoneFG");
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

                result_str += " | Kickoff: ";
                try {
                    result_str += result.getString("Kickoff");
                } catch (Exception e) {
                    result_str += "NULL";
                }

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

            // SQL Queries - 4 cases
            // - Home Team, Away Team, and Year given
            // - Home Team, Away Team given
            // - Home Team and Year given
            // - Only Home Team Given
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

            else if (!team.equals("") && awayteam.equals("") && (year < 2005 || year > 2013)) {
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

            // Fetch SQL Results for output in DBMS
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
            Statement stmt = conn.createStatement();

            String sqlStatement = String.format("SELECT DISTINCT\"FirstName\",\"LastName\",\"TeamName\",\"Uniform\","
                    + "\"Class\",\"Position\",\"Height\",\"Weight\",\"LastSchool\",\"Hometown\",\"HomeState\","
                    + "\"HomeCountry\" "

                    + "FROM\"Team\" INNER JOIN(SELECT*FROM\"Team_Yearwise\" "
                    + "INNER JOIN(SELECT*FROM\"Player_Yearwise\" INNER JOIN(SELECT*FROM\"Player\""
                    + " WHERE\"FirstName\" LIKE'%s%%' AND\"LastName\" LIKE'%s%%')AS player_data ON player_data."
                    + "\"PlayerId\"=\"Player_Yearwise\" .\"PlayerId\")AS player_data2 ON\"Team_Yearwise\""
                    + " .\"TeamYearId\"=player_data2.\"TeamYearId\")AS team_data ON\"Team\" .\"TeamId\"=team_data."
                    + "\"TeamId\";", fname, lname);

            // Fetch SQL Results for output in DBMS
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
            Statement stmt = conn.createStatement();
            String sqlStatement = "";

            // Two cases for SQL Statement - Only Full Name given | Full Name and Year is
            // given
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

            // Fetch SQL Results for output in DBMS
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

    public static String questionThree(String team, Integer year, Connection conn) {
        String result_str = "";
        try {
            Statement stmt = conn.createStatement();
            String sqlStmt = "";

            // SQL Statements Creation; Two Cases - Only Team is given | Both Team and Year
            // are given
            if (!team.equals("") && (year > 2013 || year < 2005)) {
                sqlStmt = String.format("SELECT DISTINCT \"HomeTeamName\", \"AwayTeamName\", \"YardsRush\", \"Date\" "
                        + "FROM \"Team_Metrics_Gamewise\" INNER JOIN (SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", * FROM \"Team\" "

                        + "INNER JOIN (SELECT \"TeamName\" AS \"HomeTeamName\", * FROM \"Team\" "

                        + "INNER JOIN (SELECT * FROM \"Game\" WHERE (\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') "
                        + "OR \"AwayTeamId\" = (SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%'))) AS game_data ON \"TeamId\" = \"HomeTeamId\") "
                        + "AS team1_data ON \"Team\".\"TeamId\" = \"AwayTeamId\") AS team_data2 ON team_data2. \"GameId\" = \"Team_Metrics_Gamewise\".\"GameId\" "
                        + "AND \"Team_Metrics_Gamewise\".\"TeamId\" != ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%') "
                        + "ORDER BY \"YardsRush\" DESC LIMIT 1;", team, team, team);
            }

            else if (!team.equals("") && !(year > 2013 || year < 2005)) {
                sqlStmt = String.format(
                        "SELECT DISTINCT \"HomeTeamName\", \"AwayTeamName\", \"YardsRush\", \"Date\" FROM \"Team_Metrics_Gamewise\" "
                                + "INNER JOIN (SELECT \"Team\".\"TeamName\" AS \"AwayTeamName\", * FROM \"Team\" "

                                + "INNER JOIN (SELECT \"TeamName\" AS \"HomeTeamName\", * FROM \"Team\" "

                                + "INNER JOIN (SELECT * FROM \"Game\" WHERE (\"HomeTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE "
                                + "\"TeamName\" LIKE '%s%%') OR \"AwayTeamId\" = ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE"
                                + "\"TeamName\" LIKE '%s%%'))) AS game_data ON \"TeamId\" = \"HomeTeamId\") AS team1_data ON \"Team\".\"TeamId\" "
                                + "= \"AwayTeamId\") AS team_data2 ON team_data2. \"GameId\" = \"Team_Metrics_Gamewise\".\"GameId\" "
                                + "AND \"Team_Metrics_Gamewise\".\"TeamId\" != ( SELECT DISTINCT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s%%')"
                                + "WHERE EXTRACT(YEAR FROM \"Date\") = %s ORDER BY \"YardsRush\" DESC LIMIT 1;",
                        team, team, team, year.toString());
            }

            ResultSet result = stmt.executeQuery(sqlStmt);
            String temp = "";

            while (result.next()) {
                result_str += "Team Given: ";
                temp = result.getString("HomeTeamName");

                // Adjust Query output to keep GUI output consistent
                if (!temp.equals(team)) {
                    result_str += result.getString("AwayTeamName");

                    result_str += " | Team with most yards rushed against given: ";
                    result_str += temp;
                }

                else {
                    result_str += temp;

                    result_str += " | Team with most yards rushed against given: ";
                    result_str += result.getString("AwayTeamName");
                }

                result_str += " | Yards rushed: ";
                result_str += result.getString("YardsRush");

                result_str += " | Date: ";
                result_str += result.getString("Date");

                result_str += "\n";
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error accessing most rushing yards vs. the given team. Make sure that the Team name and Year is correct");
        }
        if (result_str == "") {
            return "most rushing yards vs. the given team data non existent\n";
        }
        return result_str;
    }

    public static String questionFive(String team, Integer year, Connection conn) {
        String result_str = "";
        double wins = 0.0;
        double losses = 0.0;
        try {
            Statement stmt = conn.createStatement();

            // Two cases - Only Team given | Team and Year given
            if (!team.equals("") && (year > 2013 || year < 2005)) {
                String sqlStmt = String.format("SELECT AVG(\"Attendance\")as Attendance FROM\"Game\" "
                        + "INNER JOIN(SELECT*from\"Team_Metrics_Gamewise\" WHERE\"TeamId\"="
                        + "(SELECT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%'))as team_data ON\"Game\" "
                        + ".\"GameId\"=team_data.\"GameId\" WHERE\"Result\" LIKE'WIN';", team);

                ResultSet result = stmt.executeQuery(sqlStmt);
                while (result.next()) {
                    result_str += team + "'s Average Attendance with WINS = ";
                    wins = result.getFloat("attendance");
                    result_str += wins;
                    result_str += "\n";
                }

                String sqlStmt2 = String.format("SELECT AVG(\"Attendance\")as Attendance FROM\"Game\" "
                        + "INNER JOIN(SELECT*from\"Team_Metrics_Gamewise\" WHERE\"TeamId\"="
                        + "(SELECT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%'))as team_data ON\"Game\" "
                        + ".\"GameId\"=team_data.\"GameId\" WHERE\"Result\" LIKE'LOSS';", team);

                ResultSet result2 = stmt.executeQuery(sqlStmt2);
                while (result2.next()) {
                    result_str += team + "'s Average Attendance with LOSSES = ";
                    losses = result2.getFloat("attendance");
                    result_str += losses;
                    result_str += "\n";
                }
                if (wins > losses) {
                    result_str += "We see that the team had a higher attendance in WINS by ";
                    double percent = ((wins - losses) * 100 / losses);
                    result_str += percent;
                    result_str += "%.\n";
                } else {
                    result_str += "We see that the team had a higher attendance in LOSSES by ";
                    double percent2 = ((losses - wins) * 100 / wins);
                    result_str += percent2;
                    result_str += "%.\n";
                }

            } else if (!team.equals("") && !(year > 2013 || year < 2005)) {
                String sqlStmt = String.format("SELECT AVG(\"Attendance\")as Attendance FROM\"Game\" "
                        + "INNER JOIN(SELECT*from\"Team_Metrics_Gamewise\" WHERE\"TeamId\""
                        + "=(SELECT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%'))as team_data ON\"Game\" "
                        + ".\"GameId\"=team_data.\"GameId\" WHERE\"Result\" LIKE'WIN' AND EXTRACT(YEAR FROM \"Date\")=%s;",
                        team, year.toString());

                ResultSet result = stmt.executeQuery(sqlStmt);
                while (result.next()) {
                    result_str += team + "'s Average Attendance with WINS in " + year.toString() + " = ";
                    wins = result.getFloat("attendance");
                    result_str += wins;
                    result_str += "\n";
                }

                String sqlStmt2 = String.format("SELECT AVG(\"Attendance\")as Attendance FROM\"Game\" "
                        + "INNER JOIN(SELECT*from\"Team_Metrics_Gamewise\" WHERE\"TeamId\"="
                        + "(SELECT\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s%%'))as team_data ON\"Game\" "
                        + ".\"GameId\"=team_data.\"GameId\" WHERE\"Result\" LIKE'LOSS' AND EXTRACT(YEAR FROM \"Date\")=%s;",
                        team, year.toString());

                ResultSet result2 = stmt.executeQuery(sqlStmt2);
                while (result2.next()) {
                    result_str += team + "'s Average Attendance with LOSSES in " + year.toString() + " = ";
                    losses = result2.getFloat("attendance");
                    result_str += losses;
                    result_str += "\n";
                }
                if (wins > losses) {
                    result_str += "We see that the team had a higher attendance in WINS by ";
                    double percent = ((wins - losses) * 100 / losses);
                    result_str += percent;
                    result_str += "%.\n";
                } else {
                    result_str += "We see that the team had a higher attendance in LOSSES by ";
                    double percent2 = ((losses - wins) * 100 / wins);
                    result_str += percent2;
                    result_str += "%.\n";
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error accessing team win vs loss hypothesis");
        }

        if (result_str == "") {
            return "team win vs loss hypothesis non existent\n";
        }

        return result_str;
    }

    public static String questionOne(String team, String awayteam, Connection conn) {
        String result_str = "";
        try {
            Statement stmt = conn.createStatement();
            String sqlStmt = "";

            if (!team.equals("") && !awayteam.equals("")) {
                sqlStmt = String.format(
                        "SELECT \"TeamId\", \"OpposingTeamId\", \"GameId\" FROM \"Team_Metrics_Gamewise\" WHERE \"Result\"='WIN' AND \"TeamId\"=(SELECT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s') AND \"OpposingTeamId\"=(SELECT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s') LIMIT 1;",
                        team, awayteam);
            }

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += String.format("%s won against %s", team, awayteam);
                result_str += "\n";
            }
            if (result_str.equals("")) {
                List<String> teams = new ArrayList<>();
                JOptionPane.showMessageDialog(null,
                        "The function might take around 30s to run as we are trying our best to find a link.");

                result_str = qOneHelper(team, awayteam, team, teams, conn);
                if (result_str.contains("After going over 15 links, no match found.")) {
                    result_str = "After going over 15 links, no match found.";
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error accessing Team links please make sure team name is correct");
        }
        return result_str;
    }

    public static String qOneHelper(String team, String awayteam, String initTeam, List<String> teams,
            Connection conn) {
        String result_str = "";
        if (teams.size() > 15) {
            return "After going over 15 links, no match found.";
        }
        try {
            Statement stmt = conn.createStatement();
            String sqlStmt = "";

            if (!team.equals("") && !awayteam.equals("")) {
                sqlStmt = String.format(
                        "SELECT \"TeamId\", \"OpposingTeamId\", \"GameId\" FROM \"Team_Metrics_Gamewise\" WHERE \"Result\"='WIN' AND \"TeamId\"=(SELECT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s') AND \"OpposingTeamId\"=(SELECT \"TeamId\" FROM \"Team\" WHERE \"TeamName\" LIKE '%s') LIMIT 1;",
                        team, awayteam);
            }

            ResultSet result = stmt.executeQuery(sqlStmt);
            while (result.next()) {
                result_str += String.format("%s | %s", team, awayteam);
            }
            if (result_str.equals("")) {

                // selecting team with max wins
                Integer max = 0;
                String maxOppTeam = "";

                stmt = conn.createStatement();
                sqlStmt = "";
                if (!team.equals("") && !awayteam.equals("")) {
                    sqlStmt = String.format(
                            "SELECT\"Team_Metrics_Gamewise\" .\"TeamId\",\"OpposingTeamId\",\"TeamName\",\"GameId\" FROM\"Team_Metrics_Gamewise\" INNER JOIN\"Team\" ON\"OpposingTeamId\"=\"Team\" .\"TeamId\" WHERE\"Result\"='WIN' AND\"Team_Metrics_Gamewise\" .\"TeamId\"=(SELECT\"Team\" .\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s')LIMIT 15;",
                            team);
                }
                result = stmt.executeQuery(sqlStmt);
                while (result.next()) {
                    String oppTeam = result.getString("TeamName");
                    Statement stmt2 = conn.createStatement();
                    String sqlStmt2 = "";
                    if (!team.equals("") && !awayteam.equals("")) {
                        sqlStmt2 = String.format(
                                "SELECT Count(*) FROM \"Team_Metrics_Gamewise\" WHERE\"Result\"='WIN' AND \"TeamId\"=(SELECT\"Team\" .\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s') AND \"OpposingTeamId\"!=(SELECT\"Team\" .\"TeamId\" FROM\"Team\" WHERE\"TeamName\" LIKE'%s');",
                                oppTeam, initTeam);
                    }
                    ResultSet result2 = stmt2.executeQuery(sqlStmt2);

                    while (result2.next()) {
                        Integer count = result2.getInt("count");
                        if (count > max && !teams.contains(oppTeam)) {
                            max = count;
                            maxOppTeam = oppTeam;
                        }
                    }
                }
                teams.add(maxOppTeam);
                result_str = String.format("%s | %s", team, qOneHelper(maxOppTeam, awayteam, initTeam, teams, conn));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error accessing Team links please make sure team name is correct");
        }
        return result_str;
    }

    public static void main(String args[]) {

        launch(args);

        // question three
       /* System.out.println(questionThree("Texas A&M", 0, conn));
        System.out.println(questionThree("Auburn", 2013, conn));
        System.out.println(questionThree("Texas A&M", 2005, conn));
        System.out.println(questionThree("Clemson", 2013, conn));
        System.out.println(questionThree("Baylor", 0, conn));
        System.out.println("\n");

        // question five
        System.out.println(questionFive("Texas A&M", 0, conn));
        System.out.println("\n");
        System.out.println(questionFive("Clemson", 2013, conn));
        System.out.println("\n");*/

        // TODO: try catch to handle bad team inputs
        //System.out.println(questionFive("Shrey is Bai", 2013, conn));

    }
    // end backendmain
}// end Class