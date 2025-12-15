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

    public RepositorioPartida() throws MiExcepcion {
        this.mysqlConector = new MySqlConector();
    }

    // Agregar partida a la BD
    public void agregarPartida(Partida partida) throws SQLException {
        String sql = "INSERT INTO partidas(narrador_id, fecha, resultado) VALUES (?,?,?)";
        try (PreparedStatement ps = mysqlConector.getConnect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, partida.getNarrador().getId());
            ps.setString(2, partida.getFecha());
            ps.setString(3, partida.getResultado() != null ? partida.getResultado().name() : "ALGUNOS");
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                partida.setId(rs.getInt(1));
            }
        }
    }

    // Listar todas las partidas de la BD
    public List<Partida> listarPartidas(RepositorioJugadores repoJugadores) throws SQLException {
        List<Partida> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas ORDER BY fecha";

        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Obtener narrador
                Jugador narrador = repoJugadores.obtenerJugadorPorId(rs.getInt("narrador_id"));

                // Obtener resultado de forma segura
                String resultadoStr = rs.getString("resultado");
                Resultado resultado = null;
                if (resultadoStr != null) {
                    try {
                        resultado = Resultado.valueOf(resultadoStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valor inválido de resultado en BD: " + resultadoStr + " → se asigna null");
                    }
                }

                // Crear partida
                Partida partida = new Partida(
                        rs.getInt("id"),
                        narrador,
                        rs.getString("fecha"),
                        resultado
                );

                partidas.add(partida);
            }
        }

        return partidas;
    }

    // Contar partidas existentes
    public int contarPartidas() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM partidas";
        try (Statement stmt = mysqlConector.getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if(rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}
