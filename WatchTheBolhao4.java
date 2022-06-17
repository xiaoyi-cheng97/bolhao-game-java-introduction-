import java.util.Random;
import java.util.Scanner;
/**
 * Esta classe permite jogar o jogo Watch out for the Bolhao
 * Usa as classes que foram pedidas aos alunos na fase 4 do 
 * trabalho de IP
 * @author Isabel Nunes
 * @date Dezembro 2016
 */
public class WatchTheBolhao4 {

	// Os simbolos usados na representacao textual do caminho para representar
	// os varios tipos de posicao do jogo; correspodem, pela mesma ordem, aos
	// valores do enumerado Posicao (excetuando o NORMAL)
	public static final char [] SIMBOLOS = {'~', '^', ' '};
    // Simbolo que representa um Mau no caminho
	public static final char MAU = 'M';
    // Numero maximo de jogadas
	public static final int MAX_JOGADAS = 10;


	/**
	 * Neste metodo eh lancado um jogo com varios Aviadores e um caminho
	 * populado por varios Maus.
	 * @param args Nao eh usado
	 */
	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in);
		Random gerador = new Random(1);

		/***** Definicao de valores iniciais para o jogo *****/
		// Numero de posicoes do caminho onde se desenrolarah o jogo
		int dim = qualDimensao(leitor);
		// Movimento a escolher pelos Aviadores que querem desistir
		int desistir = dim * 2;
		// Reserva de gasolina igual para todos os Aviadores
		int gasolina = dim * 3;
        // Altura maxima para o aviao 
        int alturaMax = Math.max(Jogo.ALTURA_MIN, dim / 4); 
        // Quantos Maus
        int nMaus = Math.max(1,dim / 6);
       // Posicoes iniciais dos Maus
		int[] posM = aleatoriosDiferentes(nMaus, dim, gerador);
        // Posicoes das casas especiais - CamaElastica e FabricaDeSabao
		int[] posEspeciais = aleatoriosDiferentes(dim / 4, dim, gerador);
		int[] posCE = new int[dim / 8];
		int[] posFS = new int[dim / 8];
		distribui(posEspeciais, posCE, posFS);

		/***** Criacao de um jogo e registo de Aviadores no jogo *****/
		Jogo meuJogo = new Jogo (dim,gasolina,posM,posCE,posFS,gerador);
		// Pede ao user a info sobre os aviadores e regista-a no jogo
		quaisAviadores(meuJogo,dim,alturaMax,leitor);
		
		/***** Mostrar representacao do jogo no standard output *****/
		// Nomes dos Aviadores em jogo
		String [] emJogo = meuJogo.aviadoresEmJogo();
		// Mostra o ambiente e as gasolinas ao user
		System.out.println(representacaoTextual(meuJogo));
		imprimeGasolinas(meuJogo, emJogo);
		
		/***** Jogar o jogo *****/
		System.out.println("VAMOS COMECAR O JOGO!");
		// Para saber se terminou o jogo
		boolean terminou = false;
		// No inicio todos os Aviadores estao vivos e nenhum desistiu
		boolean aindaHaAvioes = true;
		// Prepara o valor da variavel que contem o numero de ordem do primeiro 
		// Aviador a escolher o movimento e a lancar o Bolhao (vai ser incrementado
		// no ciclo que se segue)
		int primeiro = -1;
		
		// Enquanto o jogo nao tiver terminado e pelo menos um Aviador
		// estiver em jogo
		for (int i = 1 ; i < MAX_JOGADAS && !terminou && aindaHaAvioes ; i++){
			// Calcula qual o Aviador que joga primeiro nesta jogada
			primeiro = (primeiro + 1) % emJogo.length;
			// Pede as movimentacoes a todos os Aviadores em jogo
			Ponto [] movims = pedeMovimentos(emJogo,meuJogo,primeiro,desistir,
					                         alturaMax,dim,leitor);
			// Aplica a jogada de cada Aviador, por ordem
			for (int j = 0 ; j < emJogo.length ; j++) {
				// Calcula qual o proximo Aviador a lancar o Bolhao
				int indexAviador = (primeiro + j) % emJogo.length;
				// Pede ao jogo para registar a jogada desse Aviador
				meuJogo.fazJogada(emJogo[indexAviador], movims[indexAviador],desistir);
				// mostra o ambiente ao utilizador
				System.out.println(representacaoTextual(meuJogo));
			}

			// mostra ao user a gasolina de cada Aviador em jogo 
			emJogo = meuJogo.aviadoresEmJogo();
			imprimeGasolinas(meuJogo, emJogo);

			aindaHaAvioes = emJogo.length != 0;
			terminou = meuJogo.terminado();
		}
		
		// Mostra os resultados finais
		mostraResultados(meuJogo);
	}

/***************************************************************************/
/**************  METODOS PARA PEDIR VALORES AO UTILIZADOR  *****************/
/***************************************************************************/	

	/**
	 * Ler a dimensao do caminho do jogo
	 * @param sc Canal de leitura
	 * @return Dimensao do caminho (entre Jogo.MIN_DIMENSAO e Jogo.MAX_DIMENSAO)
	 * @requires sc != null
	 */
	private static int qualDimensao(Scanner sc){ 
	    // Pede ao user a dimensao do caminho	    
		System.out.println("Qual a dimensao do caminho? " +
						   "(valor entre " + Jogo.MIN_DIMENSAO + " e " + 
				           Jogo.MAX_DIMENSAO + ")");
	    String erro = "Inteiro entre " + Jogo.MIN_DIMENSAO + " e " + 
				      Jogo.MAX_DIMENSAO + "!";
	    int result = lerValorNoIntervalo(Jogo.MIN_DIMENSAO, Jogo.MAX_DIMENSAO, 
	    		                         erro, sc);
	    sc.nextLine();
	    return result;
	}

	/**
	 * Pede ao user os nomes e as posicoes iniciais dos Aviadores e regista-os
	 * no jogo
	 * @param g O jogo
	 * @param dim Numero de posicoes do caminho
	 * @param altMax Altura maxima a que um aviao pode subir
	 * @param sc Canal de leitura
	 * @requires g != null && dim <= Jogo.MAX_DIMENSAO && sc != null
	 */
	private static void quaisAviadores(Jogo g, int dim, int altMax, Scanner sc) {

		System.out.println("Quantos Aviadores?");
		int nAviadores = lerValorNoIntervalo(1, Jogo.MAX_AVIADORES, 
				                            "Inteiro entre " + 1 + " e " + 
		                                    Jogo.MAX_AVIADORES + "!", 
		                                    sc);

		// primeiro consome o fim de linha que ficou no canal de leitura
		sc.nextLine();
		
		// Para cada Aviador em jogo, pede nome e posicao inicial
	    // e regista essas infos no jogo
	    for (int i = 1 ; i <= nAviadores ; i++) {
		    // Pede ao jogador o nome e a posicao inicial
		    System.out.println("Aviador " + i + ": qual o seu nome?");
			String nome = sc.nextLine();
		    Ponto posA = pedePosicaoAviador (nome,dim,altMax,sc);
		    sc.nextLine();
		    // Diz ao jogo para registar a informacao do cacador
		    g.registaAviador (nome, posA);
	    }
	}

	/**
	 * Posicao inicial de um dado Aviador, lida atraves do standard input
	 * @param nome Nome do Aviador a quem eh pedida a informacao
	 * @param dim Valor maximo para a posicao
	 * @param altMax Altura maxima a que um aviao pode subir
	 * @param sc Canal de leitura
	 * @return Um Ponto representando a posicao inicial do aviao 
	 *         do Aviador nome
	 * @requires nome != null && dim <= Jogo.MAX_DIMENSAO && sc != null
	 */
	private static Ponto pedePosicaoAviador(String nome,int dim, int altMax, Scanner sc){
	    System.out.println(nome.toUpperCase() + ", quais as suas coordenadas iniciais? ");
	    int x = pedeCoordenada("X", 1, dim, sc);
	    int y = pedeCoordenada("Y", Jogo.ALTURA_MIN, altMax, sc);
	    return new Ponto(x,y);
	}

    /**
     * Inteiro num dado intervalo correspondente a uma dada coordenada do aviao
     * @param c Qual a coordenada a pedir
     * @param inf Limite inferior para a coordenada
     * @param sup Limite superior para a coordenada
     * @param leitor Objeto Scanner a usar para a leitura
     * @requires c != null && inf >= sup && leitor != null
     * @return O inteiro no intervalo [inf,sup] dado pelo utilizador
     */
    private static int pedeCoordenada(String c, int inf, int sup, 
    		                          Scanner leitor) {
	
	    System.out.println("Coordenada " + c + ":" + " (inteiro entre " + 
		                   inf + " e " + sup + ")");
	    return lerValorNoIntervalo(inf,sup,
			                       "Inteiro entre " + inf + " e " + sup + "!", 
	                               leitor);
    }
	
	/**
	 * Pede e valida os movimentos dos Aviadores e devolve-os
	 * @param emJogo Os nomes dos Aviadores que ainda estao em jogo
	 * @param g O jogo
	 * @param primeiro A posicao no array emjogo do primeiro Aviador a jogar
	 * @param desistir O valor que o Aviador deve escolher para indicar que
	 *                 quer desistir
	 * @param altMax Altura maxima para os avioes
	 * @param dim Dimensao do Caminho
	 * @param sc Canal de leitura
	 * @requires emJogo != null && g != null && sc != null && primeiro >= 0 &&
	 *           primeiro < emJogo.length
	 */
	private static Ponto [] pedeMovimentos (String[] emJogo, Jogo g, int primeiro,
			                        int desistir, int altMax, int dim, Scanner sc){
		Ponto[] movims = new Ponto[emJogo.length];  // ficam todos a null
		System.out.println("Aviadores, decidam os vossos movimentos! (" +
				           desistir + " para desistir)");
		// O Aviador que vai jogar
        int proximo = primeiro;
		// Para cada Aviador em jogo	    
		for (int i = 0 ; i < emJogo.length ; i++){
			Ponto pos = g.posicaoAviao(emJogo[proximo]);
			// Pede movimento ao Aviador   
			System.out.println(emJogo[proximo].toUpperCase() + " - posicao atual (" +
			                   pos.x() + "," + pos.y() + ")");
			
			// Valores minimo e maximo para movimento horizontal
			int minMov = 1 - pos.x();
			int maxMov = dim - pos.x();
			int movX = pedeMovOuDesist("Horizontal? ", minMov, maxMov, desistir, sc);

			// Valores minimo e maximo para movimento vertical
			minMov = Jogo.ALTURA_MIN - pos.y();
			maxMov = altMax - pos.y();
			int movY = pedeMovOuDesist("Vertical? ", minMov, maxMov, desistir, sc);

			movims[proximo] = new Ponto(movX, movY);
			proximo = (proximo + 1) % emJogo.length;

		}
		return movims;
	}
	

	/**
	 * Pede ao user e devolve um inteiro entre dois valores
	 * @param texto Info para o user
	 * @param inf Limite inferior para o valor pedido
	 * @param sup Limite superior para o valor pedido
	 * @param desist Valor para desistencia
	 * @param leitor Objeto Scanner
	 * @requires texto != null && inf <= sup && leitor != null
	 * @return Um inteiro no intervalo [inf,sup] ou igual a desist introduzido
	 *          pelo user de forma robusta
	 */
	private static int pedeMovOuDesist(String texto, int inf, int sup, int desist, 
			                           Scanner leitor) {
	       System.out.println(texto +
                            "(Valor entre " + inf + " e " + sup + 
                            " ou " + desist + " para desistir)");
           return lerValorNoIntervaloOuOutro(inf, sup, desist,
  		                    "Valor entre " + inf + " e " + sup + 
  		                    " ou " + desist + " para desistir)", 
  		                    leitor);
	}


/***************************************************************************/
/******** METODOS PARA IMPRIMIR A REPRESENTACAO TEXTUAL DO JOGO ************/
/***************************************************************************/		
	
	/**
	 * Representacao textual do caminho com as posicoes dos Maus,
	 * das CamasElasticas, das FabricasDeSabao e das crateras
	 * @param jogo - O jogo 
	 * @return Um stringBuilder com a representacao do caminho
	 * @requires jogo != null
	 */
	private static StringBuilder representacaoTextual (Jogo jogo){
		int dim = jogo.dimensao();
		String espacos = nEspacos(dim);
		int[] maus = jogo.posicoesMaus();
		Posicao[] relevo = jogo.relevo();
		StringBuilder result = new StringBuilder(espacos);

		// Escreve a letra MAU nas posicoes dos Maus
		for (int i = 0 ; i < maus.length ; i++) {
		   if (maus[i] != -1) {
				result.setCharAt(maus[i] - 1, MAU);			   
		   }
		}
		result.append("\n");

		for (int i = 1 ; i <= dim ; i++) {
			if (relevo[i - 1] != Posicao.NORMAL) {
			    result.append(SIMBOLOS[relevo[i - 1].ordinal()]);
			} else {
			    result.append(i % 10);
			}
		}

		result.append("\n");
		
       for (int i = 1 ; i <= dim / 10 ; i++) {
	        result.append(espacos.substring(0,9));
	        result.append(i);
       }

		result.append('\n');
		
        return result;
	}
		
	/**
	 * Uma String formada por um dado numero de espacos
	 * @param n
	 * @return String formada por n espacos
	 */
	private static String nEspacos(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1 ; i <= n ; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
	

	/**
	 * Imprime os valores de gasolina para cada Aviador em jogo
	 * @param g O jogo
	 * @param emJogo Os nomes dos Aviadores que ainda estao em jogo
	 * @requires g != null && emJogo != null
	 */
	private static void imprimeGasolinas (Jogo g, String[] emJogo){
		// Para cada Aviador em jogo	    
		for (String nome: emJogo){
		    System.out.println("Gasolina de: " + nome.toUpperCase() + 
		    		" = " + g.gasolina(nome));  		    
		}
		System.out.println("==============================================");
	}

/***************************************************************************/
/****************** METODO PARA IMPRIMIR RESULTADOS DO JOGO ****************/
/***************************************************************************/
	/**
	 * Imprime os resultados do jogo
	 * @param g O jogo
	 * @requires g != null
	 */
	private static void mostraResultados (Jogo g){
		
     	System.out.println("Acabou o jogo!");
     	int resultado = g.vencedor();
     	if(resultado == -1) {
        	System.out.println("Parabens aos Maus! Conseguiram nao morrer todos!");    
     	} else if (resultado == 1){
            System.out.println("Parabens aos Bons! Ganharam o jogo");
     	} else {
            System.out.println("O jogo acabou empatado");
     	}
     	
		// Nomes de todos os Aviadores que estiveram a jogar
		String [] nomes = g.todosAviadores();
		// Quantos Maus cada um desses Aviadores matou
     	int[] quantosMaus = g.quantosMausMataram();
     	
     	for (int i = 0 ; i < quantosMaus.length ; i++) {
     		System.out.println("O Aviador " + nomes[i] + " matou " + 
     				           quantosMaus[i] + " mau" +
     				           (quantosMaus[i] == 1 ? "" : "s"));
     	}
	}

	/***************************************************************************/
	/**********  METODOS PARA GERAR VETOR DE ALEATORIOS DIFERENTES  ************/
	/***************************************************************************/
	
    /**
     * Um vetor de valores aleatorios, todos diferentes, num dado intervalo
     * @param n - O numero de elementos que o vetor vai ter
     * @param sup - O limite superior do intervalo
     * @param g - O gerador de aleatorios
     * @requires n <= sup (para poderem ser todos diferentes)
     */
    static int[] aleatoriosDiferentes (int n, int sup, Random g){
        int[] result = new int [n];
        int i = 0;
        while (i < result.length) {
            int aleatorio = g.nextInt(sup) + 1;
            if (!contidoEmParte(aleatorio,result,i)){
                result[i] = aleatorio;
                i++;
            }
        }
        return result;
    }

    /**
     * Um valor estah contido em parte de um array?
     * @param val - O valor a procurar
     * @param v - O array onde procurar
     * @param parte - O indice ateh onde procurar (exclusive)
     * @return true se val estah nas primeiras parte posicoes de v
     * @requires v != null && parte < v.length
     */
    static boolean contidoEmParte (int val, int[] v, int parte){
        boolean encontrou = false;
        for (int i = 0 ; i < parte && !encontrou ; i++)
            if (v[i] == val)
                encontrou = true;
            return encontrou;
        }
	
    /**
     * Distribuir os elementos de um vetor por outros 2 vetores
     * @param v - O vetor origem
     * @param v1 - O primeiro vetor destino
     * @param v2 - O segundo vetor destino
     * @requires v != null && v1 != null && v2 != null
     */
    static void distribui (int[] v, int[] v1, int[] v2){
    	int p = 0;
        for(int i = 0 ; i < v1.length ; i++, p++)
        	v1[i] = v[p];
        for(int i = 0 ; i < v2.length ; i++, p++)
        	v2[i] = v[p];
    }

/***************************************************************************/
/*********** METODOS PARA LEITURA ROBUSTA A PARTIR DO System.in ***********/
/***************************************************************************/
	
	/**
	 * Primeiro inteiro no canal de leitura que esta num dado intervalo
	 * @param infLim   Limite inferior do intervalo
	 * @param supLim  Limite superior do intervalo
	 * @param outro  Valor alternativo permitido
	 * @param errMess  Mensagem de  erro a apresentar no System.out
	 * @param sc  Canal de leitura
	 * @return um valor entre infLim e supLim
	 * @requires sc != null && infLim <= supLim && errMess != null
	 */
	private static int lerValorNoIntervaloOuOutro(int infLim, int supLim, int outro, 
                                         String errMess, Scanner sc) {
		int valor = 0;
		boolean erro;
		do {
		    valor = lerInteiro ( errMess, sc );
		    erro = valor != outro && (valor < infLim || valor > supLim);
		    if ( erro )
		       System.out.println ( errMess );
		} while ( erro );

		return valor;
	}
	
	/**
	 * Primeiro inteiro no canal de leitura que esta num dado intervalo
	 * @param infLim   Limite inferior do intervalo
	 * @param supLim  Limite superior do intervalo
	 * @param errMess  Mensagem de  erro a apresentar no System.out
	 * @param sc  Canal de leitura
	 * @return um valor entre infLim e supLim
	 * @requires sc != null && infLim <= supLim && errMess != null
	 */
	private static int lerValorNoIntervalo(int infLim, int supLim, 
                                         String errMess, Scanner sc) {
		int valor = 0;
		boolean erro;
		do {
		    valor = lerInteiro ( errMess, sc );
		    erro = valor < infLim || valor > supLim;
		    if ( erro )
		       System.out.println ( errMess );
		} while ( erro );

		return valor;
	}


	/**
	 * Primeiro inteiro no canal de leitura
	 * @param errMess - mensagem a escrever no System.out caso o valor 
	 *              acessivel no canal de leitura nao seja um inteiro
	 * @param sc - canal de leitura
	 * @return valor inteiro
	 * @requires errMess != null && sc != null
	 */
	private static int lerInteiro ( String errMess, Scanner sc ) {
		int valor = 0;
		boolean erro = true;
		do {
		    if ( sc.hasNextInt () ) {
			valor = sc.nextInt ();  // consome o inteiro
			erro = false;
		    }	
		    else {
			sc.next ();      // consome o que lah esteja
			System.out.println ( errMess );
		    }				
		} while ( erro );

		return valor;
	}	

}
