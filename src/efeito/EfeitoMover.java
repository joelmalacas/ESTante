package efeito;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import prof.jogos2D.efeito.EfeitoComDuracao;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.Vector2D;

/**
 * Efeito de mover um elemento (representado pela sua imagem) no jogo
 */
public class EfeitoMover extends EfeitoComDuracao {

    private Point pos;
    private Vector2D dir;
    private ComponenteVisual imagem;

    /**
     * Cria o efeito de mover
     * 
     * @param img      a imagem a mover
     * @param ini      a posição de início do movimento
     * @param fim      a posição do fim do movimento
     * @param duracao  a duração do efeito
     * @param listener quem avisar quando a animação terminar
     */
    public EfeitoMover(ComponenteVisual img, Point ini, Point fim, int duracao, EfeitoListener listener) {
        super(duracao, listener);
        this.imagem = Objects.requireNonNull(img);
        dir = new Vector2D(ini, fim);
        dir.escalar(1.0 / duracao);
        pos = (Point) ini.clone();
    }

    @Override
    protected void fazerAtualizacao(int ciclo) {
        pos.x += dir.x;
        pos.y += dir.y;
    }

    @Override
    public void desenhar(Graphics2D g) {
        if (!terminou())
            imagem.desenhar(g, pos);
    }
}
