
public class SlotInfo {

	Teacher teacher;

	Course course;
	int duration, day, time, room, val, clash;
	Class cls;
	public SlotInfo(Teacher teacher, Course course, int duration, int day, int time, int room) {
		super();
		this.teacher = teacher;
		this.course = course;
		this.duration = duration;
		this.day = day;
		this.time = time;
		this.room = room;
		this.clash = 0;
	}

	
	public SlotInfo(Teacher teacher, Course course, int duration, int day, int time, int room, int val) {
		super();
		this.teacher = teacher;
		this.course = course;
		this.duration = duration;
		this.day = day;
		this.time = time;
		this.room = room;
		this.val = val;
		this.clash = 0;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getRoom() {
		return room;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public void setRoom(int room) {
		this.room = room;
	}


	public int getClash() {
		return clash;
	}


	public void setClash(int clash) {
		this.clash = clash;
	}


	public void Display() {
		// TODO Auto-generated method stub
		System.out.println("Day: " + this.day + " |Time: " + this.time + " |T: " + teacher.getTeacherID() +  " |C: " + course.getCourse_id() + " |Room: " + room);
	}
	
	
	
}
