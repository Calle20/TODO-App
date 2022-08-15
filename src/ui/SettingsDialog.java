package ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Connect;
import logic.Priority;

public class SettingsDialog {
	JDialog d;
	JButton buttonColor = new JButton("Farbe für abgelaufene Daten auswählen");
	JButton buttonSavePrioritys = new JButton("Eingegebene Prioritäten speichern");
	JTextField tFPriority1=new JTextField(15);
	JTextField tFPriority2=new JTextField(15);
	JTextField tFPriority3=new JTextField(15);
	JTextField tFPriority4=new JTextField(15);
	
	JLabel lblPriority1=new JLabel("Priorität 1:");
	JLabel lblPriority2=new JLabel("Priorität 2:");
	JLabel lblPriority3=new JLabel("Priorität 3:");
	JLabel lblPriority4=new JLabel("Priorität 4:");
	
	JButton btnColPrio1=new JButton("Farbe auswählen");
	JButton btnColPrio2=new JButton("Farbe auswählen");
	JButton btnColPrio3=new JButton("Farbe auswählen");
	JButton btnColPrio4=new JButton("Farbe auswählen");
	
	JPanel jp1=new JPanel();
	JPanel jp2=new JPanel();
	JPanel jp3=new JPanel();
	JPanel jp4=new JPanel();

	public SettingsDialog(JFrame parent) {
		d = new JDialog();
		d.setModal(true);
		d.setLayout(new FlowLayout());
		d.setTitle("Einstellungen");
		d.setSize(425,275);
		d.setResizable(false);
		tFPriority1.setHorizontalAlignment(JTextField.CENTER);
		tFPriority2.setHorizontalAlignment(JTextField.CENTER);
		tFPriority3.setHorizontalAlignment(JTextField.CENTER);
		tFPriority4.setHorizontalAlignment(JTextField.CENTER);
		
		Connect.getPrioritys();
		
		tFPriority1.setText(Priority.getPriority1());
		tFPriority2.setText(Priority.getPriority2());
		tFPriority3.setText(Priority.getPriority3());
		tFPriority4.setText(Priority.getPriority4());
		
		btnColPrio1.setBackground(Connect.getPriorityColor(0));
		btnColPrio2.setBackground(Connect.getPriorityColor(1));
		btnColPrio3.setBackground(Connect.getPriorityColor(2));
		btnColPrio4.setBackground(Connect.getPriorityColor(3));
		
		buttonColor.setBackground(Connect.getColor());
		buttonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col=JColorChooser.showDialog(parent, "Farbe auswählen", Connect.getColor());
				if(col!=null) {
					Connect.setColor(col.getRGB());
					buttonColor.setBackground(Connect.getColor());
				}
				
			}
			
		});
		btnColPrio1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col=JColorChooser.showDialog(parent, "Farbe auswählen", Connect.getPriorityColor(0));
				if(col!=null) {
					Connect.setPriorityColor(col.getRGB(), 1);
					btnColPrio1.setBackground(Connect.getPriorityColor(0));
				}
			}
			
		});
		btnColPrio2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col=JColorChooser.showDialog(parent, "Farbe auswählen", Connect.getPriorityColor(1));
				if(col!=null) {
					Connect.setPriorityColor(col.getRGB(), 2);
					btnColPrio2.setBackground(Connect.getPriorityColor(1));
				}
				
			}
			
		});
		btnColPrio3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col=JColorChooser.showDialog(parent, "Farbe auswählen", Connect.getPriorityColor(2));
				if(col!=null) {
					Connect.setPriorityColor(col.getRGB(), 3);
					btnColPrio3.setBackground(Connect.getPriorityColor(2));
				}
				
			}
			
		});
		btnColPrio4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color col=JColorChooser.showDialog(parent, "Farbe auswählen", Connect.getPriorityColor(3));
				if(col!=null) {
					Connect.setPriorityColor(col.getRGB(), 4);
					btnColPrio4.setBackground(Connect.getPriorityColor(3));
				}
				
			}
			
		});

		buttonSavePrioritys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Priority.setPriority1(tFPriority1.getText());
				Priority.setPriority2(tFPriority2.getText());
				Priority.setPriority3(tFPriority3.getText());
				Priority.setPriority4(tFPriority4.getText());
				Connect.setPrioritys(Priority.getPriority1(), Priority.getPriority2(), Priority.getPriority3(), Priority.getPriority4());
			}
			
		});
		
		jp1.add(lblPriority1);
		jp1.add(tFPriority1);
		jp1.add(btnColPrio1);
		jp2.add(lblPriority2);
		jp2.add(tFPriority2);
		jp2.add(btnColPrio2);
		jp3.add(lblPriority3);
		jp3.add(tFPriority3);
		jp3.add(btnColPrio3);
		jp4.add(lblPriority4);
		jp4.add(tFPriority4);
		jp4.add(btnColPrio4);
		
		
		d.add(jp1);
		d.add(jp2);
		d.add(jp3);
		d.add(jp4);
		d.add(buttonSavePrioritys);
		d.add(buttonColor);
		
		d.setLocationRelativeTo(parent);
		d.setVisible(true);
	}
}
