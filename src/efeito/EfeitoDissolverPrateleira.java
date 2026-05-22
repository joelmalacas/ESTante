package efeito;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import prateleira.PrateleiraEmpilhavel;
import prof.jogos2D.efeito.EfeitoComDuracao;
import prof.jogos2D.particula.GeradorParticulas;
import prof.jogos2D.particula.MotorParticulas;
import prof.jogos2D.particula.Particula;

/**
 * Efeito usado quando uma prateleira empilhável fica vazia e é preciso
 * desaparecer com ela
 */
public class EfeitoDissolverPrateleira extends EfeitoComDuracao {

    // quem faz o efeito de dissolver
    private MotorParticulas dissolvedor;

    /**
     * Cria o efeito
     * 
     * @param p       a prateleira a dissolver
     * @param duracao a duração do efeito
     */
    public EfeitoDissolverPrateleira(PrateleiraEmpilhavel p, int duracao) {
        super(duracao);
        // gerar as particulas com o efeito de desvanecer
        Particula ps[] = GeradorParticulas.imagemDesvanecer((BufferedImage) p.getImage().getSprite(), p.getPosicao().x,
                p.getPosicao().y, duracao);
        for (int i = 0; i < ps.length; i += 2)
            ps[i].tempoVida = duracao / 2;
        // criar o motor de partículas que gere o efeito de desvanecer
        dissolvedor = new MotorParticulas(ps, 0.05f, 8);
    }

    @Override
    public void desenhar(Graphics2D g) {
        dissolvedor.desenhar(g);
    }

    @Override
    protected void fazerAtualizacao(int ciclo) {
        dissolvedor.atualizar();
    }
}
