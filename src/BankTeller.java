import java.sql.*;
import java.util.*;

public class BankTeller{
  private DatabaseConnection database;
  private ATM atm;
  private String currentDate;
  public BankTeller(){
    this.database = new DatabaseConnection();
    this.atm = new ATM();
    this.currentDate = getDate();
  }

  /**
   * Writes a check for the given checking account
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   * @return the check number for the check written, or null if there was an error
   */
  public String write_check(String account, double amount){
    //verify that account is checking
    try{
      ResultSet rs = database.execute_query("select type,balance from account where account.a_id = " + LoadDB.parse(account));
      if(rs.next() && rs.getString("type").matches(".*Checking")){
        double balance = rs.getDouble("balance");
        String error = atm.withdraw(account, balance);
        if(error == null){
          String checkid = generateRandomChars(20);
          atm.log_transaction(amount, "write-check", checkid, account, "");
          return checkid;
        }
      }
    }
    catch(SQLException e){

    }

    return null;
  }


//TODO
  /**
   * Generate a monthly statement for all accounts owned by the given customer ID
   * @param taxID a customer id
   * @return a string array representing the monthly statement
   */
  public String [] generateMonthlyStatement(String taxID){ //NOTE: might need to change the return type to better suit the monthly statement

    ArrayList<String> statements = new ArrayList<String>();
    taxID = LoadDB.parse(taxID);


    ResultSet rs = database.execute_query("select a_id,balance from account where PrimaryOwner = "+taxID);
    String [] accounts = parseResultSetString(rs, "a_id");
    double [] balances = parseResultSetDouble(rs, "balance");
    double totalBalance=0.0;
    for(int i=0;i<accounts.length;i++){
      String a_id = accounts[i];
      double balance = balances[i];
      totalBalance+=balance;
      String statement = "Account ID: " + a_id + "\n";

      ResultSet owns = database.execute_query("select C.name, C.address from Customer C, Owns O where O.a_id = " + LoadDB.parse(a_id) + " and C.taxID= O.taxID");
      ResultSet pos_transactions = database.execute_query("select type, timestamp, amount from transaction where receiving_id="+LoadDB.parse(a_id));
      ResultSet neg_transactions = database.execute_query("select type, timestamp, amount from transaction where paying_id="+LoadDB.parse(a_id));

      statement+=getOwnerList(owns);

      statement+= "Initial Balance: $" + calculateInitialBalance(pos_transactions, neg_transactions, balance) + "\n";

      statement+="Positive Transactions:\n";
      statement+=getTransactionList(pos_transactions);
      statement+="Negative Transactions:\n";
      statement+=getTransactionList(neg_transactions);

      statement+="Final Balance: $" + balance + "\n";


      statement+="\n";
      statements.add(statement);
    }
    if(totalBalance > 100000.0){
      statements.add("WARNING: Total balance exceeds $100,000. Insurance limit reached.");
    }
    if(statements.size() == 0){
      statements.add("No accounts found.");
    }

    String [] s = new String [statements.size()];
    statements.toArray(s);
    return s;
  }
  private String getOwnerList(ResultSet owners){
    String list = "Owners:\n";
    String [] names = parseResultSetString(owners, "name");
    String [] address = parseResultSetString(owners, "address");
    for(int i=0;i<names.length;i++){
      list += names[i] + " : " + address[i] + "\n";
    }
    list+="\n";
    return list;
  }
  private String getTransactionList(ResultSet transactions){
    String list = "";
    String [] types = parseResultSetString(transactions, "type");
    String [] dates = parseResultSetString(transactions, "timestamp");
    double [] amounts = parseResultSetDouble(transactions, "amount");
    for(int i=0;i<types.length;i++){
      list += dates[i] + "\t$" + amounts[i]+ "\t" + types[i] + "\n";
    }
    list+="\n";
    return list;
  }
  private double calculateInitialBalance(ResultSet pos_transactions, ResultSet neg_transactions, double inital){
    double [] pos = parseResultSetDouble(pos_transactions, "amount");
    for(double p : pos){
      inital -= p;
    }
    double [] neg = parseResultSetDouble(neg_transactions, "amount");
    for(double n : neg){
      inital += n;
    }
    return inital;
  }


  /**
   * List all accounts that have closed in the last month
   * @return an array of account ids
   */
  public String [] listClosedAccounts(){
    String query = "select a_id from account where isClosed=1 and "+
      "exists(select a_id from transaction where (paying_id=a_id or receiving_id=a_id) and"+
        " extract(month from timestamp) = (select MAX(extract(month from timestamp)) from currentdate))";
    ResultSet rs = database.execute_query(query);

    return parseResultSetString(rs, "a_id");
  }

  /**
   * List all customers that have deposited over $10,000 in the past month
   * @return an array of account ids
   */
  public String [] generateDTER(){
    return null;
  }

  /**
   * List all accounts associated with a customer, and whether they are open or closed
   * @return an array of account ids
   */
  public String [] generateCustomerReport(String taxId){
    return null;
  }

  /**
   * Add monthly interest to all open accounts
   */
  public void addInterest(){

  }

  /**
   * Create a new customer
   * @return an error message if applicable. null otherwise
   */
  public String createCustomer(String taxID, String name, String address, String pin){
    return null;
  }

  /** TODO
   * List all accounts associated with a customer, and whether they are open or closed
   * @param primary_owner the id of the primary owner
   * @param owner_ids the list of all customer ids that own this account
   * @param initial_balance the initial balance of the account
   * @param type the account type
   * @param branch the bank branch
   * @param linked_id the linked account id, if applicable. null otherwise
   * @return the new account id
   */
  public String createAccount(String primary_owner, String [] owner_ids, double initial_balance, String type, String branch, String linked_id){
    //create new entry in account
    //create new entry for the initial transaction
    //create new entries for owns
    return null;
  }

  /**
   * Delete closed accounts and customers who own no accounts
   */
  public void deleteClosed(){
      //delete closed accounts
      String delete_accounts = "delete from account where isClosed = 1";
      // String delete_owns = "delete from owns where exists(select * from account where owns.a_id = account.a_id)";
      database.execute_query(delete_accounts);

      //delete customers who own no accounts
      String delete_customers = "delete from customer where not exists (select * from owns where customer.taxID = customer.taxID)";
      database.execute_query(delete_customers);
  }

  /**
   * Delete all transactions
   */
  public void deleteTransactions(){
    database.execute_query("delete from transaction");
  }

  /**
   * Update the interest rate for the given account type
   * @return an error message if applicable. null otherwise
   */
  public String updateInterest(String type, double rate){
    return null;
  }

  /**
   * Updates the date and adds interest if the date is past the end of the month
   */
  public void setDate(String date){//TODO: figure out how the hell this is gonna work
    //update date
    //update interest if end of month
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

  public String[] parseResultSetString(ResultSet rs, String key){
    try{
      ArrayList al = new ArrayList();
      while(rs.next()) {
        String id = rs.getString(key);
        al.add(id.trim());
      }
      rs.beforeFirst();
      String[] a = new String[al.size()];
      al.toArray(a);
      return a;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }
  public double[] parseResultSetDouble(ResultSet rs, String key){
    try{
      ArrayList<Double> al = new ArrayList<Double>();
      while(rs.next()) {
        Double id = rs.getDouble(key);
        al.add(id);
      }
      rs.beforeFirst();
      double[] a = new double[al.size()];
      for (int i = 0; i < a.length; i++) {
         a[i] = al.get(i);
       }
      return a;
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }

}
