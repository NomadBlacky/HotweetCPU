import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import twitter4j.TwitterException;


public class Scheduling implements Runnable {

	private CPU cpu;
	private TwitterAccess twitter;
	private int tweet_min;
	private int update_sec;
	private boolean running;

	public Scheduling() throws InterruptedException, TwitterException, IOException {
		cpu = new CPU();
		twitter = new TwitterAccess();
		twitter.setAccount();
		running = true;
		this.setDefault();
	}

	public Scheduling(TwitterAccess access) throws IOException, InterruptedException {
		cpu = new CPU();
		twitter = access;
		running = true;
		this.setDefault();
	}

	/**
	 * 定期ツイートする間隔（分）、CPU温度を取得する間隔（秒）を設定
	 * @param cpu
	 * @param tweet(min)
	 * @param update(sec)
	 */
	public Scheduling(CPU c, TwitterAccess access) {
		cpu = c;
		twitter = access;
		running = true;
		this.setDefault();
	}

	public void setDefault() {
		tweet_min = 60;
		update_sec = 1;
	}
	public int getTweet_min() {
		return tweet_min;
	}

	public void setTweet_min(int tweet_min) {
		this.tweet_min = tweet_min;
	}

	public int getUpdate_sec() {
		return update_sec;
	}

	public void setUpdate_sec(int update_sec) {
		this.update_sec = update_sec;
	}

	@Override
	public void run() {

		try {

			try {
				// 計測開始のツイート
				twitter.tweet("【自動】 CPU温度の計測を開始しました。(" + new Date() + ")");
			} catch(TwitterException e) {
				System.err.println("Tweet failed");
			}

			while (true) {
				Calendar startTime = Calendar.getInstance();
				Calendar endTime = Calendar.getInstance();
				endTime.add(Calendar.MINUTE, tweet_min);

				// ツイート時間を過ぎるまで繰り返す
				while(endTime.after(Calendar.getInstance())) {

					// CPU温度を更新
					cpu.update();
					// 更新待機(秒)
					Thread.sleep(update_sec * 1000);

					// stopRun()で抜ける
					if(running == false) {
						return;
					}
				}

				try {
					// 計測結果をツイート
					this.tweet(startTime, endTime);
				} catch (TwitterException e) {
					System.err.println("Tweet failed");
				}

			}

		} catch (InterruptedException e) {
			System.err.println("Interrupted");
		} catch (IOException e) {
			System.err.println("I/O Exception");
		}

	}

	/**
	 * スレッドの終了
	 */
	public void stopRun() {
		running = false;
	}

	private void tweet(Calendar st, Calendar end) throws TwitterException {

		String message = "";

		// 計測開始時刻と終了時刻を取得
		String stTime =
				String.format("%02d", st.get(Calendar.HOUR_OF_DAY)) + ":" +
				String.format("%02d", st.get(Calendar.MINUTE));
		String endTime =
				String.format("%02d", end.get(Calendar.HOUR_OF_DAY)) + ":" +
				String.format("%02d", end.get(Calendar.MINUTE));

		// 時刻を追記
		message = message.concat(String.format("[result:%s~%s]\n", stTime, endTime));

		// Mapからiteratorを取得
		Map<String, ArrayList<TemperatureData>> map = cpu.getMap();
		Set<Entry<String, ArrayList<TemperatureData>>> set = map.entrySet();
		Iterator<Entry<String, ArrayList<TemperatureData>>> iterator = set.iterator();

		while(iterator.hasNext()) {

			Entry<String, ArrayList<TemperatureData>> entry = iterator.next();
			String name = entry.getKey();
			// Core名を追記
			message = message.concat(name + " => ");

			// min,ave,max を取得
			double[] result = cpu.getResult3(name);
			// 値を追記
			message = message.concat(String.format("min:%.2f°C ave:%.2f°C max:%.2f°C\n",
					result[0], result[1], result[2]));
		}

		// 結果をツイート
		twitter.tweet(message);
	}

}
