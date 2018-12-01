import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadDB{


  public static void main(String [] args){
    DatabaseConnection database = new DatabaseConnection();

    reset_database(database);
    // create_tables(database);

    populate_customers("res/customers.csv", database);
    populate_accounts("res/accounts.csv", database);
    populate_owns("res/owns.csv", database);
  }

  public static void reset_database(DatabaseConnection database){
    ResultSet tables = database.execute_query("select table_name from user_tables");
    System.out.println(tables);


    try{
      while(tables.next())
        delete_from_tables(database, tables.getString("table_name"));//NOTE this might cause an error if a table doesnt exist
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
  	  "type		CHAR(32),"+
  	  "balance		REAL,"+
      "bank_branch	CHAR(128),"+
      "interest_rate		REAL,"+
      "a_id		CHAR(10),"+
      "isClosed		INTEGER,"+
      "linked_id		CHAR(10),"+
      "PrimaryOwner		CHAR(9),"+
      "PRIMARY KEY (a_id),"+
      "FOREIGN KEY(PrimaryOwner) REFERENCES Customer ON DELETE CASCADE,"+
      "FOREIGN KEY(linked_id) REFERENCES Account ON DELETE CASCADE,"+
      "CONSTRAINT CHK_Balance CHECK (balance > 0.0),"+
      "CONSTRAINT CHK_Link CHECK ( (type='Pocket' and linked_id is not null) or"+
        "(type!='Pocket' and linked_id is null) )"+
     ")";
    database.execute_query(createAccounts);

    String createOwns = "CREATE TABLE Owns("+
      "taxID		CHAR(9),"+
      "a_id		CHAR(10),"+
      "PRIMARY KEY (taxID, a_id),"+
      "FOREIGN KEY(taxID) REFERENCES Customer ON DELETE CASCADE,"+
      "FOREIGN KEY(a_id) REFERENCES Account ON DELETE CASCADE"+
      ")";
    database.execute_query(createOwns);
  }

  public static void delete_from_tables(DatabaseConnection database, String tablename){
    database.execute_query("delete from " + tablename);
    // System.out.println("Deleting " + tablename);
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
        String interest_rate = parse(columns[4]);
        String linked_id = parse(columns[5]);
        String balance = parse(columns[6]);

        String query;

        if(linked_id.isEmpty()){
          query = "insert into Account (a_id, type, bank_branch, PrimaryOwner, interest_rate, isClosed, balance ) values ('"+
            id+"', '" + type + "', '" + branch + "', '" + primary_owner + "', " + interest_rate + ", 0,"+balance+")";
        }
        else{
          query = "insert into Account (a_id, type, bank_branch, PrimaryOwner, interest_rate, isClosed, linked_id, balance ) values ('"+
            id+"', '" + type + "', '" + branch + "', '" + primary_owner + "', " + interest_rate + ", 0, '" + linked_id +"', "+balance+")";
        }

        database.execute_query(query);

      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void populate_owns(String filename, DatabaseConnection database){
    String line="";
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      while ((line = br.readLine()) != null) {
        String[] columns = line.split(",");

        String tax_id = parse(columns[0]);
        String aid = parse(columns[1]);

        String query = "insert into Owns (taxID, a_id) values ('"+
          tax_id+"', '" + aid + "')";

        database.execute_query(query);

      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
