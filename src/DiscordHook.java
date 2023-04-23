package snr1s.osuscores;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.util.ArrayList;
import java.text.DecimalFormat;
import snr1s.osuscores.HttpResponse;

public class DiscordHook {
	private URL url;
	private boolean modEmojis;

	public DiscordHook(String url, boolean modEmojis) throws Exception {
		this.url = new URL(url);
		this.modEmojis = modEmojis;
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
		String msg = stringifyScore(score, modEmojis, true); // Should I make flagEmoji a CLI argument?
		try {
			postMessage(msg);
		} catch(Exception e) {
			System.out.println("Failed to post a score");
			e.printStackTrace();
		}
	}

	public static String stringifyScore(Score score, boolean modEmojis, boolean flagEmoji) {
		DecimalFormat df = new DecimalFormat("00.00");

		String fcmisssb = (score.misses+score.sb == 0 ? "FC" : score.misses + "miss+" + score.sb + "sb");
		String cover = score.cover;
		cover = cover.substring(0, cover.indexOf("?"));
		// Flag Player | Artist - Song [Diff] acc% +MODS FC/miss+sb 0000pp scorelink coverlink
		return
			stringifyFlag(score.flag, flagEmoji) + " " +
			score.player + " | " +
			score.artist + " - " +
			score.song + " [" +
			score.diff + "] " +
			df.format(score.acc) + "% +" +
			stringifyMods(score.mods, modEmojis) + " " +
			fcmisssb + " " +
			score.pp + "pp " +
			"https://osu.ppy.sh/scores/osu/" + score.scoreid + " " +
			cover;
	}

	private static String stringifyFlag(String flag, boolean flagEmoji) {
		return flagEmoji ? ":flag_" + flag.toLowerCase() + ":" : flag.toUpperCase();
	}

	private static String stringifyMods(String mods, boolean modEmojis) {
		if(mods.equals(""))
			return "NM";
		if(!modEmojis)
			return mods;

		String res = "";
		int i = 0;
		while(i < mods.length()) {
			res += ":" + mods.substring(i, i + 2).toLowerCase() + ":";
			i += 2;
		}
		return res;
	}
}
