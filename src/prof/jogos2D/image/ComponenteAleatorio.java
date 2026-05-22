package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

/**
 * Classe que representa um componente cuja aparição é aleatória, isto é, só
 * aparece de vez em quando.
 * Este tipo de componente está invisível por um tempo mínimmo e até um tempo
 * máximo. Dentro desta gama de valores pode aparecer em qualquer altura
 */
public class ComponenteAleatorio extends ComponenteDecorador {

	private boolean mostrar = false; // se � para mostrar o componente
	private int proxAparicao; // quando � a pr�xima apari��o
	private int minCiclos; // n� minimo de ciclos que deve permanecer escondido
	private int gama; // gama de tempo em que pode aparecer
	private int lastCiclo = 0; // qual foi o �ltimo ciclo de anima��o que apresentou

	public ComponenteAleatorio(ComponenteVisual dec, int minInter, int maxInter) {
		super(dec);
		minCiclos = minInter;
		gama = maxInter - minInter;
		proxAparicao = calculaProxAnim();
	}

	/**
	 * calcula quando será a próxima animação
	 * 
	 * @return número de ciclos de execuçãoo até proxima animação
	 */
	private int calculaProxAnim() {
		Random r = new Random();
		return minCiclos + r.nextInt(gama);
	}

	@Override
	public void desenhar(Graphics2D g, Point p) {
		if (estaPausa()) {
			if (mostrar)
				super.desenhar(g, p);
			return;
		}
		if (mostrar) {
			super.desenhar(g);
			if (numCiclosFeitos() > lastCiclo) {
				mostrar = false;
				proxAparicao = calculaProxAnim();
				lastCiclo = numCiclosFeitos();
			}
		} else {
			proxAparicao--;
			if (proxAparicao <= 0)
				mostrar = true;
		}
	}

	public void desenhar(Graphics g) {
		if (estaPausa()) {
			if (mostrar)
				super.desenhar(g);
			return;
		}
		if (mostrar) {
			super.desenhar(g);
			if (numCiclosFeitos() > lastCiclo) {
				mostrar = false;
				proxAparicao = calculaProxAnim();
				lastCiclo = numCiclosFeitos();
			}
		} else {
			proxAparicao--;
			if (proxAparicao <= 0)
				mostrar = true;
		}
	}

	public ComponenteAleatorio clone() {
		return new ComponenteAleatorio(getDecorado(), minCiclos, minCiclos + gama);
	}
}
