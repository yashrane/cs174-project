//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



class ATMInterface {



    JPanel cards;
    private ATM atm;

    public ATMInterface(){
      this.atm = new ATM();

      //Creating the Frame
      JFrame frame = new JFrame("Bank of Debts");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(400, 400);

      // Text Area at the Center
      JTextArea ta = new JTextArea();

      JPanel header = new JPanel(new CardLayout());
      header.add(new JLabel("Welcome to the National Bank of Debt!"), "Welcome");


      cards = new JPanel(new CardLayout());

      JPanel login = getLoginPanel();
      String [] menuButtons = {"Deposit","Top-up","Withdraw","Purchase", "Transfer", "Collect", "Wire", "Pay-friend"};
      JPanel menu = InterfaceHelper.getMenuPanel(menuButtons, new FormAction(){
        public void onSubmit(String [] inputs){
          changeScreen(inputs[0]);
        }
      });
      JPanel deposit = getDepositPanel();
      JPanel top_up = getTopUpPanel();
      JPanel withdraw = getWithdrawPanel();
      JPanel purchase = getPurchasePanel();
      JPanel transfer = getTransferPanel();
      JPanel collect = getCollectPanel();
      JPanel wire = getWirePanel();
      JPanel pay_friend = getPayFriendPanel();

      cards.add(login, "Login");
      cards.add(menu, "Menu");
      cards.add(deposit, "Deposit");
      cards.add(top_up, "Top-up");
      cards.add(withdraw, "Withdraw");
      cards.add(purchase, "Purchase");
      cards.add(transfer, "Transfer");
      cards.add(collect, "Collect");
      cards.add(wire, "Wire");
      cards.add(pay_friend, "Pay-friend");



      //Adding Components to the frame.
      frame.getContentPane().add(BorderLayout.NORTH, header);
      frame.getContentPane().add(BorderLayout.CENTER, cards);
      frame.setVisible(true);
    }




    public static void main(String args[]) {
      new ATMInterface();
    }

    //Method came from the ItemListener class implementation,
    //contains functionality to process the combo box item selecting
    public void changeScreen(String label) {
        CardLayout cl = (CardLayout)(this.cards.getLayout());
        cl.show(this.cards, label);
    }



    private JPanel getDepositPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Account ID", "Amount to Deposit"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            double amount = Double.parseDouble(inputs[1]);
            String error = atm.deposit(id, amount);
            if(error == null){
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

    private JPanel getTopUpPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Account ID", "Amount to Top-up"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            double amount = Double.parseDouble(inputs[1]);
            String error = atm.top_up(id, amount);
            if(error == null){
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

    private JPanel getWithdrawPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Account ID", "Amount to Withdraw"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            double amount = Double.parseDouble(inputs[1]);
            String error = atm.withdraw(id, amount);
            if(error == null){
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

    private JPanel getPurchasePanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Account ID", "Amount to Purchase"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            double amount = Double.parseDouble(inputs[1]);
            String error = atm.purchase(id, amount);
            if(error == null){
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

    private JPanel getTransferPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"From Account", "To Account", "Amount to Transfer"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String from_id = inputs[0];
            String to_id = inputs[1];
            double amount = Double.parseDouble(inputs[2]);
            String error = atm.transfer(from_id, to_id, amount);
            if(error == null){
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

    private JPanel getCollectPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Account ID", "Amount to Collect"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String id = inputs[0];
            double amount = Double.parseDouble(inputs[1]);
            String error = atm.collect(id, amount);
            if(error == null){
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

    private JPanel getWirePanel(){
      return InterfaceHelper.makeFormPanel(new String []{"From Account", "To Account", "Amount to Wire"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String from_id = inputs[0];
            String to_id = inputs[1];
            double amount = Double.parseDouble(inputs[2]);
            String error = atm.wire(from_id, to_id, amount);
            if(error == null){
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

    private JPanel getPayFriendPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"From Account", "To Account", "Amount to Pay"}, new FormAction(){
        public void onSubmit(String [] inputs){
          try{
            String from_id = inputs[0];
            String to_id = inputs[1];
            double amount = Double.parseDouble(inputs[2]);
            String error = atm.pay_friend(from_id, to_id, amount);
            if(error == null){
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


    private JPanel getLoginPanel(){
      return InterfaceHelper.makeFormPanel(new String []{"Name", "PIN"}, new FormAction(){
        public void onSubmit(String [] inputs){
          String name = inputs[0];
          String pin = inputs[1];

          if(atm.login(name, pin)){
              changeScreen("Menu");
          }
          else{
              InterfaceHelper.showError("Name or PIN invalid");
          }
        }
      });
    }

}
