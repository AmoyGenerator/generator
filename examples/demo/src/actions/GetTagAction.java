package actions;

import org.eclipse.jet.JET2Context;
import org.jmr.core.JmrActionSupport;

public class GetTagAction extends JmrActionSupport {

	public void execute(JET2Context context) {
		
		context.setVariable("a_str", "a_str"); 
		context.setVariable("a_bool", false);
		context.setVariable("a_int", 8);
	    double[] array = new double[]{1.1, 2.2, 3.3};
		context.setVariable("a_object", array);
		
		//ognl
	    Student student = new Student(1, "Joke");
	    context.setVariable("student", student); 
	}

}
