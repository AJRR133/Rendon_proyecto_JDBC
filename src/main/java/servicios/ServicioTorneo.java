package servicios;

import modelo.Jugador;
import modelo.Partida;
import modelo.Resultado;
import repositorio.RepositorioJugadores;
import repositorio.RepositorioPartida;
import exceptions.MiExcepcion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServicioTorneo {

    private static final Logger logger = LogManager.getLogger(ServicioTorneo.class);

    private RepositorioJugadores repoJugadores;
    private RepositorioPartida repoPartidas;

    public ServicioTorneo() {
        try {
            this.repoJugadores = new RepositorioJugadores();
            this.repoPartidas = new RepositorioPartida();
        } catch (MiExcepcion e) {
            logger.error("Error inicializando el servicio: {}", e.getMessage());
        }
    }

    public void agregarJugador(Jugador jugador) {
        try {
            repoJugadores.agregarJugador(jugador);
            logger.info("Jugador agregado: {} - {}", jugador.getNombre(), jugador.getEmail());
        } catch (SQLException e) {
            logger.error("Error al agregar jugador {}: {}", jugador.getNombre(), e.getMessage());
        }
    }

    public List<Jugador> listarJugadores() {
        List<Jugador> jugadores = new ArrayList<>();
        try {
            jugadores = repoJugadores.listarJugadores();
        } catch (SQLException e) {
            logger.error("Error al listar jugadores: {}", e.getMessage());
        }
        return jugadores;
    }

    public Jugador jugadorConMayorPuntuacion() {
        Jugador top = new Jugador();
        List<Jugador> jugadores = listarJugadores();
        if (!jugadores.isEmpty()) {
            top = jugadores.get(0);
        } else {
            logger.warn("No hay jugadores disponibles para determinar el de mayor puntuación");
        }
        return top;
    }

    public void actualizarPuntuacionNarrador(int jugadorId, Resultado resultado) {
        try {
            if (resultado == Resultado.ALGUNOS) {
                repoJugadores.actualizarPuntos(jugadorId, 3);
                logger.info("Se actualizaron 3 puntos al narrador con id {}", jugadorId);
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar puntos del narrador: {}", e.getMessage());
        }
    }

    public void actualizarPuntuacionAcertante(int jugadorId, Resultado resultado) {
        try {
            if (resultado == Resultado.ALGUNOS) {
                repoJugadores.actualizarPuntos(jugadorId, 3);
            } else if (resultado == Resultado.TODOS || resultado == Resultado.NADIE) {
                repoJugadores.actualizarPuntos(jugadorId, 2);
            }
            logger.info("Se actualizaron puntos al acertante con id {}", jugadorId);
        } catch (SQLException e) {
            logger.error("Error al actualizar puntos de acertante: {}", e.getMessage());
        }
    }

    public void actualizarPuntuacionNOAcertante(int jugadorId, Resultado resultado) {
        try {
            if (resultado == Resultado.TODOS || resultado == Resultado.NADIE) {
                repoJugadores.actualizarPuntos(jugadorId, 2);
                logger.info("Se actualizaron 2 puntos al no acertante con id {}", jugadorId);
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar puntos de no acertante: {}", e.getMessage());
        }
    }

    public void agregarPartida(Partida partida) {
        try {
            repoPartidas.agregarPartida(partida);
            logger.info("Partida agregada: {} - Narrador: {}", partida.getFecha(), partida.getNarrador().getNombre());
        } catch (MiExcepcion e) {
            logger.warn("No se pudo agregar partida: {}", e.getMessage());
        } catch (SQLException e) {
            logger.error("Error al agregar partida: {}", e.getMessage());
        }
    }

    public List<Partida> listarPartidas() {
        List<Partida> partidas = new ArrayList<>();
        try {
            partidas = repoPartidas.listarPartidas(repoJugadores);
        } catch (SQLException e) {
            logger.error("Error al listar partidas: {}", e.getMessage());
        }
        return partidas;
    }

    public List<Partida> obtenerPartidaPorId(int id) {
        List<Partida> resultado = new ArrayList<>();
        try {
            Partida partida = repoPartidas.obtenerPartidaPorId(id, repoJugadores);
            if (partida != null) {
                resultado.add(partida);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener partida con id {}: {}", id, e.getMessage());
        }
        return resultado;
    }

    public void actualizarPartida(Partida partida) {
        try {
            repoPartidas.actualizarPartida(partida);
            logger.info("Partida actualizada: {}", partida.getId());
        } catch (SQLException e) {
            logger.error("Error al actualizar partida con id {}: {}", partida.getId(), e.getMessage());
        }
    }

    public void eliminarPartida(int id) {
        try {
            repoPartidas.eliminarPartida(id);
            logger.info("Partida eliminada: {}", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar partida con id {}: {}", id, e.getMessage());
        }
    }
}
