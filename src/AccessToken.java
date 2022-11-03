package snr1s.osuscores;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import snr1s.osuscores.HttpResponse;

public class AccessToken {
	private final int clientId;
	private final String clientSecret;
	private String token;
	private long expiretime = -1;

	public AccessToken(int clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String get() {
		if(System.currentTimeMillis() >= expiretime - 2000 /* Just in case it becomes invalid during usage */) {
			System.out.println("Generating new token...");
			try {
				generate();
			} catch(Exception e) {
				System.out.println("AccessToken | Failed to renew the token");
			}
		}

		return this.token;
	}

	private void generate() throws Exception {
		String postString = "{\"client_id\":" + clientId + ",\"client_secret\":\"" + clientSecret + "\",\"grant_type\":\"client_credentials\",\"scope\":\"public\"}";
		byte[] postData = postString.getBytes();
		String json = "";
		URL url = new URL("https://osu.ppy.sh/oauth/token");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();           
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept", "application/json"); 
		conn.setRequestProperty("Content-Type", "application/json"); 
		conn.setUseCaches(false);
		conn.getOutputStream().write(postData);
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        for(int c; (c = in.read()) >= 0;) {
			json += (char) c;
		}
		JSONObject jobj = new JSONObject(json);
		if(jobj.has("error")) {
			this.token = null;
			return;
		}
		this.token = jobj.getString("access_token");
		this.expiretime = System.currentTimeMillis() + (jobj.getInt("expires_in") * 1000);
	}

	public HttpResponse remove() throws Exception {
		URL url = new URL("https://osu.ppy.sh/api/v2/oauth/tokens/current");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("Authorization", "Bearer " + this.token);
		return new HttpResponse(conn.getResponseCode(), conn.getResponseMessage());
	}
}
