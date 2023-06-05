package JuegoTablero;

import java.util.Scanner;

import JuegoTablero.Tablero.Estado;

public class App {
    
    public static void main(String[] args) {
        
        Tablero tablero = new Tablero(
                                        7,
                                        7,
                                        3
                                    );

        String bienvenida = "Bienvenido al juego de tablero\n" +
                            "Escribe la opción que deseas:\n\n" +
                            "1. Jugar\n" +
                            "2. Salir\n\n" +
                            "Tu opción: ";

        // Se imprime la bienvenida y se pide la opción
        System.out.print(bienvenida);
                            
        Scanner sc = new Scanner(System.in);

        int opcion = -1;
        
        while (opcion == -1){

            try {
    
                opcion = Integer.parseInt(sc.nextLine());
                
                if (opcion != 1 && opcion != 2) {
                    throw new Exception();
                }
                
            } catch (Exception e) {
                System.out.println("Opción no válida");
            }
            
        }
        
        // Si la opción es 2, se termina el programa
        if (opcion == 2) {
            System.out.println("¡Hasta luego!");
            System.exit(0);
        }
        
        // Comienza el juego
        while(true){

            tablero.mostrarTablero();

            // Se genera un par aleatorio del grupo de números
            int[] parNumeros = tablero.pedirParAleatorio();

            System.out.println();
            System.out.println("Par en turno: " + parNumeros[0] + " & " + parNumeros[1]);

            int coordenada1 = 0;
            int coordenada2 = 0;

            int[][] coordenadas = new int[2][2];

            for (int i = 0; i < 2; i++) {
                
                while(true){

                    try {
                        
                        System.out.println(
                            "Escribe la casilla en la que ingresarás el "
                            + (i == 0 ? "primer" : "segundo")
                            + " número: "
                        );

                        String coordCasilla = sc.nextLine();
    
                        coordenada1 = Integer.parseInt(coordCasilla.split(",")[0]);
                        coordenada2 = Integer.parseInt(coordCasilla.split(",")[1]);
                        coordenadas[i][0] = coordenada1;
                        coordenadas[i][1] = coordenada2;

                        boolean resultado = tablero.insertarNumero(coordenada1, coordenada2, parNumeros[i]);
    
                        if (!resultado) 
                            System.out.println("La casilla está ocupada. Por favor escoge otra.");
                        else
                            break;
            
                    } catch (Exception e) {
                        System.out.println("Por favor escribe la casilla en el formato correcto. Ejemplo: 1,2");
                    }

                } // Ciclo de validación de formato y de casilla ocupada

            } // Ciclo de inserción de números

            tablero.revisarConsecutivos(coordenadas[0][0], coordenadas[0][1]);
            tablero.revisarConsecutivos(coordenadas[1][0], coordenadas[1][1]);
            
            // Se verifica si el juego ha terminado
            if (tablero.getEstado() == Estado.GANADO) {
                System.out.println("¡Felicidades! Has ganado el juego.");
                break;
            } else if (tablero.getEstado() == Estado.PERDIDO) {
                System.out.println("¡Lo siento! Ya no puedes hacer más movimientos.");
                break;
            }
            
        }// Ciclo de juego
        
        sc.close();
        System.exit(0);

    }

}
