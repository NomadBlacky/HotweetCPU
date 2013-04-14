import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



public class HotweetCPU {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			CPU cpu = new CPU();
			
			for(int i = 0; i < 60; i++) {
				cpu.update();
				Thread.sleep(1000);
			}
			
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
			
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
