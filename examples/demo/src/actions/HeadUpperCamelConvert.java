package actions;

import org.jmr.core.JmrConvert;

public class HeadUpperCamelConvert extends JmrConvert {

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
				}else{
					if(returnValue.length() == 0){
						c = Character.toUpperCase(c);
					}else{
						c = Character.toLowerCase(c);
					}
				}
				
				returnValue = returnValue + c;
				underscore = false;
			}
		}
		return returnValue;
	}

}
