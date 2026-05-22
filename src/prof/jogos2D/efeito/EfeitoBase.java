package prof.jogos2D.efeito;

/**
 * Superclasse que define o comportamento base para os efeitos visuais
 * 
 * @author F. Sérgio Barbosa
 */
public abstract class EfeitoBase implements Efeito {

    private EfeitoListener listener;
    private boolean terminou = false;

    /**
     * Inicializa o efeito base
     */
    public EfeitoBase() {
        this(null);
    }

    /**
     * inicializa o efeito base indicando quem avisar quando o efeito acaba
     * 
     * @param listener quem avisar quando o efeito acaba
     */
    public EfeitoBase(EfeitoListener listener) {
        this.listener = listener;
    }

    /** avisa que o efeito terminou a sua execução */
    protected void avisaListener() {
        if (listener != null)
            listener.processaFimEfeito(this);
    }

    @Override
    public void terminar() {
        terminou = true;
    }

    @Override
    public boolean terminou() {
        return terminou || estaTerminado();
    }

    /**
     * Indica se o elemento está terminado. As subclasses devem redefinir este
     * método para implementar as suas regras adicionais em relaçao ao terminou. Se
     * quiserem redefinir competamente as regras do terminou devem redefinir esse
     * método.
     * 
     * @return true se o efeito está terminado
     */
    protected abstract boolean estaTerminado();

}
