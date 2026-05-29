package prateleira;

import java.awt.Point;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraEsgotavel extends PrateleiraSimples {

    private ComponenteMultiAnimado animFechar;
    private boolean esgotada;

    public PrateleiraEsgotavel(Point p, ComponenteVisual cv, ComponenteMultiAnimado animFechar, int capacidade, int linhaBase, int produtoLarg, int produtoAlt) {
        super(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
        this.animFechar = animFechar;
        this.esgotada = false;
    }

    public boolean podeReceber(Produto p) {
        if (esgotada) return false;
        return super.podeReceber(p);
    }

    public void removeProduto(Produto p) {
        super.removeProduto(p);
        if (totalProdutos() == 0) {
            esgotada = true;
            animFechar.setCiclico(false);
            animFechar.reset();
            animFechar.setAnim(1);
        }
    }
}