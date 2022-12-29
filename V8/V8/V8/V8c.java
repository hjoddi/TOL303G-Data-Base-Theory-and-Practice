package V8;

import java.sql.*;

public class V8c {
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
     * Hækka laun allra í EMPLOYEE töflunni í COMPANY gagnagrunninum um 3%.
     * Notar SQL UPDATE skipun til að hækka öll laun samtímis í einni skipun.
     */
    public static void blanket3PercentRaise() {
        // SQL Skipun.
        String SQLUpdateSalary = "UPDATE Employee SET Salary = Salary * 1.03";
        try {
            if (conn != null) {
                PreparedStatement preparedStatement = conn.prepareStatement(SQLUpdateSalary); // UPDATE skipunin tilbúin.
                preparedStatement.executeUpdate(); // UPDATE skipunin framkvæmd.
                System.out.println("Laun allra í Employee töflunni hækkuð um 3%.");
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        connect(); // Tengist gagnagrunni og prenta út skilaboð þess eðlis.
        blanket3PercentRaise(); // Hækka laun allra í Employee töflunni í company gagnagrunninum um 3%.
        disconnect(); // Aftengist gagnagrunni og prenta út skilaboð þess eðlis.
    }
}
