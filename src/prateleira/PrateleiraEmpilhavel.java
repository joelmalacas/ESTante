package prateleira;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import efeito.EfeitoDissolverPrateleira;
import efeito.EfeitoJuntar;
import mundo.Mundo;
import poo.util.Validator;
import prof.jogos2D.efeito.Efeito;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteVisual;

public class PrateleiraEmpilhavel extends PrateleiraSimples {

    private int capacidade;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private int indexEmprestado = -1;
    private int offsetBase;
    private Rectangle areaProdutos[];
    private int totalProdutos = 0, totalFrente = 0;
    private Mundo mundo;

    private int yAtual = 0;
    private int andar = 0;
    private Torre torre;

    public PrateleiraEmpilhavel(ComponenteVisual imagem, int capacidade, int base, int largProd,
            int altProd, Torre t) {
        super(new Point(-10000, -10000), imagem, capacidade, base, largProd, altProd);

        this.torre = Objects.requireNonNull(t);
    }

    public void atualizar() {
        if (yAtual < andar) {
            yAtual += torre.getVelocidadeQueda();
            if (yAtual > andar)
                yAtual = andar;
            super.setPosicao(new Point(getPosicao().x, yAtual));
        }
        super.atualizar();
    }

    public boolean aceitarProduto(Produto p, Point pos) {
        return super.receberProduto(p, pos);
    }

    public boolean podeAceitarProduto(Produto p) {
        return super.podeReceber(p);
    }

    public void removeProduto(Produto p) {
        super.removeProduto(p);
        checkSemProdutos();
    }

    private void checkSemProdutos() {
        if (totalProdutos() == 0) {
            EfeitoDissolverPrateleira fx = new EfeitoDissolverPrateleira(this, 30);
            getMundo().addFx(fx);
            torre.removeAndar(this);
        }
    }

    public void setAndar(int y) {
        andar = y;
    }

    public void reaverProduto() {
        super.retomarProduto();
    }

    @Override
    public void setPosicao(Point p) {
        super.setPosicao(p);
        yAtual = p.y;
    }

    public boolean estaCair() {
        return yAtual < andar;
    }

    /**
     * classe responsável por gerir as várias prateleiras emplilháveis
     */
    public static class Torre {
        private int andares[];
        private PrateleiraInfo prateleiras[];
        private int nPrateleiras = 0;
        private int x;
        private int velocidadeQueda;

        public Torre(int x, int altura, int veloc) {
            Validator.requirePositive(altura);
            andares = new int[altura];
            prateleiras = new PrateleiraInfo[altura];
            this.x = x;
            velocidadeQueda = veloc;
        }

        public void addAndar(int y, PrateleiraInfo p) {
            if (nPrateleiras > 0 && andares[nPrateleiras - 1] < y)
                throw new IllegalArgumentException("andares têm de estar por ordem decrescente de y");
            andares[nPrateleiras] = y;
            prateleiras[nPrateleiras] = p;
            nPrateleiras++;
            p.getEmpilhavel().setAndar(y);
            p.getEmpilhavel().setPosicao(new Point(x, y));
        }

        public void removeAndar(PrateleiraEmpilhavel pemp) {
            int idx = 0;
            while (prateleiras[idx].getEmpilhavel() != pemp)
                idx++;
            PrateleiraInfo pInfo = prateleiras[idx];
            for (int i = idx; i < nPrateleiras - 1; i++) {
                prateleiras[i] = prateleiras[i + 1];
                prateleiras[i + 1].getEmpilhavel().setAndar(andares[i]);
            }
            nPrateleiras--;
            prateleiras[nPrateleiras] = null;
            pemp.getMundo().removePrateleira(pInfo);
        }

        public int getVelocidadeQueda() {
            return velocidadeQueda;
        }
    }
}
