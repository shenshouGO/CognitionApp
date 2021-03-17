package MyClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2021/3/17.
 */

public class GetuiFunction {
    private static String appId = "F3X4WEsJsY6Aj2rtPP9n9";
    private static String appKey = "YuPK31YsFr8cEaAjSEAku1";
    private static String appSecret = "ODgl1l3ukH9eey4rcZZn2";
    private static String masterSecret = "kINn82fIED9DFCyWJANKH";
    private static String BaseUrl = "https://restapi.getui.com/v2/F3X4WEsJsY6Aj2rtPP9n9";

    public static void getToken(MyStringCallBack callback){
        JSONObject JO = new JSONObject();
        String timestamp = ""+System.currentTimeMillis();
        try {
            JO.put("sign", SHAEncryption.shaEncrypt(appKey+timestamp+masterSecret));
            JO.put("timestamp",timestamp);
            JO.put("appkey",appKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtil.postJson(BaseUrl+"/auth",JO.toString(),callback);
    }
}
