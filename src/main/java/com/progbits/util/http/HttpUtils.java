package com.progbits.util.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpUtils {

	public static HttpPostResponse sendHttpPost(String reqUrl, String strPost) throws HttpUtilException {
		return sendHttp(reqUrl, strPost, "application/vnd.adobe.adept+xml",
				"POST", null);
	}

	public static HttpPostResponse sendHttpPost(String reqUrl, String strPost,
			String hdrContentType) throws HttpUtilException {

		return sendHttp(reqUrl, strPost, hdrContentType, "POST", null);
	}

	public static HttpPostResponse sendHttp(String reqUrl, String strPost,
			String hdrContentType, String RequestMethod) throws HttpUtilException {
		return sendHttp(reqUrl, strPost, hdrContentType, RequestMethod, null);
	}

	public static HttpPostResponse sendHttp(String reqUrl, String strPost,
			String hdrContentType, String RequestMethod,
			Map<String, String> headers) throws HttpUtilException {
		return sendHttp(reqUrl, strPost, hdrContentType, RequestMethod, headers,
				false);
	}

	public static HttpPostResponse sendHttp(String reqUrl, String strPost,
			String hdrContentType, String RequestMethod,
			Map<String, String> headers, boolean followRedirects) throws HttpUtilException {
		return sendHttp(reqUrl, strPost, hdrContentType, RequestMethod, headers,
				followRedirects, false, null);
	}

	public static HttpPostResponse sendHttp(String reqUrl, String strPost,
			String hdrContentType, String RequestMethod,
			Map<String, String> headers, boolean followRedirects,
			boolean ignoreCert,
			String sslContextProtocol) throws HttpUtilException {
		return sendHttp(reqUrl, strPost, hdrContentType, RequestMethod, headers,
				followRedirects, ignoreCert, sslContextProtocol, false);
	}

	public static HttpPostResponse sendHttp(String reqUrl, String strPost,
			String hdrContentType,
			String RequestMethod, Map<String, String> headers,
			boolean followRedirects, boolean ignoreCert,
			String sslContextProtocol, boolean compress) throws HttpUtilException {
		InputStream post = null;
		Long lLength = null;

		if (strPost != null) {
			post = new ByteArrayInputStream(strPost.getBytes(
					StandardCharsets.UTF_8));

			lLength = new Long(strPost.length());
		} else {
			lLength = new Long(0);
		}

		HttpConfig config = new HttpConfig();

		config.setSslContextProtocol(sslContextProtocol);
		config.setIgnoreCert(ignoreCert);
		config.setFollowRedirects(followRedirects);
		config.setCompress(compress);

		return sendHttpInputStream(reqUrl, post, lLength, hdrContentType,
				RequestMethod, headers, config);
	} // public static HttpPostResponse sendHttp

	public static HttpPostResponse sendHttp(String reqUrl, String strPost,
			String hdrContentType,
			String RequestMethod, Map<String, String> headers,
			HttpConfig config) throws HttpUtilException {
		InputStream post = null;
		Long lLength = null;

		if (strPost != null) {
			post = new ByteArrayInputStream(strPost.getBytes(
					StandardCharsets.UTF_8));

			lLength = new Long(strPost.length());
		} else {
			lLength = new Long(0);
		}

		return sendHttpInputStream(reqUrl, post, lLength, hdrContentType,
				RequestMethod, headers, config);
	} // public static HttpPostResponse sendHttp

	public static HttpPostResponse sendHttpInputStream(String reqUrl,
			InputStream post, long postSize,
			String hdrContentType, String RequestMethod,
			Map<String, String> headers, boolean followRedirects,
			boolean ignoreCert,
			String sslContextProtocol, boolean compress) throws HttpUtilException {
		HttpConfig config = new HttpConfig();

		config.setSslContextProtocol(sslContextProtocol);
		config.setIgnoreCert(ignoreCert);
		config.setFollowRedirects(followRedirects);
		config.setCompress(compress);

		return sendHttpInputStream(reqUrl, post, postSize,
				hdrContentType, RequestMethod,
				headers, config);
	}

	public static HttpPostResponse sendHttpInputStream(String reqUrl,
			InputStream post, long postSize,
			String hdrContentType, String RequestMethod,
			Map<String, String> headers, HttpConfig config) throws HttpUtilException {
		try {
			URL url = new URL(reqUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(config.getReadTimeout());
			conn.setConnectTimeout(config.getConnectionTimeout());

			if (conn instanceof HttpsURLConnection) {
				if (config.isIgnoreCert()) {
					try {
						SSLContext sc;

						// What is the Poodle vulnerability ?
						// The "Poodle" vulnerability, released on October 14th, 2014, is an attack on the SSL 3.0 protocol.
						// It is aprotocol flaw, not an implementation issue; every implementation of SSL 3.0 suffers from it.
						// Please note that we are talking about the old SSL 3.0, not TLS 1.0 or later. The TLS versions are not affected (neither is DTLS).
						// 10/14/2014sc = SSLContext.getInstance("SSLv3");
						sc = SSLContext.getInstance(config.
								getSslContextProtocol());

						TrustManager[] tma = {new CertIgnore()};
						sc.init(null, tma, null);
						SSLSocketFactory ssf = sc.getSocketFactory();

						((HttpsURLConnection) conn).setSSLSocketFactory(ssf);
					} catch (Exception ee) {
						// Do Nothing here
					}
				}
			}

			conn.setInstanceFollowRedirects(config.isFollowRedirects());

//            if (hdrContentType != null) {
//                conn.setRequestProperty("Accept", hdrContentType);
//            }
			if (url.getUserInfo() != null) {
				String strAuth = url.getUserInfo();

				conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(strAuth.getBytes()));
			}

			conn.setRequestMethod(RequestMethod);

			if (hdrContentType != null) {
				conn.setRequestProperty("Content-Type", hdrContentType);
			}

			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			if (post != null) {

				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Length", String.valueOf(
						postSize));

				if (config.isCompress()) {
					conn.setRequestProperty("Content-Encoding", "gzip");

					try (GZIPOutputStream out = new GZIPOutputStream(conn.
							getOutputStream())) {
						copyStream(post, out);

						out.flush();
					}
				} else {
					try (OutputStream out = conn.getOutputStream()) {
						copyStream(post, out);
					}
				}
			}

			// Make sure connection is still live
			conn.connect();

			// Receive server's response and put into StringBuffer
			final int code = conn.getResponseCode();
			final String contentType = conn.getContentType();

			byte[] responseText = null;

			if (String.valueOf(code).startsWith("2")) {
				responseText = inputStreamToBytes(conn.getInputStream());
			} else {
				responseText = inputStreamToBytes(conn.getErrorStream());
			}

			HttpPostResponse resp = new HttpPostResponse();

			resp.setHeaders(conn.getHeaderFields());
			resp.setCode(code);
			resp.setByteResponse(responseText);

			return resp;
		} catch (IOException iex) {
			throw new HttpUtilException("sendHttp: " + iex.getMessage(), iex);
		}
	}

	/**
	 * Read an Input Stream Completely, and process with the provided Encoding
	 *
	 * @param is The input stream to pull a string from
	 * @param encoding Encoding to set the result to. DEFAULT: UTF-8
	 *
	 * @return The inputstream as a String of the specific encoding
	 * @throws HttpUtilException
	 */
	public static String inputStreamToString(InputStream is, String encoding) throws HttpUtilException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		String intEncoding = "UTF-8";

		if (encoding != null && !encoding.isEmpty()) {
			intEncoding = encoding;
		}

		if (is == null) {
			return null;
		}

		int len;
		int size = 1024;
		byte[] buf;

		try {
			if (is instanceof ByteArrayInputStream) {
				size = is.available();
				buf = new byte[size];
				len = is.read(buf, 0, size);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				buf = new byte[size];
				while ((len = is.read(buf, 0, size)) != -1) {
					bos.write(buf, 0, len);
				}
				buf = bos.toByteArray();
			}

			if (buf.length == 0) {
				return null;
			} else {
				return new String(buf, intEncoding);
			}
		} catch (IOException iex) {
			throw new HttpUtilException("inputStreamToString Issue", iex);
		}
	}

	/**
	 * Read an Input Stream Completely, and process with the provided Encoding
	 *
	 * @param is The input stream to pull a string from
	 *
	 * @return The inputstream as a String of the specific encoding
	 * @throws HttpUtilException
	 */
	public static byte[] inputStreamToBytes(InputStream is) throws HttpUtilException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		String intEncoding = "UTF-8";

		if (is == null) {
			return null;
		}

		int len;
		int size = 1024;
		byte[] buf;

		try {
			if (is instanceof ByteArrayInputStream) {
				size = is.available();
				buf = new byte[size];
				len = is.read(buf, 0, size);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				buf = new byte[size];
				while ((len = is.read(buf, 0, size)) != -1) {
					bos.write(buf, 0, len);
				}
				buf = bos.toByteArray();
			}

			if (buf.length == 0) {
				return null;
			} else {
				return buf;
			}
		} catch (IOException iex) {
			throw new HttpUtilException("inputStreamToString Issue", iex);
		}
	}

	public static HttpPostResponse sendHttpPost(String reqUrl, String strPost,
			String hdrContentType,
			String RequestMethod, Map<String, String> headers,
			boolean ignoreCert,
			String sslContextProtocol) throws HttpUtilException {
		return sendHttp(reqUrl, strPost, hdrContentType, RequestMethod, headers,
				true, ignoreCert, sslContextProtocol);
	}

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1) {
					break;
				}
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
} // public class HttpUtils {
