package V8;

import java.sql.*;

/**
 * Forrit sem gefur öllum starfsmönnum í COMPANY gagnagrunninum 200 dala launahækkun.
 */
public class V8b {
    /**
     * Breytur.
     */
    static Connection conn = null; // Connection við gagnagrunninn 'company.db'.

    /**
     * Tengist gagnagrunninum.
     */
    public static void connect() {

        try {
            // db parameter.
            String url = "jdbc:sqlite:C:\\Users\\Notandi\\Dropbox\\Skólinn\\Tölvunarfræði\\2022-haust" +
                    "\\Gagnasafnsfraedi\\V8\\forritun\\forritun\\company.db";
            // Bý til connection fyrir gagnagrunninn.
            conn = DriverManager.getConnection(url);
            System.out.println("Tenging við gagnagrunn stofnuð.");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Aftengist gagnagrunninum.
     */
    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Tenging við gagnagrunn rofin.");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Gefur öllum starfsmönnum í COMPANY gagnagrunninum 200 dala launahækkun.
     */
    public static void raiseAllSalaryBy200() {
        // SQL skipanir.
        String SQLSelectSSN = "SELECT Ssn FROM EMPLOYEE";
        String SQLUpdateSalary = "UPDATE EMPLOYEE SET Salary = Salary + 200 WHERE " +
                "Ssn = ?";

        try {
            if (conn != null) {
                Statement stmt = conn.createStatement(); // SELECT skipanin til að finna Ssn.
                ResultSet rs = stmt.executeQuery(SQLSelectSSN); // ResultSet sem inniheldur Ssn starfsmanna.

                // Ítra í gegnum Ssn og uppfæri laun samsvarandi starfsmanns.
                while (rs.next()) {
                    String ssn = rs.getString("Ssn"); // Næsta ssn.
                    PreparedStatement pstmt = conn.prepareStatement(SQLUpdateSalary); // UPDATE skipunin til að uppfæra laun.
                    pstmt.setString(1, ssn); // Set næsta ssn úr ResultSettinu inn í Update skipunina SQLUpdateSalary.
                    pstmt.executeUpdate(); // Uppfæri laun starfsmanns með Ssn ssn.
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        connect(); // Tengist gagnagrunni og prenta út skilaboð þess eðlis.
        raiseAllSalaryBy200(); // Hækka laun allra starfsmanna í gagnagrunninum um 200 dali.
        disconnect(); // Aftengist gagnagrunni og prenta út skilaboð þess eðlis.
    }
}