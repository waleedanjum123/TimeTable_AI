import java.io.IOException;
import java.util.ArrayList;

public class Main {

	
	public static void main(String args[])
	{
		
		String filename="AI_testCase.xlsx";
		ArrayList<Class> class_list = new ArrayList<Class>();
		class_list=ReadExcel.readFile(filename);
		//System.out.println(class_list.size());
		
		Algorithm algo=new Algorithm(500,3,1,ReadExcel.getValueFor("Total Rooms"),class_list);
		algo.Start();

		
	}
}
