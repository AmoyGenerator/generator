package actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.JET2Context;
import org.jmr.core.JmrActionSupport;

public class ForTagAction extends JmrActionSupport {

	public void execute(JET2Context context) {
		Book book1 = new Book(1, "Harry Potter", 45.00);
		Book book2 = new Book(2, "Sherlock Holmes", 55.25);
		Book book3 = new Book(3, "The history of Europe", 63.65);

		List<Book> list = new ArrayList<Book>();
		list.add(book1);
		list.add(book2);
		list.add(book3);
		
		context.setVariable("books", list);
	}

}
