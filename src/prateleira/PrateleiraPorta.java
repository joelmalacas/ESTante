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

public class PrateleiraPorta extends ElementoGraficoDefault {
    private int capacidade;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private int indexEmprestado = -1;
    private int offsetBase;
    private Rectangle areaProdutos[];
    private int totalProdutos = 0, totalFrente = 0;
    private Mundo mundo;
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
        super(pos, img);
        this.capacidade = Validator.requirePositive(capacidade);
        offsetBase = base;
        // determinar onde colocar os produtos
        determinarPosicoesProdutos(largProd, altProd);
        // assegurar que, mesmo sem setup, a prateleira tem espaço para os produtos
        assegurarEspacoFrente();
        this.porta = Objects.requireNonNull(porta);
        this.tempoAberta = Validator.requirePositiveOrZero(tempoAberta);
        this.tempoFechada = Validator.requirePositiveOrZero(tempoFechada);
        this.tempoAtual = Validator.requirePositiveOrZero(atual);
        this.aberta = aberta;
        this.porta.setAnim(aberta ? ABERTA : FECHADA);
    }

    public void atualizar() {
        if (totalFrente == capacidade && capacidade >= getMundo().getNumeroJuntar())
            testarIguais();
        if (totalFrente == 0 && totalProdutos != 0)
            removerFrontais();

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

    private void testarIguais() {
        Produto p = produtos.get(0);
        if (p == Produto.VAZIO)
            return;
        for (int i = 1; i < capacidade; i++)
            if (produtos.get(i).getId() != p.getId())
                return;
        // se chega aqui é porque são todos iguais
        criarEfeitoJuntar();
    }

    private void criarEfeitoJuntar() {
        Produto prodsJuntos[] = new Produto[capacidade];
        Point2D.Float posJuntos[] = new Point2D.Float[capacidade];
        for (int i = 0; i < capacidade; i++) {
            prodsJuntos[i] = produtos.set(i, Produto.VAZIO);
            posJuntos[i] = new Point2D.Float(areaProdutos[i].x, areaProdutos[i].y);
        }

        EfeitoJuntar efeito = new EfeitoJuntar(prodsJuntos, posJuntos, 30, new EfeitoListener() {
            @Override
            public void processaFimEfeito(Efeito f) {
                removerFrontais();
            }
        });
        mundo.addFx(efeito);
    }

    private void removerFrontais() {
        for (int i = capacidade - 1; i >= 0; i--)
            produtos.remove(i).setPrateleira(null);
        totalProdutos -= capacidade;
        assegurarEspacoFrente();
        totalFrente = 0;
        for (int i = 0; i < capacidade; i++) {
            Produto p = produtos.get(i);
            if (p != Produto.VAZIO) {
                totalFrente++;
            }
        }
    }

    public int totalProdutos() {
        return totalProdutos;
    }

    public int espacoDisponivel() {
        return aberta ? capacidade - totalFrente : 0;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setupProdutos(List<Produto> prods) {
        totalProdutos = 0;
        totalFrente = 0;
        if (prods.size() % capacidade != 0)
            throw new IllegalArgumentException(
                    "número de produtos (" + prods.size() + " deve ser múltiplo da capacidade (" + capacidade + ")");
        // definir o tamanho da Linkedlist
        produtos = new ArrayList<>(prods.size());
        for (int i = 0; i < prods.size(); i++) {
            Produto p = prods.get(i);
            // se for um produto a sério é preciso contabilizar
            if (p != Produto.VAZIO) {
                p.setPrateleira(new PrateleiraInfo(this));
                totalProdutos++;
                if (i < capacidade)
                    totalFrente++;
            }
            produtos.add(p);
        }
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public Produto[] getProdutosFrente() {
        if (aberta) {
            Produto frente[] = new Produto[capacidade];
            for (int i = 0; i < capacidade; i++)
                frente[i] = produtos.get(i);
            return frente;
        }
        Produto vazios[] = new Produto[getCapacidade()];
        Arrays.fill(vazios, Produto.VAZIO);
        return vazios;
    }

    public Produto getEmprestado() {
        return produtos.get(indexEmprestado);
    }

    @Override
    public void desenhar(Graphics2D g) {
        super.desenhar(g);
        desenharPosteriores(g);
        desenharFrontais(g);
        porta.desenhar(g, getPosicao());
    }

    private void desenharFrontais(Graphics2D g) {
        for (int i = 0; i < capacidade; i++) {
            Produto p = produtos.get(i);
            if (i == indexEmprestado)
                continue;
            p.desenharEm(g, areaProdutos[i].getLocation());
        }
    }

    private void desenharPosteriores(Graphics2D g) {
        int min = Math.min(capacidade * 2, produtos.size());
        for (int i = capacidade; i < min; i++) {
            Produto p = produtos.get(i);
            Graphics2D g2 = (Graphics2D) g.create();
            Point pos = areaProdutos[i - capacidade].getLocation();
            g2.translate(pos.x, pos.y);
            g2.scale(0.85, 0.85);
            g2.translate(-pos.x, -pos.y + 1);
            p.desenharSombreadoEm(g2, pos);
        }
    }

    public Produto processaClique(Point pos) {
        return aberta ? getProdutoAt(pos) : null;
    }

    public Produto getProdutoAt(Point pos) {
        int idx = getIndexAt(pos);
        if (idx == -1)
            return null;
        Produto p = produtos.get(idx);
        return p != Produto.VAZIO ? p : null;
    }

    private int getIndexAt(Point pos) {
        if (!contemPonto(pos) || indexEmprestado != -1)
            return -1;
        for (int i = 0; i < capacidade; i++) {
            if (areaProdutos[i].contains(pos))
                return i;
        }
        return -1;
    }

    public boolean temProdutoAt(Point pos) {
        return aberta ? getProdutoAt(pos) != null : false;
    }

    public Produto emprestarProdutoAt(Point pos) {
        if (indexEmprestado != -1)
            return null;
        Produto p = getProdutoAt(pos);
        if (p == null)
            throw new NoSuchElementException();
        indexEmprestado = indexProduto(p);
        return p;
    }

    public boolean aceitarProduto(Produto p, Point pos) {
        if (!podeAceitarProduto(p))
            return false;
        // ver onde clicou
        Rectangle r = getBounds();
        int x = pos.x - r.x;
        int idx = x / (r.width / capacidade);
        Produto emprestado = indexEmprestado != -1 ? produtos.get(indexEmprestado) : null;
        // se for o emprestado tem de se remover de onde estava
        if (emprestado == p) {
            produtos.set(indexEmprestado, Produto.VAZIO);
            indexEmprestado = -1;
        }
        // senão é porque é novo e atualizam-se os totais
        else {
            totalFrente++;
            totalProdutos++;
        }

        p.setPrateleira(new PrateleiraInfo(this));
        if (produtos.get(idx) == Produto.VAZIO) {
            produtos.set(idx, p);
        } else {
            int i = 0;
            while (produtos.get(i) != Produto.VAZIO)
                i++;
            produtos.set(i, p);
        }
        return true;
    }

    public boolean podeAceitarProduto(Produto p) {
        if (!aberta)
            return false;
        if (indexEmprestado != -1)
            return produtos.get(indexEmprestado) == p;
        return totalFrente < capacidade;
    }

    public void removeProduto(Produto p) {
        int idx = produtos.indexOf(p);
        if (idx == -1 || idx >= capacidade)
            throw new IllegalArgumentException("Produto não está na frente");
        produtos.set(idx, Produto.VAZIO);
        p.setPrateleira(null);
        totalFrente--;
        totalProdutos--;
        if (totalFrente == 0) {
            // se ainda houver produtos chegar esses para a frente
            if (totalProdutos > 0)
                for (int i = capacidade - 1; i >= 0; i--)
                    produtos.remove(i);
            contaTotalFrente();
        }
        indexEmprestado = -1;
    }

    private void contaTotalFrente() {
        totalFrente = 0;
        for (int i = 0; i < capacidade; i++)
            if (produtos.get(i) != Produto.VAZIO)
                totalFrente++;
    }

    private void determinarPosicoesProdutos(int largProd, int altProd) {
        areaProdutos = new Rectangle[capacidade];
        for (int i = 0; i < capacidade; i++) {
            int indexFila = i % capacidade;
            Point pos = getPosicaoCentro();// getImagem().getPosicaoCentro();
            int wtotal = getImage().getComprimento() - 20; // 10 de bordas em cada lado
            int distEntreProdutos = capacidade > 1 ? (wtotal - largProd * capacidade) / (capacidade - 1) : 0;
            int distCentro = (largProd * capacidade + distEntreProdutos * (capacidade - 1)) / 2;
            int x = pos.x - distCentro + indexFila * (largProd + distEntreProdutos);
            int y = pos.y + offsetBase - altProd;
            areaProdutos[i] = new Rectangle(x, y, largProd, altProd);
        }
    }

    private void assegurarEspacoFrente() {
        while (produtos.size() < capacidade)
            produtos.add(Produto.VAZIO);
    }

    public void reaverProduto() {
        indexEmprestado = -1;
    }

    private Rectangle[] getAreaProdutos() {
        return areaProdutos;
    }

    public void setPosicao(Point p) {
        int dx = p.x - getPosicao().x;
        int dy = p.y - getPosicao().y;
        super.setPosicao(p);
        Rectangle posicoes[] = getAreaProdutos();
        for (int i = 0; i < posicoes.length; i++) {
            posicoes[i].x += dx;
            posicoes[i].y += dy;
        }
    }

    public Mundo getMundo() {
        return mundo;
    }

    public void setMundo(Mundo mundo) {
        this.mundo = mundo;
    }

    public Point getPosicaoEmprestado() {
        return new Point(areaProdutos[indexEmprestado].x, areaProdutos[indexEmprestado].y);
    }

    public Point getPosicaoProduto(Produto p) {
        int idx = indexProduto(p);
        return new Point(areaProdutos[idx].x, areaProdutos[idx].y);
    }

    private int indexProduto(Produto p) {
        for (int i = 0; i < capacidade; i++)
            if (produtos.get(i) == p)
                return i;
        throw new NoSuchElementException();
    }

}
