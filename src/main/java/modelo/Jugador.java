package modelo;
import java.util.Objects;

public class Jugador {
	private int id, puntostotales;
	private String nombre,email;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPuntostotales() {
		return puntostotales;
	}
	public void setPuntostotales(int puntostotales) {
		this.puntostotales = puntostotales;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jugador other = (Jugador) obj;
		return id == other.id;
	}
	@Override
	public String toString() {
		return "Jugador [id=" + id + ", puntostotales=" + puntostotales + ", nombre=" + nombre + ", email=" + email
				+ "]";
	}
	public Jugador(int id, int puntostotales, String nombre, String email) {
		super();
		this.id = id;
		this.puntostotales = puntostotales;
		this.nombre = nombre;
		this.email = email;
	}
	public Jugador() {
		super();
	}
	
	
}
