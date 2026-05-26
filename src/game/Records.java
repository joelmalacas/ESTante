package game;

public class Records {
    private int numNiveis;
    private long[] tempos;
    private int[] jogadas;

    public Records(int numNiveis) {
        this.numNiveis = numNiveis;
        this.tempos = new long[numNiveis + 1];
        this.jogadas = new int[numNiveis + 1];
    }

    public int getNumNiveis() { return numNiveis; }

    public long getTempo(int nivel) { return tempos[nivel]; }
    public int getJogadas(int nivel) { return jogadas[nivel]; }

    public void setTempo(int nivel, long tempo) { tempos[nivel] = tempo; }
    public void setJogadas(int nivel, int jogadas) { this.jogadas[nivel] = jogadas; }
}