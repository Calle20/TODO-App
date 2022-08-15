package ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import data.Connect;
import data.Load;
import logic.Priority;
import logic.Priority.Prioritys;
import logic.TODO;

public class Gui{
	
	//Elemente der GUI
	JFrame f=new JFrame();
	
	public static boolean listen=true;
	
	JButton buttonEdit=new JButton("TODO hinzufügen");
	JButton buttonSettings=new JButton("Einstellungen");
	JLabel labelTODOS=new JLabel(">>>>>>>>>> Aktuell zu erledigende Aufgaben: <<<<<<<<<<");
	JToggleButton jtb=new JToggleButton("Erledigte Aufgaben");
	//Tabellendaten
	String[] columns = {"Titel", "Beschreibung", "Priorität", "Datum","Erledigt"};
    //data for JTable in a 2D table
    Object[][] data = Load.FillArray(0);
	public DefaultTableModel model = new DefaultTableModel(data, columns) {
		@Override
		public boolean isCellEditable(int row, int column) {
			if(column==4) {
				return true;
			}
			else {
				return false;
			}
			
		}
		
	};
	String[] columns2 = {"Titel", "Beschreibung", "Priorität", "Datum","Erledigt am", "Löschen?"};
    Object[][] data2 = Load.FillArray(1);
	public DefaultTableModel model2 = new DefaultTableModel(data2, columns2) {
		@Override
		public boolean isCellEditable(int row, int column) {
			if(column==5) {
				return true;
			}
			else {
				return false;
			}
			
		}
		
	};
    JTable table = new JTable(model) {
      public Class getColumnClass(int column) {
    	  return getValueAt(0, column).getClass(); 
      }
      public Component prepareRenderer(
    	        TableCellRenderer renderer, int row, int column)
    	    {
    	  		Component c = super.prepareRenderer(renderer, row, column);
    	  		c.setBackground(Color.white);
    	  		if(column==3) {
    	  			Instant now = ZonedDateTime.now().toInstant();
    	  			Instant todoDate = DatePicker.stringToDate(table.getValueAt(row, 3).toString()).toInstant();
    	  			boolean isOver = now.isAfter(todoDate);
    	  			if(isOver)
    	  			{
    	  				c.setBackground(Connect.getColor());
    	  			}
    	  		}
    	  		if(column==2) {
	    	        DefaultTableModel model = (DefaultTableModel) table.getModel();
	    	        String content= table.getValueAt(row, 2).toString();
	    			if(content.startsWith("1")) {
	    				c.setBackground(Connect.getPriorityColor(0));
	    			}
	    			if(content.startsWith("2")) {
	    				c.setBackground(Connect.getPriorityColor(1));
	    			}
	    			if(content.startsWith("3")) {
	    				c.setBackground(Connect.getPriorityColor(2));
	    			}
	    			if(content.startsWith("4")) {
	    				c.setBackground(Connect.getPriorityColor(3));
	    			}
    	  		}
    	  		return c;
    	    }
    };
    JScrollPane scrollPane = new JScrollPane(table);
    
	//GUI aufbauen
	public Gui() {
		f.setLayout(new FlowLayout());
		
		buttonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new NewEditDialog(f,"TODO hinzuf�gen");
				if (NewEditDialog.addSomething==true) {
					AddRow();
					NewEditDialog.addSomething=false;
				}
				
			}
		});
		
		ImageIcon icon = new ImageIcon(getClass().getResource("logo.png"));
		f.setIconImage(icon.getImage());
		
		jtb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jtb.isSelected()) {
					changeToDoneTasks();
					buttonEdit.setEnabled(false);
					jtb.setText("Offene Aufgaben");
				}
				else {
					changeToUndoneTasks();
					buttonEdit.setEnabled(true);
					jtb.setText("Erledigte Aufgaben");
				}
			}
			
		});
		
		buttonSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new SettingsDialog(f);
				for (int i = 0; i < table.getRowCount(); i++) {
					for (int j = 0; j < table.getColumnCount(); j++) {
						model.fireTableDataChanged();
					}
				}
			}
		});
		f.addWindowListener(new WindowAdapter() { 
		    public void windowClosing(WindowEvent e) { 
		        Connect.closeDatabase();
		        System.exit(0); 
		    } 
		});
		f.setTitle("TODO-App");
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed( false );
		
		table.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION);
		table.getModel().addTableModelListener(
				new TableModelListener() 
				{
				    public void tableChanged(TableModelEvent evt) 
				    {
				    	if(listen)
				    	{
				    		if(table.getSelectedRow()!=-1) {
				    			int row = evt.getFirstRow();
				    	        DefaultTableModel model = (DefaultTableModel)evt.getSource();
				    				
				    	        if(model==model2) {
				    				Boolean checked= (Boolean)model.getValueAt(row, 5);
				    				if(checked) {
				    					TODO.Delete(row);
				    					model.removeRow(row);
				    				}
				    			}
				    			else {
				    				Boolean checked= (Boolean)model.getValueAt(row, 4);
				    				if(checked) {
				    					TODO.Check(row);
				    					refreshRows();
				    				}
				    			}
				    		}
				    		
				    	}
				    	
				    }
				});

		f.add(labelTODOS);
		f.add(jtb);
		f.add(scrollPane);
		f.add(buttonEdit);
		f.add(buttonSettings);
		
		f.setSize(475, 575);		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);	
		f.setResizable(false);
		f.setVisible(true);
	}
	void changeToUndoneTasks() {
	    data = Load.FillArray(0);
		model = new DefaultTableModel(data, columns);
		listen=false;
		table.setModel(model);
		table.getModel().addTableModelListener(
				new TableModelListener() 
				{
				    public void tableChanged(TableModelEvent evt) 
				    {
				    	if(listen)
				    	{
				    		if(table.getSelectedRow()!=-1) {
				    			int row = evt.getFirstRow();
				    	        DefaultTableModel model = (DefaultTableModel)evt.getSource();
				    	        Boolean checked= (Boolean)model.getValueAt(row, 4);
				    			if(checked) {
				    				if(model==model2) {
				    					TODO.Delete(row);
				    					model.removeRow(row);
				    				}
				    				else {
				    					TODO.Check(row);
				    					refreshRows();
				    				}
				    			}
				    		}
				    		
				    	}
				    	
				    }
				});
		listen=true;
	}
	public void changeToDoneTasks() {
		listen=false;
		table.setModel(model2);
		table.getModel().addTableModelListener(
				new TableModelListener() 
				{
				    public void tableChanged(TableModelEvent evt) 
				    {
				    	if(listen)
				    	{
				    		if(table.getSelectedRow()!=-1) {
				    			int row = evt.getFirstRow();
				    	        DefaultTableModel model = (DefaultTableModel)evt.getSource();
				    	        if(model==model2) {
				    				Boolean checked= (Boolean)model.getValueAt(row, 5);
				    				if(checked) {
				    					TODO.Delete(row);
				    					model.removeRow(row);
				    				}
				    			}
				    			else {
				    				Boolean checked= (Boolean)model.getValueAt(row, 4);
				    				if(checked) {
				    					TODO.Check(row);
				    					refreshRows();
				    				}
				    			}
				    		}
				    	}
				    }
				});
		listen=true;
	}
	public void AddRow() {
		listen=false;
		Load.undoneCount++;
		model.addRow(Load.oneRow(Load.undoneTODOSList.size()-1, 0));
		listen=true;
	}
	public void refreshRows() {
		listen=false;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		if (model.getRowCount() > 0) {
			for (int i = model.getRowCount() - 1; i > -1; i--) {
				model.removeRow(i);
			}
		}
		for (int i = 0; i < Load.undoneCount; i++) {
			model.addRow(Load.oneRow(i,0));
		}
		model2.addRow(Load.oneRow(Load.doneTODOSList.size()-1, 1));
		listen=true;
	}
}