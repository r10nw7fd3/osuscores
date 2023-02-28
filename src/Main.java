package snr1s.osuscores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static snr1s.osuscores.Util.*;
import static snr1s.osuscores.DiscordHook.stringifyScore;

public class Main {
	private static String CREDENTIALS_DISCORD;
	private static int CREDENTIALS_OSU_ID;
	private static String CREDENTIALS_OSU_SECRET;

	private static void loadCreds(String filename) throws IOException {
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes, 0, bytes.length);
		String[] creds = new String(bytes).split("\\n");
		CREDENTIALS_DISCORD = creds[0];
		CREDENTIALS_OSU_ID = Integer.parseInt(creds[1]);
		CREDENTIALS_OSU_SECRET = creds[2];
		fis.close();
	}

	public static void main(String[] args) throws Exception {
		Args argsObj = new Args(args);
		System.out.println("Re-run with \"-h\" to see available arguments");
		try {
			loadCreds("credentials.txt");
		} catch(Exception e) {
			System.out.println("Failed to load credentials");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Discord: " + CREDENTIALS_DISCORD + "\nosu! id: " + CREDENTIALS_OSU_ID + "\nosu! secret: " + CREDENTIALS_OSU_SECRET);
		AccessToken token = new AccessToken(CREDENTIALS_OSU_ID, CREDENTIALS_OSU_SECRET);

		DiscordHook discord = null;
		try {
			discord = new DiscordHook(CREDENTIALS_DISCORD);
		} catch(Exception e) {
			System.out.println("Failed to construct DiscordHook");
			e.printStackTrace();
			System.exit(1);
		}

		new RankingProcessor(token, discord, argsObj.delay, argsObj.page).process();

		// No need to do this as process loop is infinite. Maybe in future we will have a control panel?
		System.out.print("Removing token: ");
		try {
			System.out.println(token.remove().code);
		} catch(Exception e) {
			System.out.println("Failed to remove access token");
			e.printStackTrace();
		}
	}
}
