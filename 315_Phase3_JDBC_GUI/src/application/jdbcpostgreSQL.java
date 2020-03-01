import java.sql.*;
import javax.swing.JOptionPane;

//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQL {

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

    public static void main(String args[]) {
        // dbSetup my = new dbSetup();
        // // Building the connection
        // Connection conn = null;
        // try {
        // Class.forName("org.postgresql.Driver");
        // conn =
        // DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/ace_of_spades",
        // my.user,
        // my.pswd);
        // } catch (Exception e) {
        // e.printStackTrace();
        // System.err.println(e.getClass().getName() + ": " + e.getMessage());
        // System.exit(0);
        // } // end try catch
        // JOptionPane.showMessageDialog(null, "Opened database successfully");
        // String cus_lname = "";
        // try {
        // // create a statement object
        // Statement stmt = conn.createStatement();
        // // create an SQL statement
        // String sqlStatement = "SELECT * FROM \"Player\" LIMIT 1;";
        // // send statement to DBMS
        // ResultSet result = stmt.executeQuery(sqlStatement);

        // // OUTPUT
        // JOptionPane.showMessageDialog(null, "Customer Last names from the
        // Database.");
        // // System.out.println("______________________________________");
        // while (result.next()) {
        // // System.out.println(result.getString("cus_lname"));
        // cus_lname += result.getString("FirstName") + "\n";
        // }
        // } catch (Exception e) {
        // JOptionPane.showMessageDialog(null, "Error accessing Database.");
        // }
        // JOptionPane.showMessageDialog(null, cus_lname);
        // // closing the connection
        // try {
        // conn.close();
        // JOptionPane.showMessageDialog(null, "Connection Closed.");
        // } catch (Exception e) {
        // JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
        // } // end try catch

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

        System.out.println(stadiumDataFetch("ASU", conn));
        System.out.println(stadiumDataFetch("Shrey", conn));

        // END MAIN CODE
        //

        try {
            conn.close();
            // JOptionPane.showMessageDialog(null, "Connection Closed.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
        }

    }// end main
}// end Class