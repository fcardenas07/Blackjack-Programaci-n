import java.util.*;

public class Blackjack {
    public static void main(String[] args) {
        jugarBlackjack();
    }

    public static void jugarBlackjack() {
        int volverAJugar;
        do {
            var baraja = crearBaraja();
            barajar(baraja);

            String[] manoJugador = crearMano();
            String[] manoDealer = crearMano();

            repartir(baraja, manoJugador);
            repartir(baraja, manoDealer);

            mostrarManos(manoJugador, manoDealer, 1);

            int sumaManoJugador = sumarMano(manoJugador);

            if (!esBlackjack(sumaManoJugador)) {
                manoJugador = turnoJugador(baraja, manoJugador);
            }

            if (!sePasoDe21(sumarMano(manoJugador))) {
                manoDealer = turnoDealer(baraja, manoDealer);
            }

            bajarse(manoJugador, manoDealer);
            volverAJugar = preguntarSiVuelveAJugar();

        } while (volverAJugar == 1);
    }

    public static void mostrarManos(String[] manoJugador, String[] manoDealer, int numeroJugada) {
        System.out.print("Jugador = " + sumarMano(manoJugador) + "; ");
        mostrarMano(manoJugador);

        if (numeroJugada == 1) {
            System.out.print("Dealer = ");
            mostrarMano(ocultarManoDealer(manoDealer));
        } else {
            System.out.print("Dealer = " + sumarMano(manoDealer) + "; ");
            mostrarMano(manoDealer);
        }
    }

    public static String[] ocultarManoDealer(String[] manoDealer) {
        return new String[]{"**************", manoDealer[1]};
    }

    public static int preguntarSiVuelveAJugar() {
        mostrarMenuVolverAJugar();
        return ingresarOpcion();
    }
    public static String[] turnoDealer(Stack<String> baraja, String[] manoDealer) {
        while (sumarMano(manoDealer) <= 16) {
            manoDealer = pedirCarta(baraja, manoDealer);
        }
        return manoDealer;
    }

    public static int sumarMano(String[] mano) {
        var manoSinPinta = quitarPintaACartas(mano);
        var valoresMano = obtenerValoresCartas(manoSinPinta);
        return sumarValoresMano(valoresMano);
    }

    public static String[] turnoJugador(Stack<String> baraja, String[] manoJugador) {
        int jugada = preguntarJugada();
        int sumaManoJugador = sumarMano(manoJugador);

        while (jugada == 1 && !sePasoDe21(sumaManoJugador)) {
            manoJugador = pedirCarta(baraja, manoJugador);
            mostrarMano(manoJugador);
            sumaManoJugador = sumarMano(manoJugador);

            if (!sePasoDe21(sumaManoJugador) && !esBlackjack(sumaManoJugador)) {
                jugada = preguntarJugada();
            }
            if (esBlackjack(sumaManoJugador)) {
                jugada = 0;
            }
        }
        return manoJugador;
    }

    public static boolean verificarGanador(int sumaManoJugador, int sumaManoDealer) {
        if (sePasoDe21(sumaManoJugador) || esBlackjack(sumaManoDealer)) {
            return false;
        } else if (esBlackjack(sumaManoJugador) || sePasoDe21(sumaManoDealer)) {
            return true;
        } else {
            return sumaManoJugador > sumaManoDealer;
        }
    }

    public static int preguntarJugada() {
        mostrarMenuJugada();
        return ingresarOpcion();
    }

    public static String[] pedirCarta(Stack<String> baraja, String[] mano) {
        String[] nuevaMano = Arrays.copyOf(mano, mano.length +1);
        nuevaMano[nuevaMano.length - 1] = baraja.pop();
        return nuevaMano;
    }

    public static int ingresarOpcion() {
        Scanner teclado = new Scanner(System.in);
        boolean esNumero = false;
        int opcion = 0;

        do {
            try {
                opcion = teclado.nextInt();
                esNumero = true;
            } catch (Exception e) {
                teclado.next();
                System.out.println("Ingrese una opcion valida");
            }
        } while (!esNumero);

        if (opcion < 1 || opcion > 2) {
            System.out.println("Ingrese una opcion valida");
            return ingresarOpcion();
        }
        return opcion;
    }

    public static void bajarse(String[] manoJugador, String[] manoDealer) {
        int sumaManoJugador = sumarMano(manoJugador);
        int sumaManoDealer = sumarMano(manoDealer);
        boolean esJugadorGanador = verificarGanador(sumaManoJugador, sumaManoDealer);

        mostrarResultados(manoJugador, manoDealer, sumaManoDealer, esJugadorGanador);
    }

    public static void mostrarResultados(String[] manoJugador, String[] manoDealer, int sumaManoDealer, boolean esJugadorGanador) {
        mostrarManos(manoJugador, manoDealer, sumaManoDealer);
        mostrarMensaje(esJugadorGanador);
    }

    public static boolean sePasoDe21(int mano) {
        return mano > 21;
    }

    public static Boolean esBlackjack(int mano) {
        return mano == 21;
    }

    public static void barajar(Stack<String> baraja) {
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(baraja);
        }
    }

    public static int sumarValoresMano(List<Integer> mano) {
        int suma = 0;
        for (var valor : mano) {
            suma += valor;
        }
        return suma;
    }

    public static ArrayList<String> quitarPintaACartas(String[] mano) {
        var cartasSinPinta = new ArrayList<String>();
        for (var carta : mano) {
            var valoresCarta = carta.split(" ");
            cartasSinPinta.add(valoresCarta[0]);
        }
        return cartasSinPinta;
    }

    public static List<Integer> obtenerValoresCartas(List<String> mano) {
        var valoresCartas = new ArrayList<Integer>();
        var mapa = crearMapa();

        for (var carta : mano) {
            valoresCartas.add(mapa.get(carta));
        }
        return valoresCartas;
    }

    public static HashMap<String, Integer> crearMapa() {
        var cartas = List.of("AS", "DOS", "TRES", "CUATRO", "CINCO",
                "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ", "JOTA", "QUINA", "KAISER");
        var mapa = new HashMap<String, Integer>();
        var valoresCartas = List.of(11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10);

        for (int i = 0; i < cartas.size(); i++) {
            mapa.put(cartas.get(i), valoresCartas.get(i));
        }
        return mapa;
    }

    public static void mostrarMano(String[] mano) {
        System.out.println(Arrays.toString(mano));
    }

    public static void repartir(Stack<String> baraja, String[] mano) {
        for (int i = 0; i < mano.length; i++) {
            mano[i] = baraja.pop();
        }
    }

    public static String[] crearMano() {
        return new String[2];
    }

    public static Stack<String> crearBaraja() {
        String[] pintas = pintasCartas();
        String[] numeros = numerosCartas();
        Stack<String> baraja = new Stack<>();

        for (String pinta : pintas) {
            for (String numero : numeros) {
                baraja.push(numero.concat(" ").concat(pinta));
            }
        }
        return baraja;
    }

    private static String[] numerosCartas() {
        return new String[]{"AS", "DOS", "TRES", "CUATRO", "CINCO",
                "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ", "JOTA", "QUINA", "KAISER"};
    }

    private static String[] pintasCartas() {
        return new String[]{"CORAZON", "DIAMANTE", "TREBOL", "PICA"};
    }

    public static void mostrarMensaje(boolean esJugadorGanador) {
        if (esJugadorGanador) {
            System.out.println("Ha Ganado!");
        } else {
            System.out.println("Ha Perdido!");
        }
    }

    public static void mostrarMenuJugada() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Su turno. Seleccione una opcion: ");
        System.out.println("1-> Robar Carta");
        System.out.println("2-> Bajarse");
        System.out.println("-----------------------------------------------------------------");
    }

    public static void mostrarMenuVolverAJugar() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Desea volver a jugar?. Seleccione una opcion: ");
        System.out.println("1-> SI");
        System.out.println("2-> NO");
        System.out.println("------------------------------------------------------------------");
    }
}
