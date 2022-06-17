/**
 * Class cujos objetos representam pontos de coordenadas da posicao de um corpo
 * 
 * @author Ines Lobo 49022 e Xiao Yi 49446
 *
 */
public class Ponto {
	
	private final int x, y;
	//atributos que definem um ponto
	
	/**
	 * Define um ponto
	 * @param x Coordenada x do corpo
	 * @param y Coordenasa y do corpo
	 * @requires x e y sejam numeros inteiros
	 */
	public Ponto (int x, int y){
		this.x=x;
		this.y=y;
	}
	/**
	 * Calcula a distancia entre um ponto e o ponto p
	 * @param p um ponto com coordenada x e y
	 * @return a distancia entre os dois pontos
	 * @requires p seja um ponto com coordenada x e y
	 */
	public int distancia (Ponto p){
	return (int) Math.round(Math.sqrt(Math.pow(x-p.x(), 2)+Math.pow(y-p.y(), 2)));
	}
	/**
	 * Devolve a coordenada x deste ponto
	 * @return a coordenada x
	 */
	public int x (){
	return x;
	}
	/**
	 * Devolve a coordenada y deste ponto
	 * @return a coordenada y
	 */
	public int y (){
	return y;
	}
	/**
	 * Representacao textual deste ponto
	 */
	public String toString (){
		StringBuilder sb = new StringBuilder();
		sb.append("("+ x + "," + y +")");
	return sb.toString();
	}
		
}
