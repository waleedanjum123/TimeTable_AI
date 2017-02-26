import java.util.ArrayList;

//for single class
public class Class 
{
	
	//right now not concerned about access modifiers
	Teacher teacher;
	Course course;
	int duration;
	
	//student batch for eg: 2016A, 2016B attending this class so its a list
	ArrayList<StudentBatch> student_batch_list;
	
	//extra attributes which could be added more
	
	public ArrayList<StudentBatch> getStudent_batch_list() {
		return student_batch_list;
	}

	public void setStudent_batch_list(ArrayList<StudentBatch> student_batch_list) {
		this.student_batch_list = student_batch_list;
	}

	Class(Teacher teacher,Course course,ArrayList<StudentBatch> student_batch_list,int duration)
	{
		//not concerned right now about deep copy just assignment
		//this class is tacught by this teacher and this particular course with storing student batch info
		this.teacher=teacher;
		this.course=course;
		this.student_batch_list=new ArrayList<StudentBatch>();
		
		
		//add course of this teacher
		this.teacher.addCourse(this);
		
		for(int i=0;i<student_batch_list.size();i++)
		{
			student_batch_list.get(i).addClass(this);
			this.student_batch_list.add(student_batch_list.get(i));
		}
		
		
		this.duration=duration;
	}
	
	void display()	
	{
		this.teacher.display();
		
		this.course.display();
		
		
		System.out.println("Batch Taking this class");
		
		for(int i=0;i<this.student_batch_list.size();i++)
		{
			student_batch_list.get(i).display();
		}
		
		
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

}
