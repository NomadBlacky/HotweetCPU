import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;



public class PostingTweet {
	
	public static int GUI_MODE = 0;
	public static int CHARACTER_MODE = 1;
	
	private static final String CONSUMER_KEY = "PF8CZAMRTjB0pFDYDE3Fdg";
	private static final String CONSUMER_SELECT = "N5jU1wGJLa0IJiHKgDNMe0Ydp1Ig2BqHZpQpHVl264";
	
	Twitter twitter;

	public PostingTweet() {
		twitter = TwitterFactory.getSingleton();
	}
	
	public void setAccount(int mode) throws TwitterException, IOException {
		
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SELECT);
		RequestToken requestToken = twitter.getOAuthRequestToken();
		
		if(mode == GUI_MODE) {
			this.guiMode(requestToken);
		}
		else {
			this.characterMode(requestToken);
		}
		
	}
	
	private AccessToken guiMode(RequestToken requestToken) throws IOException, TwitterException {
		
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(URI.create(requestToken.getAuthorizationURL()));
		
		String pin = JOptionPane.showInputDialog("暗証番号を入力して下さい");
		if (pin == null) {
			throw new TwitterException("暗証番号の入力がキャンセルされました");
		}
		pin = pin.trim();

		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
//		System.out.println("AccessToken\t" + accessToken);
//		System.out.println(e.getStatusCode()); //拒否されると401
		
		return accessToken;
	}
	
	private AccessToken characterMode(RequestToken requestToken) {
		return null;
	}
	
	public void tweet(String message) throws TwitterException {
		
		twitter.updateStatus(message);
	}
}
