package task.java.merge;

import task.java.University;
import task.java.IPerson;

/** @merge */
public class Employee extends University implements IPerson{
    
	/** @merge */
	public String name;
	/** @merge */
	private int age= 25;
	/** @merge */
	private double salary = 3000;

	/** @merge */
	private Employee(String name, int age, double salary) throws Exception {
		super();
		this.name = name;
		this.age = age;
		this.salary = salary;
	}
   
	/** 
	 * 
	 */
	public void printEmployee(String title) {
      System.out.println("Title:" + title);
      //insert_start id="print1"
      System.out.println("Name:" + name);
	  System.out.println("Age:" + age );
	  //insert_end
      //update_start id="print2"
      System.out.println("Salary:" + Math.round(salary));
	  //update_end
	}
	
}
