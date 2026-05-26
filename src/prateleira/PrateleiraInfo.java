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
    private PrateleiraEsgotavel pesgotavel;
    private PrateleiraCongeladora pcongeladora;
    private PrateleiraSlot pslot;
    private PrateleiraTeleportadora pteleportadora;

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

    public PrateleiraInfo(PrateleiraEsgotavel p) {
        if (p == null)
            throw new IllegalArgumentException("Prateleira null");

        categoria = CategoriaPrateleira.ESGOTAVEL;
        pesgotavel = p;
    }

    public PrateleiraInfo(PrateleiraCongeladora p) {
        if (p == null)
            throw new IllegalArgumentException("Prateleira null");

        categoria = CategoriaPrateleira.CONGELADORA;
        pcongeladora = p;
    }

    public PrateleiraInfo(PrateleiraSlot p) {
        if (p == null)
            throw new IllegalArgumentException("Prateleira null");

        categoria = CategoriaPrateleira.SLOT;
        pslot = p;
    }

    public PrateleiraInfo(PrateleiraTeleportadora p) {
        this.pteleportadora = p;
        this.categoria = CategoriaPrateleira.TELEPORTADORA;
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
    }

    public PrateleiraEsgotavel getEsgotavel() {
        return pesgotavel;
    }

    public PrateleiraCongeladora getCongeladora() {
        return pcongeladora;
    }

    public PrateleiraSlot getSlot() {
        return pslot;
    }

    public PrateleiraTeleportadora getTeleportadora() {
        return pteleportadora;
    };

}