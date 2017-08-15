package task.java.create;

/** 
 * @create
 */
public class Employee {
    /**
     * @create
     */
	private String name;
	private int age;

	public Employee(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public void printEmployee(){
      System.out.println("Name:"+ name );
      System.out.println("Age:" + age );
    }
   
}
