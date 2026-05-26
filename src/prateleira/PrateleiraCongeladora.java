package prateleira;

import java.awt.Point;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraCongeladora extends PrateleiraSimples {

    private ComponenteVisual gelo;

    public PrateleiraCongeladora(Point p, ComponenteVisual cv, ComponenteVisual gelo, int capacidade, int linhaBase, int produtoLarg, int produtoAlt) {
        super(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
        this.gelo = gelo;
    }

    public Produto emprestarProdutoAt(Point pos) {
        return null;
    }

    public boolean podeReceber(Produto p) {
        return super.podeReceber(p);
    }
}