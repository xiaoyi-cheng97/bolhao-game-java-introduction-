import java.util.Random;

/**
 * Class cujos objetos representam um caminho de tipo Posicao de dim posicoes e
 * com a posicao dos Maus
 * 
 * 
 * @author Ines Lobo 49022 e Xiao Yi 49446
 *
 */

public class Caminho {
	// atributos que dedinem este caminho
	private final int dim; // dimensao do caminho
	private int[] posM; // posicao dos maus
	private Posicao[] posicao; // vetor do tipo Posicao que representa o caminho
								// do jogo

	/**
	 * Constroi um caminho de dim posicoes de tipo Posicao
	 * 
	 * @param dim
	 *            O numero de posicoes no caminho
	 * @param posM
	 *            Posicao onde o Mau se encontra quando o Bolhao eh lancado
	 * @param posCE
	 *            Posicoes onde a cama elastica se encontra
	 * @param posFS
	 *            Posicoes onde a fabrica de sabao se encotra
	 * @requires Jogo.MIN_DIMENSAO <= dim <= Jogo.MAX_DIMENSAO && posM != null
	 *           && posCE != null && forall i 1 <= posCE[i] <= dim && posFS !=
	 *           null && forall i 1 <= posFS[i] <= dim
	 */

	public Caminho(int dim, int[] posM, int[] posCE, int[] posFS) {
		this.dim = dim;
		this.posM = posM;// fazer copia
		this.posicao = new Posicao[dim];

		for (int i = 0; i < posicao.length; i++) {
			posicao[i] = Posicao.NORMAL;
		} // vetor so com posicoes normais

		for (int i = 0; i < posCE.length; i++) {
			posicao[posCE[i] - 1] = Posicao.CAMA_ELASTICA;
		}

		for (int i = 0; i < posFS.length; i++) {
			posicao[posFS[i] - 1] = Posicao.FABRICA_DE_SABAO;
		}

	}

	/**
	 * Devolve o numero de posicoes do caminho
	 * 
	 * @return o numero inteiro que representa o numero de posicoes
	 */

	public int dimensao() {
		return dim;
	}

	/**
	 * Devolve as posicoes onde se encontra os maus no caminho
	 * 
	 * @return o vetor com as posicoes
	 */

	public int[] posicoesMaus() {
		return posM;
	}

	/**
	 * Devolve quantidade dos maus vivos no caminho
	 * 
	 * @return quantidade dos maus
	 */

	public int quantosMaus() {
		int conta = 0;
		for (int i = 0; i < posM.length; i++) {
			if (posM[i] != -1) {
				conta++;
			}
		}
		return conta;
	}

	/**
	 * Devolve o vetor do tipo Posicao
	 * 
	 * @return uma copia do vetor
	 */

	public Posicao[] relevo() {

		return copiaVetor(posicao);
	}

	/**
	 * Regista o impacto do bolhao no caminho e o efeito da jogada sobre os maus
	 * 
	 * @param posAviao
	 *            - posicao do aviao em causa
	 * @param impacto
	 *            - forca do Bolhao
	 * @param gerador
	 * @requires posAviao != null
	 */

	public void registaImpacto(Ponto posAviao, int impacto, Random gerador) {

		fugaMaus(posM, gerador, posAviao.y());
		// Faz a fuga dos Maus
		cairNaCratera();
		// Verifica se caiu em cratera NA FUGA

		impactoDoBolhao(posAviao.x(), impacto);
		// Verifica se caiu em cratera DEPOIS DO IMPACTO
		if (!caiEmCamaElastica(posAviao.x())) {
			posicao[posAviao.x() - 1] = Posicao.CRATERA;

		}
		cairNaCratera();

	}

	/**
	 * Verifica se cai numa cratera
	 * @param pos
	 *            Posicao que queremos comparar
	 * @return True - Se a posicao coincidir com uma Cratera ou False - se nao
	 *         acontecer
	 */

	public boolean caiEmCratera(int pos) {

		return posicao[pos - 1].equals(Posicao.CRATERA);
	}

	/**
	 * Verifica se cai numa cama elastica
	 * @param pos
	 *            Posicao que queremos comparar
	 * @return True - se a posicao coincidir com uma Cama Elastica ou False - se
	 *         nao acontecer
	 */

	public boolean caiEmCamaElastica(int pos) {

		return posicao[pos - 1].equals(Posicao.CAMA_ELASTICA);
	}

	/**
	 * Verifica se cai numa fabrica de sabao
	 * @param pos
	 *            Posicao que queremos comparar
	 * @return True - se a posicao coincidir com uma Fabrica de Sabao ou False -
	 *         se nao acontecer
	 */

	public boolean caiEmFabricaDeSabao(int pos) {

		return posicao[pos - 1].equals(Posicao.FABRICA_DE_SABAO);
	}

	/**
	 * 
	 * String com os tipos de posicao em cada sitio do caminho
	 * 
	 * @return representacao textual do caminho
	 */

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < posicao.length; i++) {
			if (posicao[i] != Posicao.CRATERA) {
				sb.append(posicao[i]);
			} else
				sb.append(' ');
		}
		return sb.toString();
	}

	/**
	 * Metodo que faz a copia de um vetor original
	 * 
	 * @param vectorOriginal
	 *            - vetor de Posicoes
	 * @return copia do vetor
	 */

	private Posicao[] copiaVetor(Posicao[] vectorOriginal) {
		Posicao[] copia = new Posicao[posicao.length];

		for (int i = 0; i < posicao.length; i++) {
			copia[i] = posicao[i];
		}

		return copia;
	}

	/**
	 * Modifica as posicoes dos maus durante a fuga.
	 * 
	 * @param posM
	 *            Vetor de inteiros com as posicoes em que os maus se encontram
	 * @param gerador
	 * @param distancia
	 *            Distancia que os maus fogem
	 */
	private void fugaMaus(int[] posM, Random gerador, int distancia) {
		for (int i = 0; i < posM.length; i++) {
			if (posM[i] != -1) {
				// Se o Mau nao estah morto
				// O Mau anda para a direita
				if (gerador.nextBoolean()) {
					posM[i] += distancia;
					if (posM[i] > dim) {
						posM[i] = dim;
					}
					// O Mau anda para a esquerda
				} else {
					posM[i] -= distancia;
					if (posM[i] < 1) {
						posM[i] = 1;
					}
				}
			}

		}
	}

	/**
	 * Metodo que regista os efeitos do impacto do bolhao nos Maus
	 * 
	 * @param posImp
	 *            Posicao de impacto do bolhao
	 * @param impacto
	 *            Forca de impacto do bolhao
	 */

	private void impactoDoBolhao(int posImp, int impacto) {

		for (int i = 0; i < posM.length; i++) {
			if (posM[i] != -1) {

				if (caiEmFabricaDeSabao(posImp)) {

					posM[i] = FuncoesBolhao4.efeitoBolhao4(posImp, impacto * 2, posM[i], dim);

				} else {

					posM[i] = FuncoesBolhao4.efeitoBolhao4(posImp, impacto, posM[i], dim);
				}
			}

		}
	}

	/**
	 * Metodo que verifica se o Mau cai na cratera. Caso aconteca, modifica o
	 * valor do Mau para -1.
	 */
	private void cairNaCratera() {
		for (int i = 0; i < posM.length; i++) {
			if (posM[i] != -1 && caiEmCratera(posM[i])) {
				posM[i] = -1;
			}
		}
	}
}
