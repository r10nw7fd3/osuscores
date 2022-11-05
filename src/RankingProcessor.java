package snr1s.osuscores;

import java.util.ArrayList;
import java.util.Arrays;
import static snr1s.osuscores.Util.OsuGameMode;

public class RankingProcessor {
	private static AccessToken token;

	private static ArrayList<Score> query(long last) {
		RankingOsuApiRequest rankings = new RankingOsuApiRequest(token.get(), OsuGameMode.MODE_STANDARD);
		System.out.println("Pulling rankings");
		try {
			rankings.connect();
		} catch(Exception e) {
			System.out.println("Failed to pull rankings");
			e.printStackTrace();
			return null;
		}
		int[] ids = rankings.getRanking();

		ArrayList<Score> scoresList = new ArrayList<>();
		UserRecentScoresOsuApiRequest recent = null;
		for(int id : ids) {
			recent = new UserRecentScoresOsuApiRequest(token.get(), id, OsuGameMode.MODE_STANDARD, 0); // TODO: idk fix this
			System.out.println("Pulling scores for " + id);
			try {
				recent.connect();
			} catch(Exception e) {
				System.out.println("Failed to pull recents");
				e.printStackTrace();
				return null;
			}
			
			Score[] scores = recent.getScores();
			for(Score score : scores) {
				if(score.pp >= 800 && score.time >= last)
					scoresList.add(score);
			}
		}
		return scoresList;
	}

	private static void sleep() throws Exception {
		Thread.sleep(Main.SLEEP_MILLIS);
	}

	private static void log(ArrayList<Score> scores) {
		if(scores.size() == 0)
			return;
		System.out.println("");
		for(Score score : scores)
			System.out.println(DiscordHook.stringifyScore(score));
		System.out.println("");
	}

	public static void process(AccessToken token_, DiscordHook disc) throws Exception {
		token = token_;
		long lastUpdate = System.currentTimeMillis();

		while(true) {
			// Query scores
			ArrayList<Score> scores = query(lastUpdate);
			if(scores == null) { // Try again after sleeping
				lastUpdate = System.currentTimeMillis();
				sleep();
				continue;
			}

			// Log scores
			log(scores);

			// Call discord hook
			disc.postScores(scores);

			// Sleep
			lastUpdate = System.currentTimeMillis();
			sleep();
		}
	}
}
