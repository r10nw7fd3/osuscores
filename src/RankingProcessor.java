package snr1s.osuscores;

import java.util.ArrayList;
import java.util.Arrays;
import static snr1s.osuscores.Util.OsuGameMode;

public class RankingProcessor {
	private AccessToken token;
	private DiscordHook disc;
	private int sleepTime;

	public RankingProcessor(AccessToken token, DiscordHook disc, int sleepTime) {
		this.token = token;
		this.disc = disc;
		this.sleepTime = sleepTime;
	}

	private ArrayList<Score> query(PlayerLastUpdateStore plus) {
		long start = System.currentTimeMillis();
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
		for(int i = 0; i < ids.length; i++) {
			recent = new UserRecentScoresOsuApiRequest(token.get(), ids[i], OsuGameMode.MODE_STANDARD, 0); // TODO: idk fix this
			System.out.println("Pulling scores for " + ids[i] + " (" + (i+1) + "/" + ids.length + ")");
			try {
				recent.connect();
			} catch(Exception e) {
				System.out.println("Failed to pull recents");
				e.printStackTrace();
				return null;
			}
			
			Score[] scores = recent.getScores();
			long lu = plus.getTime(ids[i]);
			for(Score score : scores) {
				if(score.pp >= 800 && score.time >= lu) 
					scoresList.add(score);
			}
			plus.setTime(ids[i]);
		}
		System.out.println("Done. Took " + (System.currentTimeMillis() - start) + "ms");
		return scoresList;
	}

	private void sleep() throws Exception {
		Thread.sleep(sleepTime);
	}

	private void log(ArrayList<Score> scores) {
		if(scores.size() == 0)
			return;
		System.out.println("");
		for(Score score : scores)
			System.out.println(DiscordHook.stringifyScore(score));
		System.out.println("");
	}

	public void process() throws Exception {
		PlayerLastUpdateStore plus = new PlayerLastUpdateStore();

		while(true) {
			// Query scores
			ArrayList<Score> scores = query(plus);
			if(scores == null) { // Try again after sleeping
				sleep();
				continue;
			}

			// Log scores
			log(scores);

			// Call discord hook
			disc.postScores(scores);

			// Sleep
			sleep();
		}
	}
}
