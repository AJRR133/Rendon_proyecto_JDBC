package modelo;

import java.util.Objects;

public class Partida {
	private int id;
	private Jugador narrador;
	private String fecha;
	private Resultado resultado;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Jugador getNarrador() {
		return narrador;
	}
	public void setNarrador(Jugador narrador) {
		this.narrador = narrador;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Resultado getResultado() {
		return resultado;
	}
	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
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
		Partida other = (Partida) obj;
		return id == other.id;
	}
	public Partida(int id, Jugador narrador, String fecha, Resultado resultado) {
		super();
		this.id = id;
		this.narrador = narrador;
		this.fecha = fecha;
		this.resultado = resultado;
	}

	
	
}
