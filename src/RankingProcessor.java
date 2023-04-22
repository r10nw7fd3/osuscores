package snr1s.osuscores;

import java.util.ArrayList;
import java.util.Arrays;
import static snr1s.osuscores.Util.OsuGameMode;

public class RankingProcessor {
	private AccessToken token;
	private DiscordHook disc;
	private int sleepTime;
	private int page;

	public RankingProcessor(AccessToken token, DiscordHook disc, int sleepTime, int page) {
		this.token = token;
		this.disc = disc;
		this.sleepTime = sleepTime;
		this.page = page;
	}

	private void query(PlayerLastUpdateStore plus) {
		long start = System.currentTimeMillis();
		RankingOsuApiRequest rankings = new RankingOsuApiRequest(token.get(), OsuGameMode.MODE_STANDARD, this.page);
		System.out.println("Pulling rankings");
		try {
			rankings.connect();
		} catch(Exception e) {
			System.out.println("Failed to pull rankings");
			e.printStackTrace();
			return;
		}
		int[] ids = rankings.getRanking();

		UserRecentScoresOsuApiRequest recent = null;
		for(int i = 0; i < ids.length; i++) {
			recent = new UserRecentScoresOsuApiRequest(token.get(), ids[i], OsuGameMode.MODE_STANDARD, 0); // TODO: idk fix this
			System.out.println("Pulling scores for " + ids[i] + " (" + (i+1) + "/" + ids.length + ")");
			try {
				recent.connect();
			} catch(Exception e) {
				System.out.println("Failed to pull recents");
				e.printStackTrace();
				return;
			}
			
			Score[] scores = recent.getScores();
			long lu = plus.getTime(ids[i]);
			for(Score score : scores) {
				if(score.pp >= 800 && score.time >= lu) {
					log(score);
					disc.postScore(score);
				}
			}
			plus.setTime(ids[i]);
		}
		System.out.println("Done. Took " + (System.currentTimeMillis() - start) + "ms");
	}

	private void log(Score score) {
		System.out.println(DiscordHook.stringifyScore(score, false, false));
	}

	private void sleep() throws Exception {
		Thread.sleep(sleepTime);
	}

	public void process() throws Exception {
		PlayerLastUpdateStore plus = new PlayerLastUpdateStore();

		while(true) {
			query(plus);
			sleep();
		}
	}
}
