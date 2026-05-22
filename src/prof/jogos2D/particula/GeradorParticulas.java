package prof.jogos2D.particula;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Esta classe possui um conjunto de métodos qpara a criação de um conjunto de
 * partículas a partir de imagens, inicializando as partículas de acordo com o
 * efeito que se pretende.
 */
public class GeradorParticulas {

    /**
     * Cria um conjunto de particulas a partir de uma imagem com a explosão a ser
     * centrada na referência de centroX e centroY
     * 
     * @param img       a imagem a dividir em particulas
     * @param px        posição x onde está a imagem
     * @param py        posição y onde está a imagem
     * @param centroX   onde fica localizado (em percentagem) o centro da explosão
     *                  no eixo dos x (0.5 = meio da imagem) em relação à largura da
     *                  imagem
     * @param centroY   onde fica localizado (em percentagem) o centro da explosão
     *                  no eixo dos y (0.5 = meio da imagem) em relaçao à altura da
     *                  imagem
     * @param forca     a força da explosão, isto é, a velocidade de escape média
     *                  das particulas
     * @param tempoVida quanto tempo demora a explosão
     * @return o conjunto de particulas gerado
     */
    public static Particula[] imagemExplodir(BufferedImage img, int px, int py, float centroX, float centroY,
            float forca,
            int tempoVida) {
        ArrayList<Particula> particulasList = new ArrayList<>();
        Rectangle area = new Rectangle();
        dividirEmParticulas(img, particulasList, area);

        // ver onde é o centro da explosão
        float meioX = (float) area.x + area.width * centroX;
        float meioY = (float) area.y + area.height * centroY;

        Particula particulas[] = new Particula[particulasList.size()];
        for (int i = 0; i < particulas.length; i++) {
            Particula p = particulasList.get(i);
            float dirX = p.x - meioX;
            float dirY = p.y - meioY;
            float dist = (float) Math.hypot(dirX, dirY);
            p.velocidadeX = dirX * (forca + (float) (Math.random() - 0.2) * 0.4f) / dist;
            p.velocidadeY = dirY * (forca + (float) (Math.random() - 0.2) * 0.4f) / dist;

            p.tempoVida = (int) (tempoVida - Math.random() * 8);

            // atualizar a posição da particula em função da posição da imagem
            p.x += px;
            p.y += py;
            particulas[i] = p;
        }
        return particulas;
    }

    /**
     * Cria um conjunto de partículas que se vão desvanecendo
     * 
     * @param img       imagem a transformar em partículas
     * @param px        a posição x onde está a imagem
     * @param py        a posição x onde está a imagem
     * @param tempoVida o tempo de vida até desvanecer
     * @return um array com as partículas criadas
     */
    public static Particula[] imagemDesvanecer(BufferedImage img, int px, int py, int tempoVida) {
        ArrayList<Particula> particulasList = new ArrayList<>();
        Rectangle area = new Rectangle();
        dividirEmParticulas(img, particulasList, area);

        Particula particulas[] = new Particula[particulasList.size()];
        for (int i = 0; i < particulas.length; i++) {
            Particula p = particulasList.get(i);
            p.tempoVida = (int) (tempoVida - Math.random() * 8);
            // atualizar a posição da particula em função da posição da imagem
            p.x += px;
            p.y += py;
            particulas[i] = p;
            p.desvanecendo = true;
        }
        return particulas;
    }

    /**
     * Divide uma imagem em particulas. Coloca as partículas na lista fornecida.
     * Como ignora os pixeis transparentes, o tamanho da imagem pode ser menor. O
     * método preenche um rectângulo com a área minima sem pixeis transparentes.
     * 
     * @param img     a imagem a dividir em partículas
     * @param outList a lista onde colocar as partículas
     * @param outArea a área da imagem que as particulas realmente afetam
     */
    public static void dividirEmParticulas(BufferedImage img, ArrayList<Particula> outList, Rectangle outArea) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        int w = img.getWidth();
        int h = img.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = img.getRGB(x, y);
                if (((argb >> 24) & 0xFF) != 0) {
                    int posicaoX = x;
                    int posicaoY = y;
                    outList.add(new Particula(posicaoX, posicaoY, 0, 0, argb, 0));
                    if (posicaoX < minX)
                        minX = posicaoX;
                    if (posicaoX > maxX)
                        maxX = posicaoX;
                    if (posicaoY < minY)
                        minY = posicaoY;
                    if (posicaoY > maxY)
                        maxY = posicaoY;
                }
            }
        }
        outArea.x = minX;
        outArea.y = minY;
        outArea.width = maxX - minX;
        outArea.height = maxY - minY;
    }
}
