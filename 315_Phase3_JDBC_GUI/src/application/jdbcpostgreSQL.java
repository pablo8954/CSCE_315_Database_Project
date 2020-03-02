package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.Random;

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
public class jdbcpostgreSQL extends Application{
	
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
	private static WindowController controller;
	public static Stage mainStage;
	
	public void start(Stage primaryStage) throws IOException {
		//Launch database connection
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
        
		FXMLLoader loader =  new FXMLLoader();
		loader.setLocation(getClass().getResource("Window.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 600, 800);
		controller = loader.getController();

		primaryStage.setScene(scene);
		primaryStage.setTitle("Gay");
		primaryStage.resizableProperty().setValue(Boolean.FALSE);
		primaryStage.show();
		
		
		
		new Thread() {
			public void run() {
				//Keep waking up thread for duration of program
				while(true) {
					
					try {
						Thread.sleep(new Random().nextInt(1000));
						System.out.println("new thread");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Platform.runLater(new Runnable() {
						
						public void run() {
							
							resultsRequested = controller.getResultsRequested();
							//If get results pushed
							if(resultsRequested) {
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
								if(dataSelection.equals("Conference")) {
									result = ConfDataFetch(conferenceName, conn);
								}
								else if(dataSelection.equals("Game")) {
									result = gameDataFetcWithNameYear(teamName, year, conn);
								}
								else if(dataSelection.equals("Stadium")) {
									result = stadiumDataFetch(stadiumName, conn);
								}
								
								controller.updateOutputTextArea(result);
								System.out.println(result);
								
								if(controller.generateTextFile()) {
									//write to file
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

            /*
             * 4 cases 
             * 
             * 1: proper name and year inputs are provided -> return game data for a team in a specific year
             * 2: only year is provided -> return game data pertaining to specific year
             * 3: only name is provided -> return game data pertaining to specific team
             * 4: no name or year inputs are provided -> return all game data available
             * 
             * */
            
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

    public static void main(String args[]) {
        
        launch(args);
        

        //
        // MAIN CODE

       /* System.out.println(stadiumDataFetch("ASU", conn));
        System.out.println(stadiumDataFetch("Shrey", conn));

        System.out.println(ConfDataFetch("Big Sky", conn));
        System.out.println(ConfDataFetch("Football_Sucks", conn));

        System.out.println(gameDataFetcWithNameYear("Texas A&", 2013, conn));
        System.out.println(gameDataFetcWithNameYear("", 2013, conn));
        System.out.println(gameDataFetcWithNameYear("Texas A&", 0, conn));
        System.out.println(gameDataFetcWithNameYear("", 0, conn));
        System.out.println(gameDataFetcWithNameYear("Bazinga", 2003, conn));*/

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