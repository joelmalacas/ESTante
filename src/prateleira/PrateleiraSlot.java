package prateleira;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraSlot extends PrateleiraSimples {

    private ComponenteAnimado animRodar;
    private Rectangle areaBotao;

    public PrateleiraSlot(Point p, ComponenteVisual cv, ComponenteAnimado animRodar, int capacidade, int linhaBase, int produtoLarg, int produtoAlt, Rectangle areaBotao) {
        super(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
        this.animRodar = animRodar;
        this.areaBotao = areaBotao;
    }

    public Produto processaClique(Point pos) {
        if (areaBotao.contains(pos)) {
            embaralhar();
            return null;
        }
        return super.processaClique(pos);
    }

    private void embaralhar() {
        int capacidade = getCapacidade();
        List<Produto> prods = new ArrayList<>(getProdutos());
        int totalPorColuna = prods.size() / capacidade;

        for (int col = 0; col < capacidade; col++) {
            List<Produto> coluna = new ArrayList<>();
            for (int row = 0; row < totalPorColuna; row++)
                coluna.add(prods.get(col + row * capacidade));
            Collections.shuffle(coluna);
            for (int row = 0; row < totalPorColuna; row++)
                prods.set(col + row * capacidade, coluna.get(row));
        }
        setupProdutos(prods);
    }
}