package org.eclipse.jet.messages;

import org.eclipse.osgi.util.NLS;
import org.jmr.lang.a.a;
import org.jmr.lang.a.b;

public class Messages extends NLS implements a{
	private static String BUNDLE_NAME_ENGLISH = "org.eclipse.jet.messages.messages_en"; //$NON-NLS-1$
	private static String BUNDLE_NAME_CHINESE = "org.eclipse.jet.messages.messages_zh"; //$NON-NLS-1$
	
	private static Messages messages;
	
	public static Messages getDefault(){
		if(messages == null){
			messages = new Messages();
		}
		return messages;
	}
	
	static {
		b.a(getDefault());
		// initialize resource bundle
		int language = b.d();
		if(a.CHINESE == language){
			NLS.initializeMessages(BUNDLE_NAME_CHINESE, Messages.class);
		}else{
			NLS.initializeMessages(BUNDLE_NAME_ENGLISH, Messages.class);
		}
	}
	
	public static String CodeMappingMessage_Unknown_Exception;
	public static String CodeMappingMessage_Template_Problem;
	public static String CodeMappingMessage_Complie_Error;
	public static String CodeMappingMessage_Include_Template_Notfound;
	public static String CodeMappingMessage_Contains_Its_Own;
	public static String CodeMappingMessage_Attribute_Value_Not_Empty;
	public static String CodeMappingMessage_Attribute_Value_Error;
	public static String CodeMappingMessage_Require_Attribute;
	public static String CodeMappingMessage_Ognl_Expression_Syntax_Exception;
	public static String CodeMappingMessage_OGNL_Inappropriate_Expression_Exception;
	public static String CodeMappingMessage_OGNL_Methodfailed_Exception;
	public static String CodeMappingMessage_OGNL_Nosuchproperty_Exception;
	public static String CodeMappingMessage_OGNL_Nofound_Exception;
	public static String CodeMappingMessage_JET2context_Invalid_Variable_Name;
	public static String CodeMappingMessage_Entity_Nofound;
	public static String CodeMappingMessage_Xml_Nofound;
	public static String CodeMappingMessage_Read_Class_Error;
	public static String CodeMappingMessage_Read_Method_Error;
	public static String CodeMappingMessage_Xpath_Error;
	public static String CodeMappingMessage_Xpath_Runtime_Error;
	public static String CodeMappingMessage_Container_Value_Error;
	public static String CodeMappingMessage_Project_Loader_Exception;
	public static String CodeMappingMessage_Class_Loader_Exception;
	public static String CodeMappingMessage_Class_Reflect_Exception;
	public static String CodeMappingMessage_Tag_Exception;
	public static String CodeMappingMessage_Complie_Class_Exception;
	public static String CodeMappingMessage_Generate_Run_Exception;
	public static String CodeMappingMessage_Container_Create_Exception;
	public static String CodeMappingMessage_Target_IO_Exception;
	public static String CodeMappingMessage_Target_Set_Exception;
	public static String CodeMappingMessage_Model_Exception;
	
	public void changeLanguage(int language) {
		if(a.CHINESE == language){
			NLS.initializeMessages(BUNDLE_NAME_CHINESE, Messages.class);
		}else{
			NLS.initializeMessages(BUNDLE_NAME_ENGLISH, Messages.class);
		}
	}
	
}
