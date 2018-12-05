import java.util.*;
import java.sql.*;

public class ATM{

  private String current_user;
  private DatabaseConnection database;
  private String currentDate;

  public ATM(){
    this.database = new DatabaseConnection();
    this.currentDate = getDate();
  }

  /**
   * Logs a user in given their pin number
   * @param name the user's name
   * @param pin the user's pin number
   * @return whether the pin is valid for the given user
   */
  public boolean login(String name, String pin){
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Customer WHERE name = "+LoadDB.parse(name)+" AND pin= "+LoadDB.parse(LoadDB.hashPin(pin))); //return a result set
      if(rs.next()) {
        current_user = rs.getString("taxID");
        return true;
      }
    }catch(SQLException e){

    }
    return false;
  }

  /**
   * Gets the list of accounts associated with the current user
   * @return an array of string account ids
   */
  public String[] get_accounts(){
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Owns WHERE taxID = "+LoadDB.parse(current_user)); //return a result set
      ArrayList al = new ArrayList();
      while(rs.next()) {
        String id = rs.getString("a_id");
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
     try{
       ResultSet rs = database.execute_query("UPDATE Account SET balance= balance+"+amount+" WHERE a_id= "+LoadDB.parse(account)+" AND (type= 'Student-Checking' OR type= 'Interest-Checking' OR type= 'Savings')");
       if(rs.next()) {}
     }catch(SQLException e){
       e.printStackTrace();
     }
    return null;
  }



  /**
   * Withdraws the specified dollar amount from the given account
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   * @return an error message if applicable. null otherwise
   */
  public String withdraw(String account, double amount){
     try{
       ResultSet rs = database.execute_query("UPDATE Account SET balance= balance-"+amount+" WHERE a_id= "+LoadDB.parse(account)+" AND (type= 'Student-Checking' OR type= 'Interest-Checking' OR type= 'Savings')");
       if(rs.next()) {}
       //if(balance <= 0.01) then close;
     }catch(SQLException e){
       e.printStackTrace();
     }
    return null;
  }




  /**
   * Helper function for all tranactions that transfer money from one account to the other
   * @return an error message if applicable. null otherwise
   */
  private String transfer_helper(String from_account, String to_account, double amount){
     try{
       ResultSet rs_from = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE a_id= "+LoadDB.parse(from_account));
       ResultSet rs_to = database.execute_query("UPDATE Account SET balance = balance+"+amount+" WHERE a_id= "+LoadDB.parse(to_account));
       if(rs_from.next() && rs_to.next()) {}
       //if(balance <= 0.01) then close;
     }catch(SQLException e){
       e.printStackTrace();
     }
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
        ResultSet rs_from = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE PrimaryOwner = "+current_user+" AND a_id= "+LoadDB.parse(from_account)+" AND (type= 'Student-Checking' OR type= 'Interest-Checking' OR type= 'Savings')");
        ResultSet rs_to = database.execute_query("UPDATE Account SET balance = balance+"+(0.98*amount)+" WHERE a_id= "+LoadDB.parse(to_account)+" AND (type= 'Student-Checking' OR type= 'Interest-Checking' OR type= 'Savings')");
        if(rs_from.next()) {}
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
        ResultSet rs = database.execute_query("SELECT * FROM Owns O1, Owns O2, Account A1, Account A2 WHERE O1.taxID = "+LoadDB.parse(current_user)+" AND O1.a_id= "+LoadDB.parse(from_account)+" and O1.a_id = A1.a_id AND (A1.type= 'Student-Checking' OR A1.type= 'Interest-Checking' OR A1.type= 'Savings') AND "+
                                                                                                                  "O2.taxID = "+LoadDB.parse(current_user)+" AND O2.a_id= "+LoadDB.parse(to_account)+" AND O2.a_id = A2.a_id AND (A2.type= 'Student-Checking' OR A2.type= 'Interest-Checking' OR A2.type= 'Savings')"); //accounts must have at least one owner in common
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
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Account WHERE a_id= "+LoadDB.parse(account)+" AND type= 'Pocket'");
      while(rs.next()){
        String l_id = rs.getString("linked_id");
        ResultSet rs_to = database.execute_query("UPDATE Account SET balance = balance+"+amount+" WHERE a_id= "+LoadDB.parse(account));
        ResultSet rs_from = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE a_id= "+LoadDB.parse(l_id));
      //$5 transaction fee- check log
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Move money from the specifed pocket account back to it's linked account. There is a 3% fee
   * If it is the first transaction of the month with this account, apply a $5 fee
   */
  public String collect(String account, double amount){
    try{
      ResultSet rs = database.execute_query("SELECT * FROM Account WHERE a_id= "+LoadDB.parse(account)+" AND type= 'Pocket'");
      while(rs.next()){
        String l_id = rs.getString("linked_id");
        ResultSet rs_to = database.execute_query("UPDATE Account SET balance = balance+"+(0.97*amount)+" WHERE a_id= "+LoadDB.parse(l_id));
        ResultSet rs_from = database.execute_query("UPDATE Account SET balance = balance-"+amount+" WHERE a_id= "+LoadDB.parse(account));
        //$5 transaction fee- check log
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Move money from the specifed pocket account to another pocket account.
   * If it is the first transaction of the month with either account, apply a $5 fee to the relevant account
   */
  public String pay_friend(String from_account, String to_account, double amount){
      try{
        ResultSet rs = database.execute_query("SELECT * FROM Account A1, Account A2 WHERE A1.a_id = "+LoadDB.parse(from_account)+" AND A1.type= 'Pocket' AND "+"A2.a_id= "+LoadDB.parse(to_account)+" AND A2.type= 'Pocket'");
        while(rs.next()) {
          String pay = transfer_helper(from_account, to_account, amount);
        }
      }catch(SQLException e){
        e.printStackTrace();
      }
    return null;
  }

  /**
   * Subtract the specified dollar amount from the given pocket account
   * If it is the first transaction of the month with this account, apply a $5 fee
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   * @return an error message if applicable. null otherwise
   */
   public String purchase(String account, double amount){
     try{
       ResultSet rs = database.execute_query("UPDATE Account SET balance= balance-"+amount+" WHERE a_id= "+LoadDB.parse(account)+" AND type= 'Pocket'");
       if(rs.next()) {
       }
       //if(balance <= 0.01) then close;
     }catch(SQLException e){
       e.printStackTrace();
     }
     return null;
   }


  public void log_transaction(double amount, String type, String check_no, String paying_id, String receiving_id){

    String t_id = "'" + generateRandomChars(10) + "'";
    type = "'" +  type + "'";
    String date = "TO_DATE('" + currentDate + "', 'YYYY-MM-DD HH24:MI:SS')";
    check_no = LoadDB.parseNULL(check_no);
    paying_id = LoadDB.parseNULL(paying_id);
    receiving_id = LoadDB.parseNULL(receiving_id);

    String query = "insert into transaction(t_id, amount, type, timestamp, check_no, paying_id, receiving_id) values("+
      t_id+", " + amount+", " + type+", " + date+", " + check_no+", " + paying_id+", " + receiving_id + ")";
    database.execute_query(query);
  }

  /**
   * Updates the date and adds interest if the date is past the end of the month
   */
  public String getDate(){
    try{
      ResultSet rs = database.execute_query("select timestamp from CurrentDate");
      if(rs.next()){
        String date = rs.getString("timestamp");
        System.out.println(date);
        return date;
      }
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  public static String generateRandomChars(int length) {
    String candidateChars  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
        sb.append(candidateChars.charAt(random.nextInt(candidateChars
                .length())));
    }

    return sb.toString();
  }

  public void closeAccountIfLowBalance(String a_id){
    database.execute_query("update account set isClosed = 1 where balance < 0.01 and a_id="+LoadDB.parse(a_id));
  }

  public void applyFeeIfFirstTransaction(String a_id){
    database.execute_query("update account A set A.balance = A.balance - 5 where A.a_id= "+LoadDB.parse(a_id)+ " and 1 > (select count(T.a_id) from transaction T where T.a_id = A.a_id and extract(month from T.timestamp) = (select MAX(extract(month from C.timestamp)) from currentdate C))");
  }

}
