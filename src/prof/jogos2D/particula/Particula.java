package prof.jogos2D.particula;

/**
 * Representa uma partícula num sistema de partículas.
 * Nesta implemetação uma particula é apenas um pixel.
 * A particula pode ser afetada pela gravidade, pelo vento e até pode ir-se
 * desvanecendo. Cabe ao motor de particulas gerir essas variáveis, consoante o
 * efeito que pretender.
 * 
 */
public class Particula {
    public float x, y;
    public float velocidadeX, velocidadeY;
    public int argb;
    public int tempoVida;
    public boolean desvanecendo;

    /**
     * Cria um partícula com vários componentes
     * 
     * @param x            a posição x da partícula
     * @param y            a posição y da partícula
     * @param velocidadeX  a velocidade em x
     * @param velocidadeY  a velocidade em y
     * @param argb         a cor da particula, indluindo o canal alfa
     * @param tempoVida    o tempo de vida
     * @param desvanecendo se é para ir desvanecendo
     */
    public Particula(float x, float y, float velocidadeX, float velocidadeY, int argb, int tempoVida,
            boolean desvanecendo) {
        this.x = x;
        this.y = y;
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
        this.argb = argb;
        this.tempoVida = tempoVida;
        this.desvanecendo = desvanecendo;
    }

    /**
     * Cria uma particula que não se desvanece
     * 
     * @param x           a posição x da partícula
     * @param y           a posição y da partícula
     * @param velocidadeX a velocidade em x
     * @param velocidadeY a velocidade em y
     * @param argb        a cor da particula, indluindo o canal alfa
     * @param tempoVida   o tempo de vida
     */
    public Particula(float x, float y, float velocidadeX, float velocidadeY, int argb, int tempoVida) {
        this(x, y, velocidadeX, velocidadeY, argb, tempoVida, false);
    }

    /**
     * @return true, se a partícula está ativa, false caso contrário
     */
    public boolean estaAtiva() {
        return tempoVida > 0;
    }
}
