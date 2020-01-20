package xuanfs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 爬虫练习
 * @author xzj
 */
public class Test {

    /**
     * 根据URL地址和网页编码集获取网页源代码
     * @param addr
     * @param encoding
     * @return 网页源代码
     * @author xzj
     */
    public static String getJobByUrl(String addr,String encoding){
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

    public static void main(String[] args) {
        int start = 1;
        StringBuffer sb = new StringBuffer();
        for(;start<=3;start++){
            String addr = "https://search.51job.com/list/030200,000000,0000,00,2,99,java,2,"+start+".html?lang=c&postchannel=0000&workyear=01&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
            String encoding = "gbk";

            //    获取网页源代码
            String html = getJobByUrl(addr, encoding);
            //    通过源代码获取文档
            Document document = Jsoup.parse(html);
            //    通过文档的节点获取元素
            Elements elements = document.getElementsByClass("el");
            for (Element ele: elements) {
                String job = ele.getElementsByClass("t1").text();
                String company = ele.getElementsByClass("t2").text();
                String address = ele.getElementsByClass("t3").text();
                String salary = ele.getElementsByClass("t4").text();
                String day = ele.getElementsByClass("t5").text();
                String href = ele.select("a").attr("href");
                if(!"".equals(job)){
                    sb.append("\r\n职位:"+job+"\r\n公司:"+company+"\r\n地址:"+address+"\r\n工资:"+salary+"\r\n发布日期:"+day+"\r\n详细链接:"+href+"\r\n");
                    System.out.println("job:"+job);
                    System.out.println("\tcompany:"+company);
                    System.out.println("\taddress:"+address+"\tsalary:"+salary+"\tday:"+day);
                    System.out.println("\thref:"+href);
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream("d:/job.txt");
            byte[] bytes1 = sb.toString().getBytes();
            fos.write(bytes1);
            System.out.println("导出完成！");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
