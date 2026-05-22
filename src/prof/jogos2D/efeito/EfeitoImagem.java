package prof.jogos2D.efeito;

import java.awt.Graphics2D;
import java.util.Objects;

import prof.jogos2D.elemento.ElementoGrafico;

/**
 * Efeito que apresenta uma imagem durante algum tempo
 * 
 * @author F. Sérgio Barbosa
 */
public class EfeitoImagem extends EfeitoComDuracao {
    private ElementoGrafico imagem;

    /**
     * Cria o efeito indicando qual o elemento gráfico a apresentar
     * 
     * @param imagem  o elemento gráfico a apresentar
     * @param duracao durante quantos ciclos o elemento deve ser apresentado
     */
    public EfeitoImagem(ElementoGrafico imagem, int duracao) {
        super(duracao);
        this.imagem = Objects.requireNonNull(imagem);
    }

    /**
     * Cria o efeito indicando qual o elemento gráfico a apresentar e a quem avisar
     * quando termina
     * 
     * @param imagem   elemento gráfico a apresentar
     * @param duracao  durante quantos ciclos o elemento deve ser apresentado
     * @param listener quem deve avisar de que terminou
     */
    public EfeitoImagem(ElementoGrafico imagem, int duracao, EfeitoListener listener) {
        super(duracao, listener);
        this.imagem = Objects.requireNonNull(imagem);
    }

    @Override
    public void desenhar(Graphics2D g) {
        imagem.desenhar(g);
    }
}
