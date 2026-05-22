package prof.jogos2D.efeito;

/**
 * Superclasse que define um efeito que termina após um tempo definido.
 * 
 * @author F. Sérgio Barbosa
 */
public abstract class EfeitoComDuracao extends EfeitoBase {

    private int duracao;
    private int cicloAtual = 0;

    /**
     * Inicializa o efeito com o tempo de duração.
     * 
     * @param duracao a duração, em ciclos de processamento, do efeito
     */
    public EfeitoComDuracao(int duracao) {
        super();
        this.duracao = duracao;
    }

    /**
     * Inicializa o efeito com o tempo de duração e quem avisar em caso de ter
     * terminado
     * 
     * @param duracao  a duração, em ciclos de processamento, do efeito
     * @param listener quem avisar que já terminou
     */
    public EfeitoComDuracao(int duracao, EfeitoListener listener) {
        super(listener);
        this.duracao = duracao;
    }

    @Override
    public void atualizar() {
        fazerAtualizacao(cicloAtual);
        cicloAtual++;
        if (terminou())
            avisaListener();
    }

    /**
     * Atualiza o efeito. Como esta classe já implementa uma versão do atualizar em
     * qeu controla o tempo de execução do efeito, as subclasses devem implementar
     * este método e não o atualizar
     * 
     * @param ciclo
     */
    protected void fazerAtualizacao(int ciclo) {
    }

    /**
     * @return Quantos ciclos de processamento faltam para o efeito terminar
     */
    public int ciclosRestantes() {
        return duracao - cicloAtual;
    }

    /**
     * @return quantos ciclos de processamento já foram efetuados
     */
    public int getTotalCiclos() {
        return duracao;
    }

    /**
     * @return qual o ciclo que está ser executado atualmente
     */
    public int getCicloAtual() {
        return cicloAtual;
    }

    @Override
    protected boolean estaTerminado() {
        return cicloAtual >= duracao;
    }

    @Override
    public void reset() {
        cicloAtual = 0;
    }
}
