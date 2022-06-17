
/**
 * Metodo que identifica cada aviador. Contem o nome, a posicao iniciail, a
 * quantidade de gasolina, o estado 'em jogo' do jogador e o numero de maus
 * mortos pelo mesmo.
 * 
 * @author Ines Lobo 49022 e Xiao Yi 49446
 *
 */

public class Aviador {

	private String name;
	private Ponto posIni;
	private int gas;
	private boolean foraDeJogo = false;
	private int quantos = 0;

	/**
	 * Define o nome, a posicao inicial e a quantidaded e gasolina do jogador
	 * 
	 * @param name
	 *            Nome do jogador
	 * @param posIni
	 *            Posicao inicial
	 * @param gas
	 *            Gasolina do jogador
	 * @return
	 * @requires
	 */

	public Aviador(String name, Ponto posIni, int gas) {
		this.name = name;
		this.posIni = posIni;
		this.gas = gas;
	}

	/**
	 * Da nos o nome do jogador
	 * 
	 * @return String com o nome do jogador
	 * @requires name != null
	 */

	public String name() {
		return name;
	}

	/**
	 * Da nos a posicao do jogador
	 * 
	 * @return Posicao do jogador
	 */

	public Ponto posicao() {
		return posIni;
	}

	/**
	 * Da nos a gasolina do jogador
	 * 
	 * @return Gasolina do jogador
	 */

	public int gasolina() {
		return gas;
	}

	/**
	 * Diz nos se o jogador esta fora de jogo
	 * 
	 * @return Estado do jogador no jogo. True - o jogador estah fora de jogo ;
	 *         False - o jogador ainda estah em jogo
	 */

	public boolean foraDeJogo() {
		return foraDeJogo;
	}

	/**
	 * Metodo que devolve quantos maus o aviador afogou
	 * 
	 * @return Quantidade de Maus que o jogador afogou
	 */

	public int quantosAfogou() {
		return quantos;
	}

	/**
	 * 
	 * @param gas
	 *            Gasolina que o jogador gasta a mover-se
	 */

	public void gastaGasolina(int gas) {
		this.gas -= gas;
	}

	/**
	 * 
	 * @param novaPos
	 *            Posicao final do jogador depois do movimento
	 */

	public void mudaPosicao(Ponto novaPos) {
		posIni = novaPos;
	}

	/**
	 * Aumenta de quantos o numero de Maus que o jogador matou
	 * 
	 * @param quantos
	 *            Numero de maus mortos numa jogada
	 */

	public void afogouMaus(int quantos) {
		this.quantos += quantos;
	}

	/**
	 * Metodo que define se o jogador saiu ou nao de jogo
	 */

	public void saiDeJogo() {
		foraDeJogo = true;
	}

	/**
	 * String que nos diz o nome e a posicao atual do jogador
	 */

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + " - posicao atual " + posIni.toString());
		return sb.toString();
	}

}
