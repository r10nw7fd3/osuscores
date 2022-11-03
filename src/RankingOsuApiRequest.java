package snr1s.osuscores;

import org.json.JSONObject;
import org.json.JSONArray;
import static snr1s.osuscores.Util.*;

public class RankingOsuApiRequest extends BaseOsuApiRequest {
	public RankingOsuApiRequest(String token, OsuGameMode mode) {
		super("https://osu.ppy.sh/api/v2/rankings/" + stringifyOsuGameMode(mode) + "/performance", token);
		this.method = "GET";
	}

	public int[] getRanking() {
		JSONArray ranking = new JSONObject(this.out).getJSONArray("ranking");
		int len = ranking.length();

		int[] ids = new int[len];
		for(int i = 0; i < len; i++)
			ids[i] = ranking.getJSONObject(i).getJSONObject("user").getInt("id");

		return ids;
	}
}
