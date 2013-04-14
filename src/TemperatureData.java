import java.util.Date;


public class TemperatureData {

	private Date date;
	private double temperature;
	
	public TemperatureData(double temp) {
		date = new Date();
		temperature = temp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	
}
