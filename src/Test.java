import java.io.IOException;
import java.util.Date;

import twitter4j.TwitterException;


public class Test {

	/**
	 * @param args
	 * @throws IOException
	 * @throws TwitterException
	 */
	public static void main(String[] args) throws TwitterException, IOException {

		TwitterAccess posting = new TwitterAccess();
		posting.setAccount();

		posting.tweet("tweet test : " + new Date());
	}

}
