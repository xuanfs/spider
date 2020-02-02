package xuanfs.util;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
public class SpiderUtil {
    /**
     * 根据URL地址和网页编码集获取网页源代码
     * @param addr
     * @param encoding
     * @return 网页源代码
     * @author xzj
     */
    public static String getHtmlByUrl(String addr,String encoding){
        URL url = null;
        URLConnection uc = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            //    获取URL
            url = new URL(addr);
            //    打开URL链接
            uc = url.openConnection();
            //    获取输入流
            isr = new InputStreamReader(uc.getInputStream(),encoding);
            //    获取缓冲流
            br = new BufferedReader(isr);
            //    存储的临时变量
            String str = null;
            while((str = br.readLine())!= null){
                sb.append(str+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static JSONObject getHttpJson(String url,int comefrom){
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            connection.connect();
            System.out.println("code:"+connection.getResponseCode());
            if (connection.getResponseCode()==200){
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[10485760];
                int len = 0;
                while((len = is.read(buffer))!=-1){
                    baos.write(buffer,0,len);
                }
                String jsonString = baos.toString();
                baos.close();
                is.close();
                return  getJsonString(jsonString, comefrom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonString(String str, int comefrom) throws Exception{
        JSONObject jo = null;
        if(comefrom==1){
            return new JSONObject(str);
        }else if(comefrom==2){
            int indexStart = 0;
            //字符处理
            for(int i=0;i<str.length();i++){
                if(str.charAt(i)=='('){
                    indexStart = i;
                    break;
                }
            }
            String strNew = "";
            //分割字符串
            for(int i=indexStart+1;i<str.length()-1;i++){
                strNew += str.charAt(i);
            }
            return new JSONObject(strNew);
        }
        return jo;
    }

}
