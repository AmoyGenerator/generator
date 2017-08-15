package org.eclipse.jet.exception;

import org.eclipse.jet.messages.Messages;

public class CodeMappingMessage {
	
//	public static Map<Integer, String> messageMap = new HashMap<Integer, String>();
//	
//	static{
//		messageMap.put(OtherCode.UNKNOWN_EXCEPTION.getNumber(), "运行时异常");
//		messageMap.put(JetTemplateCode.TEMPLATE_PROBLEM.getNumber(), "模板存在语法错误");
//		messageMap.put(JetTemplateCode.COMPLIE_ERROR.getNumber(), "模板无法编译成执行类,请检查package或class是否符合规范");
//		messageMap.put(JetTemplateCode.INCLUDE_TEMPLATE_NOTFOUND.getNumber(), "找不到include的模板");
//		messageMap.put(JetTemplateCode.CONTAINS_ITS_OWN.getNumber(), "include的模板不能是自己");
//		messageMap.put(JetTemplateCode.PARAMETER_VALUE_ERROR.getNumber(), "参数错误");
//		messageMap.put(JetTemplateCode.REQUIRE_ARGUMENT.getNumber(), "缺少参数");
//		messageMap.put(JetTemplateCode.OGNL_EXPRESSION_SYNTAX_EXCEPTION.getNumber(), "<Ognl>表达式语法错误");
//		messageMap.put(JetTemplateCode.OGNL_INAPPROPRIATE_EXPRESSION_EXCEPTION.getNumber(), "<Ognl>不正确的表达式");
//		messageMap.put(JetTemplateCode.OGNL_METHODFAILED_EXCEPTION.getNumber(), "<Ognl>方法加载错误");
//		messageMap.put(JetTemplateCode.OGNL_NOSUCHPROPERTY_EXCEPTION.getNumber(), "<Ognl>找不到属性");
//		messageMap.put(JetTemplateCode.JET2CONTEXT_INVALID_VARIABLE_NAME.getNumber(), "无效的变量名");
//		messageMap.put(JetTemplateCode.ENTITY_NOFOUND.getNumber(), "找不到Entity");
//		messageMap.put(JetTemplateCode.XML_NOFOUND.getNumber(), "找不到Xml");
//		messageMap.put(JetTemplateCode.READ_CLASS_ERROR.getNumber(), "class读取异常");
//		messageMap.put(JetTemplateCode.READ_METHOD_ERROR.getNumber(), "method读取异常");
//		messageMap.put(JetTemplateCode.XPATH_ERROR.getNumber(), "Xpath错误");
//		messageMap.put(JetTemplateCode.XPATH_RUNTIME_ERROR.getNumber(), "Xpath运行时错误");
//		messageMap.put(JetTemplateCode.CONTAINER_VALUE_ERROR.getNumber(), "标签值错误");
//		messageMap.put(ClassLoaderCode.PROJECT_LOADER_EXCEPTION.getNumber(), "内部错误:无法得到项目的ClassLoader");
//		messageMap.put(ClassLoaderCode.CLASS_LOADER_EXCEPTION.getNumber(), "无法得到Action的ClassLoader,请检查Action类是否存在或者编译是否正常");
//		messageMap.put(ClassLoaderCode.CLASS_REFLECT_EXCEPTION.getNumber(), "读取Action错误");
//		messageMap.put(GenerateCode.TAG_EXCEPTION.getNumber(), "运行时错误:模板标签异常");
//		messageMap.put(GenerateCode.COMPLIE_CLASS_EXCEPTION.getNumber(), "运行时错误:无法取得执行类,请确认模板是否编译成执行类");
//		messageMap.put(GenerateCode.GENERATE_RUN_EXCEPTION.getNumber(), "运行时错误:无法生成代码,执行类执行异常");
//		messageMap.put(JetMergeCode.TARGET_IO_EXCEPTION.getNumber(), "(新建/合并/覆盖)目标文件时,IO异常");
//		messageMap.put(JetMergeCode.TARGET_SET_EXCEPTION.getNumber(), "(新建/合并/覆盖)目标文件无法被读写或者设置编码");
//		messageMap.put(ModelCode.MODEL_EXCEPTION.getNumber(), "模型错误,请检查模型格式是否符合规范");
//	}
	
	public static String get(int code){
		if(code == OtherCode.UNKNOWN_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Unknown_Exception;
		}else if(code == JetTemplateCode.TEMPLATE_PROBLEM.getNumber()){
			return Messages.CodeMappingMessage_Template_Problem;
		}else if(code == JetTemplateCode.COMPLIE_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Complie_Error;
		}else if(code == JetTemplateCode.INCLUDE_TEMPLATE_NOTFOUND.getNumber()){
			return Messages.CodeMappingMessage_Include_Template_Notfound;
		}else if(code == JetTemplateCode.CONTAINS_ITS_OWN.getNumber()){
			return Messages.CodeMappingMessage_Contains_Its_Own;
		}else if(code == JetTemplateCode.ATTRIBUTE_VALUE_NOT_EMPTY.getNumber()){
			return Messages.CodeMappingMessage_Attribute_Value_Error;
		}else if(code == JetTemplateCode.ATTRIBUTE_VALUE_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Attribute_Value_Error;
		}else if(code == JetTemplateCode.REQUIRE_ATTRIBUTE.getNumber()){
			return Messages.CodeMappingMessage_Require_Attribute;
		}else if(code == JetTemplateCode.OGNL_EXPRESSION_SYNTAX_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Ognl_Expression_Syntax_Exception;
		}else if(code == JetTemplateCode.OGNL_INAPPROPRIATE_EXPRESSION_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_OGNL_Inappropriate_Expression_Exception;
		}else if(code == JetTemplateCode.OGNL_METHODFAILED_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_OGNL_Methodfailed_Exception;
		}else if(code == JetTemplateCode.OGNL_NOSUCHPROPERTY_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_OGNL_Nosuchproperty_Exception;
		}else if(code == JetTemplateCode.OGNL_NOFOUND_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_OGNL_Nofound_Exception;
		}else if(code == JetTemplateCode.JET2CONTEXT_INVALID_VARIABLE_NAME.getNumber()){
			return Messages.CodeMappingMessage_JET2context_Invalid_Variable_Name;
		}else if(code == JetTemplateCode.ENTITY_NOFOUND.getNumber()){
			return Messages.CodeMappingMessage_Entity_Nofound;
		}else if(code == JetTemplateCode.XML_NOFOUND.getNumber()){
			return Messages.CodeMappingMessage_Xml_Nofound;
		}else if(code == JetTemplateCode.READ_CLASS_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Read_Class_Error;
		}else if(code == JetTemplateCode.READ_METHOD_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Read_Method_Error;
		}else if(code == JetTemplateCode.XPATH_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Xpath_Error;
		}else if(code == JetTemplateCode.XPATH_RUNTIME_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Xpath_Runtime_Error;
		}else if(code == JetTemplateCode.CONTAINER_VALUE_ERROR.getNumber()){
			return Messages.CodeMappingMessage_Container_Value_Error;
		}else if(code == ClassLoaderCode.PROJECT_LOADER_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Project_Loader_Exception;
		}else if(code == ClassLoaderCode.CLASS_LOADER_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Class_Loader_Exception;
		}else if(code == ClassLoaderCode.CLASS_REFLECT_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Class_Reflect_Exception;
		}else if(code == GenerateCode.TAG_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Tag_Exception;
		}else if(code == GenerateCode.COMPLIE_CLASS_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Complie_Class_Exception;
		}else if(code == GenerateCode.GENERATE_RUN_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Generate_Run_Exception;
		}else if(code == GenerateCode.CONTAINER_CREATE_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Container_Create_Exception;
		}else if(code == JetMergeCode.TARGET_IO_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Target_IO_Exception;
		}else if(code == JetMergeCode.TARGET_SET_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Target_Set_Exception;
		}else if(code == ModelCode.MODEL_EXCEPTION.getNumber()){
			return Messages.CodeMappingMessage_Model_Exception;
		}
		
		return "";
	}
	
}
