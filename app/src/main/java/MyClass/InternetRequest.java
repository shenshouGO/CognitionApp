package MyClass;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class InternetRequest {
    //参数列表  <键-值>
    private HashMap<String, String> stringMap;

    public InternetRequest() {
        stringMap = new HashMap<String, String>();
    }

    public void addPara(String key, String value) {
        stringMap.put(key, value);
    }

    //发送请求函数
    public String requestPost(String baseUrl) {
        StringBuilder tempParams = new StringBuilder();
        int pos = 0;

        try {
            for (String key : stringMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(stringMap.get(key), "utf-8")));
                pos++;
            }
            String s = tempParams.toString();
            //获取网络上get方式提交的整个路径
            URL url=new URL(baseUrl);
            //打开网络连接
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            //设置提交方式
            conn.setRequestMethod("POST");
            //设置网络超时时间
            conn.setConnectTimeout(5000);
            //获取请求头
            conn.setRequestProperty("Content-Length",s.length()+"");//键是固定的
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");//键和值是固定的
            //设置允许对外输出数据
            conn.setDoOutput(true);
            //把界面上的所有数据写出去
            OutputStream os=conn.getOutputStream();
            os.write(s.getBytes());
            if(conn.getResponseCode()==200){
                //用io流与web后台进行数据交互
//	    		InputStream is=conn.getInputStream();
                //字节流转字符流
//	    		BufferedReader br=new BufferedReader(new InputStreamReader(is));
                //读出每一行的数据
//	    		String str=br.readLine();
                //返回读出的每一行的数据
                String result = streamToString(conn.getInputStream());
                stringMap.clear();
                return result;
//                Log.e(TAG, "Post方式请求成功，result--->" + result);
            }
            stringMap.clear();
            return "XXX";
        } catch (Exception e)  {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
    }

    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}