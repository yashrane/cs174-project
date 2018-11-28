import java.sql.*;


public class SQL_Connection{

  Connection conn;

  public SQL_Connection(DB_url){
    conn = connect(DB_url)
  }

  public static Connection connect(DB_URL){
    try{
      String JDBC_DRIVER = "com.mysql.jdbc.Driver";
      DB_URL = "jdbc:mysql://localhost:3306/iot";

      Class.forName(JDBC_DRIVER);
      Connection conn = DriverManager.getConnection(DB_URL, "root", "");
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
    finally {
            /*
             * close any jdbc instances here that weren't
             * explicitly closed during normal code path, so
             * that we don't 'leak' resources...
             */

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                    // ignore, as we can't do anything about it here
                }

                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                    // ignore, as we can't do anything about it here
                }

                conn = null;
            }
    }

    return rs;
  }

}
