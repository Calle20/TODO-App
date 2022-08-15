package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logic.Priority;
import logic.Priority.Prioritys;
import logic.TODO;

public class NewEditDialog {
	JDialog d;
	JTextField textFieldTitle = new JTextField(32);
	JTextField textFieldDescription = new JTextField(32);
	JTextField text = new JTextField(15);
	JButton b = new JButton("Datum wählen");
	JPanel p = new JPanel();
	
	JLabel labelDescription=new JLabel("Beschreibung:");
	JLabel labelPriority=new JLabel("Priorität:");
	JLabel labelDate=new JLabel("Datum:");
	JLabel labelTitle=new JLabel("Titel:");
	
	JButton button = new JButton("Speichern");
	JPanel jprb=new JPanel();
	JPanel jprb1=new JPanel();
	JPanel jpDate=new JPanel();
	ButtonGroup bgrb=new ButtonGroup();
	JRadioButton rbPriority1=new JRadioButton();
	JRadioButton rbPriority2=new JRadioButton();
	JRadioButton rbPriority3=new JRadioButton();
	JRadioButton rbPriority4=new JRadioButton();
	
	public static boolean worked=false;
	public static boolean addSomething=false;
	public static boolean close;
	
	public NewEditDialog(JFrame parent, String title) {
		d = new JDialog();
		d.setModal(true);
		d.setLayout(new FlowLayout());
		d.setTitle(title);
		d.setSize(400,300);
		d.setResizable(false);
		
		rbPriority1.setText(Priority.getPriority1());
		rbPriority2.setText(Priority.getPriority2());
		rbPriority3.setText(Priority.getPriority3());
		rbPriority4.setText(Priority.getPriority4());
		
		button.addActionListener(new ActionListener() {
			@Override
		public void actionPerformed(ActionEvent event) {
				if(textFieldTitle.getText().equals("")) {
					JOptionPane.showMessageDialog(d,"Bitte einen Titel eingeben!");
				}
				else if(!(rbPriority1.isSelected()||rbPriority2.isSelected()||rbPriority3.isSelected()||(rbPriority4.isSelected()))){
					JOptionPane.showMessageDialog(d,"Bitte eine Priorität auswählen!");
				}
				else if(DatePicker.checkParse(text.getText())==false) {
					JOptionPane.showMessageDialog(d,"Bitte ein Datum auswählen!");
				}
				else {
					Prioritys priority=null;
					if(rbPriority1.isSelected()) {
						priority=Prioritys.Priority1;
					}
					if(rbPriority2.isSelected()) {
						priority=Prioritys.Priority2;
					}
					if(rbPriority3.isSelected()) {
						priority=Prioritys.Priority3;
					}
					if(rbPriority4.isSelected()) {
						priority=Prioritys.Priority4;
					}
					TODO.NewTODO(textFieldTitle.getText(), textFieldDescription.getText(), priority, text.getText(),d);
					if(close) {
						d.dispose();
						addSomething=true;
					}
					
				}
			}
		});
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				text.setText(new DatePicker(parent).setPickedDate());
			}
		});
		
		text.setEditable(false);
		text.setHorizontalAlignment(JTextField.CENTER);
		textFieldTitle.setHorizontalAlignment(JTextField.CENTER);
		textFieldDescription.setHorizontalAlignment(JTextField.CENTER);
		button.setBounds(400, 20, 400, 20);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		jprb.add(rbPriority1);
		jprb.add(rbPriority2);
		jprb1.add(rbPriority3);
		jprb1.add(rbPriority4);
		p.add(labelDate);
		p.add(text);
		p.add(b);
		bgrb.add(rbPriority1);
		bgrb.add(rbPriority2);
		bgrb.add(rbPriority3);
		bgrb.add(rbPriority4);
		d.add(labelTitle);
		d.add(textFieldTitle);
		d.add(labelDescription);
		d.add(textFieldDescription);
		d.add(labelPriority);
		d.add(jprb);
		d.add(jprb1);
		d.add(p);
		d.add(button);
		d.add(jpDate);
		d.setLocationRelativeTo(parent);
		d.setVisible(true);
	}
}
