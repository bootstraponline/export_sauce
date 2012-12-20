package bootstraponline.export_sauce;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public final class SauceKey {
	private String key = null;
	private String user = null;
	private String url = null;

	public static String readFile(final String fileName) {
		try {
			final File key = new File(fileName);
			final byte[] data = new byte[(int) key.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(key));
			dis.readFully(data);
			dis.close();

			return new String(data).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Reads in user from user.key in repository root.
	 * http://username:---@ondemand.saucelabs.com:80/wd/hub
	 **/
	public String user() {
		if (user == null) {
			user = readFile("user.key");

			if (user == null) {
				// If it's still null, that's a problem.
				throw new RuntimeException("Unable to read user.key.");
			}
		}

		return user;
	}

	/**
	 * Reads in key from sauce.key in repository root.
	 * http://username:---@ondemand.saucelabs.com:80/wd/hub
	 **/
	public String key() {
		if (key == null) {
			key = readFile("sauce.key");

			if (key == null) {
				// If it's still null, that's a problem.
				throw new RuntimeException("Unable to read user.key.");
			}
		}

		return key;
	}

	/** Full URL to sauce. **/
	public String jobsURL() {
		if (url != null) {
			return url;
		}

		if (key == null) {
			key();
		}

		if (user == null) {
			user();
		}

		url = "https://" + user + ":" + key
				+ "@saucelabs.com/rest/v1/" + user + "/jobs?full=true";
		
		return url;
	}
}
