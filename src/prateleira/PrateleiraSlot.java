package prateleira;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import efeito.EfeitoSlot;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraSlot extends PrateleiraSimples {

    private ComponenteAnimado animRodar;
    private Rectangle areaBotao;
    private static final int DURACAO_EFEITO = 40;

    public PrateleiraSlot(Point p, ComponenteVisual cv, ComponenteAnimado animRodar, int capacidade, int linhaBase, int produtoLarg, int produtoAlt, Rectangle areaBotao) {
        super(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
        if (areaBotao == null)
            throw new IllegalArgumentException("Área botão inválida");
        this.animRodar = animRodar;
        this.areaBotao = areaBotao;
    }

    public Produto processaClique(Point pos) {
        Point posLocal = new Point(pos.x - getPosicao().x, pos.y - getPosicao().y);
        if (areaBotao.contains(posLocal)) {
            getMundo().addFx(new EfeitoSlot(animRodar.clone(), getPosicao(), DURACAO_EFEITO));
            embaralhar();
            return null;
        }
        return super.processaClique(pos);
    }

    private void embaralhar() {
        int capacidade = getCapacidade();
        List<Produto> prods = new ArrayList<>(getProdutos());
        int totalPorColuna = prods.size() / capacidade;

        for (int row = 0; row < totalPorColuna; row++) {
            List<Produto> linha = new ArrayList<>();
            for (int col = 0; col < capacidade; col++)
                linha.add(prods.get(col + row * capacidade));
            Collections.shuffle(linha);
            for (int col = 0; col < capacidade; col++)
                prods.set(col + row * capacidade, linha.get(col));
        }
        setupProdutos(prods);
    }
}