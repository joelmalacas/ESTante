package prateleira;

import java.awt.*;

import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraCongeladora extends PrateleiraSimples {

    private ComponenteVisual gelo;

    public PrateleiraCongeladora(Point p, ComponenteVisual cv, ComponenteVisual gelo, int capacidade, int linhaBase, int produtoLarg, int produtoAlt) {
        super(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
        this.gelo = gelo;
    }

    @Override
    public void desenhar(Graphics2D g) {
        super.desenhar(g);
        for (int i = 0; i < getCapacidade(); i++) {
            Produto p = getProdutos().get(i);
            if (p != Produto.VAZIO)
                gelo.desenhar(g, getPosicaoProduto(p));
        }
    }

    @Override
    public Produto emprestarProdutoAt(Point pos) {
        return null;
    }

    @Override
    public boolean podeReceber(Produto p) {
        return super.podeReceber(p);
    }
}