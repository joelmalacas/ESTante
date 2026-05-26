package prateleira;

import java.awt.Point;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraTeleportadora extends PrateleiraSimples {
    private int tempoTroca;
    private int contador;

    public PrateleiraTeleportadora(
            Point posicao,
            ComponenteVisual imagem,
            int capacidade,
            int linhaBase,
            int produtoLargura,
            int produtoAltura,
            int tempoTroca) {

        super(
                posicao,
                imagem,
                capacidade,
                linhaBase,
                produtoLargura,
                produtoAltura);

        if (tempoTroca <= 0)
            throw new IllegalArgumentException(
                    "O tempo de troca deve ser positivo.");

        this.tempoTroca = tempoTroca;
        this.contador = 0;
    }

    @Override
    public void atualizar() {

        super.atualizar();

        contador++;

        if (contador >= tempoTroca) {
            contador = 0;
            teleportarProdutos();
        }
    }

    private void teleportarProdutos() {

        if (getProdutos().size() <= 1)
            return;

        Collections.shuffle(
                getProdutos(),
                ThreadLocalRandom.current());
    }

    public int getTempoTroca() {
        return tempoTroca;
    }    
}
