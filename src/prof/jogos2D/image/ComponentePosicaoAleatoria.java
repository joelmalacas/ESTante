package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class ComponentePosicaoAleatoria extends ComponenteDecorador {

	private Point posicaoAtual;
	private Point[] posicoes;
	private int lastCiclo = 0;

	public ComponentePosicaoAleatoria(ComponenteVisual cv, Point[] pts) {
		super(cv);
		posicoes = pts;
		posicaoAtual = proximaPosicao();
	}

	@Override
	public void desenhar(Graphics2D g, Point p) {
		super.desenhar(g, posicaoAtual);
		if (!estaPausa()) {
			if (numCiclosFeitos() > lastCiclo) {
				posicaoAtual = proximaPosicao();
				lastCiclo = numCiclosFeitos();
			}
		}
	}

	@Override
	public void desenhar(Graphics g) {
		super.desenhar(g);
		if (!estaPausa()) {
			if (numCiclosFeitos() > lastCiclo) {
				setPosicao(proximaPosicao());
				lastCiclo = numCiclosFeitos();
			}
		}
	}

	private Point proximaPosicao() {
		return posicoes[(int) (Math.random() * posicoes.length)];
	}

	public ComponentePosicaoAleatoria clone() {
		return new ComponentePosicaoAleatoria(getDecorado(), posicoes.clone());
	}
}
