import java.sql.*;


public class DatabaseConnection{

  Connection conn;

  public DatabaseConnection(){
    this.conn = connect();
  }

  private static Connection connect(){
    try{
      String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
      String DB_URL = "jdbc:oracle:thin:@cloud-34-133.eci.ucsb.edu:1521:XE";

      Class.forName(JDBC_DRIVER);
      Connection conn = DriverManager.getConnection(DB_URL, "yash_rane", "3814795");
      return conn;
    }
    catch(SQLException se) {
      se.printStackTrace();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResultSet execute_query(String query){


		Statement stmt = null;
    ResultSet rs = null;
    try{
      stmt = this.conn.createStatement();
      rs = stmt.executeQuery(query);
    }
    catch(Exception e){
      System.out.print("Something went Wrong querying the server");
      e.printStackTrace();
    }
    // finally {
    //         /*
    //          * close any jdbc instances here that weren't
    //          * explicitly closed during normal code path, so
    //          * that we don't 'leak' resources...
    //          */
    //
    //         if (stmt != null) {
    //             try {
    //                 stmt.close();
    //             } catch (SQLException sqlex) {
    //                 // ignore, as we can't do anything about it here
    //             }
    //
    //             stmt = null;
    //         }
    //
    //         if (conn != null) {
    //             try {
    //                 conn.close();
    //             } catch (SQLException sqlex) {
    //                 // ignore, as we can't do anything about it here
    //             }
    //
    //             conn = null;
    //         }
    // }

    return rs;
  }

}
