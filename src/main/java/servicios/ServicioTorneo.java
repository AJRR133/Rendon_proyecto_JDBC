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

    public ServicioTorneo() {
        try {
            this.repoJugadores = new RepositorioJugadores();
            this.repoPartidas = new RepositorioPartida();
        } catch (MiExcepcion e) {
            System.err.println("Error inicializando el servicio: " + e.getMessage());
        }
    }

    // ----------------- JUGADORES -----------------
    public void agregarJugador(Jugador jugador) {
        try {
            repoJugadores.agregarJugador(jugador);
        } catch (SQLException e) {
            System.err.println("Error al agregar jugador: " + e.getMessage());
        }
    }

    public List<Jugador> listarJugadores() {
        try {
            return repoJugadores.listarJugadores();
        } catch (SQLException e) {
            System.err.println("Error al listar jugadores: " + e.getMessage());
            return List.of();
        }
    }

    public Jugador jugadorConMayorPuntuacion() {
        List<Jugador> jugadores = listarJugadores();
        return jugadores.isEmpty() ? null : jugadores.get(0);
    }

    public void actualizarPuntuacionNarrador(int jugadorId, Resultado resultado) {
        try {
            if (resultado == Resultado.ALGUNOS) {
                repoJugadores.actualizarPuntos(jugadorId, 3);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar puntos del narrador: " + e.getMessage());
        }
    }

    public void actualizarPuntuacionAcertante(int jugadorId, Resultado resultado) {
        try {
            if (resultado == Resultado.ALGUNOS) {
                repoJugadores.actualizarPuntos(jugadorId, 3);
            } else if (resultado == Resultado.TODOS || resultado == Resultado.NADIE) {
                repoJugadores.actualizarPuntos(jugadorId, 2);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar puntos de acertante: " + e.getMessage());
        }
    }

    public void actualizarPuntuacionNOAcertante(int jugadorId, Resultado resultado) {
        try {
            if (resultado == Resultado.TODOS || resultado == Resultado.NADIE) {
                repoJugadores.actualizarPuntos(jugadorId, 2);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar puntos de no acertante: " + e.getMessage());
        }
    }

    // ----------------- PARTIDAS -----------------
    public void agregarPartida(Partida partida) {
        try {
            repoPartidas.agregarPartida(partida);
        } catch (MiExcepcion e) {
            System.out.println("⚠ No se pudo agregar partida: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al agregar partida: " + e.getMessage());
        }
    }

    public List<Partida> listarPartidas() {
        try {
            return repoPartidas.listarPartidas(repoJugadores);
        } catch (SQLException e) {
            System.err.println("Error al listar partidas: " + e.getMessage());
            return List.of();
        }
    }

    public Partida obtenerPartidaPorId(int id) {
        try {
            return repoPartidas.obtenerPartidaPorId(id, repoJugadores);
        } catch (SQLException e) {
            System.err.println("Error al obtener partida: " + e.getMessage());
            return null;
        }
    }

    public void actualizarPartida(Partida partida) {
        try {
            repoPartidas.actualizarPartida(partida);
        } catch (SQLException e) {
            System.err.println("Error al actualizar partida: " + e.getMessage());
        }
    }

    public void eliminarPartida(int id) {
        try {
            repoPartidas.eliminarPartida(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar partida: " + e.getMessage());
        }
    }
}
