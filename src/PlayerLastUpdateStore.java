package snr1s.osuscores;

import java.util.HashMap;

public class PlayerLastUpdateStore {
	private HashMap<Integer, Long> players;	

	public PlayerLastUpdateStore() {
		players = new HashMap<>();
	}

	private long time() {
		return System.currentTimeMillis();
	}

	// TODO: Purge players who fell out of the leaderboard
	public long getTime(int id) {
		if(players.get(id) == null)
			players.put(id, time());

		return players.get(id);
	}

	public void setTime(int id) {
		players.replace(id, time());
	}
}
