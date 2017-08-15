package action;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jet.JET2Context;
import org.jmr.core.JmrActionSupport;

public class MyAction extends JmrActionSupport {

	public void execute(JET2Context context) {
		String s = "str";
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		context.setVariable("s", s);
		context.setVariable("list", list);
	}

}

