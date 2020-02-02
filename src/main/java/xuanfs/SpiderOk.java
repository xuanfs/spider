package xuanfs;

import com.alibaba.fastjson.JSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xuanfs.entity.Suning;
import xuanfs.util.SpiderUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SpiderOk {

    /**
     * 获取百度图片
     * @param word 关键字
     * @param forNum 循环次数(一次30张)
     */
    public static void getImageByBaiDu(String word,int forNum){
        //  字符格式
        String encoding = "utf8";
        //  图片保存路径
        String path = "D:\\spider_msg\\BaiDuImage\\"+word;

        byte[] bArr = new byte[1024*8];
        File file = null;
        FileOutputStream fos = null;
        InputStream is = null;
        //  循环次数
        for (int i = 0;i < forNum;i++){
            //  百度图片地址
            String addr = "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=%E6%B1%89%E6%9C%8D%E7%BE%8E%E5%A5%B3%E5%A4%B4%E5%83%8F&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word="+word+"&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&force=&pn="+(i*30)+"&rn=30&gsm=&1580531944193=";
            //  获取返回的html
            String html = SpiderUtil.getHtmlByUrl(addr, encoding);
            //  分割字符
            String[] thumbURLS = html.split("thumbURL");
            //  遍历分割号的字符数组
            for (int j = 1; j < thumbURLS.length; j++) {
                //  获取图片url地址
                String substring = thumbURLS[j].substring(3, thumbURLS[j].indexOf("\"",6));
                System.out.println("url:"+substring);
                try {
                    //  创建图片文件
                    file = new File(path + substring.substring(substring.indexOf("=")+1));
                    //  创建文件输出流
                    fos = new FileOutputStream(file);
                    //  获取图片链接
                    URL url = new URL(substring);
                    //  打开链接
                    URLConnection urlConnection = url.openConnection();
                    //  获取输入流
                    is = urlConnection.getInputStream();
                    int res = 0;
                    // 写入到图片文件中
                    while((res = is.read(bArr))!=-1){
                        fos.write(bArr,0,res);
                    }
                    System.out.println("拷贝成功！");
                    //  关闭流
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 获取51同城
     */
    public static void getJobTxt(){
        int start = 1;
        StringBuffer sb = new StringBuffer();
        for(;start<=3;start++){
            String addr = "https://search.51job.com/list/030200,000000,0000,00,2,99,java,2,"+start+".html?lang=c&postchannel=0000&workyear=01&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
            String encoding = "gbk";

            //    获取网页源代码
            String html = SpiderUtil.getHtmlByUrl(addr,encoding);
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
            FileOutputStream fos = new FileOutputStream("d:/spider_msg/job.txt");
            byte[] bytes1 = sb.toString().getBytes();
            fos.write(bytes1);
            System.out.println("导出完成！");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取苏宁低价信息
     * @param date 时间
     */
    static void getSnTxt(Long date){
        StringBuffer sb = new StringBuffer();
        Date time = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("苏宁天天低价场次"+sdf.format(time)+"\r\n");
        try {
            for (int i = 1; i < 4; i++) {
                String addr = "https://ju.suning.com/pc/newZsq/ajax/queryZsqCommList_3__"+i+"__"+date+"_020_queryZsqCommList.html?callback=queryZsqCommList";
                JSONObject httpJson = SpiderUtil.getHttpJson(addr, 2);
                if(httpJson == null){
                    break;
                }
                JSONObject data = httpJson.getJSONObject("data");
                JSONArray commList = data.optJSONArray("commList");
                List<Suning> sunings = JSON.parseArray(commList.toString(), Suning.class);
                for (Suning su: sunings) {
                    sb.append("\r\n商品名称："+su.getGrppurName()+"\r\n商品价格："+su.getGbPrice()+"\r\n商品公司："+su.getVendorName()+"\r\n");
                    System.out.println("grppurName:"+su.getGrppurName());
                    System.out.println("\tgbPrice:"+su.getGbPrice());
                    System.out.println("\tvendorName:"+su.getVendorName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                FileOutputStream fos = new FileOutputStream("d:/spider_msg/sn_low_price.txt");
                fos.write(sb.toString().getBytes());
                System.out.println("导出完成！");
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 抓取腾讯视频数据
     */
    public static void getTencentVideo(){
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<=90;i=i+30){
            String addr ="https://v.qq.com/x/bu/pagesheet/list?_all=1&append=1&channel=movie&iarea=100024&itype=100006&listpage=2&offset="+i+"&pagesize=30&sort=18";
            String encoding = "utf8";
            String htmlByUrl = SpiderUtil.getHtmlByUrl(addr, encoding);
            Document document = Jsoup.parse(htmlByUrl);
            Elements item = document.getElementsByClass("list_item");
            for (Element e:item) {
                String score = e.getElementsByClass("figure_score").text();
                String href = e.getElementsByTag("a").first().attr("href");
                String title = e.getElementsByClass("figure_title figure_title_two_row bold").text();
                String desc = e.getElementsByClass("figure_desc").text();
                sb.append("\r\ntitle:"+title+"\r\n\tscore:"+score+"\r\n\tdesc:"+desc+"\r\n\thref:"+href+"\r\n");
                System.out.println("title:"+title);
                System.out.println("\tscore:"+score);
                System.out.println("\tdesc："+desc);
                System.out.println("\thref:"+href);
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream("d:/spider_msg/Tencent_video.txt");
            fos.write(sb.toString().getBytes());
            fos.close();
            System.out.println("导入完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
