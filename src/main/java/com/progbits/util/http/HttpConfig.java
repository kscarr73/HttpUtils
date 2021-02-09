package com.progbits.util.http;

/**
 *
 * @author scarr
 */
public class HttpConfig {

	private int _connectionTimeout = 30000;
	private int _readTimeout = 60000;
	private boolean _compress = false;
	private boolean _followRedirects = false;
	private boolean _ignoreCert = false;
	private String _sslContextProtocol = "";

	public int getReadTimeout() {
		return _readTimeout;
	}

	public void setReadTimeout(int _readTimeout) {
		this._readTimeout = _readTimeout;
	}

	public int getConnectionTimeout() {
		return _connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this._connectionTimeout = connectionTimeout;
	}

	public boolean isCompress() {
		return _compress;
	}

	public void setCompress(boolean compress) {
		this._compress = compress;
	}

	public boolean isFollowRedirects() {
		return _followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this._followRedirects = followRedirects;
	}

	public boolean isIgnoreCert() {
		return _ignoreCert;
	}

	public void setIgnoreCert(boolean _ignoreCert) {
		this._ignoreCert = _ignoreCert;
	}

	public String getSslContextProtocol() {
		return _sslContextProtocol;
	}

	public void setSslContextProtocol(String sslContextProtocol) {
		this._sslContextProtocol = sslContextProtocol;
	}

}
