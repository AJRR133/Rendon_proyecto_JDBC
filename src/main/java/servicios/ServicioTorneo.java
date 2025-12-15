package servicios;

import modelo.Jugador;
import modelo.Partida;
import modelo.Resultado;
import repositorio.RepositorioJugadores;
import repositorio.RepositorioPartida;
import exceptions.MiExcepcion;

import java.sql.SQLException;
import java.util.List;

public class ServicioTorneo {

    private RepositorioJugadores repoJugadores;
    private RepositorioPartida repoPartidas;
    private static final int MAX_PARTIDAS = 5;

    public ServicioTorneo(RepositorioJugadores repoJugadores, RepositorioPartida repoPartidas) {
        this.repoJugadores = repoJugadores;
        this.repoPartidas = repoPartidas;
    }

    // Agregar jugador
    public void agregarJugador(Jugador jugador) throws SQLException {
        repoJugadores.agregarJugador(jugador);
    }

    // Agregar partida con límite de 5
    public void agregarPartida(Partida partida) throws SQLException, MiExcepcion {
        if (repoPartidas.contarPartidas() >= MAX_PARTIDAS) {
            throw new MiExcepcion("No se pueden agregar más de " + MAX_PARTIDAS + " partidas");
        }
        repoPartidas.agregarPartida(partida);
    }

    // Actualizar puntuación del narrador
    public void actualizarPuntuacionNarrador(int jugadorId, Resultado resultado) throws SQLException {
        if (resultado == Resultado.ALGUNOS) {
            repoJugadores.actualizarPuntos(jugadorId, 3);
        }
    }

    // Actualizar puntuación de no acertantes
    public void actualizarPuntuacionNOAcertante(int jugadorId, Resultado resultado) throws SQLException {
        if (resultado == Resultado.TODOS || resultado == Resultado.NADIE) {
            repoJugadores.actualizarPuntos(jugadorId, 2);
        }
    }

    // Actualizar puntuación de acertantes
    public void actualizarPuntuacionAcertante(int jugadorId, Resultado resultado) throws SQLException {
        if (resultado == Resultado.TODOS || resultado == Resultado.NADIE) {
            repoJugadores.actualizarPuntos(jugadorId, 2);
        } else if (resultado == Resultado.ALGUNOS) {
            repoJugadores.actualizarPuntos(jugadorId, 3);
        }
    }

    // Jugador con mayor puntuación
    public Jugador jugadorConMayorPuntuacion() throws SQLException {
        List<Jugador> jugadores = repoJugadores.listarJugadores();
        return jugadores.isEmpty() ? null : jugadores.get(0);
    }

    // Listar jugadores ordenados por puntos
    public List<Jugador> listarJugadores() throws SQLException {
        return repoJugadores.listarJugadores();
    }

    // Listar partidas ordenadas por fecha
    public List<Partida> listarPartidas() throws SQLException {
        return repoPartidas.listarPartidas(repoJugadores);
    }
}
