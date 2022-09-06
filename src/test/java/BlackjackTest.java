import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackTest {

    private int[] sumaJugador;
    private int[] sumaDealer;

    private boolean[] resultadosEsperados;

    @BeforeEach
    void init() {
        sumaJugador = new int[]{0, 21, -1, 11, 21, 13, 0, -10, 21, 23};
        sumaDealer = new int[]{12, 21, 12, 11, 20, 12, 0, -10, 22, 24};
        resultadosEsperados = new boolean[]{false, false, false, false, true, true, false, false, true, false};
    }

    @Test
    void verificarGanador() {
        for (int i = 0; i < sumaJugador.length; i++) {
            boolean resultadoObtenido = Blackjack.verificarGanador(sumaJugador[i], sumaDealer[i]);
            assertEquals(resultadoObtenido, resultadosEsperados[i]);
        }
    }
}