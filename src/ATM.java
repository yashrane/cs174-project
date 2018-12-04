import java.util.*;
import java.sql.*;

public class ATM{

  private String current_user;
  private DatabaseConnection database;

  public ATM(){
    this.database = new DatabaseConnection();
  }

  /**
   * Logs a user in given their pin number
   * @param name the user's name
   * @param pin the user's pin number
   * @return whether the pin is valid for the given user
   */
  public boolean login(String name, String pin){
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Customer WHERE name = "+name+" AND pin= "+pin); //return a result set
      if(rs.next()) {
        current_user = rs.getString("taxID");
        return true;
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Gets the list of accounts associated with the current user
   * @return an array of string account ids
   */
  public String[] get_accounts(){
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Owns WHERE taxID = "+current_user); //return a result set
      ArrayList al = new ArrayList();
      while(rs.next()) {
        String id = rs.getString("a.id");
        al.add(id);
      }
      String[] a = new String[al.size()];
      al.toArray(a);
      return a;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Deposits the specified dollar amount in the given account
   * @param account the account id to depost into
   * @param amount the money to deposit in dollars
   * @return an error message if applicable. null otherwise
   */
  public String deposit(String account, double amount){
    // try{
    //   ResultSet rs = database.execute_query("UPDATE Account SET balance = balance+"+amount+" WHERE a_id = "+account+" AND (type= 'Student-Checkings' OR type= 'Interest-Checkings' OR type= 'Savings')");
    // }catch(SQLException e){
    //   e.printStackTrace();
    // }
    return null;
  }



  /**
   * Withdraws the specified dollar amount from the given account
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   * @return an error message if applicable. null otherwise
   */
  public String withdraw(String account, double amount){
    // try{
    //   ResultSet rs = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE a_id = "+account+" AND (type= 'Student-Checkings' OR type= 'Interest-Checkings' OR type= 'Savings')");
    //   //if(balance <= 0.01) then close;
    // }catch(SQLException e){
    //   e.printStackTrace();
    // }
    return null;
  }




  /**
   * Helper function for all tranactions that transfer money from one account to the other
   * @return an error message if applicable. null otherwise
   */
  private String transfer_helper(String from_account, String to_account, double amount){
    // try{
    //   ResultSet rs_from = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE a_id = "+from_account);
    //   ResultSet rs_to = database.execute_query("UPDATE Account SET balance = balance+"+amount+" WHERE a_id = "+to_account);
    //   //if(balance <= 0.01) then close;
    // }catch(SQLException e){
    //   e.printStackTrace();
    // }
    return null;
  }

  /**
   * Wires money from one account to the other. 2% fee
   * @param from_account the account to take money from
   * @param to_account the account to send money to
   * @param amount the money to wire in dollars
   * @return an error message if applicable. null otherwise
   */
  public String wire(String from_account, String to_account, double amount){
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Account WHERE PrimaryOwner = "+current_user+" AND a_id= "+from_account);
      if(rs.next()) {
        ResultSet rs_from = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE a_id = "+from_account+" AND (type= 'Student-Checkings' OR type= 'Interest-Checkings' OR type= 'Savings')");
        ResultSet rs_to = database.execute_query("UPDATE Account SET balance = balance+"+(0.98*amount)+" WHERE a_id = "+to_account+" AND (type= 'Student-Checkings' OR type= 'Interest-Checkings' OR type= 'Savings')");
      }
      //if(balance <= 0.01) then close;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Transfers money from one account to the other. accounts must have at least one
   * owner in common. amount cannot exceed $2000
   * @param from_account the account to take money from
   * @param to_account the account to send money to
   * @param amount the money to wire in dollars
   * @return an error message if applicable. null otherwise
   */
  public String transfer(String from_account, String to_account, double amount){
    if(amount <= 2000){ //amount cannot exceed $2000
      try{
        ResultSet rs = database.execute_query("SELECT * FROM Owns WHERE (taxID = "+current_user+" AND a_id= "+from_account+" AND (type= 'Student-Checkings' OR type= 'Interest-Checkings' OR type= 'Savings'+) AND (taxID = "+current_user+" AND a_id= "+to_account+" AND (type= 'Student-Checkings' OR type= 'Interest-Checkings' OR type= 'Savings')"); //accounts must have at least one owner in common
        if(rs.next()) {
            String wire = transfer_helper(from_account, to_account, amount);
        }
      }catch(SQLException e){
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Transfer money to the specified pocket account from it's linked account.
   * If it is the first transaction of the month with this account, apply a $5 fee
   */
  public String top_up(String account, double amount){
    return null;
  }

  /**
   * Move money from the specifed pocket account back to it's liked account. There is a 3% fee
   * If it is the first transaction of the month with this account, apply a $5 fee
   */
  public String collect(String account, double amount){
    return null;
  }

  /**
   * Move money from the specifed pocket account to another pocket account.
   * If it is the first transaction of the month with either account, apply a $5 fee to the relevant account
   */
  public String pay_friend(String from_account, String to_account, double amount){
    return null;
  }

  /**
   * Subtract money from the specified pocket account
   * If it is the first transaction of the month with this account, apply a $5 fee
   */
  public String purchase(String account, double amount){
    return null;
  }


  // TODO: figure out how this works
  public void log_transaction(){

  }

  /**
   * Subtract money from the specified pocket account
   * If it is the first transaction of the month with this account, apply a $5 fee
   */

  //NOTE: functions for top-up, collect, pay-friend have not been written



}
