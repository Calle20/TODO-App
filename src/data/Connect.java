package data;

import java.awt.Color;
import java.awt.image.ColorModel;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import logic.Priority;
import logic.Priority.Prioritys;
import logic.TODO;
import ui.NewEditDialog;

public class Connect {
	public static Connection c = null;
      
	public static Statement stmt = null;
	
	public static String sql;
  
      
	public static void connect() {
		
		Path path = Paths.get("Database");
		boolean exist=Files.exists(path);
		if(!exist) {
			new File("Database").mkdir();
		}
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Database/TODOS.db");
			System.out.println("Opened database successfully");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			sql = "CREATE TABLE todos(" +
                     " Title TEXT PRIMARY KEY," +
                     " Description TEXT, " + 
                     " Priority INT NOT NULL, " + 
                     " Date TEXT NOT NULL, " + 
                     " Done INT NOT NULL, " + 
                     " DoneDate TEXT)"; 
			try {
				stmt.executeUpdate(sql);
				
				System.out.println("Table created successfully");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			sql = "CREATE TABLE Settings(" +
	                     "Id INTEGER PRIMARY KEY AUTOINCREMENT, Color INT, Prioritys TEXT, PriorityColor INT)";
			try {
				stmt.executeUpdate(sql);
				sql = "INSERT INTO Settings (Prioritys)" +
	                     "VALUES ('Unwichtig')";
				stmt.executeUpdate(sql);
				sql = "INSERT INTO Settings (Prioritys)"  +
	                     "VALUES ('Wichtig')";
				stmt.executeUpdate(sql);
				sql = "INSERT INTO Settings (Prioritys)" +
	                     "VALUES ('Sehr wichtig')";
				stmt.executeUpdate(sql);
				sql = "INSERT INTO Settings (Prioritys)" +
	                     "VALUES ('Super wichtig')";
				stmt.executeUpdate(sql);
				sql = "INSERT INTO Settings (Color)" +
	                     "VALUES ("+Color.red.getRGB()+")";
				stmt.executeUpdate(sql);
				sql = "UPDATE Settings set PriorityColor = '"+Color.green.getRGB()+"' WHERE Id=1;";
				stmt.executeUpdate(sql);
				sql = "UPDATE Settings set PriorityColor = '"+Color.cyan.getRGB()+"' WHERE Id=2;";
				stmt.executeUpdate(sql);
				sql = "UPDATE Settings set PriorityColor = '"+Color.yellow.getRGB()+"' WHERE Id=3;";
				stmt.executeUpdate(sql);
				sql = "UPDATE Settings set PriorityColor = '"+Color.orange.getRGB()+"' WHERE Id=4;";
				stmt.executeUpdate(sql);
				stmt.close();
				c.commit();
				
				System.out.println("Table Settings created successfully");
			}
			catch(Exception e){
				e.printStackTrace();
			}
				
      	} 
      	catch ( Exception e ) {
      		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      		System.exit(0);
      	}
      	
	}
	public static void writeData(TODO todo, JDialog d) {
		int priority=0;
		switch(todo.priority) {
		case Priority1:
			priority=1;
			break;
		case Priority2:
			priority=2;
			
			break;
		case Priority3:
			priority=3;
			
			break;
		case Priority4:
			priority=4;
			
			break;
		}
		
		int done=-1;
		
		if(todo.done) {
			done=1;
		}
		else if(!todo.done) {
			done=0;
		}
		
		sql = "INSERT INTO todos (Title,Description,Priority,Date,Done,DoneDate) " +
			  "VALUES ('"+todo.title+"', '"+todo.description+"', "+priority+", '"+todo.date+"', "+done+", null );"; 
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			NewEditDialog.worked=true;
			NewEditDialog.close=true;
		} 
		catch (SQLException e) {
			JOptionPane.showMessageDialog(d,"Der Titel ist bereits vorhanden. Bitte einen anderen wählen!");
			NewEditDialog.close=false;
		}
		
	}
	public static void writeDoneDate(TODO todo) {
		sql = "UPDATE todos set Done=1 WHERE Title='"+todo.title+"';";
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			sql="UPDATE todos set DoneDate='"+todo.doneDate+"' WHERE Title='"+todo.title+"';";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static TODO getData(TODO todo, String title) {
		
		try {
			ResultSet rs = c.createStatement().executeQuery( "SELECT * FROM todos WHERE Title='"+title+"';" );
			while(rs.next()) {
				todo.title = rs.getString("Title");
				todo.description = rs.getString("Description");
			
				int priority  = rs.getInt("Priority");
				switch(priority) {
				case 1:
					todo.priority=Prioritys.Priority1;
					todo.priorityString=Priority.getPriority1();
					break;
				case 2:
					todo.priority=Prioritys.Priority2;
					todo.priorityString=Priority.getPriority2();
					break;
				case 3:
					todo.priority=Prioritys.Priority3;
					todo.priorityString=Priority.getPriority3();
					break;
				case 4:
					todo.priority=Prioritys.Priority4;
					todo.priorityString=Priority.getPriority4();
					break;
				}
				
				todo.date = rs.getString("Date");
				
				int done = rs.getInt("Done");
				if(done==1) {
					todo.done=true;
				}
				else if(done==0) {
					todo.done=false;
				}
				todo.doneDate=rs.getString("DoneDate");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return todo;
	}
	public static int getCount(int done) {
		int count=0;
		sql = "SELECT COUNT(*) FROM todos WHERE Done="+done+"";
		
		try {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count=rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	public static void deleteData(String title) {
		try {
			stmt = c.createStatement();
			String sql = "DELETE from todos WHERE Title='"+title+"';";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
	}
	public static void closeDatabase() {
		try {
			stmt.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static String[] getArray(int done) {
		List<String> strl=new ArrayList();
		int count;
		if(done==1) {
			count=Load.doneCount;
		}
		else {
			count=Load.undoneCount;
		}
		String[] str=new String[count];
		ResultSet rs;
		try {
			rs = stmt.executeQuery( "SELECT Title FROM todos WHERE Done="+done+";" );
			while ( rs.next() ) {
				strl.add(rs.getString("Title"));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str= strl.toArray(new String[0]);
		return str;
	}
	public static Color getColor() {
		ResultSet rs;
		Color col=Color.red;
		try {
			rs = stmt.executeQuery( "SELECT Color FROM Settings;" );
			while ( rs.next() ) {
				int rgb=rs.getInt("Color");
				System.out.print(rgb);
				col=new Color(rgb);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return col;
	}
	public static void setColor(int rgb) {
		try {
			stmt = c.createStatement();
			sql = "UPDATE Settings set Color = "+rgb+";";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void setPrioritys(String Priority1,String Priority2,String Priority3,String Priority4) {
		 try {
			 stmt = c.createStatement();
			 sql = "UPDATE Settings set Prioritys = '"+Priority1+"' WHERE Id=1;";
			 stmt.executeUpdate(sql);
			 sql = "UPDATE Settings set Prioritys = '"+Priority2+"' WHERE Id=2;";
			 stmt.executeUpdate(sql);
			 sql = "UPDATE Settings set Prioritys = '"+Priority3+"' WHERE Id=3;";
			 stmt.executeUpdate(sql);
			 sql = "UPDATE Settings set Prioritys = '"+Priority4+"' WHERE Id=4;";
			 stmt.executeUpdate(sql);
			 c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void getPrioritys() {
		ResultSet rs;
		List<String> priList=new ArrayList();
		List<Integer> priListCol=new ArrayList();
		try {
			stmt = c.createStatement();
			rs = stmt.executeQuery( "SELECT Prioritys FROM Settings;" );
			while ( rs.next() ) {
				priList.add(rs.getString("Prioritys"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Priority.setPriority1(priList.get(0));
		Priority.setPriority2(priList.get(1));
		Priority.setPriority3(priList.get(2));
		Priority.setPriority4(priList.get(3));
	}
	public static Color getPriorityColor(int priority) {
		ResultSet rs;
		Color col=Color.red;
		List <Color> colList=new ArrayList();
		try {
			rs = stmt.executeQuery( "SELECT PriorityColor FROM Settings;" );
			while ( rs.next() ) {
				int rgb=rs.getInt("PriorityColor");
				colList.add(new Color(rgb));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		col=colList.get(priority);
		return col;
	}
	public static void setPriorityColor(int rgb, int priority) {
		try {
			stmt = c.createStatement();
			sql = "UPDATE Settings set PriorityColor = '"+rgb+"' WHERE Id="+priority+";";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
