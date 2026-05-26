package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;

import mundo.*;
import prateleira.*;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.Vector2D;

/**
 * Classe usada para ler um ficheiro com a informação de um nível
 */
public class NivelReader {

	private int produtoLarg, produtoAlt;
	private static final String pastaFundos = "data/fundos/";
	private static final String pastaImagens = "data/imagens/";

	/**
	 * Lê o ficheiro de nível
	 * 
	 * @param file ficheiro a ler
	 * @return o gestor de nível criado com a informação do ficheiro
	 * @throws IOException quando a leitura corre mal
	 */
	public void lerNivel(Mundo mundo, String file) throws IOException {

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String backgroundFile = in.readLine(); // lê o nome da imagem de fundo
			mundo.setFundo(new ComponenteSimples(pastaFundos + backgroundFile));

			// lê a info sobre os produtos (número, nome ficheiro imagem, largura e altura
			// de cada produto)
			String prodsInfo[] = in.readLine().split("\t");
			String produtosFile = prodsInfo[0];
			produtoLarg = Integer.parseInt(prodsInfo[1]);
			produtoAlt = Integer.parseInt(prodsInfo[2]);

			// lê a info sobre os produtos a usar e quantos são necessários para emparelhar
			String prodQuantInfo[] = in.readLine().split("\t");
			int produtosUsados = Integer.parseInt(prodQuantInfo[0]);
			int numeroEmparelhar = Integer.parseInt(prodQuantInfo[1]);

			// ler imagem dos produtos e preparar os mesmos
			BufferedImage prodsImage = ImageIO.read(new File(pastaImagens + "/" + produtosFile));
			mundo.prepararProdutos(prodsImage, produtoLarg, produtoAlt, produtosUsados, numeroEmparelhar);

			String linha;
			while ((linha = in.readLine()) != null) {
				if (!linha.startsWith("<"))
					continue;
				switch (linha) {
					case "<simples>":
						lerSimples(mundo, in);
						break;
					case "<esgotavel>":
						lerEsgotavel(mundo, in);
						break;
					case "<movel>":
						lerMovel(mundo, in);
						break;
					case "<torre>":
						lerTorre(mundo, in);
						break;
					case "<congeladora>":
						lerCongeladora(mundo, in);
						break;
					case "<porta>":
						lerPorta(mundo, in);
						break;
					case "<sequencial>":
						lerSequencial(mundo, in);
						break;
					case "<slot>":
						lerSlot(mundo, in);
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
	}

	public Records lerConfiguracoesIniciais(String ficheiroRecords) throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(ficheiroRecords))) {
			int numNiveis = Integer.parseInt(in.readLine().trim());
			Records records = new Records(numNiveis);
			for (int i = 1; i <= numNiveis; i++) {
				String[] partes = in.readLine().split(",");
				records.setTempo(i, Long.parseLong(partes[0]));
				records.setJogadas(i, Integer.parseInt(partes[1]));
			}
			return records;
		}
	}

	public void gravarConfiguracoes(String ficheiroRecords, Records records) throws IOException {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(ficheiroRecords))) {
			out.write(String.valueOf(records.getNumNiveis()));
			out.newLine();
			for (int i = 1; i <= records.getNumNiveis(); i++) {
				out.write(records.getTempo(i) + "," + records.getJogadas(i));
				out.newLine();
			}
		}
	}

	private void lerSimples(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual cv = lerComponenteVisual(in);
		String posInfo[] = in.readLine().split("\t");
		Point p = lerPosicao(posInfo[0], posInfo[1]);
		int linhaBase = Integer.parseInt(posInfo[2]);
		int capacidade = Integer.parseInt(posInfo[3]);
		int produtos[] = lerProdutos(in.readLine());
		PrateleiraSimples ps = new PrateleiraSimples(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
		mundo.prepararPrateleira(new PrateleiraInfo(ps), produtos);
	}

	private void lerEsgotavel(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual cv = lerComponenteVisual(in);
		ComponenteMultiAnimado cvAnim = (ComponenteMultiAnimado) lerComponenteVisual(in);
		String posInfo[] = in.readLine().split("\t");
		Point p = lerPosicao(posInfo[0], posInfo[1]);
		int linhaBase = Integer.parseInt(posInfo[2]);
		int capacidade = Integer.parseInt(posInfo[3]);
		int produtos[] = lerProdutos(in.readLine());
		// TODO preparar e adicionar ao mundo
	}

	private void lerMovel(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual cv = lerComponenteVisual(in);
		String posInfo[] = in.readLine().split("\t");
		Point p = lerPosicao(posInfo[0], posInfo[1]);
		int linhaBase = Integer.parseInt(posInfo[2]);
		int capacidade = Integer.parseInt(posInfo[3]);
		Point2D.Float inicio = new Point2D.Float(Integer.parseInt(posInfo[4]), Integer.parseInt(posInfo[5]));
		Point2D.Float fim = new Point2D.Float(Integer.parseInt(posInfo[6]), Integer.parseInt(posInfo[7]));
		Vector2D dir = new Vector2D(Integer.parseInt(posInfo[8]), Integer.parseInt(posInfo[9]));
		float veloc = Float.parseFloat(posInfo[10]);
		int produtos[] = lerProdutos(in.readLine());
		PrateleiraMovel pm = new PrateleiraMovel(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt, inicio,
				fim, dir, veloc);
		mundo.prepararPrateleira(new PrateleiraInfo(pm), produtos);
	}

	private void lerTorre(Mundo mundo, BufferedReader in) throws IOException {
		String torreInfo[] = in.readLine().split("\t");
		int x = Integer.parseInt(torreInfo[0]);
		int y = Integer.parseInt(torreInfo[1]);
		int nPrateleiras = Integer.parseInt(torreInfo[2]);
		int alturaPrateleira = Integer.parseInt(torreInfo[3]);
		int velocQueda = Integer.parseInt(torreInfo[4]);
		PrateleiraEmpilhavel.Torre torre = new PrateleiraEmpilhavel.Torre(x, nPrateleiras, velocQueda);
		for (int i = 0; i < nPrateleiras; i++) {
			ComponenteVisual cv = lerComponenteVisual(in);
			String posInfo[] = in.readLine().split("\t");
			int linhaBase = Integer.parseInt(posInfo[0]);
			int capacidade = Integer.parseInt(posInfo[1]);
			int produtos[] = lerProdutos(in.readLine());
			PrateleiraEmpilhavel pemp = new PrateleiraEmpilhavel(cv, capacidade, linhaBase, produtoLarg, produtoAlt,
					torre);
			PrateleiraInfo pinfo = new PrateleiraInfo(pemp);
			torre.addAndar(y - alturaPrateleira * i, pinfo);
			mundo.prepararPrateleira(pinfo, produtos);
		}
	}

	private void lerCongeladora(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual cv = lerComponenteVisual(in);
		ComponenteVisual gelo = lerComponenteVisual(in);
		String posInfo[] = in.readLine().split("\t");
		Point p = lerPosicao(posInfo[0], posInfo[1]);
		int linhaBase = Integer.parseInt(posInfo[2]);
		int capacidade = Integer.parseInt(posInfo[3]);
		int produtos[] = lerProdutos(in.readLine());
		// TODO preparar e adicionar ao mundo
	}

	private void lerPorta(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual cv = lerComponenteVisual(in);
		ComponenteMultiAnimado animPorta = (ComponenteMultiAnimado) lerComponenteVisual(in);
		String posInfo[] = in.readLine().split("\t");
		Point p = lerPosicao(posInfo[0], posInfo[1]);
		int linhaBase = Integer.parseInt(posInfo[2]);
		int capacidade = Integer.parseInt(posInfo[3]);
		int tempoAberta = Integer.parseInt(posInfo[4]);
		int tempoFechada = Integer.parseInt(posInfo[5]);
		boolean aberta = posInfo[6].equals("aberta");
		int tempoAtual = Integer.parseInt(posInfo[7]);
		int produtos[] = lerProdutos(in.readLine());
		PrateleiraPorta pp = new PrateleiraPorta(p, cv, animPorta, tempoAberta, tempoFechada, aberta, tempoAtual,
				capacidade, linhaBase, produtoLarg, produtoAlt);
		mundo.prepararPrateleira(new PrateleiraInfo(pp), produtos);
	}

	private void lerSequencial(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual porta = lerComponenteVisual(in);
		String sequenciaInfo[] = in.readLine().split("\t");
		int numPrateleiras = Integer.parseInt(sequenciaInfo[0]);
		int idxFechada = Integer.parseInt(sequenciaInfo[1]);
		int direcao = Integer.parseInt(sequenciaInfo[2]);

		PrateleiraSequencia.Sequenciador sequenciador = new PrateleiraSequencia.Sequenciador(porta, direcao);
		for (int i = 0; i < numPrateleiras; i++) {
			ComponenteVisual cv = lerComponenteVisual(in);
			String posInfo[] = in.readLine().split("\t");
			Point p = lerPosicao(posInfo[0], posInfo[1]);
			int linhaBase = Integer.parseInt(posInfo[2]);
			int capacidade = Integer.parseInt(posInfo[3]);
			int produtos[] = lerProdutos(in.readLine());
			PrateleiraSequencia pseq = new PrateleiraSequencia(p, cv, capacidade, linhaBase, produtoLarg, produtoAlt);
			sequenciador.addPrateleira(pseq);
			mundo.prepararPrateleira(new PrateleiraInfo(pseq), produtos);
		}
		sequenciador.setFechada(idxFechada);
	}

	private void lerSlot(Mundo mundo, BufferedReader in) throws IOException {
		ComponenteVisual cv = lerComponenteVisual(in);
		ComponenteAnimado animRodar = (ComponenteAnimado) lerComponenteVisual(in);
		String posInfo[] = in.readLine().split("\t");
		Point p = lerPosicao(posInfo[0], posInfo[1]);
		int linhaBase = Integer.parseInt(posInfo[2]);
		int capacidade = Integer.parseInt(posInfo[3]);
		int xBotao = Integer.parseInt(posInfo[4]);
		int yBotao = Integer.parseInt(posInfo[5]);
		int largBotao = Integer.parseInt(posInfo[6]);
		int altBotao = Integer.parseInt(posInfo[7]);
		Rectangle areaBotao = new Rectangle(xBotao, yBotao, largBotao, altBotao);
		int produtos[] = lerProdutos(in.readLine());
		// TODO preparar e adicionar ao mundo
	}

	/**
	 * Lê os produtos a associar a uma prateleira
	 * 
	 * @param linha com a informação dos produtos
	 * @return um array com os ids dos produtos
	 */
	private int[] lerProdutos(String linha) {
		String ids[] = linha.split(",");
		int produtos[] = new int[ids.length];
		for (int i = 0; i < ids.length; i++)
			produtos[i] = ids[i].equals("_") ? -1 : Integer.parseInt(ids[i].substring(1)) - 1;
		return produtos;
	}

	/**
	 * Lê a posição codificada em duas strings
	 * 
	 * @param xs a string com a coordenada x
	 * @param ys a string com a coordenada y
	 * @return uma posição
	 */
	private Point lerPosicao(String xs, String ys) {
		return new Point(Integer.parseInt(xs), Integer.parseInt(ys));
	}

	private ComponenteVisual lerComponenteVisual(BufferedReader in) throws IOException {
		String cvInfo[] = in.readLine().split("\t");
		switch (cvInfo[0]) {
			case "CS":
				return lerComponenteSimples(cvInfo, 1);
			case "CMA":
				return lerComponenteMultiAnimado(cvInfo, 1);
			case "CAd":
				return lerComponenteAnimadoDiretorio(cvInfo, 1);
		}
		return null;
	}

	private ComponenteVisual lerComponenteSimples(String[] cvInfo, int startIdx) throws IOException {
		return new ComponenteSimples(pastaImagens + cvInfo[startIdx]);
	}

	private ComponenteVisual lerComponenteMultiAnimado(String[] cvInfo, int startIdx) throws IOException {
		int nAnims = Integer.parseInt(cvInfo[startIdx + 1]);
		int nFrames = Integer.parseInt(cvInfo[startIdx + 2]);
		int delay = Integer.parseInt(cvInfo[startIdx + 3]);
		return new ComponenteMultiAnimado(pastaImagens + cvInfo[startIdx], nAnims, nFrames, delay);
	}

	private ComponenteAnimado lerComponenteAnimadoDiretorio(String[] cvInfo, int startIdx) throws IOException {
		int delay = Integer.parseInt(cvInfo[startIdx + 1]);
		return ComponenteAnimado.fromDiretorio(pastaImagens + cvInfo[startIdx], delay);
	}
}
