package repositorio;

import modelo.Jugador;
import utiles.MySqlConector;
import exceptions.MiExcepcion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioJugadores {

    private MySqlConector mysqlConector;

    public RepositorioJugadores() throws MiExcepcion {
        this.mysqlConector = new MySqlConector();
    }

    public void agregarJugador(Jugador jugador) throws SQLException {
        String sql = "INSERT INTO jugadores(nombre,email,puntosTotales) VALUES (?,?,?)";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, jugador.getNombre());
            ps.setString(2, jugador.getEmail());
            ps.setInt(3, jugador.getPuntostotales());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                jugador.setId(rs.getInt(1));
            }
        }
    }

    public List<Jugador> listarJugadores() throws SQLException {
        List<Jugador> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM jugadores ORDER BY puntosTotales DESC";
        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                jugadores.add(new Jugador(
                        rs.getInt("id"),
                        rs.getInt("puntosTotales"),
                        rs.getString("email"),
                        rs.getString("nombre")
                ));
            }
        }
        return jugadores;
    }

    public void actualizarPuntos(int jugadorId, int puntos) throws SQLException {
        String sql = "UPDATE jugadores SET puntosTotales = puntosTotales + ? WHERE id = ?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, puntos);
            ps.setInt(2, jugadorId);
            ps.executeUpdate();
        }
    }

    public Jugador obtenerJugadorPorId(int id) throws SQLException {
        String sql = "SELECT * FROM jugadores WHERE id = ?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Jugador(
                        rs.getInt("id"),
                        rs.getInt("puntosTotales"),
                        rs.getString("email"),
                        rs.getString("nombre")
                );
            }
        }
        return null;
    }
}
