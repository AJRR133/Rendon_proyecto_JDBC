package utiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import exceptions.MiExcepcion;

public class MySqlConector {
	private Connection connect;
	private String url;
	private String user;
	private String clave;

	public MySqlConector() throws MiExcepcion {
		try {
			Properties properties = new Properties();
			FileInputStream fs = new FileInputStream("src/main/resources/database.properties");
			properties.load(fs);

			this.url = properties.getProperty("url");
			this.user = properties.getProperty("user");
			this.clave = properties.getProperty("clave");
			
			this.connect = DriverManager.getConnection(this.url, this.user, this.clave);
			
		} catch (IOException e) {
			throw new MiExcepcion("Error al conectar a la base de datos" + e.getMessage());
		} catch (SQLException e) {
			throw new MiExcepcion("Error al conectar a la base de datos" + e.getMessage());
		}
	}

	public Connection getConnect() {
		return connect;
	}

	public void release() {
		try {
			System.out.print("--- CERRANDO CONEXION ---");
			if (this.connect != null)
				this.connect.close();
			this.connect = null;
			this.url = null;
			this.user = null;
			this.clave = null;

		} catch (SQLException e) {
			System.err.println("No se ha podido cerrar la conexion con la BD");
			e.printStackTrace();
		}
	}
}