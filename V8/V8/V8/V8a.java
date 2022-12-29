package V8;
import java.sql.*;

/**
 * Forrit sem skrifar meðallaun allra starfsmanna í COMPANY gagnagrunninum.
 */
public class V8a {
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
            String testurl = "ðöæóáí";
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
     * Skilar meðaltali launa allra starfsmanna.
     */
    public static void meanSalaryOfAllEmployees() {
        String sql = "SELECT AVG(Salary) FROM EMPLOYEE";

        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    System.out.println(rs.getDouble("AVG(Salary)"));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        connect(); // Tengist gagnagrunni og prenta út skilaboð þess eðlis.
        meanSalaryOfAllEmployees(); // Framkvæmi sql statement-ið og prenta út útkomuna.
        disconnect(); // Aftengist gagnagrunni og prenta út skilaboð þess eðlis.
    }
}
