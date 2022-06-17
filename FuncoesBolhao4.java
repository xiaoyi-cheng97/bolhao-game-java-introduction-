import java.util.Random;
/**
 * Esta classe contem os metodos necessarios para calcular a nova
 * situacao de um Mau devido ao lancamento de um Bolhao
 * @author Isabel Nunes
 * @date Dezembro 2016
 */
public class FuncoesBolhao4 {

	/**
	 * Posicao em que o Mau vai ficar apohs o impacto do Bolhao
	 * @param impactX Posicao de impacto do Bolhao no caminho
	 * @param impacto Numero de posicoes fortemente inundadas pelo impacto
	 *                do Bolhao para a direita e para a esquerda
	 * @param posMau Posicao onde o Mau se encontra quando o Bolhao eh lancado
	 * @param dim Numero de posicoes do caminho
     *
	 *        - Se a posicao onde cai o Bolhao eh fora dos limites do caminho 
	 *          ou se o Mau estah fora das zonas de impacto, o Mau fica onde
	 *          estah;
	 *        - Se o Mau estah dentro da zona principal de impacto (zona 
	 *          de afogamento), a posicao do Mau eh alterada para -1
	 *        - Se o Mau estah dentro da zona de impacto secundario, a
	 *          nova posicao depende da sua posicao e da forca do impacto 
	 *          (de acordo com o enunciado da Fase 1).
	 * 
	 * @requires impactX >= 1 && impactX <= dim && impacto >= 0 && posMau >= 1 && 
	 *           posMau <= dim && dim > 0 && dim <= Jogo.MAX_DIMENSAO
	 *           
	 * @return Nova posicao do Mau apohs impacto ou -1 se se afogou
	 */
	public static int efeitoBolhao4 (int impactX, int impacto, int posMau, int dim) {
		
	    int result = posMau;      // Palpite inicial: fica na mesma posicao

	    int distancia = Math.abs(posMau - impactX);
		
		if (distancia <= impacto) { // jogador vai-se afogar
		
		       result = -1;
		
		} else if (distancia <= 2 * impacto) { // jogador vai ser arrastado
		
		       int arrast = arrastamento (impacto, distancia);
		
		       if (posMau < impactX) {// jogador estava ah direita do alvo
			
			        result = posicaoEsquerda(arrast,posMau);
			
		       } else {                   // jogador estava ah esquerda do alvo
			
			       result = posicaoDireita(arrast,posMau,dim);
			       
		       }
		}
		
		return result;
	}

	/**
	 * Posicao em que o jogador vai ficar apohs ser arrastado para a direita
	 * @param arrast O numero de posicoes que deve mover-se
	 * @param posJogador A posicao em que se encontra antes do arrastamento
	 * @param dim O numero de posicoes no caminho
	 * @return Posicao em que o jogador fica apohs ser arrastado arrast posicoes
	 *         ah direita; se ultrapassa o limite do caminho (posicao dim) entao
	 *         a nova posicao eh a que resulta do ricochete na parede
	 */
	private static int posicaoDireita(int arrast, int posJogador, int dim) {
		int novaPos = posJogador + arrast;
		if (novaPos > dim) {
			novaPos = dim - (novaPos - dim);
		}
		return novaPos;
	}

	/**
	 * Posicao em que o jogador vai ficar apohs ser arrastado para a esquerda
	 * @param arrast O numero de posicoes que deve mover-se
	 * @param posJogador A posicao em que se encontra antes do arrastamento
	 * @return Posicao em que o jogador fica apohs ser arrastado arrast posicoes
	 *         ah esquerda; se ultrapassa o limite do caminho (posicao 1) entao
	 *         a nova posicao eh a que resulta do ricochete na parede
	 */
	private static int posicaoEsquerda(int arrast, int posJogador) {
		int novaPos = posJogador - arrast;
		if (novaPos < 1) {
			novaPos = 1 - (novaPos - 1) ;
		}
		return novaPos;
	}

	/**
	 * Arrastamento que o jogador deve sofrer com o impacto do Bolhao se estah
	 * a uma distancia da posicao de impacto que eh superior ao valor do impacto
	 * do Bolhao.
	 * @param impacto Numero de posicoes afetadas pelo impacto do Bolhao
	 *                para a direita e para a esquerda
	 * @param distancia Distancia (em valor absoluto) a que o jogador se 
	 *                  encontrava da posicao de impacto antes do impacto do
	 *                  Bolhao
	 * @return O arrastamento que o jogador vai sofrer como consequencia do 
	 *         impacto do Bolhao. 
	 *         - Se distancia > 2 * impacto o arrastamento eh zero
	 *         - Senao:
	 *           - Se impacto == 1 entao arrastamento eh 1
	 *           - Se impacto == 2:
	 *              - se distancia == impacto + 1 entao arrastamento eh 2
	 *              - se distancia == impacto + 2 entao arrastamento eh 1
	 *           - Se impacto >= 3:
	 *             - Seja I o intervalo ]impacto, 2 * impacto]; o arrastamento serah de:
	 *               - impacto, se distancia estah no primeiro terco de I
	 *               - impacto/2, se distancia estah no segundo terco de I
	 *               - impacto/4, se distancia estah no ultimo terco de I
	 * @requires impacto > 0 && distancia > impacto   
	 */
	private static int arrastamento(int impacto, int distancia) {

		long arrast = 0;
		
		if (distancia <= 2 * impacto) {
			
			if (impacto == 1) {
				
				arrast = 1;
				
			} else if (impacto == 2) {
				
			    arrast = 3 - (distancia - impacto);
			    
			} else if (impacto >= 3) {
				
				if (distancia <= impacto + Math.round (impacto / 3.0)) {
					arrast = impacto;
				} else if (distancia <= impacto + Math.round (2 * impacto / 3.0)) {
					arrast = Math.round(impacto / 2.0);
				} else {
					arrast = Math.round(impacto / 4.0);
				}
			}

		}
		return (int) arrast;
	}

}
