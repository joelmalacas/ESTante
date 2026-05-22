package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Interface que representa todos os componentes visuais
 * 
 * @author F. Sérgio Barbosa
 */
public interface ComponenteVisual {

	/**
	 * Desenha o componente visual do jogo no ambiente gráfico g e na posição (x,y).
	 * Na sua implementação base, todos os outros métodos desenhar e
	 * desenharCentrado chamam este método, pelo que as classes que implementam esta
	 * interface apenas necessitam de implementar este método.
	 * 
	 * @param g o ambiente gráfico onde se desenha o elemento
	 * @param x coordenada x onde desenhar o elemento
	 * @param y coordenada y onde desenhar o elemento
	 */
	public void desenhar(Graphics2D g, int x, int y);

	/**
	 * Desenhar o componente visual do jogo no ambiente gráfico g e na posição p.
	 * Este é um método de conveniência que chama o método
	 * {@link #desenhar(Graphics2D, int, int)}.
	 * 
	 * @param g o ambiente gráfico onde se desenha o elemento
	 * @param p posição onde desenhar o elemento
	 */
	default public void desenhar(Graphics2D g, Point p) {
		desenhar(g, p.x, p.y);
	}

	/**
	 * desenhar o componente visual do jogo no ambiente gráfico g e na posição p
	 * Este é um método de conveniência que chama o método
	 * {@link #desenhar(Graphics2D, int, int)}.
	 * 
	 * @param g o ambiente gráfico onde se desenha o elemento
	 * @param p posição onde desenhar o elemento
	 */
	default public void desenhar(Graphics2D g, Point2D.Float p) {
		desenhar(g, (int) (p.getX() + 0.5f), (int) (p.getY() + 0.5f));
	}

	/**
	 * desenhar o componente visual do jogo no ambiente gráfico g e centrado na
	 * posição p
	 * Este é um método de conveniência que chama o método
	 * {@link #desenhar(Graphics2D, int, int)}), centrando o desenho.
	 * 
	 * @param g o ambiente gráfico onde se desenha o elemento
	 * @param p posição do centro da imagem
	 */
	default public void desenharCentrado(Graphics2D g, Point p) {
		desenhar(g, p.x - getComprimento() / 2, p.y - getAltura() / 2);
	}

	/**
	 * desenhar o componente visual do jogo no ambiente gráfico g e centrado na
	 * posição (x,y))
	 * Este é um método de conveniência que chama o método
	 * {@link #desenhar(Graphics2D, int, int)}), centrando o desenho.
	 * 
	 * @param g o ambiente gráfico onde se desenha o elemento
	 * @param x coordenada x do centro da imagem
	 * @param y coordenada y do centro da imagem
	 */
	default public void desenharCentrado(Graphics2D g, int x, int y) {
		desenhar(g, x - getComprimento() / 2, y - getAltura() / 2);
	}

	/**
	 * desenhar o componente visual do jogo no ambiente gráfico g e centrado na
	 * posição p
	 * Este é um método de conveniência que chama o método
	 * {@link #desenhar(Graphics2D, int, int)}), centrando o desenho.
	 * 
	 * @param g o ambiente gráfico onde se desenha o elemento
	 * @param p coordenada do centro da imagem
	 */
	default public void desenharCentrado(Graphics2D g, Point2D.Float p) {
		desenhar(g, (int) (p.getX() + 0.5f - getComprimento() / 2), (int) (p.getY() + 0.5f - getAltura() / 2));
	}

	/**
	 * desenhar o componente visual do jogo no ambiente gr�fico g
	 * 
	 * @param g o ambiente gr�fico onde se desenha o elemento
	 */
	@Deprecated
	public void desenhar(Graphics g);

	/**
	 * indica em que posi��o do ecran se encontra o componente
	 * 
	 * @return a posi��o do ecran
	 */
	@Deprecated
	public Point getPosicao();

	/**
	 * indica em que posi��o do ecran se encontra o centro do componente
	 * 
	 * @return a posi��o do ecran
	 */
	@Deprecated
	public Point getPosicaoCentro();

	/**
	 * posiciona o componente na posi��o p do �cran
	 * 
	 * @param p nova posi��o do componente
	 */
	@Deprecated
	public void setPosicao(Point p);

	/**
	 * posiciona o componente centrado na posi��o p do �cran
	 * 
	 * @param p nova posi��o do centro do componente
	 */
	@Deprecated
	public void setPosicaoCentro(Point p);

	/**
	 * retorna o comprimento, em pixeis, do componente
	 * 
	 * @return o comprimento, em pixeis
	 */
	public int getComprimento();

	/**
	 * retorna a altura, em pixeis, do componente
	 * 
	 * @return a altura, em pixeis
	 */
	public int getAltura();

	/**
	 * retorna o rect�ngulo que engloba o componente
	 * 
	 * @return o rect�ngulo que engloba o componente
	 */
	@Deprecated
	public Rectangle getBounds();

	/**
	 * retorna a imagem do componente
	 * 
	 * @return a imagem do componente
	 */
	public Image getSprite();

	/**
	 * permite alterar a imagem do componente
	 * 
	 * @param sprite
	 */
	public void setSprite(Image sprite);

	/**
	 * roda o desenho
	 * 
	 * @param angulo o �ngulo de rota��o (em radianos) a aplicar
	 */
	public void rodar(double angulo);

	/**
	 * Coloca o desenho numa dada orienta��o
	 * 
	 * @param angulo o �ngulo da orienta��o (em radianos)
	 */
	public void setAngulo(double angulo);

	/**
	 * Devolve o �ngulo de que o desenho � rodado
	 * 
	 * @return o �ngulo (em radianos) da imagem
	 */
	public double getAngulo();

	/**
	 * indica quantas vezes j� reproduziu as anima��es.
	 * 
	 * @return o n�mero de vezes que fez a anima��o completa
	 */
	public int numCiclosFeitos();

	/**
	 * indica se o componente � ciclico, isto �, se quando
	 * termina uma anima��ovolta a repetir ou n�o
	 * 
	 * @return se � ciclico
	 */
	public boolean eCiclico();

	/**
	 * define se o componente volta ao in�cio das anima��es quando termina
	 * 
	 * @param ciclico tipo de cicl�co a definir
	 */
	public void setCiclico(boolean ciclico);

	/**
	 * coloca o componente em pausa, ou em movimento
	 * 
	 * @param pausa true para colocar o compoente e pausa, false para por em
	 *              movimento
	 */
	public void setPausa(boolean pausa);

	/**
	 * indica se o componente está em pausa, isto é, sem animação
	 * 
	 * @return true se está em pausa
	 */
	public boolean estaPausa();

	/**
	 * faz o reset � anima��o, se for um elemento animado, se n�o for
	 * � ignorado
	 */
	public void reset();

	/**
	 * inverte a animação, isto é, passa a animação de trás para a frente.
	 * Se não for um elemento animado ignora
	 */
	public void inverter();

	/**
	 * cria um componente visual igual a este.
	 * A c�pia partilha a mesma imagem do original.
	 * 
	 * @return um clone do componente visual
	 */
	public ComponenteVisual clone();
}
