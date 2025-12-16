package controlador;

import modelo.Jugador;
import modelo.Partida;
import modelo.Resultado;
import servicios.ServicioTorneo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GestionaTorneo {

    private static final Logger logger = LogManager.getLogger(GestionaTorneo.class);

    public static void main(String[] args) {

        ServicioTorneo servicio = new ServicioTorneo();

        Jugador j1 = new Jugador(0, 0, "Ana", "ana@mail.com");
        Jugador j2 = new Jugador(0, 0, "Luis", "luis@mail.com");
        Jugador j3 = new Jugador(0, 0, "Marta", "marta@mail.com");
        Jugador j4 = new Jugador(0, 0, "Pedro", "pedro@mail.com");

        servicio.agregarJugador(j1);
        servicio.agregarJugador(j2);
        servicio.agregarJugador(j3);
        servicio.agregarJugador(j4);

        servicio.agregarPartida(new Partida(0, j1, "2025-12-01", Resultado.ALGUNOS));
        servicio.agregarPartida(new Partida(0, j2, "2025-12-02", Resultado.TODOS));
        servicio.agregarPartida(new Partida(0, j3, "2025-12-03", Resultado.NADIE));
        servicio.agregarPartida(new Partida(0, j4, "2025-12-04", Resultado.ALGUNOS));
        servicio.agregarPartida(new Partida(0, j1, "2025-12-05", Resultado.TODOS));
        servicio.agregarPartida(new Partida(0, j2, "2025-12-06", Resultado.ALGUNOS)); // intento sexta partida

        servicio.actualizarPuntuacionNarrador(j1.getId(), Resultado.ALGUNOS);
        servicio.actualizarPuntuacionAcertante(j2.getId(), Resultado.ALGUNOS);
        servicio.actualizarPuntuacionNOAcertante(j3.getId(), Resultado.TODOS);

        Jugador top = servicio.jugadorConMayorPuntuacion();
        if (top != null) {
            logger.info(" Jugador con mayor puntuación: {} - {}", top.getNombre(), top.getPuntostotales());
        } else {
            logger.warn("No hay jugadores con puntuación registrada.");
        }

        List<Jugador> jugadores = servicio.listarJugadores();
        logger.info(" Listado de jugadores y sus puntos:");
        for (Jugador j : jugadores) {
            logger.info("{} -> {}", j.getNombre(), j.getPuntostotales());
        }

        List<Partida> partidas = servicio.listarPartidas();
        logger.info(" Listado de partidas ordenadas por fecha:");
        for (Partida p : partidas) {
            logger.info("{} | Narrador: {} | Resultado: {}", p.getFecha(), p.getNarrador().getNombre(), p.getResultado());
        }
    }
}
