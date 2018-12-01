import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadDB{


  public static void main(String [] args){
    DatabaseConnection database = new DatabaseConnection();

    reset_database(database);
    create_tables(database);

    populate_customers("res/customers.csv", database);
  }

  public static void reset_database(DatabaseConnection database){
    ResultSet tables = database.execute_query("select * from user_tables");
    System.out.println(tables);
    try{
      if(tables.next())
        drop_tables(database);//NOTE this might cause an error if a table doesnt exist
    }catch(SQLException e){
      e.printStackTrace();
    }
  }


  public static void create_tables(DatabaseConnection database){
    String createCustomer = "CREATE TABLE Customer ("+
  	   "name		CHAR(128),"+
  	   "taxID		CHAR(9),"+
       "address	CHAR(128),"+
       "pin		CHAR(4) DEFAULT '1717',"+
       "PRIMARY KEY (taxID),"+
       "UNIQUE (pin)"+
     ")";
    database.execute_query(createCustomer);

    String createAccounts = "CREATE TABLE Account ("+
  	  "type		CHAR(16),"+
  	  "balance		REAL,"+
      "bank_branch	CHAR(128),"+
      "interest_rate		REAL,"+
      "a_id		CHAR(10),"+
      "isClosed		INTEGER,"+
      "linked_id		CHAR(10),"+
      "PrimaryOwner		CHAR(9),"+
      "PRIMARY KEY (a_id),"+
      "FOREIGN KEY(PrimaryOwner) REFERENCES Customer,"+
      "FOREIGN KEY(linked_id) REFERENCES Account,"+
      "CONSTRAINT CHK_Balance CHECK (balance > 0.0),"+
      "CONSTRAINT CHK_Link CHECK ( (type='pocket' and linked_id is not null) or"+
        "(type!='pocket' and linked_id is null) )"+
     ")";
    database.execute_query(createAccounts);
  }

  public static void drop_tables(DatabaseConnection database){
    database.execute_query("drop table customer");
    database.execute_query("drop table account");
  }

  public static String parse(String s){
    return s.replace("'", "''");
  }

  public static void populate_customers(String filename, DatabaseConnection database){


    String line="";
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      while ((line = br.readLine()) != null) {
        String[] columns = line.split(",");

        String name = parse(columns[0]);
        String taxID = parse(columns[1]);
        String address = parse(columns[2]);
        String pin = parse(columns[3]);

        String query = "insert into Customer (name, taxID, address, pin) values ('"+
          name+"', '" + taxID + "', '" + address + "', '" + pin + "')";

        database.execute_query(query);

      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void populate_accounts(String filename, DatabaseConnection database){
    String line="";
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      while ((line = br.readLine()) != null) {
        String[] columns = line.split(",");

        String id = parse(columns[0]);
        String type = parse(columns[1]);
        String branch = parse(columns[2]);
        String primary_owner = parse(columns[3]);

        // String interest_rate
        // String linked_id
        // String balance


        String query = "insert into Account (a_id, type, bank_branch, PrimaryOwner, interest_rate, isClosed, linked_id, balance ) values ('"+
          id+"', '" + type + "', '" + branch + "', '" + primary_owner + "', " + interest_rate + ", 0, '" + linked_id +"', "+balance+")";

        database.execute_query(query);

      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
