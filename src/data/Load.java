package data;

import java.util.ArrayList;
import java.util.List;
import logic.Priority;
import logic.Priority.Prioritys;
import logic.TODO;

public class Load {
	public static int doneCount;
	public static int undoneCount;
	private static String[] doneTODOS;
	private static String[] undoneTODOS;
	
	public static List<TODO> doneTODOSList=new ArrayList<TODO>();
	public static List<TODO> undoneTODOSList=new ArrayList<TODO>();
	
	public static Object[][] FillArray(int done) {
		List<TODO> list;
		Object[][] data;
		int count;
		if(done==1) {
			doneTODOS=Connect.getArray(done);
			doneCount=Connect.getCount(done);
			data =new Object[doneCount][6];
			for (int i = 0; i < Load.doneCount; i++) {
				data[i][5]=false;
			}
			count=doneCount;
			list=doneTODOSList;
		}
		else {
			undoneTODOS=Connect.getArray(done);
			undoneCount=Connect.getCount(done);
			data =new Object[undoneCount][5];
			count=undoneCount;
			list=undoneTODOSList;
		}
		LoadDataInArray(done);
		for (int i = 0; i < count; i++) {
			data[i][0]=list.get(i).title;
			data[i][1]=list.get(i).description;
			data[i][2]=list.get(i).priority;
			Prioritys priority=Enum.valueOf(Prioritys.class, data[i][2].toString());
			switch (priority) {
			case Priority1:
				data[i][2]="1 "+(Priority.getPriority1());
				break;
			case Priority2:
				data[i][2]="2 "+(Priority.getPriority2());
				break;
			case Priority3:
				data[i][2]="3 "+(Priority.getPriority3());
				break;
			case Priority4:
				data[i][2]="4 "+(Priority.getPriority4());
				break;
			}
			data[i][3]=list.get(i).date;
			if(done==0) {
				data[i][4]=list.get(i).done;
			}
			else {
				data[i][4]=list.get(i).doneDate;;
			}
			
		}
		
     	return data;
	}

	private static void LoadDataInArray(int done) {
		int count;
		if(done==1) {
			count=doneCount;
		}
		else {
			count=undoneCount;
		}
		for (int i = 0; i < count; i++)
		{
			TODO todo=new TODO();
			if(done==1) {
				todo=TODO.Load(todo,doneTODOS[i], done);
				doneTODOSList.add(todo);
			}
			else {
				todo=TODO.Load(todo,undoneTODOS[i], done);
				undoneTODOSList.add(todo);
			}
			
		}
		
	}
	public static Object[]oneRow(int i, int done){
		Object[] data;
		List<TODO> list;
		int count;
		if(done==1) {
			list=doneTODOSList;
			data=new Object[6];
			data[4]=list.get(i).doneDate;
			count=doneCount;
		}
		else {
			list=undoneTODOSList;
			data=new Object[5];
			data[4]=list.get(i).done;
			count=undoneCount;
		}
		if(count!=0) {
			data[0]=list.get(i).title;
			data[1]=list.get(i).description;
			data[2]=list.get(i).priority;
			Prioritys priority=Enum.valueOf(Prioritys.class, data[2].toString());
			switch (priority) {
			case Priority1:
				data[2]="1 "+(Priority.getPriority1());
				break;
			case Priority2:
				data[2]="2 "+(Priority.getPriority2());
				break;
			case Priority3:
				data[2]="3 "+(Priority.getPriority3());
				break;
			case Priority4:
				data[2]="4 "+(Priority.getPriority4());
				break;
			}
			data[3]=list.get(i).date;
		}
		
		return data;
	}
}
