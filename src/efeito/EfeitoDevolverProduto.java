package efeito;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import prateleira.PrateleiraInfo;
import prof.jogos2D.efeito.EfeitoComDuracao;
import prof.jogos2D.efeito.EfeitoListener;

/**
 * Este efeito é responsável pela animação de devolver o produto à prateleira de
 * origem, caso a jogada não seja válida
 */
public class EfeitoDevolverProduto extends EfeitoComDuracao {

    private PrateleiraInfo prateleira;
    private BufferedImage produto;
    private Point2D.Float posicao;

    /**
     * Cria o efeito
     * 
     * @param prat     a prateleira para onde devolver
     * @param prod     o produto a devolver
     * @param pos      a posição de onde começar a devolver
     * @param duracao  a duração do efeito em ciclos
     * @param listener quem avisar que o efeito terminou
     */
    public EfeitoDevolverProduto(PrateleiraInfo prat, BufferedImage prod, Point2D.Float pos, int duracao,
            EfeitoListener listener) {
        super(duracao, listener);
        prateleira = prat;
        produto = prod;
        posicao = pos;
    }

    @Override
    public void desenhar(Graphics2D g) {
        g.drawImage(produto, (int) posicao.x, (int) posicao.y, null);
    }

    @Override
    protected void fazerAtualizacao(int ciclo) {
        Point posEmp = getPosicaoEmprestado();
        float dirx = (posEmp.x - posicao.x) / ciclosRestantes();
        float diry = (posEmp.y - posicao.y) / ciclosRestantes();
        posicao.x += dirx;
        posicao.y += diry;
    }

    private Point getPosicaoEmprestado() {
        switch (prateleira.getCategoria()) {
            case SIMPLES:
                return prateleira.getSimples().getPosicaoEmprestado();
            case EMPILHAVEL:
                return prateleira.getEmpilhavel().getPosicaoEmprestado();
            case MOVEL:
                return prateleira.getMovel().getPosicaoEmprestado();
            case PORTA:
                return prateleira.getPorta().getPosicaoEmprestado();
            case SEQUENCIA:
                return prateleira.getSequencia().getPosicaoEmprestado();
        }
        return null;
    }
}
