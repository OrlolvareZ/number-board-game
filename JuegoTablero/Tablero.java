/*
 * - Existe una matriz de mxm (ahí usaré una lista de colas)
 * - Esta matriz comienza vacía
 * - En las celdas de la matriz se ingresan números de un pool (para ello usaré un árbol binario)
 * - Este pool inicia con los números uno y dos
 * - No se puede ingresar un número en una casilla ocupada
 * - En cada turno, se deben ingresar los dos números del pool
 * - El número que se le da al usuario pertenece al pool, pero es aleatorio
 * - Cuando en las celdas se junta el mismo número de manera consecutiva de
 * forma horizontal o vertical, estos se mezclan
 * - El resultado de la mezcla es que las casillas anteriores se quedan vacías y
 * la última donde se ingresó el número queda en pie, con el mismo valor pero
 * sumado uno (e.g. de 3 veces 3 pasa a una vez el 4)
 * - Al pool de números se agrega el número generado (e.g., si se tiene como
 * selección el 1,2 y 3, y generamos el 4 en el tablero, ahora el 4 también
 * puede colocarse)
 * - Todo será escrito en Java con una interfaz en la línea de comandos
 * 
 */

package JuegoTablero;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import ArbolBinario.ABEnterosPositivos;

public class Tablero {

    private class Casilla {

        private int valor;

        private Casilla() {
            valor = 0;
        }

        private int getValor() {
            return valor;
        }
        
        private void setValor(int valor) {
            this.valor = valor;
        }

        private void vaciar() {
            valor = 0;
        }

        private boolean estaVacia() {
            return valor == 0;
        }

    }
    
    /**
     * Clase que representa un grupo de números a través de un árbol binario.
     * 
     * <p>
     * Esta clase se encarga de mantener un registro de los números que pueden insertarse en el tablero,
     * así como de generar números aleatorios a partir de los números que contiene para que puedan ser
     * insertados en alguna de las casillas del tablero.
     * </p>
     */
    private class GrupoNumeros {
        
        private ABEnterosPositivos numeros;

        private GrupoNumeros() {
            numeros = new ABEnterosPositivos();
        }

        private void agregar(int... numerosPorAgregar) {
            numeros.insertar(numerosPorAgregar);
        }

        private boolean contiene(int numero) {
            return numeros.buscar(numero);
        }

        private int[] obtenerParAleatorio() {

            int[] par = new int[2];

            par[0] = numeros.obtenerValorAleatorio();
            par[1] = numeros.obtenerValorAleatorio();

            while(par[0] == par[1])
                par[1] = numeros.obtenerValorAleatorio();

            return par;

        }

        private int obtenerValorMaximo() {
            return numeros.getValorMaximo();
        }

    }

    private class ProcesoMezcla {

        private List<Casilla> casillasPorVaciar;
        private Casilla casillaPorMantener;
        private int valorMezcla;
        
        private ProcesoMezcla(List<Casilla> casillasPorVaciar, Casilla casillaPorMantener, int valorMezcla) {
            this.casillasPorVaciar = casillasPorVaciar;
            this.casillaPorMantener = casillaPorMantener;
            this.valorMezcla = valorMezcla;
        }

        private void ejecutar(){
            
            for(Casilla casilla : casillasPorVaciar)
                casilla.vaciar();

            casillaPorMantener.setValor(valorMezcla);
        }

    }

    private int dimension;

    /**
     * Lista de colas que representa el tablero.
     */
    private List<Queue<Casilla>> tablero;

    /**
     * Grupo de números que pueden insertarse en el tablero.
     */
    private GrupoNumeros numeros;

    /**
     * Estado del juego.
     * 
     * <p>
     * El juego puede estar en tres estados:
     * <ul>
     * <li>Ganado: El jugador ha generado el número máximo posible en el tablero.</li>
     * <li>Perdido: El jugador no puede realizar más movimientos.</li>
     * <li>Jugando: El jugador puede realizar movimientos.</li> 
     * </ul>
     * </p>
     */
    public enum Estado {
        GANADO,
        PERDIDO,
        JUGANDO
    }

    private Estado estado;

    /**
     * Número máximo que puede generarse en el tablero para ganar el juego.
     */
    private int numeroMaximo;

    /**
     * Número de veces que se deben generar de manera consecutiva un número para que se mezclen.
     */
    private int consecutivosNecesarios;

    /**
     * Constructor de la clase Tablero.
     * 
     * @param dimension Dimensión del tablero.
     * @param numeroMaximo Número máximo que puede generarse en el tablero para ganar el juego.
     * @param consecutivosNecesarios Número de veces que se deben generar de manera consecutiva un número para que se mezclen.
     * @throws IllegalArgumentException Si la dimensión del tablero es menor a 2, si el número máximo esperado para ganar es menor a 2 o si el número de consecutivos necesarios es menor a 2 o mayor a la dimensión del tablero.
     */
    public Tablero(int dimension, int numeroMaximo, int consecutivosNecesarios) {

        if (dimension < 2)
            // Si la dimensión del tablero es menor a 2, no se podrían colocar números consecutivos.
            throw new IllegalArgumentException("La dimensión del tablero debe ser mayor o igual a 2.");

        if (numeroMaximo < 2)
            // Si el número máximo esperado para ganar es menor a 2, siempre se ganaría el juego.
            throw new IllegalArgumentException("El número máximo debe ser mayor o igual a 2.");

        if (consecutivosNecesarios < 2 || consecutivosNecesarios > dimension)
            // El número de consecutivos necesarios debe ser mayor o igual a 2, porque al menos se deben generar dos números para que se mezclen.
            // Además, debe ser menor a la dimensión del tablero, porque si no, no se podrían colocar suficientes números consecutivos.
            throw new IllegalArgumentException("El número de consecutivos necesarios debe ser mayor o igual a 2 y menor o igual a la dimensión del tablero.");
        
        this.dimension = dimension;

        tablero = new ArrayList<Queue<Casilla>>(dimension);

        for(int i = 0; i < dimension; i++) {
            
            Queue<Casilla> fila = new ArrayDeque<Casilla>();

            for(int j = 0; j < dimension; j++) {
                fila.add(new Casilla());
            }

            tablero.add(fila);

        }

        numeros = new GrupoNumeros();
        numeros.agregar(1, 2);

        estado = Estado.JUGANDO;
        this.numeroMaximo = numeroMaximo;
        this.consecutivosNecesarios = consecutivosNecesarios;
    }

    /** 
     * Imprime el tablero en la consola, con un formato de matriz.
     * 
     * <p>
     * El formato de matriz luce de la siguiente forma:
     * <pre>
     * ----------
     * | 1| 2| 3|
     * ----------
     * | 4| 5| 6|
     * ----------
     * ...
     * ----------
     * </pre>
     * </p>
    */
    public void mostrarTablero() {
        
        // Se obtiene la cantidad de dígitos del número máximo para poder imprimir el tablero
        // de forma ordenada.
        int digitosNumeroMaximo = String.valueOf(numeroMaximo).length();
        
        imprimirNumerosColumnas(digitosNumeroMaximo);
        System.out.println();
        imprimirLimite(digitosNumeroMaximo);
       
        for(int i = 0; i < dimension; i++) {
            
            System.out.print(i + 1 + " ");
            Queue<Casilla> fila = tablero.get(i);
            imprimirFila(fila, digitosNumeroMaximo);
            System.out.println();

        }
        
        imprimirLimite(digitosNumeroMaximo);
        System.out.println();

    }

    /**
     * Devuelve un par de números aleatorios que pueden insertarse en el tablero.
     * 
     * @return Par de números aleatorios que pueden insertarse en el tablero.
     */
    public int[] pedirParAleatorio() {
        return numeros.obtenerParAleatorio();
    }

    private void imprimirNumerosColumnas(int espacioRequerido) {
        
        System.out.print("   ");

        for(int i = 0; i < dimension; i++) {

            System.out.print(i + 1);

            for(int j = 0; j < espacioRequerido; j++)
                System.out.print(" ");
                    }

    }

    /** 
     * Imprime en consola una línea horizontal que sirve como límite para el tablero.
     * 
     * @param espacioRequerido Espacio adicional que deberá compensar la línea horizontal para que el tablero luzca ordenado.
     * 
    */
    private void imprimirLimite(int espacioRequerido) {
        
        System.out.print("  ");
        
        for(int i = 0; i < dimension; i++) {
            System.out.print("-");
            for(int j = 0; j < espacioRequerido; j++)
                System.out.print("-");
        }

        System.out.println("-");

    }

    /**
     * Imprime una fila del tablero con un formato de matriz.
     * 
     * <p>
     * El formato de matriz luce de la siguiente forma:
     * <pre>
     * | 1| 2| 3|
     * </pre>
     * </p>
     * 
     * @param fila Fila del tablero a imprimir.
     * @param espacioRequerido Espacio mínimo que ocupará cada casilla en la fila.
    */
    private void imprimirFila(Queue<Casilla> fila, int espacioRequerido) {

        Queue<Casilla> filaAuxiliar = new ArrayDeque<Casilla>(fila);
        
        System.out.print("|");

        while(!filaAuxiliar.isEmpty()) {

            Casilla casilla = filaAuxiliar.poll();

            // Se obtiene el valor de la casilla y se convierte a String, para imprimirlo
            // con en un espacio de tamaño fijo. Si la casilla está vacía, se imprime un espacio.
            String valorConEspacio =
                casilla.estaVacia() ?
                    String.format("%" + espacioRequerido + "s", " ")
                    : // Si no esta vacía, imprimir el valor
                    String.format("%" + espacioRequerido + "d", casilla.getValor());
            
            System.out.print(valorConEspacio + "|");

        }
        
    }

    /**
     * Agrega un número al grupo de números disponibles.
     * 
     * @param numero Número a agregar.
     */
    private void agregarNumeroEnGrupo(int numero) {

        numeros.agregar(numero);

    }

    /**
     * Inserta un número en el tablero.
     * 
     * <p>
     * No se pueden insertar números en casillas ocupadas.
     * Una vez que se inserta el número, se revisa si hay números consecutivos en la fila o columna.
     * De ser así, se eliminan los números consecutivos, salvo por el que se insertó y se le suma 1.
     * </p>
     * 
     * @param x Coordenada x de la casilla.
     * @param y Coordenada y de la casilla.
     * @param numero Número a insertar.
     * @return <code>true</code> si el número se insertó correctamente, <code>false</code> si la casilla está ocupada.
     * @throws IllegalArgumentException Si el número no está en el grupo de números disponibles.
     * @throws IndexOutOfBoundsException Si las coordenadas están fuera del tablero.
    */    
    public boolean insertarNumero(int x, int y, int numero) throws IllegalArgumentException, IndexOutOfBoundsException {

        if(x < 1 || x > dimension || y < 1 || y > dimension)
            throw new IndexOutOfBoundsException("Las coordenadas están fuera del tablero.");

        if(!numeros.contiene(numero))
            throw new IllegalArgumentException("El número no está en el grupo de números disponibles.");

        Casilla casilla = getCasilla(x, y);

        if(!casilla.estaVacia())
            return false;

        casilla.setValor(numero);
        
        if (!hayCasillasVacias())
            setEstado(Estado.PERDIDO);

        return true;

    }

    /**
     * Obtiene el valor de una casilla.
     * 
     * <p>
     * Para obtener la casilla, se navega por el eje vertical (la lista de colas) hasta llegar a la fila
     * correspondiente, y luego se navega por el eje horizontal (la cola) hasta llegar a la casilla.
     * </p>
     * 
     * @param x Coordenada x de la casilla.
     * @param y Coordenada y de la casilla.
     * @return Valor de la casilla.
    */
    private Casilla getCasilla(int x, int y) {

        // Se obtiene la columna correspondiente a la coordenada y
        Queue<Casilla> columna = tablero.get(y - 1);

        // Si la casilla es la primera, se devuelve directamente
        if (x == 1){

            return columna.peek();
        }
        // Si no, se recorre la columna hasta llegar a la casilla
        else {
            
            Casilla casillaActual = null;
            Casilla casillaObjetivo = null;
            int i = 1;

            // Se recorre toda la columna
            while(i <= dimension) {
                
                casillaActual = columna.poll();

                // Si se llega a la casilla, se guarda la referencia
                if (i == x)
                    casillaObjetivo = casillaActual;

                // Se agrega la casilla actual al final de la columna
                columna.add(casillaActual);
                i++;
            }
            /*
            * Este proceso se repite para todas las casillas, a fin de mantener el orden de la fila.
            * Para entender mejor el proceso, se puede imaginar que se tiene un montón de papeles,
            * si vamos tomando el que está encima y lo ponemos abajo, uno tras otro hasta llegar al
            * primer papel que sacamos, el orden de los papeles se mantiene.
            */ 

            return casillaObjetivo;
        }

    }

    /**
     * Revisa si hay elementos consecutivos en la fila y columna de una casilla.
     * 
     * <p>
     * Si hay elementos consecutivos, se mezclan y se revisa de nuevo la fila y columna,
     * ya que se pueden haber generado nuevos elementos consecutivos.
     * </p>
     * 
     * @param x Coordenada x de la casilla.
     * @param y Coordenada y de la casilla.
    */
    public void revisarConsecutivos(int x, int y){

        ProcesoMezcla mezclaHorizontal = revisarConsecutivosH(x, y);
        ProcesoMezcla mezclaVertical = revisarConsecutivosV(x, y);

        // Si no hay elementos consecutivos, no se hace nada
        if (mezclaHorizontal == null && mezclaVertical == null)
            return;
        
        if (mezclaHorizontal != null)
            mezclaHorizontal.ejecutar();

        if (mezclaVertical != null)
            mezclaVertical.ejecutar();

        // Si se hizo una mezcla, se revisa de nuevo la fila y columna,
        // ya que se pueden haber generado nuevos elementos consecutivos.
        // revisarConsecutivos(x, y);
        

        // Además, se revisa si el número resultante de la mezcla es mayor al mayor número
        // del grupo de números a escoger. De ser así, debe registrarse en el grupo de números.
        int valorCasilla = getCasilla(x, y).getValor();

        if (valorCasilla == numeroMaximo)
            setEstado(Estado.GANADO);
        else if (valorCasilla > numeros.obtenerValorMaximo())
            agregarNumeroEnGrupo(valorCasilla);

    }

    /**
     * Revisa si hay elementos consecutivos en la fila de una casilla.
     * 
     * <p>
     * Si hay elementos consecutivos, se mezclan y se revisa de nuevo la fila,
     * ya que se pueden haber generado nuevos elementos consecutivos.
     * </p>
     * 
     * @param x Coordenada x de la casilla.
     * @param y Coordenada y de la casilla.
     * @return ProcesoMezcla que contiene la información de la mezcla, o <code>null</code> si no hay mezcla.
    */
    private ProcesoMezcla revisarConsecutivosH(int x, int y){

        Casilla casillaActual = getCasilla(x, y);
        
        List<Casilla> casillasConsecutivas = new ArrayList<Casilla>();
        casillasConsecutivas.add(casillaActual);

        for (int i = x + 1; i <= dimension; i++) {
            
            Casilla casillaSiguiente = getCasilla(i, y);

            if (casillaSiguiente.estaVacia())
                break;

            if (casillaSiguiente.getValor() == casillaActual.getValor())
                casillasConsecutivas.add(casillaSiguiente);
            else
                break;
        }

        for (int i = x - 1; i > 0; i--) {
            
            Casilla casillaAnterior = getCasilla(i, y);

            if (casillaAnterior.estaVacia())
                break;

            if (casillaAnterior.getValor() == casillaActual.getValor())
                casillasConsecutivas.add(casillaAnterior);
            else
                break;
        }

        if (casillasConsecutivas.size() >= consecutivosNecesarios)
            return new ProcesoMezcla(casillasConsecutivas, casillaActual, casillaActual.getValor() +1);

        return null;

    }

    /**
     * Revisa si hay elementos consecutivos en la columna de una casilla.
     * 
     * <p>
     * Si hay elementos consecutivos, se mezclan y se revisa de nuevo la columna,
     * ya que se pueden haber generado nuevos elementos consecutivos.
     * </p>
     * 
     * @param x Coordenada x de la casilla.
     * @param y Coordenada y de la casilla.
     * @return ProcesoMezcla que contiene la información de la mezcla, o <code>null</code> si no hay mezcla.
    */
    private ProcesoMezcla revisarConsecutivosV(int x, int y){

        Casilla casillaActual = getCasilla(x, y);
        
        List<Casilla> casillasConsecutivas = new ArrayList<Casilla>();
        casillasConsecutivas.add(casillaActual);

        for (int i = y + 1; i <= dimension; i++) {
            
            Casilla casillaSiguiente = getCasilla(x, i);

            if (casillaSiguiente.estaVacia())
                break;

            if (casillaSiguiente.getValor() == casillaActual.getValor())
                casillasConsecutivas.add(casillaSiguiente);
            else
                break;
        }

        for (int i = y - 1; i > 0; i--) {
            
            Casilla casillaAnterior = getCasilla(x, i);

            if (casillaAnterior.estaVacia())
                break;

            if (casillaAnterior.getValor() == casillaActual.getValor())
                casillasConsecutivas.add(casillaAnterior);
            else
                break;
        }

        if (casillasConsecutivas.size() >= consecutivosNecesarios)
            return new ProcesoMezcla(casillasConsecutivas, casillaActual, casillaActual.getValor() +1);

        return null;

    }

    /**
     * Revisa si hay casillas vacías en el tablero.
     * 
     * @return <code>true</code> si hay casillas vacías, <code>false</code> en caso contrario.
    */
    private boolean hayCasillasVacias(){

        for (int i = 1; i <= dimension; i++) {
            for (int j = 1; j <= dimension; j++) {
                
                Casilla casilla = getCasilla(i, j);

                if (casilla.estaVacia())
                    return true;
            }
        }

        return false;

    }

    public Estado getEstado() {
        return estado;
    }

    private void setEstado(Estado estado) {
        this.estado = estado;
    }

}