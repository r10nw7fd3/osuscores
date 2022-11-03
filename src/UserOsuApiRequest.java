package snr1s.osuscores;

import static snr1s.osuscores.Util.*;

public class UserOsuApiRequest extends BaseOsuApiRequest {
	public UserOsuApiRequest(String token, String nameOrId, String type, OsuGameMode mode) {
		super("https://osu.ppy.sh/api/v2/users/" + nameOrId + "/" + stringifyOsuGameMode(mode) + "?key=" + type, token);
		this.method = "GET";
	}
}
