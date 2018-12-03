import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InterfaceHelper{
  public InterfaceHelper(){}

  public static JPanel makeFormPanel(String [] forms, FormAction form){
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

  public static void showError(String message){
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

  public static JPanel getMenuPanel(String [] buttons, FormAction action){
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(Box.createVerticalGlue());
    for(String button: buttons){
      panel.add(makeCenteredButton(button, action));
      panel.add(Box.createRigidArea(new Dimension(0,5)));
    }
    panel.add(Box.createVerticalGlue());
    return panel;
  }

  private static JButton makeCenteredButton(String name, FormAction action){
    JButton button = new JButton(name);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setAlignmentY(Component.CENTER_ALIGNMENT);
    button.setPreferredSize(new Dimension(400,30));
    button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            action.onSubmit(new String[] {name});
          }
      });
    return button;
  }
}
