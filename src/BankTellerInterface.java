//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;



class BankTellerInterface {



    JPanel cards;
    private BankTeller teller;

    public BankTellerInterface(){
      this.teller = new BankTeller();

      //Creating the Frame
      JFrame frame = new JFrame("Bank of Debts");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(400, 400);

      // Text Area at the Center
      JTextArea ta = new JTextArea();

      JPanel header = new JPanel(new CardLayout());
      header.add(new JLabel("Bank Teller"), "Welcome");


      cards = new JPanel(new CardLayout());


      String [] menuButtons = {"Write Check","Monthly Statement","List Closed Accounts",
        "DTER", "Customer Report", "Add Interest", "Create New Account", "Create New Customer",
        "Delete Closed Accounts and Customers", "Delete Transactions", "Update Interest Rates", "Set Date"};
      JPanel menu = InterfaceHelper.getMenuPanel(menuButtons, new FormAction(){
        public void onSubmit(String [] inputs){
          changeScreen(inputs[0]);
        }
      });

      JPanel check = getCheckPanel();
      JPanel statement = getMonthlyStatementPanel();
      JPanel close_accounts = getClosedAccountsPanel();
      JPanel dter = getDTERPanel();
      JPanel report = getReportPanel();
      JPanel interest = getInterestPanel();
      JPanel new_account = getNewAccountPanel();
      JPanel new_customer = getNewCustomerPanel();
      JPanel delete_account = getDeleteAccountsPanel();
      JPanel delete_transactions = getDeleteTransactionsPanel();
      JPanel update_interest = getUpdateInterestPanel();
      JPanel date = getDatePanel();

      cards.add(menu, "Menu");
      cards.add(check, "Write Check");
      cards.add(statement, "Monthly Statement");
      cards.add(close_accounts, "List Closed Accounts");
      cards.add(dter, "DTER");
      cards.add(report, "Customer Report");
      cards.add(interest, "Add Interest");
      cards.add(new_account, "Create New Account");
      cards.add(new_customer, "Create New Customer");
      cards.add(delete_account, "Delete Closed Accounts and Customers");
      cards.add(delete_transactions, "Delete Transactions");
      cards.add(update_interest, "Update Interest Rates");
      cards.add(date, "Set Date");


      //Adding Components to the frame.
      frame.getContentPane().add(BorderLayout.NORTH, header);
      frame.getContentPane().add(BorderLayout.CENTER, cards);
      frame.setVisible(true);
    }




    public static void main(String args[]) {
      new BankTellerInterface();
    }


    public void changeScreen(String label) {
        CardLayout cl = (CardLayout)(this.cards.getLayout());
        cl.show(this.cards, label);
    }

    private JPanel getCheckPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Account ID", "Amount to Subtract"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            double amount = Double.parseDouble(inputs[1]);
            String error = teller.write_check(id, amount);
            if(error != null){
              changeScreen("Menu");
            }
            else{
              InterfaceHelper.showError("Something went wrong");
            }
          }
          catch(java.lang.NumberFormatException e){
            InterfaceHelper.showError("Invalid input");
          }
        }
      });
    }

    private JPanel getMonthlyStatementPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Customer ID"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            String [] statements = teller.generateMonthlyStatement(id);
            if(statements != null){
              InterfaceHelper.displayList("Monthly Statement",statements);
            }
            changeScreen("Menu");
          }
          catch(java.lang.NumberFormatException e){
            InterfaceHelper.showError("Invalid input");
          }
        }
      });
    }

    private JPanel getClosedAccountsPanel(){
      return InterfaceHelper.makeButtonPanel("List Closed Accounts", new FormAction(){
        public void onSubmit(String [] inputs){
          String [] accounts = teller.listClosedAccounts();
          if(accounts != null){
            InterfaceHelper.displayList("List Closed Accounts", accounts);
          }
          changeScreen("Menu");
        }
      });
    }

    private JPanel getDTERPanel(){
      return InterfaceHelper.makeButtonPanel("Generate DTER", new FormAction(){
        public void onSubmit(String [] inputs){
          String [] accounts = teller.listClosedAccounts();
          if(accounts != null){
            InterfaceHelper.displayList("Generate DTER", accounts);
          }
          changeScreen("Menu");
        }
      });
    }

    private JPanel getReportPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Customer ID"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            String [] report = teller.generateCustomerReport(id);
            if(report != null){
              InterfaceHelper.displayList("Customer Report",report);
            }
            changeScreen("Menu");
          }
          catch(java.lang.NumberFormatException e){
            InterfaceHelper.showError("Invalid input");
          }
        }
      });
    }

    private JPanel getInterestPanel(){
      return InterfaceHelper.makeButtonPanel("Add Interest", new FormAction(){
        public void onSubmit(String [] inputs){
          teller.addInterest();
          changeScreen("Menu");
        }
      });
    }

    private JPanel getNewAccountPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Primary Owner", "Owners (Seperated by Commas)", "Initial Balance", "Type","Bank Branch" ,"Linked ID (If Applicable)"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String primary_owner = inputs[0];
            String [] owners = inputs[1].split("\\s*,\\s*");
            double amount = Double.parseDouble(inputs[2]);
            String type = inputs[3];
            String branch = inputs[4];
            String linked_id = inputs[5];

            String account_id = teller.createAccount(primary_owner, owners, amount, type,branch, linked_id);

            if(account_id != null){
              InterfaceHelper.displayList("Account ID", new String[]{"New Account ID",account_id});
              changeScreen("Menu");
            }
            else{
              InterfaceHelper.showError("A new account could not be created");
            }
          }
          catch(java.lang.NumberFormatException e){
            InterfaceHelper.showError("Invalid input");
          }
        }
      });
    }

    private JPanel getNewCustomerPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"ID", "Name", "Address", "PIN"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            String name = inputs[1];
            String address = inputs[2];
            String pin = inputs[3];

            String error = teller.createCustomer(id, name, address,pin);

            if(error == null){
              InterfaceHelper.displayList("Account ID", new String[]{"New Customer Created"});
              changeScreen("Menu");
            }
            else{
              InterfaceHelper.showError(error);
            }
          }
          catch(java.lang.NumberFormatException e){
            InterfaceHelper.showError("Invalid input");
          }
        }
      });
    }

    private JPanel getDeleteAccountsPanel(){
      return InterfaceHelper.makeButtonPanel("Close Accounts", new FormAction(){
        public void onSubmit(String [] inputs){
          teller.deleteClosed();
          changeScreen("Menu");
        }
      });
    }

    private JPanel getDeleteTransactionsPanel(){
      return InterfaceHelper.makeButtonPanel("Delete Transactions", new FormAction(){
        public void onSubmit(String [] inputs){
          teller.deleteTransactions();
          changeScreen("Menu");
        }
      });
    }

    private JPanel getUpdateInterestPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Type", "New Interest Rate"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String type = inputs[0];
            double rate = Double.parseDouble(inputs[1]);

            String error = teller.updateInterest(type, rate);

            if(error == null){
              InterfaceHelper.displayList("Interest Rates", new String[]{"Interest Rates Updated!"});
              changeScreen("Menu");
            }
            else{
              InterfaceHelper.showError(error);
            }
          }
          catch(java.lang.NumberFormatException e){
            InterfaceHelper.showError("Invalid input");
          }
        }
      });
    }

    private JPanel getDatePanel(){
      return InterfaceHelper.makeFormPanel(new String []{"New Date (YYYY-MM-DD)"}, new FormAction(){
        public void onSubmit(String [] inputs){
            String date = inputs[0];

            if (isValidDate(date)){
              teller.setDate(date);
              InterfaceHelper.displayList("Date", new String[]{"The date is now: " + date});
              changeScreen("Menu");
            }
            else{
              InterfaceHelper.showError("Not a Valid Date");
            }
        }
      });
    }

    private boolean isValidDate(String inDate) {
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       dateFormat.setLenient(false);
       try {
           dateFormat.parse(inDate.trim());
       } catch (ParseException pe) {
           return false;
       }
       return true;
   }


}
