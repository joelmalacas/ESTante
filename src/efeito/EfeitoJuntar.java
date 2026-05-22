package efeito;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import prateleira.Produto;
import prof.jogos2D.efeito.EfeitoComDuracao;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.particula.GeradorParticulas;
import prof.jogos2D.particula.MotorParticulas;
import prof.jogos2D.particula.Particula;
import prof.jogos2D.util.Vector2D;

/**
 * Efeito usado para juntar os produtos que devem desaparecer porque foram
 * juntos
 */
public class EfeitoJuntar extends EfeitoComDuracao {

    private Produto produtos[];
    private Point2D.Float posicoes[];
    private Vector2D velocidades[];
    private int cicloExplosao;
    private MotorParticulas particulas;

    /**
     * Cria o efeito
     * 
     * @param produtos os produtos a juntar
     * @param posicoes as posições dos produtos
     * @param duracao  a duracao do efeito, em ciclos
     * @param listener quem avisar quando o feito terminar
     */
    public EfeitoJuntar(Produto[] produtos, Point2D.Float posicoes[], int duracao, EfeitoListener listener) {
        super(duracao, listener);
        if (produtos.length != posicoes.length)
            throw new IllegalArgumentException("Arrays de produtos e posições têm de ter o mesmo tamanho!");
        this.produtos = produtos;
        this.posicoes = posicoes;

        velocidades = new Vector2D[produtos.length];

        // metade da duração para a junção e a outra metade para a explosão
        cicloExplosao = duracao / 3;
        // determinar onde se vai dar a junção
        Point2D.Float meio = calcularPosicaoJuncao();
        for (int i = 0; i < produtos.length; i++) {
            velocidades[i] = new Vector2D(posicoes[i], meio);
            velocidades[i].escalar(1.0 / cicloExplosao);
        }
    }

    @Override
    protected void fazerAtualizacao(int ciclo) {
        if (ciclo < cicloExplosao)
            moverProdutos();
        else if (ciclo == cicloExplosao)
            fazerExplosao();
        else
            moverExplosao();
    }

    private void moverProdutos() {
        for (int i = 0; i < posicoes.length; i++) {
            posicoes[i].x += velocidades[i].x;
            posicoes[i].y += velocidades[i].y;
        }
    }

    private void fazerExplosao() {
        Particula ps[] = GeradorParticulas.imagemExplodir((BufferedImage) produtos[0].getImagem().getSprite(),
                (int) posicoes[0].x, (int) posicoes[0].y,
                0.5f, 0.5f, 7.0f, ciclosRestantes());
        particulas = new MotorParticulas(ps, 0.9f, 9);
        particulas.atualizar();
    }

    private void moverExplosao() {
        particulas.atualizar();
    }

    @Override
    public void desenhar(Graphics2D g) {
        // se ainda não há particulas é porque ainda se está a movimentar os produtos
        if (particulas == null)
            for (int i = 0; i < produtos.length; i++)
                produtos[i].getImagem().desenhar(g, posicoes[i]);
        else
            particulas.desenhar(g);
    }

    /**
     * Calcula a posição onde deve ser feita a junção dos produtos
     * 
     * @return a posição onde deve ser feita a junção dos produtos
     */
    private Point2D.Float calcularPosicaoJuncao() {
        // se número de posições for impar é o do meio
        if (posicoes.length % 2 != 0)
            return (Point2D.Float) posicoes[posicoes.length / 2].clone();

        // se for par é no meio das posições do meio
        int posMeio = posicoes.length / 2;
        Point2D.Float p1 = posicoes[posMeio - 1];
        Point2D.Float p2 = posicoes[posMeio];
        return new Point2D.Float((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }
}
