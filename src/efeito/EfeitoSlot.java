package efeito;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import prof.jogos2D.efeito.EfeitoComDuracao;
import prof.jogos2D.image.ComponenteAnimado;

public class EfeitoSlot extends EfeitoComDuracao {

    private ComponenteAnimado animRodar;
    private Point2D.Float posicao;
    private int duracaoRodar;

    public EfeitoSlot(ComponenteAnimado animRodar, Point pos, int duracao) {
        super(duracao);
        this.animRodar = animRodar;
        this.posicao = new Point2D.Float(pos.x, pos.y);
        this.duracaoRodar = duracao;
        animRodar.setCiclico(true);
        animRodar.reset();
    }

    @Override
    protected void fazerAtualizacao(int ciclo) {
        animRodar.proximaFrame();
        if (ciclo == duracaoRodar - 1) {
            animRodar.setCiclico(false);
        }
    }

    @Override
    public void desenhar(Graphics2D g) {
        animRodar.desenhar(g, (int) posicao.x, (int) posicao.y);
    }
}