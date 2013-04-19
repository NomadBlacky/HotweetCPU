
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import twitter4j.TwitterException;



public class HotweetCPU {

	public static void printAll(CPU cpu) {

		// Mapからiteratorを取得
		Map<String, ArrayList<TemperatureData>> map = cpu.getMap();
		Set<Entry<String, ArrayList<TemperatureData>>> set = map.entrySet();
		Iterator<Entry<String, ArrayList<TemperatureData>>> iterator = set.iterator();

		while(iterator.hasNext()) {
			Entry<String, ArrayList<TemperatureData>> entry = iterator.next();
			String key = entry.getKey();
			System.out.printf("========== %s ==========\n", key);
			Iterator<TemperatureData> iterator2 = entry.getValue().iterator();
			while (iterator2.hasNext()) {
				TemperatureData data = iterator2.next();
				System.out.println(data.getDate() + " : " + data.getTemperature() + "°C");
			}
			System.out.println('\n');
		}
		System.out.println("============================\n");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			TwitterAccess twitter = new TwitterAccess();
			twitter.setAccount();

			Scheduling scheduling = new Scheduling(twitter);
			scheduling.setTweet_min(1);
			Thread measurement = new Thread(scheduling);
			measurement.start();

		} catch (IOException e) {

		} catch (InterruptedException e) {

		} catch (TwitterException e) {

		}
	}
}
