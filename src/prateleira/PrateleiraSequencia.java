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
import efeito.EfeitoMover;
import mundo.Mundo;
import poo.util.Validator;
import prof.jogos2D.efeito.Efeito;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraSequencia extends PrateleiraSimples {
    private boolean aberta = true;
    private boolean alterando = false;
    private Sequenciador comutador;

    public PrateleiraSequencia(Point pos, ComponenteVisual imagem, int capacidade, int base, int largProd,
            int altProd) {
        super(pos, imagem, capacidade, base, largProd, altProd);
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
        if (!aberta && !alterando)
            comutador.getPorta().desenhar(g, getPosicao());
    }

    public Produto processaClique(Point pos) {
        if (aberta)
            super.processaClique(pos);
        if (!alterando)
            comutador.abrirProxima();
        return null;
    }

    public void setAberta(boolean aberta) {
        this.aberta = aberta;
    }

    void setAlterando(boolean alterando) {
        this.alterando = alterando;
    }

    void setSequenciador(Sequenciador comutador) {
        this.comutador = comutador;
    }

    /**
     * Classe responsável por gerir as várias prateleiras numa sequência
     */
    public static class Sequenciador {
        private ArrayList<PrateleiraSequencia> prateleiras = new ArrayList<>();
        private int atual = 0, direcao;
        private ComponenteVisual porta;

        public Sequenciador(ComponenteVisual porta, int direcao) {
            this.porta = Objects.requireNonNull(porta);
            if (direcao != -1 && direcao != 1)
                throw new IllegalArgumentException("Direção tem de ser 1 ou -1");
            this.direcao = direcao;
        }

        public void abrirProxima() {
            PrateleiraSequencia origem = prateleiras.get(atual);

            atual += direcao;
            if (atual < 0) {
                atual = 1;
                direcao = 1;
            }
            if (atual >= prateleiras.size()) {
                atual = prateleiras.size() - 2;
                direcao = -1;
            }
            PrateleiraSequencia destino = prateleiras.get(atual);
            destino.setAberta(false);
            origem.setAlterando(true);
            destino.setAlterando(true);
            EfeitoMover fx = new EfeitoMover(porta, origem.getPosicao(), destino.getPosicao(), 20,
                    new EfeitoListener() {
                        @Override
                        public void processaFimEfeito(Efeito f) {
                            origem.setAberta(true);
                            destino.setAberta(false);
                            origem.setAlterando(false);
                            destino.setAlterando(false);
                        }
                    });
            origem.getMundo().addFx(fx);
        }

        public void addPrateleira(PrateleiraSequencia p) {
            prateleiras.add(p);
            p.setSequenciador(this);
        }

        public void setFechada(int idx) {
            prateleiras.get(atual).setAberta(true);
            atual = idx;
            prateleiras.get(atual).setAberta(false);
        }

        public ComponenteVisual getPorta() {
            return porta;
        }
    }
}
