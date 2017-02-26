import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

	public static int sheetno=1;
	public static String file="";
	public static HashMap<String, Integer> teacherPrefernce=new HashMap<String,Integer>(); 
	
	public static ArrayList<Class> readFile(String filename)//filename="E:\\AI\\AI_testCase.xlsx"
	{
		file=filename;
		ArrayList<StudentBatch> student_batch_list=new ArrayList<StudentBatch>();
		ArrayList<Class> classes=new ArrayList<Class>();
		

		// TODO Auto-generated method stub
		try {
	        FileInputStream file = new FileInputStream(new File(filename));

	        //Create Workbook instance holding reference to .xlsx file
	        XSSFWorkbook workbook = new XSSFWorkbook(file);

	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(sheetno);

	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();
    		ArrayList<Course> courses=new ArrayList<Course>();
    		ArrayList<Teacher> teachers=new ArrayList<Teacher>();
    		ArrayList<Integer> durations=new ArrayList<Integer>();

    		
    		Boolean courseLine=false;

	        while (rowIterator.hasNext())
	        {

	    		Boolean TeacherLine=false;
	    		Boolean durationLine=false;
	    		Boolean GroupsCount=false;


	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                       // System.out.print(cell.getNumericCellValue() + "\t");
	                        if(GroupsCount)
	                        {
//	                        	System.err.println("******");
	                        	GroupsCount=false;
	                        	courseLine=true;
	                        }
	                        if(durationLine)
	                        {
	                        	durations.add(new Integer((int)((float)cell.getNumericCellValue()*2)));
//	                        	durations.add(new Integer((int)(Math.ceil(cell.getNumericCellValue()))));
	                        }
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                        if(TeacherLine)
	                        {
	    		            	//System.err.println(cell.getStringCellValue());
	                        	Teacher teacher=new Teacher(cell.getStringCellValue());
	                        	teachers.add(teacher);
	                        }

	                        //System.out.print(cell.getStringCellValue() + "\t");
	                        if(cell.getStringCellValue().contains("Batch" ))
	                        {
	                        	courseLine=false;
	                        	TeacherLine=true;
	                        	//System.err.println(cell.getStringCellValue());
	                        	
	                    		StudentBatch student_batch1=new StudentBatch(cell.getStringCellValue(),cell.getStringCellValue());
	                    		student_batch_list.add(student_batch1);
	                        }

	                        if(cell.getStringCellValue().contains("Groups C" ))
	                        {
	                        	GroupsCount=true;
	                        }
	                        if(courseLine)
	                        {
	                        	//System.err.println(cell.getStringCellValue());
	                        	Course course=new Course(cell.getStringCellValue());
	                        	courses.add(course);
	                        }
	                        
	                        if(cell.getStringCellValue().contains("class duration" ))
	                        {
	                        	durationLine=true;
	                        }

	                        break;
	                }
	            }
	            if(durationLine)
	            {
	            	durationLine=false;
	            	//System.out.println(teachers.size()+" "+courses.size()+" "+durations.size());
	            	
		            for(int i=0;i<teachers.size();i++)
		            {
		            	//System.err.println("******");
		            	ArrayList<StudentBatch> student_batch_list_course_teacher=new ArrayList<StudentBatch>();
		            	//student_batch_list_course_teacher=getStudentBatchList(courses.get(i),teachers.get(i));        	
		            	
		            	ArrayList<StudentBatch> sbList=new ArrayList<StudentBatch>();
		            	sbList.add(student_batch_list.get(student_batch_list.size()-1));
//		        		Class c1=new Class(teachers.get(i),courses.get(i),sbList,durations.get(i));
		        		//c1.display();
		            	//System.err.println("******");

		        		if(durations.get(i)==2)
		        		{
		        			for(int l=0;l<3;l++)
		        			{
				        		Class c1=new Class(teachers.get(i),courses.get(i),sbList,durations.get(i));
		        				classes.add(c1);	            	
	
		        			}
		        		}
		        		if(durations.get(i)==3)
		        		{
			        		
		        			for(int l=0;l<2;l++)
		        			{
				        		Class c1=new Class(teachers.get(i),courses.get(i),sbList,durations.get(i));
		        				classes.add(c1);	            	
	
		        			}
		        		}
		        		
		            }
		    		teachers=new ArrayList<Teacher>();
		    		durations=new ArrayList<Integer>();

	            }
	           // System.out.println("");
	        }
	        file.close();
	        


	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		//Getting classes filled with relevant teachers
//		ArrayList<Class> finalClasses=new ArrayList<Class>();
//		for(int i=0;i<classes.size();i++)
//		{
//			Class temp=classes.get(i); 
//        	ArrayList<StudentBatch> student_batch_list_course_teacher=new ArrayList<StudentBatch>();
//        	student_batch_list_course_teacher=getStudentBatchList(temp.course,temp.teacher);        	
//        	finalClasses.add(new Class(temp.teacher, temp.course, student_batch_list_course_teacher, temp.duration));
//    		
//        }
		
		return classes;
	}
	
	private static ArrayList<StudentBatch> getStudentBatchList(Course course, Teacher teacher) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		//System.out.println("Teacher: "+teacher.teacherID+" Course: "+course.course_id);

		ArrayList<StudentBatch> batches=new ArrayList<StudentBatch>();
		try {
	        FileInputStream file = new FileInputStream(new File(ReadExcel.file));

	        //Create Workbook instance holding reference to .xlsx file
	        XSSFWorkbook workbook = new XSSFWorkbook(file);

	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(sheetno);

	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();

	        int course_index=0;	
            ArrayList<String> courses=new ArrayList<String>();
        	//System.out.println("sasdas");
            String batch="";




	        while (rowIterator.hasNext())
	        {
	            Row row = rowIterator.next();

	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();
	            
	            int index=0;
	            
	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();


	            	if(course_index==index||index==0 ||batch.equals(""))
	            	{
	            				                
		                //Check the cell type and format accordingly
		                switch (cell.getCellType()) 
		                {                        		

	                    case Cell.CELL_TYPE_NUMERIC:
		                       // System.out.print(cell.getNumericCellValue() + "\t");
		                        break;

		                    case Cell.CELL_TYPE_STRING:

	                        if(cell.getStringCellValue().contains("Batch" ))
	                        {
	                        	batch=cell.getStringCellValue();
	                        }
	                        if(cell.getStringCellValue().equals(course.getCourse_id()))
	                        {
	                        	course_index=index;
	                        }
	                        if(cell.getStringCellValue().contains(teacher.getTeacherID()))
	                        {	                        	
	                        	batches.add(new StudentBatch(batch,batch));
	                        }
//	                        if(cell.getStringCellValue().contains("Teachers"))
//	                        {
//                        		System.out.println("***********S***********");
//                        		System.out.println("Teacher: "+teacher.teacherID+" Course: "+course.course_id);
//                        		
//	                        	for(int i=0;i<batches.size();i++)
//	                        	{
//	                        		System.out.println(batches.get(i).batch_name);
//	                        	}
//                        		System.out.println("************E**********");
//                        		System.in.read();
//	                        	return batches;
//	                        }
	                         

	
	                    	break;
		                }
	            	}//if End
	                index++;

	            }
	           // System.out.println("");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		

		return batches;
	}
	public static int getValueFor(String search)//filename="E:\\AI\\AI_testCase.xlsx"
	{
		String filename=file;
		ArrayList<StudentBatch> student_batch_list=new ArrayList<StudentBatch>();
		ArrayList<Class> classes=new ArrayList<Class>();

		// TODO Auto-generated method stub
		try {
	        FileInputStream file = new FileInputStream(new File(filename));

	        //Create Workbook instance holding reference to .xlsx file
	        XSSFWorkbook workbook = new XSSFWorkbook(file);

	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(sheetno);

	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();

    		
    		Boolean courseLine=false;

	        while (rowIterator.hasNext())
	        {

	    		Boolean TotalClassesLine=false;


	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	if(TotalClassesLine==true)
	                    	{
	                    		return (int)cell.getNumericCellValue();
	                    	}
	                        break;
	                    case Cell.CELL_TYPE_STRING:
                	 if(cell.getStringCellValue().contains(search ))
                    {
                    	TotalClassesLine=true;
                    }
	                    	 break;
	                }
	            }
	            file.close();
	    
	        }
		}
		catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return 0;
	}

	public static ArrayList<StudentBatch> getBatchList()//filename="E:\\AI\\AI_testCase.xlsx"
	{
		String filename=file;
		ArrayList<StudentBatch> student_batch_list=new ArrayList<StudentBatch>();

		// TODO Auto-generated method stub
		try {
	        FileInputStream file = new FileInputStream(new File(filename));

	        //Create Workbook instance holding reference to .xlsx file
	        XSSFWorkbook workbook = new XSSFWorkbook(file);

	        //Get first/desired sheet from the workbook
	        XSSFSheet sheet = workbook.getSheetAt(sheetno);

	        //Iterate through each rows one by one
	        Iterator<Row> rowIterator = sheet.iterator();

    		
    		Boolean courseLine=false;

	        while (rowIterator.hasNext())
	        {

	    		Boolean TotalClassesLine=false;


	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	                //Check the cell type and format accordingly
	                switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                        break;
	                    case Cell.CELL_TYPE_STRING:
                	 if(cell.getStringCellValue().contains("Batch"))
                    {
                    	student_batch_list.add(new StudentBatch(cell.getStringCellValue(), cell.getStringCellValue()));
                    }
	                    	 break;
	                }
	            }
	            file.close();
	    
	        }
		}
		catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return student_batch_list;
	}

	public static int getTeacherPrefernce(String teacherN)//filename="E:\\AI\\AI_testCase.xlsx"
	{
		if(ReadExcel.teacherPrefernce.isEmpty())
		{
			ReadExcel.teacherPrefernce=new HashMap<String,Integer>();
		String filename=file;
		ArrayList<StudentBatch> student_batch_list=new ArrayList<StudentBatch>();

		// TODO Auto-generated method stub
			try {
		        FileInputStream file = new FileInputStream(new File(filename));
	
		        //Create Workbook instance holding reference to .xlsx file
		        XSSFWorkbook workbook = new XSSFWorkbook(file);
	
		        //Get first/desired sheet from the workbook
		        XSSFSheet sheet = workbook.getSheetAt(sheetno);
	
		        //Iterate through each rows one by one
		        Iterator<Row> rowIterator = sheet.iterator();
	
	    		
	    		Boolean TeacherPreferenceLine=false;
	
		        while (rowIterator.hasNext())
		        {
	
		    		Boolean TotalClassesLine=false;
	
	
		            Row row = rowIterator.next();
		            //For each row, iterate through all the columns
		            Iterator<Cell> cellIterator = row.cellIterator();
	
		            String teacherName="";
		            
		            while (cellIterator.hasNext()) 
		            {
		                Cell cell = cellIterator.next();
		                //Check the cell type and format accordingly
		                switch (cell.getCellType()) 
		                {
		                    case Cell.CELL_TYPE_NUMERIC:
		                        break;
		                    case Cell.CELL_TYPE_STRING:
	                	 if(cell.getStringCellValue().contains("Teachers "))
	                    {
	                		 TeacherPreferenceLine=true;
	                    }
	                	 
	                	 if(TeacherPreferenceLine)
	                	 {
	                		 if(cell.getStringCellValue().equals("m")||cell.getStringCellValue().equals("e"))
	                		 {
	                			 if(cell.getStringCellValue().equals("m"))
	                			 {
	                				 ReadExcel.teacherPrefernce.put(teacherName,0);
	                			 }
	                			 else
	                			 {
	                				 
	                				 ReadExcel.teacherPrefernce.put(teacherName,1);
	
	                			 }
	                		 }
	                		 else
	                		 {
	                			 //System.out.println(cell.getStringCellValue());
	                       		 teacherName=cell.getStringCellValue();
	                       		 
	                		 }
	                	 }
		                    	 break;
		                }
		            }
		            file.close();
		    
		        }
			}
			catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		try{
			if(ReadExcel.teacherPrefernce.containsKey(teacherN))
			{
				Integer mapval=ReadExcel.teacherPrefernce.get(teacherN);
				if(mapval==null)
				{
					return 2;
				}
				if(mapval==0)
				{
					return 0;
				}
				if(mapval==1)
				{
					return 1;
				}
			}
		}
		catch(Exception e)
		{
			return 2;
		}
		return 2;

	}



}
