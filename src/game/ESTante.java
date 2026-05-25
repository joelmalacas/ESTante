package game;

import java.awt.*;

import javax.swing.*;

import efeito.EfeitoDevolverProduto;
import efeito.EfeitoMostrarJogada;

import java.io.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import prof.jogos2D.efeito.Efeito;
import prof.jogos2D.efeito.EfeitoImagem;
import prof.jogos2D.efeito.EfeitoListener;
import prof.jogos2D.elemento.ElementoGraficoDefault;
import prof.jogos2D.image.*;
import prof.jogos2D.util.ImageLoader;
import java.util.concurrent.ThreadLocalRandom;

import mundo.*;
import prateleira.PrateleiraInfo;
import prateleira.Produto;

/**
 * classe que representa o jogo
 */
public class ESTante extends JFrame {
	private static final long serialVersionUID = 1L;

	// constantes que definem o tamanho da zona de jogo
	private static final int COMPRIMENTO = 1000;
	private static final int ALTURA = 650;

	/** Define quanto tempo, em milisegundos, sem jogar até aparecer uma pista */
	private static final int TEMPO_SEM_JOGADA_HINT = 5000;

	// constantes que definem a posição da barra de estado e locais onde escrever a
	// informação textual, bem como as fontes e cores a usar
	private static final int POSICAO_STATUS_X = 650;
	private static final int POSICAO_STATUS_Y = 0;
	private static final int POSICAO_NIVEL = POSICAO_STATUS_X + 10;
	private static final int POSICAO_TEXTOS_Y = POSICAO_STATUS_Y + 23;
	private static final int POSICAO_TEMPO = POSICAO_STATUS_X + 100;
	private static final int POSICAO_JOGADAS = POSICAO_STATUS_X + 270;
	private static final int SOMBRA = 2;
	private static final Color statusCor = new Color(220, 220, 50);
	private static final Color statusCorSombra = new Color(90, 90, 90);
	private static final Font statusFont = new Font("Monospaced", Font.BOLD, 22);

	// nome do ficheiro onde estão o número de níveis e respetivos records
	private static final String ficheiroRecords = "data/niveis/pontuacoes.txt";

	// variáveis para os vários elementos gráficos
	private JPanel zonaJogo = null; // onde o jogo vai ser desenhado
	private BufferedImage imagemJogo; // imagem para o fundo do jogo
	private final Object bufferLock = new Object(); // para evitar escritas ao mesmo tempo na imagem

	private Image statusImg; // a imagem da barra de estado
	private EfeitoImagem msgVitoria; // a mensagem a mostrar em caso de vitória
	private EfeitoImagem msgPerdeu; // a mensagem a mostrar em caso de derrota

	// variáveis transversais aos vários níveis
	private int numNiveis; // quantos níveis existem

	// variáveis para a gestão do nível atual
	private int nivel; // número do nivel atual
	private Mundo mundo = null; // o mundo deste nível
	private boolean nivelTerminado = true; // se está a decorrer ou não
	private boolean completouNivel;
	private long tempoInicioJogo; // o tempo em que começou o nível
	private long tempoFimJogo; // o tempo em que acabou o nível (se este tiver sido completado)
	private int numeroJogadas; // quantas jogadas já fez

	// guardar as informações dos elementos selecionados pelo jogador
	private Produto produtoSel; // o produto a mover
	private PrateleiraInfo prateleiraOrigem; // a prateleira de onde o produto foi escolhido
	private BufferedImage imagemMover; // a imagem selecionada do produto

	// variáveis de controlo dos tempos do jogo
	private Thread atualizadora; // thread que vai actualizar o jogo de x em x milisegundos

	// timer que indica que se deve mostrar a pista ao jogador
	private Timer semJogada = new Timer(TEMPO_SEM_JOGADA_HINT, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			muitoTempoSemJogar();
		}
	});
	private EfeitoMostrarJogada mostrandoJogada;

	private long[] recordsTempos;
	private int[] recordsJogadas;

	/**
	 * Inicializa o jogo e começa no nível 1
	 */
	public void play() {
		criarImagemJogo();
		lerConfiguracoesIniciais();
		setNivel(1);
		// no fim está tudo pronto para mandar desenhar o mundo
		zonaJogo.repaint();
	}

	/*
	 * lê o ficheiro com a informação do número de níveis e os recordes de cada
	 * nível
	 */
	private void lerConfiguracoesIniciais() {
		// TODO fazer a leitura do ficheiro de pontuações : ficheiroRecords
		try (BufferedReader in = new BufferedReader(new FileReader(ficheiroRecords))) {
			numNiveis = Integer.parseInt(in.readLine().trim());
			recordsTempos = new long[numNiveis + 1];
			recordsJogadas = new int[numNiveis + 1];
			for (int i = 1; i <= numNiveis; i++) {
				String[] partes = in.readLine().split(",");
				recordsTempos[i] = Long.parseLong(partes[0]);
				recordsJogadas[i] = Integer.parseInt(partes[1]);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

	/** Gravar os recordes */
	private void gravarRecords() {
		// TODO gravar o ficheiro das pontuações : ficheiroRecords
		try (BufferedWriter out = new BufferedWriter(new FileWriter(ficheiroRecords))) {
			out.write(String.valueOf(numNiveis));
			out.newLine();
			for (int i = 1; i <= numNiveis; i++) {
				out.write(recordsTempos[i] + "," + recordsJogadas[i]);
				out.newLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * define qual o nível que se vai jogar
	 * 
	 * @param n número do nível a jogar
	 */
	private void setNivel(int n) {
		nivel = n;
		lerNivel("data/niveis/nivel_" + nivel + ".txt");
		tempoFimJogo = 1;
		tempoInicioJogo = 1;
		comecarNivel();
	}

	/**
	 * ler a informação do nivel no ficheiro indicado
	 * 
	 * @param file ficheiro com a informação do nível
	 */
	private void lerNivel(String file) {
		Mundo m = new Mundo(zonaJogo.getWidth(), zonaJogo.getHeight());

		NivelReader reader = new NivelReader();
		try {
			reader.lerNivel(m, file);
			mundo = m;
		} catch (IOException e) {
			System.out.println("erro na leitura do ficheiro " + file);
			e.printStackTrace();
		}
	}

	/** Começa a jogar o nível */
	private void comecarNivel() {
		JOptionPane.showMessageDialog(this,
				"<html><center>NÍVEL " + nivel + "!<br><br>Tempo a bater:"
						+ tempoJogoString(getRecordTempo(nivel))
						+ " <br>Jogadas a bater: " + getRecordJogadas(nivel) + "<br><br>BOA SORTE!!</center></html>");
		// reiniciar as variáveis de controlo do nível
		nivelTerminado = false;
		semJogada.start();
		semJogada.restart();
		msgVitoria.reset();
		msgPerdeu.reset();
		completouNivel = false;
		tempoInicioJogo = System.currentTimeMillis();
		tempoFimJogo = 0;
		numeroJogadas = 0;
		atualizadora = new Actualizador();
		atualizadora.start();
	}

	/**
	 * método chamado quando o jogador pressiona o rato para selecionar um produto a
	 * mover
	 * 
	 * @param e o evento associado ao rato
	 */
	private void selecionarProduto(MouseEvent e) {
		Point rato = e.getPoint();

		// parar o contador do tempo, pois está-se a faz<er uma jogada
		semJogada.stop();
		// desativar o feito de mostrar jogada, se ativo
		if (mostrandoJogada != null) {
			mostrandoJogada.terminar();
			mostrandoJogada = null;
		}
		PrateleiraInfo prat = mundo.getPrateleiraAt(rato);
		if (prat != null) {
			Produto prod = mundo.processaCliqueAt(rato);
			if (prod != null) {
				prateleiraOrigem = prat;
				switch (prat.getCategoria()) {
					case SIMPLES:
						produtoSel = prat.getSimples().emprestarProdutoAt(rato);
						break;
					case EMPILHAVEL:
						produtoSel = prat.getEmpilhavel().emprestarProdutoAt(rato);
						break;
					case MOVEL:
						produtoSel = prat.getMovel().emprestarProdutoAt(rato);
						break;
					case PORTA:
						produtoSel = prat.getPorta().emprestarProdutoAt(rato);
						break;
					case SEQUENCIA:
						produtoSel = prat.getSequencia().emprestarProdutoAt(rato);
						break;
				}
			}
		}
		if (produtoSel == null)
			return;
		imagemMover = criarImagemSel((BufferedImage) produtoSel.getImagem().getSprite());
	}

	/**
	 * Chamado quando o jogador está a mover o produto com o rato
	 */
	private void moverProduto() {
		if (produtoSel == null)
			return;
		zonaJogo.repaint();
	}

	/**
	 * Chamado quando o jogador liberta o rato por forma a pousar o produto
	 * 
	 * @param e evento do rato
	 */
	private void pousarProduto(MouseEvent e) {
		semJogada.restart();
		if (produtoSel == null)
			return;

		Point rato = e.getPoint();
		PrateleiraInfo prat = mundo.getPrateleiraAt(rato);
		if (prat != null && prateleiraPodeReceber(prat, produtoSel)) {
			if (prat != prateleiraOrigem) {
				removerProdutoPrateleira(prateleiraOrigem, produtoSel);
				numeroJogadas++;
			}
			receberProdutoPrateleiraEm(prat, produtoSel, rato);
		} else {
			Point2D.Float pos = new Point2D.Float(rato.x - produtoSel.getImagem().getComprimento() / 2,
					rato.y - produtoSel.getImagem().getAltura() / 2);
			PrateleiraInfo pratDevolver = prateleiraOrigem;
			EfeitoDevolverProduto f = new EfeitoDevolverProduto(prateleiraOrigem, imagemMover, pos, 10,
					new EfeitoListener() {
						@Override
						public void processaFimEfeito(Efeito f) {
							retomarProdutoPrateleira(pratDevolver);
						}
					});
			mundo.addFx(f);
		}
		produtoSel = null;
	}

	private boolean prateleiraPodeReceber(PrateleiraInfo prat, Produto p) {
		switch (prat.getCategoria()) {
			case SIMPLES:
				return prat.getSimples().podeReceber(p);
			case EMPILHAVEL:
				return prat.getEmpilhavel().podeAceitarProduto(p);
			case MOVEL:
				return prat.getMovel().podeReceber(p);
			case PORTA:
				return prat.getPorta().podeAceitarProduto(p);
			case SEQUENCIA:
				return prat.getSequencia().podeReceber(p);
		}
		return false;
	}

	private void removerProdutoPrateleira(PrateleiraInfo prat, Produto p) {
		switch (prat.getCategoria()) {
			case SIMPLES:
				prat.getSimples().removeProduto(p);
				break;
			case EMPILHAVEL:
				prat.getEmpilhavel().removeProduto(p);
				break;
			case MOVEL:
				prat.getMovel().removeProduto(p);
				break;
			case PORTA:
				prat.getPorta().removeProduto(p);
				break;
			case SEQUENCIA:
				prat.getSequencia().removeProduto(p);
				break;
		}
	}

	private void receberProdutoPrateleiraEm(PrateleiraInfo prat, Produto p, Point pos) {
		switch (prat.getCategoria()) {
			case SIMPLES:
				prat.getSimples().receberProduto(produtoSel, pos);
				break;
			case EMPILHAVEL:
				prat.getEmpilhavel().aceitarProduto(produtoSel, pos);
				break;
			case MOVEL:
				prat.getMovel().receberProduto(produtoSel, pos);
				break;
			case PORTA:
				prat.getPorta().aceitarProduto(produtoSel, pos);
				break;
			case SEQUENCIA:
				prat.getSequencia().receberProduto(produtoSel, pos);
				break;
		}
	}

	private void retomarProdutoPrateleira(PrateleiraInfo prat) {
		switch (prat.getCategoria()) {
			case SIMPLES:
				prat.getSimples().retomarProduto();
				break;
			case EMPILHAVEL:
				prat.getEmpilhavel().reaverProduto();
				break;
			case MOVEL:
				prat.getMovel().retomarProduto();
				break;
			case PORTA:
				prat.getPorta().reaverProduto();
				break;
			case SEQUENCIA:
				prat.getSequencia().retomarProduto();
				break;
		}
	}

	/**
	 * Chamado sempre que passou o tempo especificado sem nenhuma jogada, verificar
	 * se há jogadas possíveis e dar uma pista ao jogador
	 */
	private void muitoTempoSemJogar() {
		java.util.List<Produto> prods = mundo.getProdutosJuntaveis();
		// se não há produtos juntáveis sai
		if (prods.size() == 0)
			return;

		Produto mudar[] = new Produto[mundo.getNumeroJuntar()];
		EfeitoMostrarJogada.ImagemProduto imagens[] = new EfeitoMostrarJogada.ImagemProduto[mudar.length];
		// escolher um conjunto aleatoriamente e criar o feito para o mostrar
		int offset = 3 * ThreadLocalRandom.current().nextInt(prods.size() / mundo.getNumeroJuntar());
		for (int i = 0; i < mudar.length; i++) {
			Produto p = prods.get(offset + i);
			BufferedImage img = criarImagemSel((BufferedImage) p.getImagem().getSprite());
			imagens[i] = new EfeitoMostrarJogada.ImagemProduto(img, p);
		}
		mostrandoJogada = new EfeitoMostrarJogada(imagens, 30);
		mundo.addFx(mostrandoJogada);
	}

	/**
	 * Desenha o mundo na imagem de jogo, não é o que desenha o jogo na janela. Este
	 * método é chamado após o atualizar() para garantir que o mundo é desenhado
	 * com as últimas alterações
	 */
	private void desenharMundo() {
		synchronized (bufferLock) {
			if (mundo != null)
				mundo.draw(imagemJogo.createGraphics());
		}
		// mandar atualizar o desenho das coisas
		zonaJogo.repaint();
	}

	/**
	 * Desenha a zona de jogo
	 * 
	 * @param g onde desenhar
	 */
	private synchronized void drawGameArea(Graphics2D g) {
		// desenha o mundo
		synchronized (bufferLock) {
			if (imagemJogo != null) {
				g.drawImage(imagemJogo, 0, 0, null);
			}
		}

		// desenha o produto selecionado na zona do rato, se o houver
		Point rato = zonaJogo.getMousePosition();
		if (produtoSel != null && rato != null && imagemMover != null)
			g.drawImage(imagemMover, rato.x - imagemMover.getWidth() / 2, rato.y - imagemMover.getHeight() / 2, null);

		// desenha a informação de status do nível
		g.drawImage(statusImg, POSICAO_STATUS_X, POSICAO_STATUS_Y, null);
		String nivelStr = "" + nivel;
		String tempoStr = "" + tempoJogoString(tempoJogo());
		String jogadasStr = "" + numeroJogadas;
		g.setFont(statusFont);
		g.setColor(statusCorSombra);
		g.drawString(nivelStr, POSICAO_NIVEL + SOMBRA, POSICAO_TEXTOS_Y + SOMBRA);
		g.drawString(tempoStr, POSICAO_TEMPO + SOMBRA, POSICAO_TEXTOS_Y + 2);
		g.drawString(jogadasStr, POSICAO_JOGADAS + SOMBRA, POSICAO_TEXTOS_Y + SOMBRA);

		g.setColor(statusCor);
		g.drawString(nivelStr, POSICAO_NIVEL, POSICAO_TEXTOS_Y);
		g.drawString(tempoStr, POSICAO_TEMPO, POSICAO_TEXTOS_Y);
		g.drawString(jogadasStr, POSICAO_JOGADAS, POSICAO_TEXTOS_Y);

	}

	/**
	 * Retorna o tempo de jogo já formatado numa string
	 * 
	 * @param tempo tempo a formatar
	 * @return o tempo de jogo já formatado numa string
	 */
	private String tempoJogoString(long tempo) {
		long duracao = tempo;
		long minutos = duracao / 60000;
		long segundos = (duracao % 60000) / 1000;
		long milis = tempo % 1000;
		return String.format("%d:%02d.%03d", minutos, segundos, milis);
	}

	/**
	 * Calcula o tempo de jogo, isto é, o tempo que o jogador está a jogar no nível
	 * atual
	 * 
	 * @return o tempo de jogo
	 */
	private long tempoJogo() {
		return tempoFimJogo != 0 ? tempoFimJogo - tempoInicioJogo
				: System.currentTimeMillis() - tempoInicioJogo;
	}

	/** indica se ainda há mais níveis para jogar */
	private boolean temProximoNivel() {
		return nivel < numNiveis;
	}

	/** método chamado quando se termina um nível */
	private void terminouNivel() {
		if (completouNivel) {
			String[] opcoes = { "Repetir", "Seguinte" };
			String msg = "<html>Completaste este nível com sucesso!<br>Demoraste " + tempoJogoString(tempoJogo())
					+ " <br>" + "Fizeste " + numeroJogadas + " jogadas<br>";
			if (bateuTempo() && bateuJogadas()) {
				msg += "Bateste os record de tempo e de jogadas!!! PARABÈNS";
				updateRecordTempo(nivel, tempoJogo());
				updateRecordJogadas(nivel, numeroJogadas);
			} else if (bateuTempo())
				msg += "Bateste o record de tempo!!! PARABÈNS";
			else if (bateuJogadas())
				msg += "Bateste o record de jogadas!!! PARABÈNS";
			else
				msg += "Não deu para um record, mas podes sempre tentar novamente!";
			int res = JOptionPane.showOptionDialog(this, msg, "Vitória!!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
			gravarRecords();
			if (res == 1)
				proximoNivel();
			else
				setNivel(nivel);
		} else {
			String[] opcoes = { "Repetir", "Recomeçar" };
			String msg = "<html>Não conseguiste completar este nível!<br>Mas podes sempre repetir, ou até começar de novo!";
			int res = JOptionPane.showOptionDialog(this, msg, "Vitória!!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
			if (res == 1)
				setNivel(1);
			else
				setNivel(nivel);
		}
	}

	/** passa para o próximo nível */
	private void proximoNivel() {
		if (temProximoNivel())
			setNivel(nivel + 1);
	}

	/**
	 * Indica se bateu o record de jogadas deste nível
	 * 
	 * @return true se bateu o record
	 */
	private boolean bateuJogadas() {
		// TODO implementar este método
		return recordsJogadas[nivel] == 0 || numeroJogadas < recordsJogadas[nivel];
	}

	/**
	 * Indica se bateu o record de tempo deste nível
	 * 
	 * @return true se bateu o record
	 */
	private boolean bateuTempo() {
		// TODO implementar este método
		return recordsTempos[nivel] == 0 || tempoJogo() < recordsTempos[nivel];
	}

	/**
	 * Retorna o record de tempo para um nível
	 * 
	 * @param nivel o nivel a ver o record
	 * @return o record de tempo do nível indicado
	 */
	private long getRecordTempo(int nivel) {
		// TODO implementar este método
		return recordsTempos[nivel];
	}

	/**
	 * Retorna o record de jogadas para um nível
	 * 
	 * @param nivel o nivel a ver o record
	 * @return o record de jogadas do nível indicado
	 */
	private int getRecordJogadas(int nivel) {
		// TODO implementar este método
		return recordsJogadas[nivel];
	}

	/**
	 * atualiza o record de jogadas do nível indicado. Este método NÃO deve
	 * atualizar o ficheiro de records.
	 * 
	 * @param nivelBatido o número do nível a atualizar
	 * @param jogadas     o novo record
	 */
	private void updateRecordJogadas(int nivelBatido, int jogadas) {
		// TODO implementar este método
		recordsJogadas[nivelBatido] = jogadas;
	}

	/**
	 * atualiza o record de tempo do nível indicado. Este método NÃO deve
	 * atualizar o ficheiro de records.
	 * 
	 * @param nivelBatido o número do nível a atualizar
	 * @param tempo       o novo record
	 */
	private void updateRecordTempo(int nivelBatido, long tempo) {
		// TODO implementar este método
		recordsTempos[nivelBatido] = tempo;
	}

	/**
	 * Classe responsável pela criação da thread que vai actualizar o mundo de x em
	 * x segundos
	 */
	class Actualizador extends Thread {
		public void run() {
			boolean finished = false;
			while (!nivelTerminado) {
				mundo.atualizar();
				// vai atualizar todos os elementos do jogo
				if (!finished) {
					// TODO verificar se o round acabou com vitória
					if (mundo.ganhou()) {
						finished = true;
						completouNivel = true;
						mundo.addFx(msgVitoria);
						tempoFimJogo = System.currentTimeMillis();
					} else if (mundo.perdeu()) {
						finished = true;
						mundo.addFx(msgPerdeu);
					}
				}
				if (finished && mundo.isOver()) {
					nivelTerminado = true;
				}
				desenharMundo();

				// esperar 33 milisegundos o que dá umas 30 frames por segundo
				try {
					sleep(33);
				} catch (InterruptedException e) {
				}
			}
			terminouNivel();
		}
	};

	/** prepara as mensagens do final do nível e da barra de status */
	private void setupImagens() {
		ComponenteAnimado vit = new ComponenteAnimado(
				(BufferedImage) ImageLoader.getLoader().getImage("data/mensagens/vitoria.png"), 3, 3);
		int px = (zonaJogo.getWidth() - vit.getComprimento()) / 2;
		int py = (zonaJogo.getHeight() - vit.getAltura()) / 2;
		msgVitoria = new EfeitoImagem(new ElementoGraficoDefault(new Point(px, py), vit), 60);

		ComponenteAnimado go = new ComponenteAnimado(
				(BufferedImage) ImageLoader.getLoader().getImage("data/mensagens/perdeu.png"), 3, 3);
		px = (getZonaJogo().getWidth() - go.getComprimento()) / 2;
		py = (getZonaJogo().getHeight() - go.getAltura()) / 2;
		msgPerdeu = new EfeitoImagem(new ElementoGraficoDefault(new Point(px, py), go), 60);

		statusImg = ImageLoader.getLoader().getImage("data/fundos/status.png");
	}

	/**
	 * Constroi o jogo
	 */
	public ESTante() {
		super();
		setupInterface();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/** Prepara a interface gráfica */
	private void setupInterface() {
		this.setTitle("ESTante");
		this.setContentPane(getJContentPane());
		this.pack();
		this.setResizable(false);

		setupImagens();
	}

	/**
	 * Cria uma imagem de um produto selecionado, basicamente colocando uma borda à
	 * volta da imagem deste.
	 */
	private BufferedImage criarImagemSel(BufferedImage source) {
		int borda = 3;
		int wo = source.getWidth();
		int ho = source.getHeight();
		int wd = source.getWidth() + borda * 2;
		int hd = source.getHeight() + borda * 2;
		int vizinhos[] = new int[4 * borda * borda];
		int v = 0;
		for (int y = -borda; y < borda; y++)
			for (int x = -borda; x < borda; x++)
				vizinhos[v++] = 4 * (x + y * wd * 2);

		BufferedImage sourceRGB = new BufferedImage(wo, ho, BufferedImage.TYPE_4BYTE_ABGR);
		sourceRGB.createGraphics().drawImage(source, 0, 0, null);
		BufferedImage result = new BufferedImage(wd, hd, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = result.createGraphics();
		g.drawImage(source, borda, borda, null);
		byte[] pixelDestino = ((DataBufferByte) result.getRaster().getDataBuffer()).getData();
		byte cor[] = { 0, (byte) 255, (byte) 180, (byte) 0 };
		for (int y = 0; y < hd; y++) {
			for (int x = 0; x < wd; x++) {
				int idxD = x * 4 + y * wd * 4;
				byte transparencia = pixelDestino[idxD];
				if (transparencia == 0) {
					for (int i = 0; i < vizinhos.length; i++) {
						int index = idxD + vizinhos[i];
						if (index < 0 || index >= wd * hd * 4)
							continue;
						byte transVizinho = pixelDestino[index];
						if (transVizinho != 0 && transVizinho != -2) {
							pixelDestino[idxD] = -2;
							for (int p = 1; p < 4; p++)
								pixelDestino[idxD + p] = cor[p];
							break;
						}
					}
				}
			}
		}

		g.dispose();
		vizinhos = new int[] { -4, 4, -4 * wd, 4 * wd };
		for (int y = 0; y < hd; y++) {
			for (int x = 0; x < wd; x++) {
				int idxD = x * 4 + y * wd * 4;
				byte transparencia = pixelDestino[idxD];
				if (transparencia == -2) {
					int count = 0;
					for (int i = 0; i < vizinhos.length; i++) {
						int idx = idxD + vizinhos[i];
						if (idx < 0 || idx >= wd * hd * 4)
							continue;
						byte transp = pixelDestino[idxD + vizinhos[i]];
						if (transp == -2 || transp == 120)
							count++;
					}
					if (count < 2)
						pixelDestino[idxD] = 120;
				}
			}
		}
		return result;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		JPanel jContentPane = null;
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getZonaJogo(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * Inicializa a zonaJogo, definindo a gestão do rato e dos desenhos do mundo
	 */
	private JPanel getZonaJogo() {
		if (zonaJogo == null) {
			zonaJogo = new JPanel() {
				public void paint(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					drawGameArea((Graphics2D) g);
				}
			};
			zonaJogo.setLayout(new GridBagLayout());
			zonaJogo.setPreferredSize(new Dimension(COMPRIMENTO, ALTURA));
			zonaJogo.setSize(COMPRIMENTO, ALTURA);
			zonaJogo.setBackground(Color.pink);
			MouseAdapter rato = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1)
						selecionarProduto(e);
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					moverProduto();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					pousarProduto(e);
				}
			};
			zonaJogo.addMouseListener(rato);
			zonaJogo.addMouseMotionListener(rato);
		}
		return zonaJogo;
	}

	/** Cria a imagem que vai ser usada para desenhar o mundo em background */
	private void criarImagemJogo() {
		int w = Math.max(1, getWidth());
		int h = Math.max(1, getHeight());
		synchronized (bufferLock) {
			imagemJogo = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}
	}

	/**
	 * Arranca com o jogo
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ESTante bloons = new ESTante();
		bloons.setVisible(true);
		bloons.play();
	}
}
