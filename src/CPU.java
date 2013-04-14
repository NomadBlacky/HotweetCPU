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
