import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

// Notkun: java -cp .;sqlite-jdbc-....jar V11 <args>
//         þar sem <args> er: [autocommit|noautocommit] [index|noindex]
// Eftir:  Búið er að mæla tíma fyrir gagnagrunnsaðgerðir og
//         skrifa niðurstöður

// Use:  java -cp .;sqlite-jdbc-....jar V11 <args>
//       where <args> is: [autocommit|noautocommit] [index|noindex]
// Post: The duration of database operations has been measured and
//       the results written.
public class V11
{
    public static void main( String[] args )
        throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        boolean USE_AUTOCOMMIT = args[0].equals("autocommit");
        boolean USE_INDEX = args[1].equals("index");
    
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection("jdbc:sqlite:v11.db");
            conn.setAutoCommit(USE_AUTOCOMMIT);

            /* Hér vantar Java forritstexta sem gerir eftirfarandi:
             * 1. Eyðir töflunni R ef hún er til, þ.e. (í SQLite)
             *         DROP TABLE IF EXISTS R
             * 2. Eyðir vísaskránni RINDEX ef hún er til, þ.e.
             *         DROP INDEX IF EXISTS RINDEX
             * 3. Býr til töflu R sem hefur dálk key af tagi INTEGER
             *    sem skal vera lykill, og dálk value af tagi DOUBLE, þ.e.
             *         CREATE TABLE R( key INTEGER PRIMARY KEY, value DOUBLE )
             * 4. Býr til vísaskrá RINDEX fyrir dálkinn value í R, þ.e.
             *         CREATE INDEX RINDEX ON R(value)
             *    en aðeins ef USE_INDEX er satt.
             * 5. Býr til PreparedStatement pstmt til að setja gildi í R, þ.e.
             *         INSERT INTO R VALUES(?,?)
             */
            
            Statement stmt = conn.createStatement();
            String cm1 = "DROP TABLE IF EXISTS R";
            String cm2 = "DROP INDEX IF EXISTS RINDEX";
            String cm3 = "CREATE TABLE R (key INTEGER PRIMARY KEY,value DOUBLE)";
            String cm4 = "CREATE INDEX RINDEX ON R(value)";
            stmt.execute(cm1);
            stmt.execute(cm2);
            stmt.execute(cm3);
            if (USE_INDEX) {
                stmt.execute(cm4);
            }
            String pstmtString = "INSERT INTO R VALUES(?,?)";
            PreparedStatement pstmt = conn.prepareStatement(pstmtString);


            long start,end;

            start = System.nanoTime();
            int i;
            for( i=0 ; i!=1000000 ; i++ )
            {
                /*
                 * Hér vantar Java forritstexta til að bæta við tvennd (i,y)
                 * í R þar sem y skal vera slembitala jafndreifð á bilinu
                 * [0,2[. Kallið á Math.random() til að fá tölu á bilinu
                 * [0,1[ og varpið tölunni með viðeigandi margföldun yfir
                 * í bilið [0,2[.
                 * Notið pstmt til að framkvæma innsetninguna.
                 * Einnig skal fylgjast með tímanum með kalli á
                 * System.nanoTime(). Ef meira en ein mínúta er liðin síðan
                 * lykkjan hófst skal hætta í lykkjunni.
                 *
                 * Takið eftir að ef USE_AUTOCOMMIT er satt þá gerist sjálfkrafa
                 * COMMIT eftir hverja SQL aðgerð í þessari lykkju.
                 */
                if (System.nanoTime() > (start + 60000)) {
                    break;
                }
                int min = 0;
                int max = 2;
                double random_int = (double)Math.floor(Math.random()*(max-min+1)+min);
                pstmt.setDouble(i, random_int);
                pstmt.execute();

            }
            if( !USE_AUTOCOMMIT ) conn.commit();

            end = System.nanoTime();
            System.out.println("Tími fyrir/Time for "+
                               i+" innsetningar/inserts: "+
                               (double)(end-start)/1e9
                              );

            System.out.println("Tími per innsetningu/Time per insert: "+
                               (double)(end-start)/1e9/i
                              );

            start = System.nanoTime();
            ResultSet r =
                stmt.executeQuery
                    ("SELECT COUNT(*) FROM R WHERE "+
                     "value BETWEEN 0.05 AND 0.15"
                    );
            r.next();
            System.out.println("Niðurstaða leitar/Result of search: "+r.getInt(1));
            System.out.println("Tími fyrir leit/Time for search: "+
                               (double)(System.nanoTime()-start)/1e9
                              );
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(conn != null)
                  conn.close();
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }
}
