package efeito;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import prateleira.Produto;
import prof.jogos2D.efeito.EfeitoComDuracao;
import prof.jogos2D.efeito.EfeitoListener;

public class EfeitoTeleporte extends EfeitoComDuracao {

    private Point[] posicoes;
    private BufferedImage[] imagensProdutos;
    private int largProd, altProd;
    private int duracao;
    private int ciclosMeio;

    public EfeitoTeleporte(Produto[] produtos, Point[] posicoes,
                           int largProd, int altProd,
                           int duracao, EfeitoListener listener) {
        super(duracao, listener);
        this.duracao = duracao;
        this.ciclosMeio = duracao / 2;
        this.largProd = largProd;
        this.altProd = altProd;

        this.posicoes = new Point[posicoes.length];
        this.imagensProdutos = new BufferedImage[produtos.length];

        for (int i = 0; i < posicoes.length; i++)
            this.posicoes[i] = new Point(posicoes[i]);

        for (int i = 0; i < produtos.length; i++)
            imagensProdutos[i] = (BufferedImage) produtos[i].getImagem().getSprite();
    }

    @Override
    protected void fazerAtualizacao(int ciclo) {
    }

    @Override
    public void desenhar(Graphics2D g) {
        if (terminou())
            return;

        int cicloAtual = duracao - ciclosRestantes();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < posicoes.length; i++) {
            int cx = posicoes[i].x + largProd / 2;
            int cy = posicoes[i].y + altProd / 2;

            if (cicloAtual <= ciclosMeio) {
                float progresso = (float) cicloAtual / ciclosMeio;
                float escala = 1.0f - progresso;
                float alpha = 1.0f - progresso;
                desenharProdutoEscalado(g, imagensProdutos[i], cx, cy, escala, alpha);
                desenharAnel(g, cx, cy, progresso, true);
            } else {
                float progresso = (float) (cicloAtual - ciclosMeio) / (duracao - ciclosMeio);
                float escala = progresso;
                float alpha = progresso;
                desenharProdutoEscalado(g, imagensProdutos[i], cx, cy, escala, alpha);
                desenharAnel(g, cx, cy, progresso, false);
            }
        }
    }

    private void desenharProdutoEscalado(Graphics2D g, BufferedImage img,
                                         int cx, int cy, float escala, float alpha) {
        if (escala <= 0.01f)
            return;

        Composite anterior = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, Math.max(0f, alpha))));

        AffineTransform at = new AffineTransform();
        at.translate(cx, cy);
        at.scale(escala, escala);
        at.translate(-img.getWidth() / 2.0, -img.getHeight() / 2.0);

        g.drawRenderedImage(img, at);
        g.setComposite(anterior);
    }

    private void desenharAnel(Graphics2D g, int cx, int cy, float progresso, boolean encolher) {
        int raioMax = (int) (Math.max(largProd, altProd) * 0.75f);
        int raio = (int) (raioMax * progresso);
        float alpha = encolher ? (1.0f - progresso) * 0.8f : progresso * 0.8f;

        if (raio < 2 || alpha < 0.02f)
            return;

        Composite anterior = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, alpha)));
        g.setStroke(new BasicStroke(3.0f));
        g.setColor(new Color(100, 180, 255));
        g.drawOval(cx - raio, cy - raio, raio * 2, raio * 2);

        int raio2 = (int) (raio * 0.6f);
        if (raio2 > 1) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, alpha * 0.5f)));
            g.setColor(new Color(200, 230, 255));
            g.drawOval(cx - raio2, cy - raio2, raio2 * 2, raio2 * 2);
        }

        g.setComposite(anterior);
    }
}