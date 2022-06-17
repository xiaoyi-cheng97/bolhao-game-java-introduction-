import java.util.Random;

/**
 * Class que constroi e altera os valores de um jogo
 * 
 * @author Ines Lobo 49022 e Xiao Yi 49446
 *
 */

public class Jogo {

	public static final int MIN_DIMENSAO = 2;
	public static final int MAX_DIMENSAO = 80;
	public static final int MAX_AVIADORES = 5;
	public static final int ALTURA_MIN = 2;
	private final int dim;
	private Caminho cam;
	private int[] posM;
	private int gasolina;
	private Aviador[] aviadores;
	private int numeroJogador;
	private Random gerador;
	private int aviadoresEmJogo;

	/**
	 * 
	 * @param dim
	 *            Numero de posicoes do campo de jogo
	 * @param gasolina
	 *            Gasolina do jogador
	 * @param posM
	 *            Vector de inteiros que contem a posicao dos Maus
	 * @param posCE
	 *            Vetor de inteiros que contem a posicao das Camas Elasticas
	 * @param posFS
	 *            Vetor de inteiros que contem a posicao das Fabricas de Sabao
	 * @param gerador
	 */

	public Jogo(int dim, int gasolina, int[] posM, int[] posCE, int[] posFS, Random gerador) {
		this.dim = dim;
		this.posM = posM;
		this.gasolina = gasolina;
		this.gerador = gerador;
		cam = new Caminho(dim, posM, posCE, posFS);
		aviadores = new Aviador[MAX_AVIADORES];
		numeroJogador = 0;
		aviadoresEmJogo = 0;
	}

	// fim do construtor
	/**
	 * Da nos a dimensao do caminho
	 * 
	 * @return dim
	 */

	public int dimensao() {

		return dim;
	}

	// fim do metodo
	/**
	 * Da nos o relevo do caminho
	 * 
	 * @return relevo do caminho
	 */

	public Posicao[] relevo() {

		Posicao[] caminho = cam.relevo();

		return caminho;
	}

	// fim do metodo
	/**
	 * Da nos as posicoes dos Maus
	 * 
	 * @return posM
	 */

	public int[] posicoesMaus() {
		return cam.posicoesMaus();
	}

	// fim do metodo
	/**
	 * Metodo que cria um aviador com os parametros dados
	 * 
	 * @param nome
	 *            Nome do jogador
	 * @param pos
	 *            Posicao do jogador
	 */

	public void registaAviador(String nome, Ponto pos) {
		aviadores[numeroJogador] = new Aviador(nome, pos, gasolina);

		numeroJogador++;
		aviadoresEmJogo++;
	}

	// fim do metodo
	/**
	 * Da nos a informacao sobre o fim do jogo
	 * 
	 * @return True - o jogo esta terminado ou false se tal nao acontecer
	 */

	public boolean terminado() {
		boolean resultado = true;

		if (mausVivos()) {
			resultado = false;
		}

		return resultado;
	}

	// fim do metodo
	/**
	 * retorna a gasolina do aviador como nome igual ao nome dado como parametro
	 * 
	 * @param nome
	 *            nome do aviador
	 * @return Gasolina do aviador em causa
	 */

	public int gasolina(String nome) {
		int gasolina = 0;
		Aviador quem = aviadorQueJoga(nome);
		gasolina = quem.gasolina();

		return gasolina;
	}

	// fim do metodo
	/**
	 * retorna a posicao do aviador em causa (com nome)
	 * 
	 * @param nome
	 *            nome do um aviador
	 * @return Posicao do aviador em causa
	 */

	public Ponto posicaoAviao(String nome) {
		Ponto posAviao = null;

		Aviador quem = aviadorQueJoga(nome);
		posAviao = quem.posicao();

		return posAviao;
	}

	// fim do metodo
	/**
	 * retorna os nomes de todos aviadores que nao estao fora de jogo
	 * 
	 * @return array com os nomes do aviador em jogo
	 */

	public String[] aviadoresEmJogo() {
		String[] emJogo = new String[aviadoresEmJogo];
		int conta = 0;
		for (int i = 0; i < aviadores.length; i++) {
			if (aviadores[i] != null && !aviadores[i].foraDeJogo()) {
				emJogo[conta] = aviadores[i].name();
				conta++;
			}
		}
		return emJogo;
	}

	// fim do metodo
	/**
	 * retorna o resultado do jogo
	 * 
	 * @return 0 se o jogo acaba empactado 1 se ganha os aviadores -1 se ganha
	 *         os maus
	 */

	public int vencedor() {
		int vencedor = 0;

		if (!terminado()) {

			vencedor = -1;
		} else if (terminado())
			vencedor = 1;

		return vencedor;
	}

	// fim do metodo
	/**
	 * Metodo que faz a jogada de certo Jogador
	 * 
	 * @param nome
	 *            - nome do jogador que vai fazer a jogada
	 * @param movim
	 *            - as distancias x e y que o jogador vai andar ate ao proximo
	 *            ponto
	 * @param desistir
	 *            - numero que os aviadores podem marcar, de movimento, para
	 *            desistir do jogo
	 */

	public void fazJogada(String nome, Ponto movim, int desistir) {

		Aviador aviador = aviadorQueJoga(nome);

		if (movim.x() == desistir || movim.y() == desistir) {
			aviador.saiDeJogo();
			aviadoresEmJogo--;
			// Verifica se o jogador quer desistir do jogo

		} else {

			int nMausIni = cam.quantosMaus();

			Ponto posF = new Ponto(aviador.posicao().x() + movim.x(), aviador.posicao().y() + movim.y());
			int distancia = aviador.posicao().distancia(posF);
			aviador.gastaGasolina(distancia);

			aviador.mudaPosicao(posF);

			// Calcula o novo ponto, distancia que se moveu e "gasta" a gasolina
			// necessaria para se mover

			int impacto = aviador.posicao().y() / 2;
			int posImp = aviador.posicao().x();

			if (aviador.gasolina() <= 0) {
				aviador.saiDeJogo();
				aviadoresEmJogo--;
				// Ficando sem gasolina, o jogador sai do jogo
			} else if (cam.caiEmCamaElastica(posImp)) {
				aviador.saiDeJogo();
				aviadoresEmJogo--;
				// Se o Bolhao cai numa cama elastica, o jogador sai do jogo
			} else if (!cam.caiEmCratera(posImp)) {
				// Faz o impacto do Bolhao
				cam.registaImpacto(aviador.posicao(), impacto, gerador);
			}

			int nMausFim = cam.quantosMaus();
			int mausMatados = nMausIni - nMausFim;
			aviador.afogouMaus(mausMatados);
			// Verifica o estado dos maus no final da jogada
		}
	}
	// fim do metodo

	/**
	 * Metodo que cria um array com os nomes de todos os jogadores em Jogo
	 * 
	 * @return array de Strings com os nomes de todos os Aviadores
	 */

	public String[] todosAviadores() {

		String[] todosAviadores = new String[numeroJogador];
		int conta = 0;
		for (int i = 0; i < aviadores.length; i++) {
			if (aviadores[i] != null) {
				todosAviadores[conta] = aviadores[i].name();
				conta++;
			}
		}
		return todosAviadores;
	}

	// fim do metodo
	/**
	 * Diz nos quantos Maus os Aviadores mataram
	 * 
	 * @return vetor de inteiros com o numero de Maus mortos em ordem dos nomes
	 *         dos aviadores que jagaram o jogo
	 */
	public int[] quantosMausMataram() {
		int[] mausMortos = new int[todosAviadores().length];
		String[] nome = todosAviadores();
		int j = 0;
		for (int i = 0; i < aviadores.length; i++) {
			for (int n = 0; n < nome.length; n++) {
				if (aviadores[i] != null && aviadores[i].name().equals(todosAviadores()[n])) {
					mausMortos[j] = aviadores[i].quantosAfogou();
					j++;
				}
			}
		}
		return mausMortos;
	}

	// fim do metodo
	/**
	 * Verifica se ha maus vivos
	 * 
	 * @return true se houverem maus vivos ou false se isso nao acontecer
	 */
	private boolean mausVivos() {
		boolean resultado = false;
		for (int i = 0; i < posM.length && !resultado; i++) {
			if (posM[i] != -1) {
				resultado = true;
			}
		}
		return resultado;
	}
	// fim do metodo

	/**
	 * Da nos o aviador que joga
	 * 
	 * @param nome
	 * @return o aviador em jogar
	 */

	private Aviador aviadorQueJoga(String nome) {
		Aviador quemJoga = null;

		for (int i = 0; i < aviadores.length && quemJoga == null; i++) {
			if (aviadores[i] != null && aviadores[i].name().equals(nome)) {
				quemJoga = aviadores[i];
			}
		}
		return quemJoga;
	}
	// fim do metodo
}
