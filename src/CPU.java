<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CPU {

	private Map<String, ArrayList<TemperatureData>> coreMap;

	public CPU() throws IOException, InterruptedException {
		this.init();
	}

	public void init() {
		coreMap = new HashMap<>();
	}

	public void update() throws InterruptedException, IOException {

		/*
		 * パイプやリダイレクトなど、シェルが解釈するものはそのまま、
		 * String[] command = {"ls",">","list"};
		 * とできないので、
		 * $ bash -c "command"
		 * として、間接的に実行する。
		 */

		// $ /bin/bash -c "sensors | grep -e \"Core\""
		String[] command = { "/bin/bash", "-c", "sensors | grep -e \"Core\"" };
		ProcessBuilder pb = new ProcessBuilder(command);

		// プロセスの実行と終了待ち
		Process process = pb.start();
		process.waitFor();

		// コマンドの結果をInputStreamで取得
		InputStream is = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;
		while ((line = br.readLine()) != null) {

			// コマンドの結果を整形する
			String name = line.substring(0, 6); // Core名称
			String str = line.substring(line.indexOf("+"), line.indexOf("°")); // 温度
			double temperature = Double.valueOf(str);
			TemperatureData data = new TemperatureData(temperature);

			if(coreMap.containsKey(name)) {
				// 実行時点の温度を追加
				coreMap.get(name).add(data);
			}
			else {
				// データが存在しなければ新しく追加
				ArrayList<TemperatureData> list = new ArrayList<>();
				list.add(data);
				coreMap.put(name, list);
			}
		}

	}

	public Map<String, ArrayList<TemperatureData>> getMap() {
		return coreMap;
	}

	public ArrayList<TemperatureData> getDatas(String coreName) {
		return coreMap.get(coreName);
	}

	/**
	 * CPU温度の最小値、平均値、最大値を取得する。
	 * @param name
	 * @return
	 */
	public double[] getResult3(String name) {
		ArrayList<TemperatureData> datas = this.getDatas(name);

		double ave, max, min;
		ave = max = min = 0;
		Iterator<TemperatureData> iterator = datas.iterator();

		// 初期化
		if(iterator.hasNext()) {
			ave = max = min = iterator.next().getTemperature();
		}
		while (iterator.hasNext()) {
			double data = iterator.next().getTemperature();
			if(max < data) {
				max = data;
			}
			if(min > data) {
				min = data;
			}
			ave += data;
		}
		ave = ave / datas.size();
		double[] result = {min, ave, max};

		return result;
	}
}
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CPU {

	private Map<String, ArrayList<TemperatureData>> coreMap;

	public CPU() throws IOException, InterruptedException {
		this.init();
	}
	
	public void init() {
		coreMap = new HashMap<>();
	}
	
	public void update() throws InterruptedException, IOException {
		
		/* 
		 * パイプやリダイレクトなど、シェルが解釈するものはそのまま、
		 * String[] command = {"ls",">","list"};
		 * とできないので、
		 * $ bash -c "command"
		 * として、間接的に実行する。
		 */
	
		// $ /bin/bash -c "sensors | grep -e \"Core\""
		String[] command = { "/bin/bash", "-c", "sensors | grep -e \"Core\"" };
		ProcessBuilder pb = new ProcessBuilder(command);
		// プロセスの実行と終了待ち
		Process process = pb.start();
		process.waitFor();

		// コマンドの結果をInputStreamで取得
		InputStream is = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;
		while ((line = br.readLine()) != null) {
			
			// コマンドの結果を整形する
			String name = line.substring(0, 6);
			String str = line.substring(line.indexOf("+"), line.indexOf("°"));
			double temperature = Double.valueOf(str);
			TemperatureData data = new TemperatureData(temperature);

			if(coreMap.containsKey(name)) {
				// 実行時点の温度を追加
				coreMap.get(name).add(data);
			}
			else {
				// データが存在しなければ新しく追加
				ArrayList<TemperatureData> list = new ArrayList<>();
				list.add(data);
				coreMap.put(name, list);
			}
			
		}
		
	}
	
	public Map<String, ArrayList<TemperatureData>> getMap() {
		return coreMap;
	}
	
	public ArrayList<TemperatureData> getDatas(String coreName) {
		return coreMap.get(coreName);
	}

}
>>>>>>> temp1
