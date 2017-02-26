import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Schedule 
{
	ArrayList<ArrayList<Class>> slots;
	int total_days=5;
	int day_hours=9*2;//need to be changed
	boolean isSetup;
	int total_rooms;
	int mutation_size;
	
	public static int show=0;
	int jumah=4;
	int breakTime=2;
	ArrayList<String> freeDaysForGroup=new ArrayList<String>();
	int freeDaysForGroupCount=0;


	HashMap<Class,Integer> classes_map;
	private ArrayList<Class> class_list=new ArrayList<Class>();
	private ArrayList<SlotInfo> slotsOccupied = new ArrayList<>();
	
	
	
	
	Schedule(int total_rooms)
	{
		int arraysize=this.day_hours*this.total_days*total_rooms;
		this.slots=new ArrayList<ArrayList<Class>>(arraysize);
		
		for(int i=0;i<arraysize;i++)
		{
			this.slots.add(i, new ArrayList());
		}
		
		
		
		ensureSize(this.slots, arraysize);
		
		
		
		//System.out.println("slotSize: "+slots.size());
		
		this.classes_map=new HashMap<Class,Integer>();
		//System.out.println(slots.size());
		this.isSetup=false;
		this.total_rooms=total_rooms;
		this.mutation_size=2;//please put this in argument should be changed
		
	}
	
	
	
	void ensureSize(ArrayList<?> list, int size) {

	    list.ensureCapacity(size);
	    while (list.size() < size) {
	        list.add(null);
	    }
	}
	
	void justCheckForFun() {
		int day_size = this.day_hours * this.total_rooms;
		for (Class key : this.classes_map.keySet()) {
			int val = this.classes_map.get(key);// value of hash against that
			// key
			int day = val / day_size;// get that day for eg 3rd day etc
			int time = val % day_size;// gives the time
			int room = time / this.day_hours;// get the room
			time = time % this.day_hours;

			int duration = key.duration;

			key.display();
			System.out.println("info");
			System.out.println("day: " + day);
			System.out.println("time: " + time);
			System.out.println("room: " + room);

		}
	}

	Schedule makeChromosome(ArrayList<Class> class_list,int total_rooms)
	{
		//now making new chromosome which will be array
		this.class_list=class_list;
		Random rand = new Random();
		//System.out.println(total_rooms);
		Schedule newChromosome=new Schedule(total_rooms);
		
		
		//System.out.println(class_list.size());
		
		
		
		
		for(int i=0;i<class_list.size();i++)
		{
			int nr=this.total_rooms;
			int day=rand.nextInt(this.total_days);
			int dur=class_list.get(i).duration;
			int room=rand.nextInt(nr);
			
			int time=rand.nextInt(this.day_hours+1-dur);

			
//			//for delay at jumah time
//			if(day==this.jumah && (time==this.breakTime ||(time==this.breakTime-1 &&dur==2)))
//			{
//				time=0;
//				//continue;
//			}
			int pos=0;
			pos=(day*nr*this.day_hours)+(room*this.day_hours)+time;
	
			
			//System.out.println(pos);
		
			//now filling the time space slots			
			for(int j=dur-1;j>=0;j--)
			{
				newChromosome.slots.get(pos+j).add(class_list.get(i));
			}
			
			//System.out.println("pos after"+pos);
			newChromosome.classes_map.put(class_list.get(i), pos);
			
		}
		//System.out.println(newChromosome.slots);
		//System.out.println(newChromosome.classes_map);

		newChromosome.calculateFitness();
		return newChromosome;
		
	}
	
	float calculateFitness()
	{
		
		double score = 0; //this is chromosome score
		int day_size=this.day_hours*this.total_rooms;
		ArrayList<String> countfreeDaysForGroup=new ArrayList<String>(); 
		
		//for free day for some batch
		ArrayList <StudentBatch> groupList=ReadExcel.getBatchList();
		for(StudentBatch sb:groupList)
		{
			ArrayList<Integer> TotalDays=new ArrayList<Integer>(); 
			int dayfreeforbatch=0;
			for(Class key: this.classes_map.keySet())
			{
				
				int val=this.classes_map.get(key);//value of hash against that key
				int day=val/day_size;//get that day for eg 3rd day etc
				int time=val%day_size;//gives the time
				int room=time/this.day_hours;//get the room
				time=time%this.day_hours;

					if(StudentBatch.findIndex(key.student_batch_list,sb)>0)
					{
						//System.out.println(TotalDays.size()+" "+this.total_days);
						if(TotalDays.indexOf(day)<0)
						{
							dayfreeforbatch=day;
							TotalDays.add(new Integer(day));
						}
					}
			} 
			
			if(TotalDays.size()!=this.total_days && TotalDays.size()!=0)
			{
				//System.out.println(TotalDays.size()+" "+this.total_days);	
				score=score+(this.total_days-TotalDays.size());
				countfreeDaysForGroup.add(sb.batch_code+" is free For days count:  "+(this.total_days-TotalDays.size()));
			}

		}

		this.freeDaysForGroup=countfreeDaysForGroup;
		ArrayList<String> TeacherConsectiveClasses=new ArrayList<String>();//Teacher giving consective lecture
		
		int constraint1=0;
		int constraint2=0;
		int constraint3=0;
		int constraint4=0;		
		//5 is done in other func
		int constraint5=0;		
		int constraint6=0;		
		//7 is don upward in same func


		for(Class key: this.classes_map.keySet())
		{
			int val=this.classes_map.get(key);//value of hash against that key
			int day=val/day_size;//get that day for eg 3rd day etc
			int time=val%day_size;//gives the time
			int room=time/this.day_hours;//get the room
			time=time%this.day_hours;
			Boolean c2set=false;
			Boolean c3set=false;
			Boolean c1set=false;
			Boolean c5set=false;
			Boolean c6set=false;
			Boolean c7set=false;

			
//			if(c1set)
//			{
//				constraint1--;
//				c1set=false;
//			}
//			if(c2set)
//			{
//				constraint2--;
//				c2set=false;
//			}
//			if(c3set)
//			{
//				constraint3--;
//				c3set=false;
//			}

//			if(c5set)
//			{
//				constraint5--;
//				c5set=false;
//			}

			int duration=key.duration;
			
			String teachrc=key.teacher.teacherID+"D:"+day+time;

			int pref=ReadExcel.getTeacherPrefernce(key.teacher.teacherID);
			//if teacher preference not met
			if(pref==0&&time>this.day_hours/2
					||pref==1&&time<this.day_hours/2 )
			{
				constraint6++;
			}
			//For Jumah Break
			//for delay at jumah time
			if(day==this.jumah && time==this.breakTime)
			{
				constraint4++;
			}

			
			
			for(Class key2: this.classes_map.keySet())
			{
				if(key2.equals(key))
				{
					continue;
				}
				int val2=this.classes_map.get(key2);//value of hash against that key
				int day2=val2/day_size;//get that day for eg 3rd day etc
				int time2=val2%day_size;//gives the time
				int room2=time2/this.day_hours;//get the room
				time2=time2%this.day_hours;
				
				//if Same teacher is teaching 2 classes same time
				if(key2.teacher.teacherID.equals(key.teacher.teacherID)&&
						day2==day&&
						time==time2
						)
				{
					c1set=true;
				}
				//Classroom not shared
				if(room2==room&&time==time2&&day==day2)
				{
					c3set=true;

				}
				//Same Class twice batch taking
				if(day==day2&&key.duration==1&&key2.duration==1&&
					key.student_batch_list.get(0).batch_code.equals(key2.student_batch_list.get(0).batch_code)&&
					key.teacher.teacherID.equals(key2.teacher.teacherID)&&
					key.course.course_id.equals(key2.course.course_id)&&
					(time!=time2-1||time!=time2+1)
						)
				{
					c2set=true;
				}
				//*****************Teacher Consective  classes*****
				if((teachrc.equals(key2.teacher.teacherID+"D:"+day2+(time2+1))||
						teachrc.equals(key2.teacher.teacherID+"D:"+day2+(time2-1)))
						&& !key.course.course_id.equals(key2.course.course_id)
						 )
				{
					c5set=true;
				}						
				
			}
			if(c2set)
			{
				c2set=false;
				constraint2++;
			}
			if(c3set)
			{
				c3set=false;
				constraint3++;
			}
			if(c1set)
			{
				c1set=false;
				constraint1++;
			}
			if(c5set)
			{
				c5set=false;
				constraint5++;
			}

		
		}
		
		//for count of free days for batch
		score=score/((this.total_days-3)*(groupList.size()*1.0));
		//System.out.println("s: "+score);
		constraint2=constraint2/2;
		constraint3=constraint3/2;
		
		
		int sum1=(classes_map.size()*3)-(constraint1+constraint3+constraint2);
		int sum2=(classes_map.size()*3)-(constraint6+constraint5+(int)score);

		float sum=(float)((sum1/(this.classes_map.size() *3.0))*.60);
		if(constraint4>this.day_hours)
		{
			constraint4=this.day_hours;
		}
		sum=sum+(float)((float)((float)(this.day_hours-constraint4)/this.day_hours)*.25);
		sum=sum+(float)((sum2/(this.classes_map.size() *3.0))*0.15);

//		int sum=(classes_map.size()*5)-(constraint1+constraint3+constraint5+constraint2+constraint6);
		if(Schedule.show==0)
		{
			this.show=500;
			System.out.println("c: "+constraint1+" c2: "+constraint2+" c3: "+constraint3+" c4: "+constraint4+" c5:"
			+constraint5+" c6:");//this.classes_map.get(clsA)
			System.out.println(sum);

		}
		this.show--;

		return (float) (sum);
	}
	
	//if at the given slot there are clashes of group/section 
	//then the function return true
	private boolean getGroupClashes(int index) {
		
		for(Class i:this.class_list)
		{
			
		}
		return false;
	}



	void check()
	{
		//System.out.println(this.classes_map);
	}
	
	
	
	//does mutation on chromosomes
	void Mutation()
	{
		
		//save number of classes
		int number_of_classes=this.classes_map.size();
		
		//save slot size
		int size_slot=this.slots.size();
		
		Random rand = new Random();
		
		//now we have to move some classes at random position based on mutation size
		
		for(int i=1;i>0;i--)
		{
			//we have to select random chromosome to move
			
			int mpos=rand.nextInt(number_of_classes);
			//System.out.println(mpos);
			int pos1=0;
			
			//System.out.println(this.classes_map.keySet());
			List<Class> list = this.classes_map.keySet().stream().collect(Collectors.toList());
			
			Class c1=list.get(mpos);
			//System.out.println(c1);
			try
			{
				pos1=this.classes_map.get(c1);
			}
			catch(Exception e)
			{
				System.out.println("Exception in Mutation");
				try {
					System.in.read();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
			//System.out.println("pos"+pos1);
			int nr=this.total_rooms;
			int duration=c1.duration;
			int day=rand.nextInt(this.total_days);
			int room=rand.nextInt(nr);
			
			int pref=2;
			try
			{
				pref=ReadExcel.getTeacherPrefernce(class_list.get(i).teacher.teacherID);
			}
			catch(Exception e)
			{
				//System.out.println("Exception in Mutation : pref=ReadExcel.getTeacherPrefernce(class_list.get(i).teacher.teacherID);");
//				try {
//					System.in.read();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
			}
			int time=rand.nextInt(this.day_hours+1-duration);

//			if(pref==0)
//			{
//				if(time>this.day_hours/2)
//				{
//					time=time-((this.day_hours+1-duration)/2);
//				}
//			}
//			else if(pref==1)
//			{
//				if(time<this.day_hours/2)
//				{
//					time=time+((this.day_hours+1-duration)/2);
//
//				}
//
//			}
			
//			if(day==this.jumah && (time==this.breakTime ||(time==this.breakTime-1 &&duration==2)))
//			{
//				time=0;
//			}
//
			int pos2=day*nr*this.day_hours+room*this.day_hours+time;
			
			
			
			//System.out.println(c1);
			//System.out.println(this.slots);
			for(int j=duration-1;j>=0;j--)
			{
				//System.out.println("j "+j);
				ArrayList<Class> cl=this.slots.get(pos1+j);
				//System.out.println("cl:"+cl);
				
				for(int m=0;m<cl.size();m++)
				{
					if(cl.get(m)==c1)
					{
						//System.out.println(cl.get(m));
						//System.out.println(this.slots.get(pos1+j));
						this.slots.get(pos1+j).remove(m);
						//System.out.println("now: "+this.slots.get(pos1+j));
					}
				}
				
				//move to new slot
				this.slots.get(pos2+j).add(c1);
			}
			this.classes_map.put(c1, pos2);
		}
		
		this.calculateFitness();
	}
	
	
	//optimize this make new functions 
	Schedule cross_over(Schedule parent2)
	{
		
		//System.out.println(this.classes_map);
		//new chromosome
		
		Schedule n=new Schedule(this.total_rooms);
		
		
		//HashMap classMap1=this.classes_map;
		//HashMap classMap2=parent2.classes_map;
		
		
		//System.out.println(this.slots);
		
	/*	for(Class key: this.classes_map.keySet())
		{
			System.out.print(key+" : ");
			System.out.println(this.classes_map.get(key));
		}
		
		System.out.println("");
		for(Class key: parent2.classes_map.keySet())
		{
			System.out.print(key+" : ");
			System.out.println(parent2.classes_map.get(key));
		}
*/
		//make new node by combing 2 parents
		
		//try using iterator that is more efficient and good this is just for quickness
		for(Class key: this.classes_map.keySet())
		{
			
			if(Math.random()<=0.5)
			{
				//copy from 1st parent
				n.classes_map.put(key, this.classes_map.get(key));
				
				//copy time space slot from parent 1 to this
				//this.slots
				int duration=key.duration;
				int val=this.classes_map.get(key);
				for(int j=duration-1;j>=0;j--)
				{
					/*if(n.slots.get(val)==null)
					{
						n.slots.add(val,new ArrayList<Class>() );
						n.slots.get(val).add(key);
						
					}
					else
					{
						n.slots.get(val).add(key);
					}*/
					n.slots.get(val+j).add(key);
					
				}
				
			}
			else
			{
				//copy from second parent
				//copy hash
				n.classes_map.put(key, parent2.classes_map.get(key));
				
				
				//now copy time space slots
				
				int duration=key.duration;
				if(parent2.classes_map.get(key)!=null)
				{
					int val=parent2.classes_map.get(key);
					
					for(int j=duration-1;j>=0;j--)
					{
						/*if(n.slots.get(val)==null)
						{
							n.slots.add(val,new ArrayList<Class>() );
							n.slots.get(val).add(key);
							
						}
						else
						{
							n.slots.get(val).add(key);
						}*/
						n.slots.get(val+j).add(key);
						
					}					
				}
			}
			
		}//end for
		
		System.out.println("Crossover and now new ");
		//System.out.println("size: "+n.classes_map.size());
//		for(Class key: n.classes_map.keySet())
//		{
//			System.out.print(key+" : ");
//			System.out.println(n.classes_map.get(key));
//		}
		
		n.calculateFitness();
		System.out.println("fitness now : "+n.calculateFitness());
		
		return n;
	
	}
	

	void RemoveFromTimeTableClash() {
		
		for(Class clsA: this.classes_map.keySet())
		{
			int valA=this.classes_map.get(clsA);

			for(Class clsB: this.classes_map.keySet())
			{
				if(valA==this.classes_map.get(clsB))
				{
					classes_map.remove(clsA);
				}
			}

		}
			
			
		}
	void WriteToExcel() throws IOException {

		
		int day_size = this.day_hours * this.total_rooms;

		System.err.println(this.day_hours + " | " + this.total_rooms + " | " + this.total_days);

		for (Class key : this.classes_map.keySet()) {
			int val = this.classes_map.get(key);// value of hash against that
			
			// key
			int day = val / day_size;// get that day for eg 3rd day etc
			int time = val % day_size;// gives the time
			int room = time / this.day_hours;// get the room
			
			time = time % this.day_hours;

			int duration = key.duration;

			//			key.display();
			//			System.out.println("info");
			//			System.out.println("day: " + day);
			//			System.out.println("time: " + time);
			//			System.out.println("room: " + room);

		}

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		CellStyle style = workbook.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    Font font = workbook.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            style.setFont(font);

         // Set up fonts
            XSSFFont blueFont = workbook.createFont();
            blueFont.setColor(HSSFColor.BLUE.index);

            XSSFFont greenFont = workbook.createFont();
            greenFont.setColor(HSSFColor.GREEN.index);
            
		for (int l = 0; l < this.total_days; l++) {

			XSSFSheet spreadsheet = null;

			if(l == 0){
				// Create a blank sheet
				spreadsheet = workbook.createSheet("Monday");
				
			}else if(l == 1){
				// Create a blank sheet
				spreadsheet = workbook.createSheet("Tuesday");
			}else if(l == 2){
				// Create a blank sheet
				spreadsheet = workbook.createSheet("Wednesday");
			}else if(l == 3){
				// Create a blank sheet
				spreadsheet = workbook.createSheet("Thursday");
			}else if(l == 4){
				// Create a blank sheet
				spreadsheet = workbook.createSheet("Friday");
			}

			// Create row object
			XSSFRow row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new TreeMap<String, Object[]>();

			ArrayList<Object> slots_heading = new ArrayList<Object>();
			slots_heading.add("Venue"); 

			for (int i = 1; i <= this.day_hours; i++) {
				slots_heading.add(Integer.toString(i));
			}

			//add headings
			empinfo.put("0", slots_heading.toArray());		

			for (int i = 0; i < this.total_rooms; i++) {

				ArrayList<Object> slots = new ArrayList<Object>();	

				// col 0
				slots.add(Integer.toString( ((i+1)/2) ));
				//System.err.println("r:" + Integer.toString( (i+1) ));



				// fill slots for room i
				for (int k = 0; k < this.day_hours; k++) {
					slots.add(""); //col 01 to total hours
					
				}

				//check room info for all hours
				for (int j = 0; j < this.day_hours; j++) {
					SlotInfo slotInfo = getClassInfo(l, j, i);

					if( slotInfo != null){
						for (int d = 0; d < slotInfo.getDuration(); d++) {
							slots.add( j+1+d ,slotInfo.cls.student_batch_list.get(0).batch_code+"-"+ slotInfo.getTeacher().getTeacherID() + "-"+ slotInfo.getCourse().getCourse_id());
						
						}	
					}
				}
				empinfo.put(Integer.toString(i + 1), slots.toArray());
			}//done for all rooms

			// Iterate over data and write to sheet
			int ll=0;
			for (int r = 0; r < this.total_rooms + 1; r++) {

				row = spreadsheet.createRow(r);
				Object[] objectArr = empinfo.get(Integer.toString(ll++));

				int cellid = 0;
				for (Object obj : objectArr) {
					Cell cell = row.createCell(cellid++);
					if(r==0)
					{
						try{
							float v=(float) Integer.parseInt((String)obj)/2;
							v=v+8;
							cell.setCellValue(v);
						}
						catch(Exception e)
						{
							cell.setCellValue((String) obj);

						}
					}
					else
					{
						cell.setCellValue((String) obj);

					}
					if(r%5==0)
					{
						style.setFont(blueFont);
						cell.setCellStyle(style);
					}
					if(r%2==0)
					{
						style.setFont(greenFont);
						cell.setCellStyle(style);
					}
					if(cellid==1)
					{
						cell.setCellStyle(style);
					}
				}
			}

		} // for (int l = 0; l < this.total_days; l++)

		//		empinfo.put("1", new Object[] { "EMP ID", "EMP NAME", "DESIGNATION" });
		autoSizeColumns(workbook);
		// Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File("Writesheet.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("Writesheet.xlsx written successfully");
	}

	SlotInfo getClassInfo(int argday, int argtime, int argroom){
		SlotInfo slotInfo = null;
		int day_size = this.day_hours * this.total_rooms;
		for (Class key : this.classes_map.keySet()) {
			int val = this.classes_map.get(key);// value of hash against that
			// key
			int day = val / day_size;// get that day for eg 3rd day etc
			int time = val % day_size;// gives the time
			int room = time / this.day_hours;// get the room
			time = time % this.day_hours;

			int duration = key.duration;

			if(day == argday && time == argtime && room == argroom){
				slotInfo = new SlotInfo(key.getTeacher(), key.getCourse(), duration, day, time, room, val);
				int indexOfExistingSlot = checkIfSlotAlreadyExist(slotInfo);
				slotInfo.cls=key;
				if(indexOfExistingSlot ==-1){
						//System.out.println("new SLOT");	
						slotsOccupied.add(slotInfo);
				}else{
					
				}
				
			}
		}
		return slotInfo;
	}
	
	public int checkIfSlotAlreadyExist(SlotInfo slotInfo){
		int index = -1;
		
		for (int i = 0; i < slotsOccupied.size(); i++) {
			SlotInfo occupiedSlot = slotsOccupied.get(i);
			if(occupiedSlot.getVal() == slotInfo.getVal()){
				System.out.println("\nClash Found");
				slotInfo.Display();
				occupiedSlot.Display();
				
				index = i;
				break;
			}
			//if(occupiedSlot.getDay() == slotInfo.getDay() && occupiedSlot.getTime == slotInfo.getTime)
		}
		
		return index;
	}

	public void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	            Row row = sheet.getRow(0);
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	            }
	        }
	    }
	}	
	
}
