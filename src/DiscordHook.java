package snr1s.osuscores;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.util.ArrayList;
import snr1s.osuscores.HttpResponse;

public class DiscordHook {
	private URL url;

	public DiscordHook(String url) throws Exception {
		this.url = new URL(url);
	}

	private void setParams(HttpURLConnection conn, String json) throws Exception {
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", Integer.toString(json.getBytes().length));
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("User-Agent", "osu!scores"); // Discord dislikes default java user-agent
	}

	public HttpResponse postMessage(String content) throws Exception {
		String json = "{\"content\": \"" + content + "\"}";
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		setParams(conn, json);
		OutputStream os = conn.getOutputStream();
		os.write(json.getBytes());
		os.close();
		return new HttpResponse(conn.getResponseCode(), conn.getResponseMessage());
	}

	public void postScore(Score score) {
		String msg = stringifyScore(score);
		try {
			postMessage(msg);
		} catch(Exception e) {
			System.out.println("Failed to post a score");
			e.printStackTrace();
		}
	}

	public static String stringifyScore(Score score) {
		String fcmisssb = (score.misses+score.sb == 0 ? "FC" : score.misses + "miss+" + score.sb + "sb");
		String cover = score.cover;
		cover = cover.substring(0, cover.indexOf("?"));
		// Flag Player | Artist - Song [Diff] %acc +MODS FC/miss+sb 0000pp maxcombo scorelink
		return
			":flag_" + score.flag.toLowerCase() + ": " +
			score.player + " | " +
			score.artist + " - " +
			score.song + " [" +
			score.diff + "] %" +
			score.acc + " +" +
			(score.mods == "" ? "NM" : score.mods )+ " " +
			fcmisssb + " " +
			score.pp + "pp " +
			score.maxcombo + "x " +
			"https://osu.ppy.sh/scores/osu/" + score.scoreid + " " +
			cover;
	}
}
