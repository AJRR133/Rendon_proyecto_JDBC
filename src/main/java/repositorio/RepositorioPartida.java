package repositorio;

import modelo.Partida;
import modelo.Jugador;
import modelo.Resultado;
import utiles.MySqlConector;
import exceptions.MiExcepcion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPartida {

    private MySqlConector mysqlConector;
    private static final int MAX_PARTIDAS = 5;

    public RepositorioPartida() throws MiExcepcion {
        this.mysqlConector = new MySqlConector();
    }

    // ---------------- CREATE ----------------
    public void agregarPartida(Partida partida) throws SQLException, MiExcepcion {
        if (contarPartidas() >= MAX_PARTIDAS) {
            throw new MiExcepcion("No se pueden agregar más de " + MAX_PARTIDAS + " partidas");
        }
        _insertarPartida(partida);
    }

    // Método interno para insertar
    private void _insertarPartida(Partida partida) throws SQLException {
        String sql = "INSERT INTO partidas(narrador_id, fecha, resultado) VALUES (?,?,?)";
        try (PreparedStatement ps = mysqlConector.getConnect()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, partida.getNarrador().getId());
            ps.setString(2, partida.getFecha());
            ps.setString(3, partida.getResultado() != null ? partida.getResultado().name() : "ALGUNOS");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                partida.setId(rs.getInt(1));
            }
        }
    }

    // ---------------- READ ----------------
    public Partida obtenerPartidaPorId(int id, RepositorioJugadores repoJugadores) throws SQLException {
        String sql = "SELECT * FROM partidas WHERE id=?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Jugador narrador = repoJugadores.obtenerJugadorPorId(rs.getInt("narrador_id"));
                Resultado resultado = _parseResultado(rs.getString("resultado"));
                return new Partida(rs.getInt("id"), narrador, rs.getString("fecha"), resultado);
            }
        }
        return null;
    }

    public List<Partida> listarPartidas(RepositorioJugadores repoJugadores) throws SQLException {
        List<Partida> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas ORDER BY fecha";
        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                partidas.add(obtenerPartidaPorId(rs.getInt("id"), repoJugadores));
            }
        }
        return partidas;
    }

    public int contarPartidas() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM partidas";
        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // ---------------- UPDATE ----------------
    public void actualizarPartida(Partida partida) throws SQLException {
        String sql = "UPDATE partidas SET narrador_id=?, fecha=?, resultado=? WHERE id=?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, partida.getNarrador().getId());
            ps.setString(2, partida.getFecha());
            ps.setString(3, partida.getResultado() != null ? partida.getResultado().name() : "ALGUNOS");
            ps.setInt(4, partida.getId());
            ps.executeUpdate();
        }
    }

    // ---------------- DELETE ----------------
    public void eliminarPartida(int id) throws SQLException {
        String sql = "DELETE FROM partidas WHERE id=?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------------- UTILIDADES ----------------
    private Resultado _parseResultado(String resultadoStr) {
        if (resultadoStr == null) return null;
        try {
            return Resultado.valueOf(resultadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Valor inválido de resultado en BD: " + resultadoStr + " → se asigna null");
            return null;
        }
    }
}
