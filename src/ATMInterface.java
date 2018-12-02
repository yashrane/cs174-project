//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

interface FormAction{
  public void onSubmit(String [] inputs);
}

class ATMInterface {



    JPanel cards;

    public ATMInterface(){
      //Creating the Frame
      JFrame frame = new JFrame("Bank of Debts");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(400, 400);

      // Text Area at the Center
      JTextArea ta = new JTextArea();

      JLabel header = new JLabel("Welcome to the National Bank of Debt!");

      cards = new JPanel(new CardLayout());

      JPanel login = getLoginPanel();
      JPanel menu = getMenuPanel();
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

    private void showError(String message){
      JFrame frame = new JFrame("ERROR");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setSize(200, 100);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      JLabel error = new JLabel(message);
      error.setAlignmentX(Component.CENTER_ALIGNMENT);
      error.setAlignmentY(Component.CENTER_ALIGNMENT);
      JButton close = new JButton("OK");
      close.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
          frame.dispose();
        }
      });

      panel.add(Box.createVerticalGlue());
      panel.add(error);
      panel.add(Box.createRigidArea(new Dimension(0,5)));
      panel.add(close);
      panel.add(Box.createVerticalGlue());

      frame.getContentPane().add(panel);
      frame.setVisible(true);
    }

    private JPanel getDepositPanel(){
      return makeFormPanel(new String []{"Account ID", "Amount to Deposit"}, new FormAction(){
        public void onSubmit(String [] inputs){
          for(String input: inputs){
            System.out.print(input + " ");
          }
          System.out.println();
          changeScreen("Menu");
        }
      });
    }

    private JPanel getLoginPanel(){
      return makeFormPanel(new String []{"Name", "PIN"}, new FormAction(){
        public void onSubmit(String [] inputs){
          for(String input: inputs){
            System.out.print(input + " ");
          }
          System.out.println();

          changeScreen("Menu");
          // showError("DATABASES SUCKS ");
        }
      });
    }


    private JPanel getMenuPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      JButton deposit = makeCenteredButton("Deposit");
      JButton withdraw = makeCenteredButton("Withdraw");
      JButton transfer = makeCenteredButton("Transfer");

      panel.add(Box.createVerticalGlue());
      panel.add(deposit);
      panel.add(Box.createRigidArea(new Dimension(0,5)));
      panel.add(withdraw);
      panel.add(Box.createRigidArea(new Dimension(0,5)));
      panel.add(transfer);
      panel.add(Box.createVerticalGlue());
      return panel;
    }

    private JButton makeCenteredButton(String name){
      JButton button = new JButton(name);
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setAlignmentY(Component.CENTER_ALIGNMENT);
      button.setPreferredSize(new Dimension(400,30));
      button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              changeScreen(name);
            }
        });
      return button;
    }




    public JPanel makeFormPanel(String [] forms, FormAction form){
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      JPanel submitpanel = new JPanel();
      JButton submit = new JButton("Submit");

      JTextField [] fields = new JTextField[forms.length];

      submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              String [] inputs = new String [fields.length];
              for(int i =0;i<fields.length;i++){
                inputs[i] = fields[i].getText();
              }
              form.onSubmit(inputs);
            }
        });


      panel.add(Box.createVerticalGlue());

      for(int i=0;i<forms.length;i++){
        String f = forms[i];
        JPanel temppanel = new JPanel();
        JTextField tempfield = new JTextField(16);
        temppanel.add(new JLabel(f));
        temppanel.add(tempfield);
        panel.add(temppanel);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        fields[i] = tempfield;
      }

      submitpanel.add(submit);
      panel.add(submitpanel);

      panel.add(Box.createVerticalGlue());

      return panel;
    }

}
