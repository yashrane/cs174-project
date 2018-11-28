public class ATM{

  private:
    String current_user;


  public ATM(){

  }

  /**
   * Logs a user in given their pin number
   * @param name the user's name
   * @param pin the user's pin number
   * @return whether the pin is valid for the given user
   */
  public boolean login(String name, String pin){
    return true;
  }

  /**
   * Gets the list of accounts associated with the current user
   * @return an array of string account ids
   */
  public String[] get_accounts(){
    return [];
  }

  /**
   * Deposits the specified dollar amount in the given account
   * @param account the account id to depost into
   * @param amount the money to deposit in dollars
   */
  public void deposit(String account, double amount){
    return;
  }

  /**
   * Withdraws the specified dollar amount from the given account
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   */
  public void withdraw(String account, double amount){
    return;
  }


  /**
   * Helper function for all tranactions that transfer money from one account to the other
   */
  private void transfer_helper(String from_account, String to_account, double amount){

  }

  /**
   * Wires money from one account to the other. 2% fee
   * @param from_account the account to take money from
   * @param to_account the account to send money to
   * @param amount the money to wire in dollars
   */
  public void wire(String from_account, String to_account, double amount){

  }

  /**
   * Wires money from one account to the other. accounts must have at least one
   * owner in common. amount cannot exceed $2000
   * @param from_account the account to take money from
   * @param to_account the account to send money to
   * @param amount the money to wire in dollars
   */
  public void transfer(String from_account, String to_account, double amount){

  }

  /**
   * Writes a check for the given checking account
   * @param account the account id to withdraw from
   * @param amount the money to withdraw in dollars
   * @return the check number for the check written
   */
  public String write_check(String account, double amount){
    //verify that account is checking
    //use withdraw as helper function
    //generate check number
    return "";
  }


  //NOTE: functions for top-up, collect, pay-friend have not been written



}
