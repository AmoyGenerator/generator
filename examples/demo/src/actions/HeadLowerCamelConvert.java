package actions;

import org.jmr.core.JmrConvert;

public class HeadLowerCamelConvert extends JmrConvert {

	public String convert(String value) {
		String returnValue = "";
		boolean underscore = false;
		if(value.length() > 0){
			for(int i = 0; i < value.length(); i++){
				char c = value.charAt(i);
				if('_' == c){
					underscore = true;
					continue;
				}
				
				if(underscore){
					c = Character.toUpperCase(c);
				}
				
				returnValue = returnValue + c;
				underscore = false;
			}
		}
		return returnValue;
	}
	
}
