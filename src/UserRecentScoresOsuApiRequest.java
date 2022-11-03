package snr1s.osuscores;

import org.json.JSONArray;
import static snr1s.osuscores.Util.*;

public class UserRecentScoresOsuApiRequest extends BaseOsuApiRequest {
	public UserRecentScoresOsuApiRequest(String token, int userid, OsuGameMode mode, int offset) {
		super("https://osu.ppy.sh/api/v2/users/" + userid + "/scores/recent?mode=" + stringifyOsuGameMode(mode) + "&offset=" + offset, token);
		this.method = "GET";
	}

	public Score[] getScores() {
		JSONArray arr = new JSONArray(this.out);
		int len = arr.length();

		Score[] scores = new Score[len];
		for(int i = 0; i < len; i++)
			scores[i] = Score.fromJSONObject(arr.getJSONObject(i));

		return scores;
	}

}
