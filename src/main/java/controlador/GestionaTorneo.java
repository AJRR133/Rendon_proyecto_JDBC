package controlador;

import modelo.Jugador;
import modelo.Partida;
import modelo.Resultado;
import repositorio.RepositorioJugadores;
import repositorio.RepositorioPartida;
import servicios.ServicioTorneo;
import exceptions.MiExcepcion;

import java.sql.SQLException;
import java.util.List;

public class GestionaTorneo {

    public static void main(String[] args) {
        try {
            // Inicializar repositorios y servicio
            RepositorioJugadores repoJugadores = new RepositorioJugadores();
            RepositorioPartida repoPartidas = new RepositorioPartida();
            ServicioTorneo servicio = new ServicioTorneo(repoJugadores, repoPartidas);

            // Crear jugadores usando constructor correcto
            Jugador j1 = new Jugador(0, 0, "Ana", "ana@mail.com");
            Jugador j2 = new Jugador(0, 0, "Luis", "luis@mail.com");
            Jugador j3 = new Jugador(0, 0, "Marta", "marta@mail.com");
            Jugador j4 = new Jugador(0, 0, "Pedro", "pedro@mail.com");

            servicio.agregarJugador(j1);
            servicio.agregarJugador(j2);
            servicio.agregarJugador(j3);
            servicio.agregarJugador(j4);

            // Crear partidas
            servicio.agregarPartida(new Partida(0, j1, "2025-12-01", Resultado.ALGUNOS));
            servicio.agregarPartida(new Partida(0, j2, "2025-12-02", Resultado.TODOS));
            servicio.agregarPartida(new Partida(0, j3, "2025-12-03", Resultado.NADIE));
            servicio.agregarPartida(new Partida(0, j4, "2025-12-04", Resultado.ALGUNOS));
            servicio.agregarPartida(new Partida(0, j1, "2025-12-05", Resultado.TODOS));

            // Intentar agregar sexta partida para probar excepción
            try {
                servicio.agregarPartida(new Partida(0, j2, "2025-12-06", Resultado.ALGUNOS));
            } catch (MiExcepcion e) {
                System.out.println("Excepción al agregar sexta partida: " + e.getMessage());
            }

            // Actualizar puntuaciones ejemplo
            servicio.actualizarPuntuacionNarrador(j1.getId(), Resultado.ALGUNOS);
            servicio.actualizarPuntuacionAcertante(j2.getId(), Resultado.ALGUNOS);
            servicio.actualizarPuntuacionNOAcertante(j3.getId(), Resultado.TODOS);

            // Mostrar jugador con mayor puntuación
            Jugador top = servicio.jugadorConMayorPuntuacion();
            System.out.println("\nJugador con mayor puntuación: " + top.getNombre() + " - " + top.getPuntostotales());

            // Listar jugadores
            System.out.println("\nListado de jugadores:");
            List<Jugador> jugadores = servicio.listarJugadores();
            for (Jugador j : jugadores) {
                System.out.println(j.getNombre() + " - " + j.getPuntostotales());
            }

            // Listar partidas
            System.out.println("\nListado de partidas:");
            List<Partida> partidas = servicio.listarPartidas();
            for (Partida p : partidas) {
                System.out.println(p.getFecha() + " - " + p.getNarrador().getNombre() + " - " + p.getResultado());
            }

        } catch (SQLException | MiExcepcion e) {
            e.printStackTrace();
        }
    }
}
