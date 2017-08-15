package app.j2ee.data;

import java.util.Date;

/**
 * 类说明:随机码生成器
 */
public class RandKeyCreator {

	public static final long MIN_VALUE = 0x8000000000000000L;

    public static final long MAX_VALUE = 0x7fffffffffffffffL;

    final static char[] digits ={ '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z'};

    private static String toUnsignedString(long i, int shift) {
        String rankKey;
    	char[] buf = new char[36];
        int charPos = 36;
        int radix = 1 << shift;
        long mask = radix - 1;
        do{
            buf[--charPos] = digits[(int) (i & mask)];
            i >>>= shift;
        } while (i != 0);
        rankKey =  new String(buf, charPos, (36 - charPos));
        if(rankKey.length() < 12 ){
            switch (rankKey.length()) {
                case 11 : rankKey += "A";break;
                case 10 : rankKey += "BB";break;
                case 9 : rankKey += "CCC";break;
                case 8 : rankKey += "DDDD";break;
            }
        }
        return rankKey;
    }
    
     
     // j为2的次方，如转成16进制就是4，32进制就是5...
     public static String getRand(long i,int j){
         return toUnsignedString(i, j); 
     }
     
     // 随机码＋时间時間戳＋随机码的生成
     public static Long getRand(){
         String str1,str2,str3;
         str1=getRandStr(2);
         str3=getRandStr(3);
         str2=(new Date()).getTime()+"";
         //System.out.println(str1+str2+str3);
         return Long.parseLong(str1+str2+str3);
     }
     
     // 主键生成
     public static String getKey(){
         return getRand(getRand(),5);
     }
     
//   主键生成
     public static String getKey(int num){
         return getRand(getRand(),num);
     }
     
     //    生成指定长度的随机串
     public static String getRandStr(Integer length){
         String str="";
         while(str.length()!=length){
             str=(Math.random()+"").substring(2,2+length);
         }
         return str;
     }
     
    public static void main(String arg[]){
    	for(int i=0;i<100;i++){
            System.out.println("第"+i+"个："+ getKey(5));
       }

    }

}

