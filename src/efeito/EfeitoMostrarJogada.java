package efeito;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import prateleira.Produto;
import prof.jogos2D.efeito.EfeitoComDuracao;

/**
 * Efeito responsável por mostrar ao jogador uma jogada possível
 */
public class EfeitoMostrarJogada extends EfeitoComDuracao {

    // as imagens dos produtos
    private ImagemProduto imagens[];
    // as várias escalas a fazer às imagens para dar um efeito de aumentar e
    // diminuir
    private static float escalas[] = { 0.98f, 0.98f, 0.96f, 0.96f, 0.98f, 0.98f, 1f, 1, 1.02f, 1.02f, 1.04f, 1.04f,
            1.02f,
            1.02f, 1 };

    /**
     * Cria o efeito
     * 
     * @param imagens as imagens dos produtos a animar
     * @param duracao a duração do efeito
     */
    public EfeitoMostrarJogada(ImagemProduto imagens[], int duracao) {
        super(duracao);
        this.imagens = Arrays.copyOf(imagens, imagens.length);
    }

    @Override
    public void desenhar(Graphics2D g) {
        for (ImagemProduto img : imagens) {
            img.desenhar(g);
        }
    }

    /** Representa a imagem modificada de um produto que pode ser movido */
    public static class ImagemProduto {
        private BufferedImage img;
        private Produto prod;
        private int scaleIdx;

        /**
         * Cria a imagem
         * 
         * @param img  a imagem modificada do produto
         * @param prod o produto a ser animado
         */
        public ImagemProduto(BufferedImage img, Produto prod) {
            this.img = Objects.requireNonNull(img);
            this.prod = Objects.requireNonNull(prod);
            scaleIdx = ThreadLocalRandom.current().nextInt(escalas.length);
        }

        public void desenhar(Graphics2D g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Point pos = prod.getPosicao();
            int meioX = pos.x + img.getWidth() / 2;
            int meioY = pos.y + img.getHeight() / 2;
            g2.translate(meioX, meioY);
            g2.scale(escalas[scaleIdx], escalas[scaleIdx]);
            g2.translate(-meioX, -meioY);
            g2.drawImage(img, pos.x, pos.y, null);
            scaleIdx++;
            if (scaleIdx >= escalas.length)
                scaleIdx = 0;
            g2.dispose();
        }
    }
}
