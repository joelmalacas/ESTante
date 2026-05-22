package prof.jogos2D.elemento;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que implementa o comportamento básico de um elemento gráfico
 * 
 * @author F. Sérgio Barbosa
 */
public class ElementoGraficoDefault implements ElementoGrafico {

	private Point posicao;
	private ComponenteVisual imagem;

	/**
	 * Cria e inicializa o elemento gráfico
	 * 
	 * @param image imagem associado ao elemento gráfico
	 */
	public ElementoGraficoDefault(Point pos, ComponenteVisual image) {
		posicao = Objects.requireNonNull(pos);
		imagem = Objects.requireNonNull(image);
	}

	@Override
	public void desenhar(Graphics2D g) {
		imagem.desenhar(g, posicao);
	}

	@Override
	public ComponenteVisual getImage() {
		return imagem;
	}

	@Override
	public void setImage(ComponenteVisual image) {
		imagem = Objects.requireNonNull(image);
	}

	@Override
	public void setPosicao(Point p) {
		posicao = Objects.requireNonNull(p);
	}

	@Override
	public Point getPosicao() {
		return posicao;
	}

	@Override
	public Point getPosicaoCentro() {
		return new Point(posicao.x + imagem.getComprimento() / 2, posicao.y + imagem.getAltura() / 2);
	}

	@Override
	public void setPosicaoCentro(Point p) {
		setPosicao(new Point(p.x - imagem.getComprimento() / 2, p.y - imagem.getAltura() / 2));
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(posicao.x, posicao.y, imagem.getComprimento(), imagem.getAltura());
	}

	@Override
	public boolean contemPonto(Point p) {
		return p.x >= posicao.x && p.y >= posicao.y && p.x <= posicao.x + imagem.getComprimento()
				&& p.y <= posicao.y + imagem.getAltura();
	}
}
