package prof.jogos2D.efeito;

import java.awt.Graphics2D;

/**
 * Representa os efeitos visuais que se podem adicionar a um jogo.
 * Cada efeito deve ser atualizado e pode ser desenhado
 * 
 * @author F. Sérgio Barbosa
 */
public interface Efeito {

    /**
     * Desenha o efeito no ambiente gráfico
     * 
     * @param g
     */
    void desenhar(Graphics2D g);

    /**
     * Efetua um ciclo de processamento
     */
    void atualizar();

    /**
     * força a paragem do efeito
     */
    void terminar();

    /**
     * Indica se o efeito já terminou a sua execução
     * 
     * @return true, se o efeito já completou a sua execução
     */
    boolean terminou();

    /**
     * reinicia o efeito
     */
    void reset();
}
