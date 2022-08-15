package logic;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import data.Connect;
import data.Load;
import logic.Priority.Prioritys;
import ui.Gui;
import ui.NewEditDialog;
import ui.Notification;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import ui.DatePicker;

public class TODO {
	public String title, description,priorityString;
	public Prioritys priority;
	public String date, doneDate;
	public boolean done;
	
	public static void Check(int index) {
		TODO todo=Load.undoneTODOSList.get(index);;
		Load.undoneCount--;
		Load.doneCount++;
		todo.done=true;
		todo.doneDate=DatePicker.now();
		Connect.writeDoneDate(todo);
		Load.undoneTODOSList.remove(index);
		Load.doneTODOSList.add(todo);
		
	}
	public static void Delete(int index) {
		TODO todo=Load.doneTODOSList.get(index);;
		Load.doneCount--;
		Load.doneTODOSList.remove(index);
		Connect.deleteData(todo.title);
	}
	public static void NewTODO(String title, String description, Prioritys priority, String date,JDialog d) {
		TODO todo=new TODO();
		todo.title=title;
		todo.description=description;
		todo.priority=priority;
		switch(todo.priority) {
		case Priority1:
			todo.priorityString=Priority.getPriority1();
			break;
		case Priority2:
			todo.priorityString=Priority.getPriority2();
			break;
		case Priority3:
			todo.priorityString=Priority.getPriority3();
			break;
		case Priority4:
			todo.priorityString=Priority.getPriority4();
			break;
		}
		todo.date=date;
		todo.done=false;
		Connect.writeData(todo,d);
		if(NewEditDialog.close) {
			Load.undoneTODOSList.add(TODO.Load(todo,title, 0));
		}
		NewEditDialog.worked=true;
	}
	
	public static TODO Load(TODO todo, String title, int done) {
		todo=Connect.getData(todo,title);
		Instant now = ZonedDateTime.now().toInstant();
		Instant todoDate = DatePicker.stringToDate(todo.date).toInstant();
		if(now.isBefore(todoDate.plusSeconds(68400))) {
			//try {
				//Notification.dateIsOver(todo);
			//} 
			//catch (AWTException e) {
				
			//}
		}
		return todo;
	}
}
	
