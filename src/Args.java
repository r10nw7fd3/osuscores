package snr1s.osuscores;

public class Args {
	public int page = 1;
	public int delay = 5000 * 60;

	public Args(String[] args) {
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-h") || args[i].equals("--help") || args[i].equals("-help")) {
				help();
				System.exit(0);
			}
			else if(args[i].equals("-p")) {
				i++;
				if(i == args.length)
					noValForArg("-p");

				this.page = argVal("-p", args[i]);
				if(this.page <= 0)
					invValForArg("-d");
			}
			else if(args[i].equals("-d")) {
				i++;
				if(i == args.length)
					noValForArg("-d");
				this.delay = argVal("-d", args[i]);
				if(this.delay <= 0)
					invValForArg("-d");
			}
		}
	}

	private static void help() {
		System.out.println("Options:");
		System.out.println("  -p <page>  Leaderboard page. Default: 1");
		System.out.println("  -d <delay> Delay in millis between api hits. Default: 300000");
	}

	private static int argVal(String arg, String val) {
		try {
			return Integer.valueOf(val);
		} catch(NumberFormatException e) {
			invValForArg(arg);
		}
		return 0;
	}

	private static void noValForArg(String arg) {
		System.out.println("No value for argument \"" + arg +"\"");
		System.exit(1);
	}

	private static void invValForArg(String arg) {
		System.out.println("Invalid value for argument \"" + arg + "\"");
		System.exit(1);
	}
}
