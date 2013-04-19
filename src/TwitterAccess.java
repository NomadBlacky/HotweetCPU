import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;



public class TwitterAccess {

	// Consumer key
	private static final String CONSUMER_KEY = "PF8CZAMRTjB0pFDYDE3Fdg";
	// Consumer select
	private static final String CONSUMER_SELECT = "N5jU1wGJLa0IJiHKgDNMe0Ydp1Ig2BqHZpQpHVl264";

	Twitter twitter;

	/**
	 * Twitterインスタンスを取得
	 */
	public TwitterAccess() {
		twitter = TwitterFactory.getSingleton();
	}

	public void setAccount() throws TwitterException, IOException {

		// OAuth用の設定
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SELECT);
		// リクエストトークンを取得
		RequestToken requestToken = twitter.getOAuthRequestToken();

		// OAuthでログイン
		this.loginOAuth(requestToken);

	}

	private AccessToken loginOAuth(RequestToken requestToken) throws TwitterException, IOException {

		// OSデフォルトのブラウザを起動
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(URI.create(requestToken.getAuthorizationURL()));

		// pin入力ウィンドウを表示
		String pin = JOptionPane.showInputDialog("暗証番号を入力して下さい");
		if (pin == null) {
			throw new TwitterException("暗証番号の入力がキャンセルされました");
		}
		// 空白除去
		pin = pin.trim();

		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
//		System.out.println("AccessToken\t" + accessToken);
//		System.out.println(e.getStatusCode()); //拒否されると401

		return accessToken;
	}

	/**
	 * TwitterにPOST
	 * @param message
	 * @throws TwitterException
	 */
	public void tweet(String message) throws TwitterException {

		// 140文字以上なら文字を削る
		if(message.length() > 140) {
			message = message.substring(0, 140);
		}
		twitter.updateStatus(message);
	}
}
