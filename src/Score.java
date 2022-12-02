package snr1s.osuscores;

import org.json.JSONObject;
import org.json.JSONArray;
import java.math.BigDecimal;
import java.math.RoundingMode;
import static snr1s.osuscores.Util.getTime;

public class Score {
	public String flag;
	public String player;
	public String artist;
	public String song;
	public String diff;
	public float acc;
	public String mods;
	public int misses;
	public int sb;
	public int pp;
	public int maxcombo;
	public long scoreid;
	public String cover = "";
	public long time;

	public Score(String flag, String player, String artist, String song, String diff, float acc, String mods, int misses, int sb, int pp, int maxcombo, long scoreid) {
		this.flag = flag;
		this.player = player;
		this.artist = artist;
		this.song = song;
		this.diff = diff;
		this.acc = acc;
		this.mods = mods;
		this.misses = misses;
		this.sb = sb;
		this.pp = pp;
		this.maxcombo = maxcombo;
		this.scoreid = scoreid;
	}

	public Score() {}

	public static Score fromJSONObject(JSONObject json) {
		Score newscore = new Score();

		try {

		JSONObject user = json.getJSONObject("user");
		newscore.flag = user.getString("country_code").toLowerCase();
		newscore.player = user.getString("username");

		JSONObject mapset = json.getJSONObject("beatmapset");
		newscore.artist = mapset.getString("artist");
		newscore.song = mapset.getString("title");

		newscore.diff = json.getJSONObject("beatmap").getString("version");

		newscore.acc = new BigDecimal(json.getFloat("accuracy") * 100).setScale(2, RoundingMode.HALF_UP).floatValue();

		JSONArray mods = json.getJSONArray("mods");
		newscore.mods = "";
		for(int i = 0; i < mods.length(); i++)
			newscore.mods += mods.getString(i);

		newscore.misses = json.getJSONObject("statistics").getInt("count_miss");

		newscore.sb = 0; // Broken for now, osu! api does not return sb count


		if(json.get("pp") == JSONObject.NULL)
			newscore.pp = 0;
		else
			newscore.pp = Math.round(json.getFloat("pp"));

		newscore.maxcombo = json.getInt("max_combo");

		if(json.get("best_id") == JSONObject.NULL)
			newscore.scoreid = 0;
		else
			newscore.scoreid = json.getLong("best_id");

		newscore.cover = mapset.getJSONObject("covers").getString("cover");

		newscore.time = getTime(json.getString("created_at"));


		} catch(Exception e) {
			e.printStackTrace();
		}

		return newscore;
	}
}
