package com.progbits.util.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.testng.annotations.Test;

/**
 *
 * @author scarr
 */
public class RunHttpUtil {
    @Test(enabled = false)
    public void testRun() throws Exception {
        HttpPostResponse post = HttpUtils.sendHttp("http://www.mywhatebverasfdasdf.com:9999/mytest", "This is a "
                + "test"
                + "to see if this works", "application/text", "POST");
    }
    
    @Test(enabled = false)
    public void testHttpsRun() throws Exception {
        HttpPostResponse post = HttpUtils.sendHttp("https://www.google.com", null, "text/html", "GET", null, true, true, "SSLv3");
    }
    
    @Test(enabled = true)
    public void testRun2() throws Exception {
        HttpPostResponse post = HttpUtils.sendHttpPost(
                "http://notification.ingramcontent.com/notification/UpdateAlert?ITSD-61205&user_id=ISGService&user_key=isgservice",
                " ", null);
        
        System.out.println(post);
    }
    
    @Test(enabled = false)
    public void testOkJava() throws Exception {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), " ");
        
        Request request = new Request.Builder()
  .url("http://172.21.10.67:9001/notification/UpdateAlert?ITSD-61205=&user_id=ISGService&user_key=isgservice")
  .post(body)
  .addHeader("content-type", "text/plain")
  .addHeader("cache-control", "no-cache")
  .addHeader("postman-token", "95175af9-c229-e65b-4d9b-b8e164012863")
  .build();

        Response response = client.newCall(request).execute();
        
        System.out.println(response.code());
    }
}
