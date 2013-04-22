import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test {

	public static void main(String[] args) throws Exception {

		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported!");
			return;
		}
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage("test2.png");

		PopupMenu popup = new PopupMenu();
		MenuItem defaultItem = new MenuItem("mustang");
		defaultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				System.out.println("popup action");
			}
		});
		popup.add(defaultItem);

		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo", popup);
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				System.out.println("trayIcon action");
			}
		});
		tray.add(trayIcon);

	}

}
