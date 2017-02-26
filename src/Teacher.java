import java.util.ArrayList;

class Teacher 
{
	String teacherID;
	public String getTeacherID() {
		return teacherID;
	}


	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	//no teacher name in the file
	String teacher_name;
	
	public String getTeacher_name() {
		return teacher_name;
	}


	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	ArrayList<Class> class_list;
	
	Teacher(String teacherID)
	{
		this.teacherID=teacherID;
		//this.teacher_name=teacher_name;
		class_list=new ArrayList<Class>();
	}
	
	
	void display()
	{
		System.out.println("Teacher ID: "+ teacherID);
		//System.out.println("Teacher Name: "+teacher_name);
	}
	
	void addCourse(Class c)
	{
		class_list.add(c);
	}
}
