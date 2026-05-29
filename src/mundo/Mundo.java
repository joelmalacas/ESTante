package mundo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import prateleira.PrateleiraInfo;
import prateleira.Produto;
import prof.jogos2D.efeito.Efeito;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/**
 * classe que agrupa todos os elementos que se movem no jogo
 */
public class Mundo {

	/**
	 * listas com os vários elementos do jogo
	 */
	private List<PrateleiraInfo> prateleiras = new ArrayList<>();
	private List<Efeito> fxs = new ArrayList<>(); // efeitos visuais como explosões, etc
	private List<Efeito> fxsNovos = new ArrayList<>(); // efeitos visuais a serem adicionados num ciclo
	private ComponenteVisual backGround; // imagem de fundo do mundo
	private int numeroJuntar; // quantos produtos é necessário juntar
	private BufferedImage[] produtos; // imagens dos vários produtos
	private int largura, altura; // largura e altura de cada produto

	/**
	 * Cria o mundo, indicando a largura e altura dos produtos
	 * 
	 * @param largura largura dos produtos
	 * @param altura  altura dos produtos
	 */
	public Mundo(int largura, int altura) {
		this.largura = largura;
		this.altura = altura;
	}

	/**
	 * atualiza o mundo, isto é, efetua um ciclo de processamento
	 */
	public void atualizar() {
		// adicionar os efeitos que foram criados no último ciclo, pois só neste devem
		// ser considerados
		fxs.addAll(fxsNovos);
		fxsNovos.clear();
		// atualizar as prateleiras e os efeitos
		for (PrateleiraInfo p : prateleiras) {
			switch (p.getCategoria()) {
				case SIMPLES:
					p.getSimples().atualizar();
					break;
				case EMPILHAVEL:
					p.getEmpilhavel().atualizar();
					break;
				case MOVEL:
					p.getMovel().atualizar();
					break;
				case PORTA:
					p.getPorta().atualizar();
					break;
				case SEQUENCIA:
					p.getSequencia().atualizar();
					break;
				case ESGOTAVEL:
					p.getEsgotavel().atualizar();
					break;
				case CONGELADORA:
					p.getCongeladora().atualizar();
					break;
				case SLOT:
					p.getSlot().atualizar();
					break;
				case TELEPORTADORA:
					p.getTeleportadora().atualizar();
					break;
			}
		}
		for (Efeito fx : fxs)
			fx.atualizar();
	}

	/**
	 * desenha o mundo e os seus constituintes
	 * 
	 * @param g o ambiente gráfico onde se vai desenhar
	 */
	public void draw(Graphics2D g) {
		// desenhar a imagem de fundo
		if (backGround != null)
			backGround.desenhar(g, 0, 0);

		// desenhar prateleiras e efeitos
		for (PrateleiraInfo p : prateleiras) {
			switch (p.getCategoria()) {
				case SIMPLES:
					p.getSimples().desenhar(g);
					break;
				case EMPILHAVEL:
					p.getEmpilhavel().desenhar(g);
					break;
				case MOVEL:
					p.getMovel().desenhar(g);
					break;
				case PORTA:
					p.getPorta().desenhar(g);
					break;
				case SEQUENCIA:
					p.getSequencia().desenhar(g);
					break;
				case ESGOTAVEL:
					p.getEsgotavel().desenhar(g);
					break;

				case CONGELADORA:
					p.getCongeladora().desenhar(g);
					break;

				case SLOT:
					p.getSlot().desenhar(g);
					break;
				case TELEPORTADORA:
					p.getTeleportadora().desenhar(g);
					break;
			}
		}
		for (Efeito fx : fxs)
			fx.desenhar(g);

		// depois de tudo desenhado remover os efeitos visuais "fora de prazo"
		for (int i = fxs.size() - 1; i >= 0; i--)
			if (fxs.get(i).terminou())
				fxs.remove(i);
	}

	/**
	 * Indica se o nível foi completado com sucesso
	 * 
	 * @return true, se ganhou
	 */
	public boolean ganhou() {
		for (PrateleiraInfo p : prateleiras)
			switch (p.getCategoria()) {
				case SIMPLES:
					if (p.getSimples().totalProdutos() != 0)
						return false;
					break;
				case EMPILHAVEL:
					if (p.getEmpilhavel().totalProdutos() != 0)
						return false;
					break;
				case MOVEL:
					if (p.getMovel().totalProdutos() != 0)
						return false;
					break;
				case PORTA:
					if (p.getPorta().totalProdutos() != 0)
						return false;
					break;
				case SEQUENCIA:
					if (p.getSequencia().totalProdutos() != 0)
						return false;
					break;
				case ESGOTAVEL:
					if (p.getEsgotavel().totalProdutos() != 0)
						return false;
					break;
				case CONGELADORA:
					if (p.getCongeladora().totalProdutos() != 0)
						return false;
					break;
				case SLOT:
					if (p.getSlot().totalProdutos() != 0)
						return false;
					break;
				case TELEPORTADORA:
					if (p.getTeleportadora().totalProdutos() != 0)
						return false;
					break;
			}
		return true;
	}

	/**
	 * Indica se já não há mais jogadas possíveis e o nível está por completar
	 * 
	 * @return true, se perdeu
	 */
	public boolean perdeu() {
		if (prateleiras.size() <= 1)
			return false;
		int totalLugares = 0;
		for (PrateleiraInfo p : prateleiras) {
			switch (p.getCategoria()) {
				case SIMPLES:
					totalLugares += p.getSimples().espacoDisponivel();
					break;
				case EMPILHAVEL:
					totalLugares += p.getEmpilhavel().espacoDisponivel();
					break;
				case MOVEL:
					totalLugares += p.getMovel().espacoDisponivel();
					break;
				case PORTA:
					totalLugares += p.getPorta().espacoDisponivel();
					break;
				case SEQUENCIA:
					totalLugares += p.getSequencia().espacoDisponivel();
					break;
				case ESGOTAVEL:
					totalLugares += p.getEsgotavel().espacoDisponivel();
					break;
				case CONGELADORA:
					totalLugares += p.getCongeladora().espacoDisponivel();
					break;
				case SLOT:
					totalLugares += p.getSlot().espacoDisponivel();
					break;
				case TELEPORTADORA:
					totalLugares += p.getTeleportadora().espacoDisponivel();
					break;

			}
			if (totalLugares >= 2)
				return false;
		}
		// se não tiver lugares vagos perde,
		// se tiver apenas 1 lugar vago tem de ter uma jogada válida
		return totalLugares == 0 || getProdutosJuntaveis().size() == 0;
	}

	/**
	 * Retorna uma lista com todos os produtos juntáveis que estejam visíveis
	 * 
	 * @return uma lista com todos os produtos juntáveis que estejam visíveis
	 */
	public List<Produto> getProdutosJuntaveis() {
		HashMap<Integer, List<Produto>> prods = new HashMap<>();
		for (PrateleiraInfo prat : prateleiras) {
			Produto produtosFrente[] = null;
			switch (prat.getCategoria()) {
				case SIMPLES:
					int capSimples = prat.getSimples().getCapacidade();
					produtosFrente = new Produto[capSimples];
					List<Produto> produtosSimples = prat.getSimples().getProdutos();
					for (int i = 0; i < capSimples; i++)
						produtosFrente[i] = produtosSimples.get(i);
					break;
				case EMPILHAVEL:
					int capEmpi = prat.getEmpilhavel().getCapacidade();
					produtosFrente = new Produto[capEmpi];
					List<Produto> produtosEmpi = prat.getEmpilhavel().getProdutos();
					for (int i = 0; i < capEmpi; i++)
						produtosFrente[i] = produtosEmpi.get(i);
					break;
				case MOVEL:
					int capMovel = prat.getMovel().getCapacidade();
					produtosFrente = new Produto[capMovel];
					List<Produto> produtosMovel = prat.getMovel().getProdutos();
					for (int i = 0; i < capMovel; i++)
						produtosFrente[i] = produtosMovel.get(i);
					break;
				case PORTA:
					produtosFrente = prat.getPorta().getProdutosFrente();
					break;
				case SEQUENCIA:
					produtosFrente = prat.getSequencia().getProdutosFrente();
					break;
			}
			for (Produto p : produtosFrente) {
				if (p == Produto.VAZIO)
					continue;
				Point pos = p.getPosicao();
				// se está fora do mundo não processa
				if (pos.x + p.getImagem().getComprimento() / 2 < 0 || pos.x > largura
						|| pos.y + p.getImagem().getAltura() / 2 < 0 || pos.y > altura)
					continue;
				if (prods.containsKey(p.getId()))
					prods.get(p.getId()).add(p);
				else {
					ArrayList<Produto> ps = new ArrayList<>();
					ps.add(p);
					prods.put(p.getId(), ps);
				}
			}
		}
		ArrayList<Produto> resultado = new ArrayList<>();
		for (Integer id : prods.keySet())
			if (id != -1 && prods.get(id).size() >= numeroJuntar)
				resultado.addAll(prods.get(id));
		return resultado;
	}

	/**
	 * Retorna quantos produtos é necessário juntar para desaparecerem
	 * 
	 * @return quantos produtos é necessário juntar para desaparecerem
	 */
	public int getNumeroJuntar() {
		return numeroJuntar;
	}

	/**
	 * define a imagem de fundo do mundo
	 * 
	 * @param fundo a imagem de fundo do mundo
	 */
	public void setFundo(ComponenteVisual fundo) {
		this.backGround = fundo;
	}

	/**
	 * Adiciona uma prateleira ao mundo
	 * 
	 * @param p a prateleira a adicionar
	 */
	public void addPrateleira(PrateleiraInfo p) {
		prateleiras.add(p);
		switch (p.getCategoria()) {
			case SIMPLES:
				p.getSimples().setMundo(this);
				break;
			case EMPILHAVEL:
				p.getEmpilhavel().setMundo(this);
				break;
			case MOVEL:
				p.getMovel().setMundo(this);
				break;
			case PORTA:
				p.getPorta().setMundo(this);
				break;
			case SEQUENCIA:
				p.getSequencia().setMundo(this);
				break;
			case SLOT:
				p.getSlot().setMundo(this);
				break;
			case TELEPORTADORA:
				p.getTeleportadora().setMundo(this);
				break;
			case CONGELADORA:
				p.getCongeladora().setMundo(this);
				break;
			case ESGOTAVEL:
				p.getEsgotavel().setMundo(this);
				break;
		}
	}

	/**
	 * Adiciona um efeito especial ao mundo
	 * 
	 * @param fx o efeito a adicionar
	 */
	public void addFx(Efeito fx) {
		fxsNovos.add(fx);
	}

	/**
	 * Remove um efeito especial ao mundo
	 * 
	 * @param fx o efeito a remover
	 */
	public void removeFx(Efeito fx) {
		fxs.remove(fx);
	}

	/** indica se o nível já acabou */
	public boolean isOver() {
		return fxs.size() == 0 && fxsNovos.size() == 0;
	}

	/**
	 * Retorna a prateleira que está na posição indicada
	 * 
	 * @param pos a posição a testar
	 * @return a prateleira que está na posição indicada ou null se não houver
	 *         nenhuma
	 */
	public PrateleiraInfo getPrateleiraAt(Point pos) {
		for (PrateleiraInfo p : prateleiras) {
			switch (p.getCategoria()) {
				case SIMPLES:
					if (p.getSimples().contemPonto(pos))
						return p;
					break;
				case EMPILHAVEL:
					if (p.getEmpilhavel().contemPonto(pos))
						return p;
					break;
				case MOVEL:
					if (p.getMovel().contemPonto(pos))
						return p;
					break;
				case PORTA:
					if (p.getPorta().contemPonto(pos))
						return p;
					break;
				case SEQUENCIA:
					if (p.getSequencia().contemPonto(pos))
						return p;
					break;
				case ESGOTAVEL:
					if (p.getEsgotavel().contemPonto(pos))
						return p;
					break;
				case CONGELADORA:
					if (p.getCongeladora().contemPonto(pos))
						return p;
					break;
				case SLOT:
					if (p.getSlot().contemPonto(pos))
						return p;
					break;
				case TELEPORTADORA:
					if (p.getTeleportadora().contemPonto(pos))
						return p;
					break;
			}

		}
		return null;
	}

	/**
	 * Retorna qual o produto que está na posição indicada
	 * 
	 * @param pos a posição a testar
	 * @return o produtos na posição indicada ou null se não houver nenhum
	 */
	public Produto processaCliqueAt(Point pos) {
		for (PrateleiraInfo p : prateleiras) {
			switch (p.getCategoria()) {
				case SIMPLES:
					if (p.getSimples().contemPonto(pos))
						return p.getSimples().processaClique(pos);
					break;
				case EMPILHAVEL:
					if (p.getEmpilhavel().contemPonto(pos))
						return p.getEmpilhavel().processaClique(pos);
					break;
				case MOVEL:
					if (p.getMovel().contemPonto(pos))
						return p.getMovel().processaClique(pos);
					break;
				case PORTA:
					if (p.getPorta().contemPonto(pos))
						return p.getPorta().processaClique(pos);
					break;
				case SEQUENCIA:
					if (p.getSequencia().contemPonto(pos))
						return p.getSequencia().processaClique(pos);
					break;
				case ESGOTAVEL:
					if (p.getEsgotavel().contemPonto(pos))
						return p.getEsgotavel().processaClique(pos);
					break;
				case CONGELADORA:
					if (p.getCongeladora().contemPonto(pos))
						return p.getCongeladora().processaClique(pos);
					break;
				case SLOT:
					if (p.getSlot().contemPonto(pos))
						return p.getSlot().processaClique(pos);
					break;
				case TELEPORTADORA:
					if (p.getTeleportadora().contemPonto(pos))
						return p.getTeleportadora().processaClique(pos);
					break;
			}
		}
		return null;
	}

	/**
	 * Remove uma prataleira
	 * 
	 * @param p a prateleira a remover
	 */
	public void removePrateleira(PrateleiraInfo p) {
		prateleiras.remove(p);
	}

	/**
	 * Prepara os produtos a usar no mundo
	 * 
	 * @param imagem           imagem de todos os produtos
	 * @param produtoComp      comprimento dos produtos
	 * @param produtoAlt       altura dos produtos
	 * @param produtosUsados   quantos produtos se vão usar neste nível
	 * @param numeroEmparelhar quantos produtos é necessário emparelhar
	 */
	public void prepararProdutos(BufferedImage imagem, int produtoComp, int produtoAlt, int produtosUsados,
			int numeroEmparelhar) {
		BufferedImage produtosImg[] = dividirImagem(imagem, produtoComp, produtoAlt);
		produtos = new BufferedImage[produtosUsados];
		// usaer apenas os necessários para este nível
		for (int i = 0; i < produtosUsados; i++)
			produtos[i] = produtosImg[i];
		embaralharProdutos(produtos);
		numeroJuntar = numeroEmparelhar;
	}

	/**
	 * divide uma imagem em várias subimagens
	 * 
	 * @param imagemGeral a imagem original
	 * @param comprimento o comprimento de cada subimagem
	 * @param altura      a altura de cada subimagem
	 * @return um array com as várias subimagens
	 */
	private BufferedImage[] dividirImagem(BufferedImage imagemGeral, int comprimento, int altura) {
		int imgComp = imagemGeral.getWidth();
		int imgAlt = imagemGeral.getHeight();
		int colunas = imgComp / comprimento;
		int linhas = imgAlt / altura;

		BufferedImage partes[] = new BufferedImage[colunas * linhas];
		int parteIdx = 0;
		for (int row = 0; row < linhas; row++) {
			for (int col = 0; col < colunas; col++) {
				int x = col * comprimento;
				int y = row * altura;

				partes[parteIdx++] = imagemGeral.getSubimage(x, y, comprimento, altura);
			}
		}
		return partes;
	}

	/**
	 * Embaralha um array com as imagens dos produtos
	 * 
	 * @param prods o array a embaralhar
	 */
	private void embaralharProdutos(BufferedImage[] prods) {
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		for (int i = prods.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			BufferedImage img = prods[index];
			prods[index] = prods[i];
			prods[i] = img;
		}
	}

	/**
	 * Prepara uma prateleira
	 * 
	 * @param p       a prateleira a preparar
	 * @param prodIds os ids dos produtos a usar nesta prateleiras
	 */
	public void prepararPrateleira(PrateleiraInfo p, int[] prodIds) {
		ArrayList<Produto> prods = new ArrayList<>(prodIds.length);
		for (int i = 0; i < prodIds.length; i++)
			prods.add(prodIds[i] == -1 ? Produto.VAZIO
					: new Produto(
							prodIds[i], new ComponenteSimples(produtos[prodIds[i]])));
		switch (p.getCategoria()) {
			case SIMPLES:
				p.getSimples().setupProdutos(prods);
				p.getSimples().setMundo(this);
				break;
			case EMPILHAVEL:
				p.getEmpilhavel().setupProdutos(prods);
				p.getEmpilhavel().setMundo(this);
				break;
			case MOVEL:
				p.getMovel().setupProdutos(prods);
				p.getMovel().setMundo(this);
				break;
			case PORTA:
				p.getPorta().setupProdutos(prods);
				p.getPorta().setMundo(this);
				break;
			case SEQUENCIA:
				p.getSequencia().setupProdutos(prods);
				p.getSequencia().setMundo(this);
				break;
			case ESGOTAVEL:
				p.getEsgotavel().setupProdutos(prods);
				p.getEsgotavel().setMundo(this);
				break;
			case CONGELADORA:
				p.getCongeladora().setupProdutos(prods);
				p.getCongeladora().setMundo(this);
				break;
			case SLOT:
				p.getSlot().setupProdutos(prods);
				p.getSlot().setMundo(this);
				break;
			case TELEPORTADORA:
				p.getTeleportadora().setupProdutos(prods);
				p.getTeleportadora().setMundo(this);
				break;
		}
		prateleiras.add(p);
	}

	/**
	 * Retorna o número de efeitos que tem
	 * 
	 * @return o número de efeitos que tem
	 */
	public int getFxs() {
		return fxs.size();
	}
}
