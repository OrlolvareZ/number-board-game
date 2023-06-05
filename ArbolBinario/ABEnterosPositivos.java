package ArbolBinario;

import java.util.Random;

/**
 * Implementación de un árbol binario de busqueda para enteros positivos consecutivos.
 */

public class ABEnterosPositivos implements ArbolBinario {

	/**
	* Implementación de un nodo para el árbol binario de busqueda.
	*/
	
	private class Nodo {


		private int valor;
		private Nodo izquierdo, derecho;

		private Nodo(int valor) {
			this.valor = valor;
			izquierdo = null;
			derecho = null;
		}
	}

	/**
	 * El nodo raiz del árbol.
	 */
	private Nodo raiz;

	public ABEnterosPositivos() {
		raiz = null;
	}

	/**
	 * Inserta un valor en el árbol.
	 * 
	 * @param valor: valor a insertar
	 */
	public void insertar(int valor) {

		if (raiz == null) {
			raiz = new Nodo(valor);
			return;
		}

		insertarRecursivamente(raiz, valor);
	}

	/**
	 * Inserta varios valores en el árbol.
	 * 
	 * @param valores: valores a insertar
	 */
	public void insertar(int... valores) {

		for (int valor : valores) {
			insertar(valor);
		}
	}

	/**
	 * Recorre el árbol hasta encontrar el lugar donde debe insertarse el valor, y lo
	 * inserta.
	 * 
	 * @param nodo:  nodo actual
	 * @param valor: valor a insertar
	 */
	private void insertarRecursivamente(Nodo nodo, int valor) {

		if (valor < nodo.valor) {

			if (nodo.izquierdo != null) {
				insertarRecursivamente(nodo.izquierdo, valor);
			} else {
				nodo.izquierdo = new Nodo(valor);
			}

		} else if (valor > nodo.valor) {

			if (nodo.derecho != null) {
				insertarRecursivamente(nodo.derecho, valor);
			} else {
				nodo.derecho = new Nodo(valor);
			}
		}
	}

	/**
	 * Busca un valor en el árbol.
	 * 
	 * @param valor: valor a buscar
	 * @return true si el valor se encuentra en el árbol, false en caso contrario
	 */
	public boolean buscar(int valor) {

		if (raiz == null) {
			return false;
		}

		return buscarRecursivamente(raiz, valor);
	}

	/**
	 * Recorre el árbol hasta encontrar el valor buscado.
	 * 
	 * @param nodo:  nodo actual
	 * @param valor: valor a buscar
	 * @return true si el valor se encuentra en el árbol, false en caso contrario
	 */
	private boolean buscarRecursivamente(Nodo nodo, int valor) {

		if (nodo == null) {
			return false;
		}

		if (valor == nodo.valor) {
			return true;
		}

		if (valor < nodo.valor) {
			return buscarRecursivamente(nodo.izquierdo, valor);
		}

		return buscarRecursivamente(nodo.derecho, valor);
	}
	
	/**
	 * Elimina un valor del árbol.
	 * 
	 * @param valor: valor a eliminar
	 * @return true si el valor se encontraba en el árbol y fue eliminado, false en caso contrario
	 */
	public boolean eliminar(int valor) {

		if (raiz == null) {
			return false;
		}

		if (raiz.valor == valor) {
			raiz = null;
			return true;
		}

		return eliminarRecursivamente(raiz, valor);
	}

	/**
	 * Recorre el árbol hasta encontrar el valor buscado, y lo elimina.
	 * 
	 * @param nodo:  nodo actual
	 * @param valor: valor a eliminar
	 * @return true si el valor se encontraba en el árbol y fue eliminado, false en caso contrario
	 */
	private boolean eliminarRecursivamente(Nodo nodo, int valor) {

		if (nodo == null) {
			return false;
		}

		if (valor < nodo.valor) {

			if (nodo.izquierdo == null) {
				return false;
			}

			if (nodo.izquierdo.valor == valor) {
				nodo.izquierdo = null;
				return true;
			}

			return eliminarRecursivamente(nodo.izquierdo, valor);
		}

		if (nodo.derecho == null) {
			return false;
		}

		if (nodo.derecho.valor == valor) {
			nodo.derecho = null;
			return true;
		}

		return eliminarRecursivamente(nodo.derecho, valor);
	}

	/**
	 * Regresa un valor aleatorio que se encuentre en el árbol.
	 * 
	 * @return un número aleatorio que se encuentre en el árbol.
	 */
	public int obtenerValorAleatorio() {

		Random generador = new Random();
		int valorMaximo = getValorMaximo();
		int valorAleatorio = generador.nextInt(valorMaximo + 1);

		while (!buscar(valorAleatorio)) {
			valorAleatorio = generador.nextInt(valorMaximo + 1);
		}

		return valorAleatorio;
		
	}

	/**
	 * Regresa el valor máximo que se encuentra en el árbol.
	 * 
	 * @return el valor máximo que se encuentra en el árbol.
	 */
	public int getValorMaximo() {

		if (raiz == null) {
			return -1;
		}

		return getValorMaximoRecursivo(raiz);
	}

	/**
	 * Recorre el árbol hasta encontrar el valor máximo.
	 * 
	 * @param nodo: nodo actual
	 * @return el valor máximo que se encuentra en el árbol.
	 */
	private int getValorMaximoRecursivo(Nodo nodo) {

		if (nodo.derecho == null) {
			return nodo.valor;
		}

		return getValorMaximoRecursivo(nodo.derecho);
	}

}