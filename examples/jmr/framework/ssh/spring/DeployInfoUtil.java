package ssh.spring;


/**
 * 类说明：部署环境信息
 */
public class DeployInfoUtil {
	
	/**
	 * 取得文件上传位置
	 * @return
	 */
	public  static String getUploadFilePath(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getUploadFilePath();
	}
	
	
	/**
	 * 取得文件访问地址
	 * @return
	 */
	public  static String getFileServerHost(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getFileServerHost();
	}
	
	
	/**
	 * 取得项目名称
	 * @return
	 */
	public  static String getProjectName(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getProjectName();
	}
	
	/**
	 * 取得加密密钥
	 * @return
	 */
	public  static String getDesKey(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getDesKey();
	}
	
	
	/**
	 * 取得加密路径
	 * @return
	 */
	public  static String getDesModeAndPadding(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getDesModeAndPadding();
	}
	
	/**
	 * 取得发送地址
	 * @return
	 */
	public  static String getEmailFrom(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getFrom();
	}
	
	/**
	 * 取得服务smtp
	 * @return
	 */
	public  static String getEmailHost(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getHost();
	}
	
	/**
	 * 取得账户
	 * @return
	 */
	public  static String getUsername(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getUsername();
	}
	
	/**
	 * 取得密码
	 * @return
	 */
	public  static String getPassword(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getPassword();
	}
	
	/**
	 * 取得语系
	 * @return
	 */
	public  static String getLanguage(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getLanguage();
	}
	
	/**
	 * 取得广告最大大小
	 * @return
	 */
	public  static int getAdMaxSize(){
		return ((DeployInfo)SpringContextUtil.getApplicationContext().getBean("deployInfo")).getAdMaxSize();
	}
	
	
}
