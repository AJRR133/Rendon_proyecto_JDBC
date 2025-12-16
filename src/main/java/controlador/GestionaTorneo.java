package controlador;

import modelo.Jugador;
import modelo.Partida;
import modelo.Resultado;
import servicios.ServicioTorneo;

import java.util.List;

public class GestionaTorneo {

    public static void main(String[] args) {

        // Crear el servicio (ya maneja internamente la inicialización)
        ServicioTorneo servicio = new ServicioTorneo();

        // ----------------- Crear jugadores -----------------
        Jugador j1 = new Jugador(1, 0, "Ana", "ana@mail.com");
        Jugador j2 = new Jugador(2, 0, "Luis", "luis@mail.com");
        Jugador j3 = new Jugador(3, 0, "Marta", "marta@mail.com");
        Jugador j4 = new Jugador(4, 0, "Pedro", "pedro@mail.com");

        servicio.agregarJugador(j1);
        servicio.agregarJugador(j2);
        servicio.agregarJugador(j3);
        servicio.agregarJugador(j4);

        // ----------------- Crear partidas -----------------
        servicio.agregarPartida(new Partida(1, j1, "2025-12-01", Resultado.ALGUNOS));
        servicio.agregarPartida(new Partida(2, j2, "2025-12-02", Resultado.TODOS));
        servicio.agregarPartida(new Partida(3, j3, "2025-12-03", Resultado.NADIE));
        servicio.agregarPartida(new Partida(4, j4, "2025-12-04", Resultado.ALGUNOS));
        servicio.agregarPartida(new Partida(5, j1, "2025-12-05", Resultado.TODOS));

        // Intentar agregar sexta partida (la excepción se maneja dentro del servicio)
        servicio.agregarPartida(new Partida(6, j2, "2025-12-06", Resultado.ALGUNOS));

        // ----------------- Actualizar puntuaciones -----------------
        servicio.actualizarPuntuacionNarrador(j1.getId(), Resultado.ALGUNOS);
        servicio.actualizarPuntuacionAcertante(j2.getId(), Resultado.ALGUNOS);
        servicio.actualizarPuntuacionNOAcertante(j3.getId(), Resultado.TODOS);

        // ----------------- Mostrar jugador con mayor puntuación -----------------
        Jugador top = servicio.jugadorConMayorPuntuacion();
        System.out.println("\n🏆 Jugador con mayor puntuación:");
        if (top != null) {
            System.out.println(top.getNombre() + " - " + top.getPuntostotales());
        }

        // ----------------- Listar jugadores -----------------
        System.out.println("\n📊 Puntuaciones de todos los jugadores:");
        List<Jugador> jugadores = servicio.listarJugadores();
        for (Jugador j : jugadores) {
            System.out.println(j.getNombre() + " -> " + j.getPuntostotales());
        }

        // ----------------- Listar partidas -----------------
        System.out.println("\n📅 Partidas ordenadas por fecha:");
        List<Partida> partidas = servicio.listarPartidas();
        for (Partida p : partidas) {
            System.out.println(
                    p.getFecha() + " | Narrador: " +
                    p.getNarrador().getNombre() +
                    " | Resultado: " + p.getResultado()
            );
        }
    }
}
