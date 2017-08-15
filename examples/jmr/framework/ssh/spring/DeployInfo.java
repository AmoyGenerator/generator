package ssh.spring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类说明:部署信息
 */
public class DeployInfo {
	private final static Log log = LogFactory.getLog(DeployInfo.class);

	private String uploadFilePath; // 文件上传位置
	private String fileServerHost;// 文件访问地址

	private String projectName; // 项目名称
	
	private String desKey; //加密密钥
    private String desModeAndPadding; //加密路径
    
    private String from;//发送地址
    private String host;//服务smtp地址
    private String username;//账户
    private String password;//密码
    
    private String language; //语系
    



	private int adMaxSize;//广告大小
	
    public int getAdMaxSize() {
		return adMaxSize;
	}


	public void setAdMaxSize(int adMaxSize) {
		this.adMaxSize = adMaxSize;
	}

    
    public void echoDeployInfo() {
		InetAddress addr;
		try {
			//log.info("jmr 部署信息：");
			addr = InetAddress.getLocalHost();
			//log.info("Web应用伺服器主机->" + addr.getHostName() + ":"
			//		+ addr.getHostAddress());
			//log.info("档上传位置->" + uploadFilePath);
			//log.info("文件访问地址->" + fileServerHost);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}


	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public String getFrom() {
		return from;
	}



	public void setFrom(String from) {
		this.from = from;
	}



	public String getHost() {
		return host;
	}



	public void setHost(String host) {
		this.host = host;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	


	public String getDesKey() {
		return desKey;
	}



	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}



	public String getDesModeAndPadding() {
		return desModeAndPadding;
	}



	public void setDesModeAndPadding(String desModeAndPadding) {
		this.desModeAndPadding = desModeAndPadding;
	}



	public String getFileServerHost() {
		return fileServerHost;
	}



	public void setFileServerHost(String fileServerHost) {
		this.fileServerHost = fileServerHost;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}



	public String getUploadFilePath() {
		return uploadFilePath;
	}



	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}





	
	
}
