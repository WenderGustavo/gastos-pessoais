package io.github.wendergustavo.gastospessoais.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GastoTest {

    @Test
    public void testCriarGasto() {
        Gasto gasto = new Gasto();
        gasto.setValor(BigDecimal.valueOf(155.0));
        gasto.setGastoTipo(GastoTipo.LAZER);

        assertEquals(BigDecimal.valueOf(155.0), gasto.getValor());
        assertEquals(GastoTipo.LAZER, gasto.getGastoTipo());
    }
}
