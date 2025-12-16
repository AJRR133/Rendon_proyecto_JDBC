package repositorio;

import modelo.Partida;
import modelo.Jugador;
import modelo.Resultado;
import utiles.MySqlConector;
import exceptions.MiExcepcion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPartida {

    private static final Logger logger = LogManager.getLogger(RepositorioPartida.class);
    private MySqlConector mysqlConector;
    private static final int MAX_PARTIDAS = 5;

    public RepositorioPartida() throws MiExcepcion {
        this.mysqlConector = new MySqlConector();
    }

    public void agregarPartida(Partida partida) throws SQLException, MiExcepcion {
        if (contarPartidas() >= MAX_PARTIDAS) {
            logger.warn("No se pueden agregar más de {} partidas", MAX_PARTIDAS);
            throw new MiExcepcion("No se pueden agregar más de " + MAX_PARTIDAS + " partidas");
        }
        insertarPartida(partida);
    }

    private void insertarPartida(Partida partida) throws SQLException {
        String sql = "INSERT INTO partidas(narrador_id, fecha, resultado) VALUES (?,?,?)";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, partida.getNarrador().getId());
            ps.setString(2, partida.getFecha());
            ps.setString(3, partida.getResultado() != null ? partida.getResultado().name() : "ALGUNOS");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) partida.setId(rs.getInt(1));

            logger.info("Partida agregada: {} | Narrador: {}", partida.getFecha(), partida.getNarrador().getNombre());
        } catch (SQLException e) {
            logger.error("Error al agregar partida: {}", e.getMessage());
            throw e;
        }
    }

    public List<Partida> listarPartidas(RepositorioJugadores repoJugadores) throws SQLException {
        List<Partida> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas ORDER BY fecha";
        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                partidas.add(obtenerPartidaPorId(rs.getInt("id"), repoJugadores));
            }
        } catch (SQLException e) {
            logger.error("Error al listar partidas: {}", e.getMessage());
            throw e;
        }
        return partidas;
    }

    public Partida obtenerPartidaPorId(int id, RepositorioJugadores repoJugadores) throws SQLException {
        Partida partida = null;
        String sql = "SELECT * FROM partidas WHERE id=?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Jugador narrador = repoJugadores.obtenerJugadorPorId(rs.getInt("narrador_id"));
                Resultado resultado = parseResultado(rs.getString("resultado"));
                partida = new Partida(rs.getInt("id"), narrador, rs.getString("fecha"), resultado);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener partida con id {}: {}", id, e.getMessage());
            throw e;
        }
        return partida;
    }

    public int contarPartidas() throws SQLException {
        int total = 0;
        String sql = "SELECT COUNT(*) as total FROM partidas";
        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) total = rs.getInt("total");
        } catch (SQLException e) {
            logger.error("Error al contar partidas: {}", e.getMessage());
            throw e;
        }
        return total;
    }

    public void actualizarPartida(Partida partida) throws SQLException {
        String sql = "UPDATE partidas SET narrador_id=?, fecha=?, resultado=? WHERE id=?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, partida.getNarrador().getId());
            ps.setString(2, partida.getFecha());
            ps.setString(3, partida.getResultado() != null ? partida.getResultado().name() : "ALGUNOS");
            ps.setInt(4, partida.getId());
            ps.executeUpdate();
            logger.info("Partida actualizada: {}", partida.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar partida {}: {}", partida.getId(), e.getMessage());
            throw e;
        }
    }

    public void eliminarPartida(int id) throws SQLException {
        String sql = "DELETE FROM partidas WHERE id=?";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Partida eliminada: {}", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar partida {}: {}", id, e.getMessage());
            throw e;
        }
    }

    private Resultado parseResultado(String resultadoStr) {
        if (resultadoStr == null) return null;
        try {
            return Resultado.valueOf(resultadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Valor inválido de resultado en BD: {} → se asigna null", resultadoStr);
            return null;
        }
    }
}
