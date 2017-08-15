package app.j2ee.servlet.checkcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;


/**
 * 类说明:验证码图片
 */
public class CodeImage {

    public static final String CONTENT_TYPE = "image/jpeg; charset=utf-8";
	
	private static CodeImage singleton;
	
	private String randValue;
	private int width = 50;
	private int height = 20;
	private Color bgColor = Color.WHITE;
	private Color fontColor = Color.BLACK;
	private boolean isDisturb = true;
	
	public static CodeImage getInstance(){
		if (singleton == null) singleton = new CodeImage();
		return singleton;
	}
	
	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isDisturb() {
		return isDisturb;
	}

	public void setDisturb(boolean isDisturb) {
		this.isDisturb = isDisturb;
	}

	public String getRandValue() {
		return randValue;
	}

	public void setRandValue(String randValue) {
		this.randValue = randValue;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 生成图片
	 * @return
	 */
	public BufferedImage getImage() {
		Random random = new Random();		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();		
		g.setColor(bgColor);
		g.fillRect(0, 0, width, height);		
		g.setFont(new Font("Courier New",Font.BOLD,18));		
		if (isDisturb) {
			g.setColor(getRandColor(160,200));
			for (int i=0;i<155;i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(8);
				int yl = random.nextInt(8);
				g.drawLine(x,y,x+xl-4,y+yl-4);
			}
		}		
		g.setColor(Color.BLACK);
		g.drawRect(0,0,width-1,height-1);
		String sTemp = getRandNum();
		for (int i = 0; i < 4; i++) {
			g.setColor(fontColor);
			g.drawString(String.valueOf(sTemp.charAt(i)),i*12+2,15);		
		}
		g.dispose();
		return image;
	}
	
	/**
	 * 4位随机数
	 * @return
	 */
	public String getRandNum(){
		//
		Random random = new Random();
		StringBuffer sbRand = new StringBuffer();
		String sTemp = null;
		for (int i = 0; i < 4; i++) {
			sTemp = "" + random.nextInt(10);
			sbRand.append(sTemp);
		}
		randValue = sbRand.toString();
		return randValue;
	}
	
	

	/**
	 * 输出图片
	 * @param response
	 * @param image
	 */
	public static void outPut(OutputStream out, BufferedImage image) {
		try {
			ImageIO.write(image, "JPEG", out);
		} catch (IOException e) {
		//	e.printStackTrace();
		}finally{
			try{
				if (out != null){
					out.close();
				}
			}catch(Exception e){
			}
		}
	}
	
	/**
	 * 取随机color
	 * @param fc
	 * @param bc
	 * @return
	 */
	public Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if(fc>255) fc=255;
		if(bc>255) bc=255;
		int r=fc+random.nextInt(bc-fc);
		int g=fc+random.nextInt(bc-fc);
		int b=fc+random.nextInt(bc-fc);
		return new Color(r,g,b);
	}
	
}
