package cn.luowq.spider.httpclient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @Auther: rowan
 * @Date: 2018/9/27 16:35
 * @Description:
 */
public class TestSpider {

    //获取sessionId
    private static String getSessionInfo() throws IOException {

        //登录网站，返回sessionId信息
        Connection.Response res = Jsoup.connect("http://www.bjtax.gov.cn/ptfp/fpindex.jsp")//要登录的url可以在登录页面将post改为get查看它是如何传参数的（我这里选择的是东北林业大学：具体可以查看下图）
//                .data("USERNAME", username, "PASSWORD", password)
                .method(Connection.Method.GET)
                .header("Connection","keep-alive")
                .header("Cache-Control","max-age=0")
                .header("Upgrade-Insecure-Requests","1")
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("Accept-Encoding","gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8")
                .header("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cookie","node1=8fHbbsQbRCyQwLYQf63ccG14J22JR1QRfm1Swv1cqJl1FzY2JsZn^!-790622327")
                .timeout(10000)
                .execute();
        //获得sessionId
        System.out.println(res);
        String sessionId = res.cookie("node1");
        System.out.println(sessionId);
        return sessionId;
    }

    public static String sendPost(String url, String param) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        getSessionInfo();
//        String url = "http://www.bjtax.gov.cn/ptfp/getVFImage?sessionrandom=0.3295077803681634&sessionId=8fHbbsQbRCyQwLYQf63ccG14J22JR1QRfm1Swv1cqJl1FzY2JsZn!-790622327!1538035899226";
        String url = "http://www.bjtax.gov.cn/ptfp/getVFImage?sessionrandom=0.3295077803681634&sessionId=8fHbbsQbRCyQwLYQf63ccG14J22JR1QRfm1Swv1cqJl1FzY2JsZn^!-790622327";
        System.out.println(TestSpider.sendGet(url,null));
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     * @return URL 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String sendGet(String url, String param) throws Exception {
        String result = "";
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
//            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义 BufferedReader输入流来读取URL的响应
            in = connection.getInputStream();
            byte[] data = new byte[1024];
            int len = 0;

                fileOutputStream = new FileOutputStream("D:\\test1.jpg");
                while ((len = in.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {

            if (in != null) {
                    in.close();
            }
            if (fileOutputStream!=null){
                fileOutputStream.close();
            }
        }
        return result;
    }


}