package ArbolBinario;

/**
 * Esta interfaz define las operaciones que debe implementar un árbol binario.
 */
public interface ArbolBinario {

    /**
     * Inserta un valor en el árbol.
     * 
     * @param valor: valor a insertar
     */
    public void insertar(int valor);

    /* 
     * Busca un valor en el árbol.
     * 
     * @param valor: valor a buscar
     * @return true si el valor se encuentra en el árbol, false en caso contrario
     */
    public boolean buscar(int valor);

    /**
     * Elimina un valor del árbol.
     * 
     * @param valor: valor a eliminar
     * @return true si el valor se encontraba en el árbol y fue eliminado, false en caso contrario
     */
    public boolean eliminar(int valor);
    
}
