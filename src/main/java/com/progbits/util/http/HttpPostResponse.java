package com.progbits.util.http;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpPostResponse {

	private int code;
	private String response;
	private byte[] byteResponse;
	private Map<String, List<String>> headers = new HashMap<String, List<String>>();
	private String _charSet = "UTF-8";

	public HttpPostResponse() {

	}

	public HttpPostResponse(String charSet) {
		_charSet = charSet;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getResponse() {
		return getResponse(null);
	}

	public String getResponse(String characterSet) {
		try {
			if (byteResponse != null) {
				if (characterSet == null) {
					return new String(byteResponse, _charSet);
				} else {
					return new String(byteResponse, characterSet);
				}
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException uex) {
			return "";
		}
	}

	public byte[] getResponseAsBytes() {
		return byteResponse;
	}

	public void setByteResponse(byte[] response) {
		byteResponse = response;
	}

	public boolean containsHeader(String header) {
		return headers.containsKey(header);
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public String getHeader(String key) {
		List<String> entry = headers.get(key);

		if (entry != null && entry.size() > 0) {
			return entry.get(0);
		} else {
			return null;
		}
	}

	public void putHeader(String key, String value) {
		headers.put(key, Arrays.asList(new String[]{value}));
	}

}
