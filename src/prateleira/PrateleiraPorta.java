package prateleira;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import efeito.EfeitoJuntar;
import mundo.Mundo;
import poo.util.Validator;
import prof.jogos2D.efeito.Efeito;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraPorta extends PrateleiraSimples {
    private ComponenteMultiAnimado porta;
    private boolean aberta;
    private int tempoAberta, tempoFechada, tempoAtual;
    private static final int ABERTA = 0;
    private static final int FECHANDO = 1;
    private static final int FECHADA = 2;
    private static final int ABRINDO = 3;

    public PrateleiraPorta(Point pos, ComponenteVisual img, ComponenteMultiAnimado porta, int tempoAberta,
            int tempoFechada, boolean aberta, int atual, int capacidade, int base,
            int largProd, int altProd) {
        super(pos, img, capacidade, base, largProd, altProd);
        this.porta = Objects.requireNonNull(porta);
        this.tempoAberta = Validator.requirePositiveOrZero(tempoAberta);
        this.tempoFechada = Validator.requirePositiveOrZero(tempoFechada);
        this.tempoAtual = Validator.requirePositiveOrZero(atual);
        this.aberta = aberta;
        this.porta.setAnim(aberta ? ABERTA : FECHADA);
    }

    public void atualizar() {
        super.atualizar();

        tempoAtual--;
        if (tempoAtual == 0) {
            porta.setAnim(aberta ? FECHANDO : ABRINDO);
            porta.reset();
            aberta = false; // mal a porta começa a abrir/fechar está fechada
        }
        if (tempoAtual < 0 && porta.numCiclosFeitos() >= 1) {
            aberta = porta.getAnim() == ABRINDO;
            if (aberta) {
                tempoAtual = tempoAberta;
                porta.setAnim(ABERTA);
            } else {
                tempoAtual = tempoFechada;
                porta.setAnim(FECHADA);
            }
        }
    }

    public int espacoDisponivel() {
        return aberta ? super.espacoDisponivel() : 0;
    }

    public Produto[] getProdutosFrente() {
        if (aberta) {
            Produto frente[] = new Produto[getCapacidade()];
            for (int i = 0; i < getCapacidade(); i++)
                frente[i] = getProdutos().get(i);
            return frente;
        }
        Produto vazios[] = new Produto[getCapacidade()];
        Arrays.fill(vazios, Produto.VAZIO);
        return vazios;
    }

    @Override
    public void desenhar(Graphics2D g) {
        super.desenhar(g);
        porta.desenhar(g, getPosicao());
    }

    public Produto processaClique(Point pos) {
        return aberta ? super.processaClique(pos) : null;
    }

    public boolean temProdutoAt(Point pos) {
        return aberta ? super.temProdutoAt(pos) : false;
    }

    public boolean aceitarProduto(Produto p, Point pos) {
        return super.receberProduto(p, pos);
    }

    public boolean podeAceitarProduto(Produto p) {
        if (!aberta)
            return false;
        return super.podeReceber(p);
    }

    public void reaverProduto() {
        super.retomarProduto();
    }
}
