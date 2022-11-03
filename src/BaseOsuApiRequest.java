package snr1s.osuscores;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.InputStreamReader;
import snr1s.osuscores.HttpResponse;

public class BaseOsuApiRequest {
	private final String endpoint;
	private final String token;
	public String method;
	private String data = "";
	public String out = null;

	public BaseOsuApiRequest(String endpoint, String token) {
		this.endpoint = endpoint;
		this.token = token;
	}

	public HttpResponse connect() throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Accept", "application/json"); 
		conn.setRequestProperty("Content-Type", "application/json"); 
		conn.setRequestProperty("Authorization", "Bearer " + this.token);
		conn.setRequestProperty("User-Agent", "osu!scores");
		conn.setUseCaches(false);
		if(data != "")
			conn.getOutputStream().write(data.getBytes());
		if(conn.getResponseCode() / 100 == 4)
			return new HttpResponse(conn.getResponseCode(), "");
		String msg = "";
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        for(int c; (c = in.read()) >= 0;) {
			msg += (char) c;
		}
		conn.getInputStream().close(); // https://stackoverflow.com/a/11533423/19823424
		this.out = msg;
		return new HttpResponse(conn.getResponseCode(), msg);
	}
}
