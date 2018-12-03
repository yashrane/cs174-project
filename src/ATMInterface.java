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

      JLabel header = new JLabel("Welcome to the National Bank of Debt!");

      cards = new JPanel(new CardLayout());

      JPanel login = getLoginPanel();
      // JPanel menu = getMenuPanel();
      String [] menuButtons = {"Deposit", "Withdraw", "Transfer"};
      JPanel menu = InterfaceHelper.getMenuPanel(menuButtons, new FormAction(){
        public void onSubmit(String [] inputs){
          changeScreen(inputs[0]);
        }
      });
      JPanel deposit = getDepositPanel();

      cards.add(login, "Login");
      cards.add(menu, "Menu");
      cards.add(deposit, "Deposit");



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
