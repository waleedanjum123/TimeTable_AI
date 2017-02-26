import java.util.ArrayList;

public class Course 
{
	String course_id;
	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	//no course name in the file
	String course_name;
	
	
	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	Course(String course_id)
	{
		this.course_id=course_id;
		//this.course_name=course_name;
	}
	
	void display()
	{
		System.out.println("Course ID: "+course_id);
		//System.out.println("Course Name"+course_name);
	}
}
