/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progbits.util.http;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

/**
 *
 * @author scarr
 */
public class RunJira {

    @Test
    public void testId() throws Exception {
        String strReq = "https://tickets-staging7.ingramcontent.com/rest/api/2/issue/ITSD-46080?fields=key,status";
        String strPost = null;
        String strMethod = "GET";
        String strContentType = null;
        
        Map<String, String> headers = new HashMap<>();
        
        HttpPostResponse resp = HttpUtils.sendHttp(strReq, strPost, strContentType, strMethod, headers);
        
        System.out.println(resp.getCode());
		  System.out.println(resp.getResponse());
    }
}
