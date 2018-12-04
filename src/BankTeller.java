public class BankTeller{
  private DatabaseConnection database;
  public BankTeller(){
    this.database = new DatabaseConnection();
  }

  /**
   * Writes a check for the given checking account
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   * @return the check number for the check written, or null if there was an error
   */
  public String write_check(String account, double amount){
    //verify that account is checking
    //use withdraw as helper function
    //generate check number
    return null;
  }

  /**
   * Generate a monthly statement for all accounts owned by the given customer ID
   * @param taxID a customer id
   * @return a string array representing the monthly statement
   */
  public String [] generateMonthlyStatement(String taxID){ //NOTE: might need to change the return type to better suit the monthly statement
    return null;
  }

  /**
   * List all accounts that have closed in the last month
   * @return an array of account ids
   */
  public String [] listClosedAccounts(){
    return null;
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

  /**
   * List all accounts associated with a customer, and whether they are open or closed
   * @param primary_owner the id of the primary owner
   * @param owner_ids the list of all customer ids that own this account
   * @param initial_balance the initial balance of the account
   * @param type the account type
   * @param linked_id the linked account id, if applicable. null otherwise
   * @return the new account id
   */
  public String createAccount(String primary_owner, String [] owner_ids, double initial_balance, String type, String linked_id){
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
    //delete customers who own no accounts
  }

  /**
   * Delete all transactions
   */
  public void deleteTransactions(){

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

}
