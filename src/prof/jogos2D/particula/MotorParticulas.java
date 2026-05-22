package prof.jogos2D.particula;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

/**
 * Classe que gere um conjunto de partículas
 * 
 * @author F. Sérgio Barbosa
 */
public class MotorParticulas {

    private Particula[] particulas;
    private int numeroAtivas;
    private float ventoX = 0, ventoY = 0;
    private float friccaoX = 0, friccaoY = 0;
    private float gravidade = 0, maxGravidade = 0;
    private float minX, maxX, minY, maxY;

    /**
     * Cria o motor
     * 
     * @param particulas   as particlas a controlar
     * @param gravidade    a gravidade a aplciar às particulas
     * @param maxGravidade o limite máximo da velocidade de queda
     * @param ventoX       o valor do vento na horizontal
     * @param ventoY       o vaor do vento na vertical
     * @param friccaoX     o valor da fricção na horizontal
     * @param friccaoY     o valor da fricçao na vertical
     */
    public MotorParticulas(Particula[] particulas, float gravidade,
            float maxGravidade, float ventoX, float ventoY, float friccaoX, float friccaoY) {
        this.particulas = particulas;
        this.ventoX = ventoX;
        this.ventoY = ventoY;
        this.gravidade = gravidade;
        this.maxGravidade = maxGravidade;
        this.friccaoX = friccaoX;
        this.friccaoY = friccaoY;
        numeroAtivas = particulas.length;
    }

    /**
     * Cria o motos indicando as particulas. Todos os outros valores de
     * confiuguração são 0 (zero)
     * 
     * @param particulas as particulas a controlar
     */
    public MotorParticulas(Particula[] particulas) {
        this(particulas, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Cria o motor indicando as particulas, a gravidade e a velocidade máxima de
     * queda. Os restantes elementos são conseidereados 0 (zero)
     * 
     * @param particulas   as particulas a controlar
     * @param gravidade    a velocidade da gravidade
     * @param maxGravidade a velocidade máxima de queda
     */
    public MotorParticulas(Particula[] particulas, float gravidade, float maxGravidade) {
        this(particulas, gravidade, maxGravidade, 0, 0, 0, 0);
    }

    /**
     * Atualiza o conjunto de partículas
     */
    public void atualizar() {
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        for (int i = 0; i < numeroAtivas; i++) {
            Particula p = particulas[i];
            if (!p.estaAtiva()) {
                // para otimizar, não se remove, mas troca-se com a última ativa e diminui-se o
                // número de partículas ativas
                Particula pAux = particulas[numeroAtivas - 1];
                particulas[numeroAtivas - 1] = particulas[i];
                particulas[i] = pAux;
                numeroAtivas--;
                i--; // garantir que se processa a que se trocou
                continue;
            }
            p.x += p.velocidadeX + ventoX;
            p.y += p.velocidadeY + ventoY;

            if (p.x < minX)
                minX = p.x;
            if (p.x > maxX)
                maxX = p.x;
            if (p.y < minY)
                minY = p.y;
            if (p.y > maxY)
                maxY = p.y;

            if (friccaoX > 0)
                if (p.velocidadeX > 0)
                    p.velocidadeX -= friccaoX;
                else if (p.velocidadeX < 0)
                    p.velocidadeX += friccaoX;

            if (friccaoY > 0)
                if (p.velocidadeY > 0)
                    p.velocidadeY -= friccaoY;
                else if (p.velocidadeY < 0)
                    p.velocidadeY += friccaoY;

            if (p.velocidadeY < maxGravidade)
                p.velocidadeY += gravidade;

            if (p.desvanecendo) {
                int alfa = (p.argb & 0xFF000000) >>> 24;
                int novoAlfa = alfa - alfa / p.tempoVida;
                p.argb &= 0x00FFFFFF;
                p.argb |= (novoAlfa << 24);
            }
            p.tempoVida--;
        }
    }

    /**
     * desenhar as várias partículas
     * 
     * @param g onde desenhar
     */
    public void desenhar(Graphics2D g) {
        if (numeroAtivas <= 0)
            return;
        int comp = (int) (maxX - minX + 0.5f);
        int larg = (int) (maxY - minY + 0.5f);
        BufferedImage imagem = new BufferedImage(comp, larg, BufferedImage.TYPE_INT_ARGB);
        desenhar(imagem, (int) (minX + 0.5), (int) (minY + 0.5));
        g.drawImage(imagem, (int) (minX + 0.5f), (int) (minY + 0.5f), null);
    }

    /**
     * Desenha numa dada posição
     * 
     * @param image   imagem onde desenhar
     * @param inicioX onde se desenhar a imagem em x
     * @param inicioY onde se desenhar a imagem em y
     */
    private void desenhar(BufferedImage image, int inicioX, int inicioY) {
        int dataType = image.getRaster().getDataBuffer().getDataType();
        if (dataType != DataBuffer.TYPE_BYTE && dataType != DataBuffer.TYPE_INT)
            throw new UnsupportedOperationException();
        if (dataType == DataBuffer.TYPE_BYTE) {
            byte[] bytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            int largLinha = image.getWidth() * 4;
            for (int i = 0; i < numeroAtivas; i++) {
                Particula p = particulas[i];
                byte a = (byte) ((p.argb >> 24) & 0xFF);
                byte r = (byte) ((p.argb >> 16) & 0xFF);
                byte g = (byte) ((p.argb >> 8) & 0xFF);
                byte b = (byte) (p.argb & 0xFF);
                int idx = (int) (p.x - inicioX + 0.5) * 4 + (int) (p.y - inicioY + 0.5) * largLinha;
                bytes[idx] = a;
                bytes[idx + 1] = r;
                bytes[idx + 2] = g;
                bytes[idx + 3] = b;
            }
        } else if (dataType == DataBuffer.TYPE_INT) {
            int[] bytes = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            int largLinha = image.getWidth();
            for (int i = 0; i < numeroAtivas; i++) {
                Particula p = particulas[i];
                int idx = (int) (p.x - inicioX + 0.5f) + (int) (p.y - inicioY + 0.5f) * largLinha;
                if (idx < bytes.length)
                    bytes[idx] = p.argb;
            }
        }
    }
}
