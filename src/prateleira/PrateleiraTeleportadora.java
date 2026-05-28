package prateleira;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import efeito.EfeitoTeleporte;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraTeleportadora extends PrateleiraSimples {
    private int tempoTroca;
    private int contador;
    private int produtoLargura, produtoAltura;

    public PrateleiraTeleportadora(
            Point posicao,
            ComponenteVisual imagem,
            int capacidade,
            int linhaBase,
            int produtoLargura,
            int produtoAltura,
            int tempoTroca) {

        super(posicao, imagem, capacidade, linhaBase, produtoLargura, produtoAltura);

        if (tempoTroca <= 0)
            throw new IllegalArgumentException("O tempo de troca deve ser positivo.");

        this.tempoTroca = tempoTroca;
        this.contador = 0;
        this.produtoLargura = produtoLargura;
        this.produtoAltura = produtoAltura;
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
        List<Produto> prods = getProdutos();
        int capacidade = getCapacidade();
        int totalPorColuna = prods.size() / capacidade;

        if (totalPorColuna <= 1)
            return;

        // recolher produtos e posições da frente para o efeito visual
        List<Produto> prodsEfeito = new ArrayList<>();
        List<Point> posicoesEfeito = new ArrayList<>();
        for (int i = 0; i < capacidade; i++) {
            Produto p = prods.get(i);
            if (p != Produto.VAZIO) {
                prodsEfeito.add(p);
                posicoesEfeito.add(getPosicaoProduto(p));
            }
        }

        // embaralhar cada coluna em profundidade
        List<Produto> novaLista = new ArrayList<>(prods);
        for (int col = 0; col < capacidade; col++) {
            List<Produto> coluna = new ArrayList<>();
            for (int row = 0; row < totalPorColuna; row++)
                coluna.add(novaLista.get(col + row * capacidade));
            Collections.shuffle(coluna, ThreadLocalRandom.current());
            for (int row = 0; row < totalPorColuna; row++)
                novaLista.set(col + row * capacidade, coluna.get(row));
        }
        setupProdutos(novaLista);

        if (!prodsEfeito.isEmpty())
            getMundo().addFx(new EfeitoTeleporte(
                    prodsEfeito.toArray(new Produto[0]),
                    posicoesEfeito.toArray(new Point[0]),
                    produtoLargura, produtoAltura, 30, null));
    }

    public int getTempoTroca() {
        return tempoTroca;
    }
}
