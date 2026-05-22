package prateleira;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Objects;

import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Representa um produto no jogo, basicamentre uma imagem e um identificador.
 * Produtos com o mesmo identificador assumem-se como sendo iguais
 */
public class Produto {
    private ComponenteVisual imagem, imagemSombreada;
    private PrateleiraInfo prateleira;
    private int id;

    public Produto(int id, ComponenteVisual imagem) {
        this.imagem = Objects.requireNonNull(imagem);
        this.imagemSombreada = imagem.clone();
        imagemSombreada.setSprite(criarSombreada((BufferedImage) imagemSombreada.getSprite()));
        this.id = id;
    }

    void setPrateleira(PrateleiraInfo prateleira) {
        this.prateleira = prateleira;
    }

    public PrateleiraInfo getPrateleira() {
        return prateleira;
    }

    public ComponenteVisual getImagem() {
        return imagem;
    }

    public int getId() {
        return id;
    }

    public void desenharEm(Graphics2D g, Point p) {
        imagem.desenhar(g, p);
    }

    public void desenharSombreadoEm(Graphics2D g, Point p) {
        imagemSombreada.desenhar(g, p);
    }

    @Override
    public String toString() {
        return "p" + id;
    }

    public Point getPosicao() {
        if (prateleira == null)
            throw new IllegalStateException(this + " não está em nenhuma prateleira! ");
        switch (prateleira.getCategoria()) {
            case SIMPLES:
                return prateleira.getSimples().getPosicaoProduto(this);
            case EMPILHAVEL:
                return prateleira.getEmpilhavel().getPosicaoProduto(this);
            case MOVEL:
                return prateleira.getMovel().getPosicaoProduto(this);
            case PORTA:
                return prateleira.getPorta().getPosicaoProduto(this);
            case SEQUENCIA:
                return prateleira.getSequencia().getPosicaoProduto(this);
        }
        return null;
    }

    private BufferedImage criarSombreada(BufferedImage source) {
        BufferedImage result = new BufferedImage(
                source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int w = source.getWidth();
        int h = source.getHeight();
        float factor = 0.45f;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = source.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (int) (((argb >> 16) & 0xFF) * factor);
                int g = (int) (((argb >> 8) & 0xFF) * factor);
                int b = (int) ((argb & 0xFF) * factor);

                result.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return result;
    }

    /**
     * Representa um produto vazio que é usado para não estar sempre a verificar se
     * o produto é null, caso não hajam produtos na prateleira.
     */
    public static final Produto VAZIO = new ProdutoVazio(
            new ComponenteSimples(new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR)));

    private static class ProdutoVazio extends Produto {

        public ProdutoVazio(ComponenteVisual imagem) {
            super(-1, imagem);
        }

        @Override
        public void desenharEm(Graphics2D g, Point p) {
        }

        @Override
        public void desenharSombreadoEm(Graphics2D g, Point p) {
        }
    }
}
