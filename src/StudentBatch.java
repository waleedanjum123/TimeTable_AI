import java.util.ArrayList;

class StudentBatch {

	String batch_code;
	String batch_name;
	
	//total classes this batch attends
	ArrayList<Class> class_list;
		
	
	//extra
	int  total_batch_students;
	
	public static int findIndex(ArrayList<StudentBatch> batches,StudentBatch sb)
	{
		for(int i=0;i<batches.size();i++)
		{
			StudentBatch batch=batches.get(i);
			//System.out.println(sb.batch_code+" "+batch.batch_code);
			if(batch.batch_code.equals(sb.batch_code)||batch.batch_name.equals(sb.batch_name))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	
	StudentBatch(String batch_code,String batch_name)
	{
		this.batch_code=batch_code;
		this.batch_name=batch_name;
		
		class_list=new ArrayList<Class>();
				
	}
	
	
	void addClass(Class c)
	{
		class_list.add(c);
	}
	
	
	void display()
	{
		System.out.println("Batch Code: "+this.batch_code);
		
		System.out.println("Batch Name: "+this.batch_name);
		
	}
	
	
}
