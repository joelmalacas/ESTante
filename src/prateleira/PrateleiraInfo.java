package prateleira;

import java.util.Objects;

/**
 * Guarda a informação de uma prateleira e a sua categoria
 */
public class PrateleiraInfo {
    private CategoriaPrateleira categoria;
    private PrateleiraSimples psimples;
    private PrateleiraMovel pmovel;
    private PrateleiraEmpilhavel pempilhavel;
    private PrateleiraSequencia psequencia;
    private PrateleiraPorta pporta;

    public PrateleiraInfo(PrateleiraSimples psimples) {
        this.psimples = Objects.requireNonNull(psimples);
        categoria = CategoriaPrateleira.SIMPLES;
    }

    public PrateleiraInfo(PrateleiraEmpilhavel pemp) {
        this.pempilhavel = Objects.requireNonNull(pemp);
        categoria = CategoriaPrateleira.EMPILHAVEL;
    }

    public PrateleiraInfo(PrateleiraMovel pmovel) {
        this.pmovel = Objects.requireNonNull(pmovel);
        categoria = CategoriaPrateleira.MOVEL;
    }

    public PrateleiraInfo(PrateleiraSequencia ps) {
        this.psequencia = Objects.requireNonNull(ps);
        categoria = CategoriaPrateleira.SEQUENCIA;
    }

    public PrateleiraInfo(PrateleiraPorta pp) {
        this.pporta = Objects.requireNonNull(pp);
        categoria = CategoriaPrateleira.PORTA;
    }

    public CategoriaPrateleira getCategoria() {
        return categoria;
    }

    public PrateleiraSimples getSimples() {
        return psimples;
    }

    public PrateleiraMovel getMovel() {
        return pmovel;
    }

    public PrateleiraEmpilhavel getEmpilhavel() {
        return pempilhavel;
    }

    public PrateleiraSequencia getSequencia() {
        return psequencia;
    }

    public PrateleiraPorta getPorta() {
        return pporta;
    };

}