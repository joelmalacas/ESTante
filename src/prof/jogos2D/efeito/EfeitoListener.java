package prof.jogos2D.efeito;

/**
 * Interface que deve ser implementada por quem pretender saber se um efeito
 * terminou a sua execução
 * 
 * @author F. Sérgio Barbosa
 */
public interface EfeitoListener {
    public void processaFimEfeito(Efeito f);
}
