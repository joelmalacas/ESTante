package prof.jogos2D.efeito;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import prof.jogos2D.image.ComponenteAnimado;

/**
 * Efeito que tem por base uma imagem animada. O efeito acaba quando a imagem
 * termina a sua animação.
 * 
 * @author F. Sérgio Barbosa
 */
public class EfeitoAnimacao extends EfeitoBase {

    private Point posicao;
    private ComponenteAnimado animacao;

    /**
     * Cria o efeito animado
     * 
     * @param p        posição onde colocar a animação
     * @param animacao o elemento animado
     */
    public EfeitoAnimacao(Point p, ComponenteAnimado animacao) {
        super();
        posicao = Objects.requireNonNull(p);
        this.animacao = Objects.requireNonNull(animacao);
    }

    /**
     * Cria o efeito animado, que avisa quando termina
     * 
     * @param p        a posiçao onde colocar a animação
     * @param animacao o elemento animado
     * @param listener quem avisar quando terminar a animação
     */
    public EfeitoAnimacao(Point p, ComponenteAnimado animacao, EfeitoListener listener) {
        super(listener);
        posicao = Objects.requireNonNull(p);
        this.animacao = Objects.requireNonNull(animacao);
    }

    @Override
    public void desenhar(Graphics2D g) {
        animacao.desenhar(g, posicao);
        if (terminou())
            avisaListener();
    }

    @Override
    public void atualizar() {
    }

    @Override
    protected boolean estaTerminado() {
        return animacao.numCiclosFeitos() >= 1;
    }

    @Override
    public void reset() {
        animacao.reset();
    }
}
