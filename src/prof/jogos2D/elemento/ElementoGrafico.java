package prof.jogos2D.elemento;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Define o comportamento que todos os elementos gráficos terão de
 * implementar. Um elemento gráfico tem uma posição no écran, bem como uma
 * imagem associada
 * 
 * @author F. Sérgio Barbosa
 */
public interface ElementoGrafico {

	/**
	 * desenha o elemento gráfico
	 * 
	 * @param g ambiente gráfico onde desenhar o elemento gráfico
	 */
	public void desenhar(Graphics2D g);

	/**
	 * retorna o elemento visual associado ao elemento gráfico
	 * 
	 * @return o elemento visual associado ao elemento gráfico
	 */
	public ComponenteVisual getImage();

	/**
	 * altera a imagem associada ao elemento gráfico
	 * 
	 * @param image a nova imagem a associar ao elemento gráfico
	 */
	public void setImage(ComponenteVisual image);

	/**
	 * define a posição do elemento gráfico
	 * 
	 * @param p a nova posição do elemento gráfico
	 */
	public void setPosicao(Point p);

	/**
	 * retorna a posição onde está
	 * 
	 * @return a posição onde está
	 */
	public Point getPosicao();

	/**
	 * retorna a área que este elemento gráfico ocupa
	 * 
	 * @return a área que este elemento gráfico ocupa
	 */
	public Rectangle getBounds();

	/**
	 * retorna a posição do centro do elemento
	 * 
	 * @return a posiçãoo do centro do elemento
	 */
	public Point getPosicaoCentro();

	/**
	 * define a posiçãoo do centro do elemento gráfico
	 * 
	 * @param p a nova posição do centro do elemento gráfico
	 */
	public void setPosicaoCentro(Point p);

	/**
	 * Indica se a área ocupada pelo elemento gráfico contém o ponto. Atenção que
	 * este método não tem em conta pixeis transparentes, apenas a área.
	 * 
	 * @param p o ponto a verificar se está dentro do elemento
	 * @return true se o ponto está dentro da área do elemento
	 */
	boolean contemPonto(Point p);

}
