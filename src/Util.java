package snr1s.osuscores;

import java.time.OffsetDateTime;

public class Util {
	public static enum OsuGameMode {
		MODE_STANDARD,
		MODE_TAIKO,
		MODE_CATCH,
		MODE_MANIA
	}

	public static String stringifyOsuGameMode(OsuGameMode mode) {
		if(mode == OsuGameMode.MODE_STANDARD)
			return "osu";
		if(mode == OsuGameMode.MODE_TAIKO)
			return "taiko";
		if(mode == OsuGameMode.MODE_MANIA)
			return "mania";
		if(mode == OsuGameMode.MODE_CATCH)
			return "fruits";
		return null;
	}

	public static long getTime(String isoDate) {
		// ISO 8601 parsing example
		//String date = "2022-10-19T05:55:13+00:00";
		//java.time.OffsetDateTime odt = java.time.OffsetDateTime.parse(date);
		//System.out.println("Date: " + odt.toInstant().toEpochMilli());
		//System.out.println("Current: " + System.currentTimeMillis());

		OffsetDateTime odt = OffsetDateTime.parse(isoDate); // afaik this can break because osu api sometimes returns date with 'Z' at the and instead of +00:00
		return odt.toInstant().toEpochMilli();
	}
}
