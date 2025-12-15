package utiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnection {
	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			FileInputStream fs = new FileInputStream("src/main/resources/database.properties");
			properties.load(fs);

			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String clave = properties.getProperty("clave");

			DriverManager.getConnection(url, user, clave);
			System.out.println("Conexión exitosa a MySQL en Docker!");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}