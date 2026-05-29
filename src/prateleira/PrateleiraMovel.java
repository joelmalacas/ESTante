package prateleira;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import efeito.EfeitoJuntar;
import mundo.Mundo;
import poo.util.Validator;
import prof.jogos2D.efeito.Efeito;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.Vector2D;

public class PrateleiraMovel extends PrateleiraSimples {
    private Point2D.Float pAtual, pInicio, pFim;
    private Vector2D direcao;
    private float velocidade;

    public PrateleiraMovel(Point pos, ComponenteVisual imagem, int capacidade, int base, int largProd, int altProd,
            Point2D.Float pInicio,
            Point2D.Float pFim, Vector2D dir, float veloc) {
        super(pos, imagem, capacidade, base, largProd, altProd);

        pAtual = new Point2D.Float(pos.x, pos.y);
        this.pInicio = Objects.requireNonNull(pInicio);
        this.pFim = Objects.requireNonNull(pFim);
        direcao = dir;
        direcao.normalizar();
        velocidade = veloc;
    }

    public void atualizar() {
        super.atualizar();

        pAtual.x += direcao.x * velocidade;
        pAtual.y += direcao.y * velocidade;
        if (pAtual.distance(pFim) < velocidade)
            pAtual = (Point2D.Float) pInicio.clone();

        Point novo = new Point((int) pAtual.x, (int) pAtual.y);
        setPosicao(novo);
    }
}
