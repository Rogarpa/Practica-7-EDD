package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
* Clase para ordenar y buscar arreglos genéricos.
*/
public class Arreglos {
    
    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}
    
    /**
    * Ordena el arreglo recibido usando QickSort.
    * @param <T> tipo del que puede ser el arreglo.
    * @param arreglo el arreglo a ordenar.
    * @param comparador el comparador para ordenar el arreglo.
    */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        
        if(arreglo == null || comparador == null ){
            return;
        }
        
        quickSort(arreglo, comparador, 0, (arreglo.length)-1);
    }
    
    private static <T> void quickSort(T[] arreglo, Comparator<T> comparador, int a, int b){
        T auxi;
        
        if(b <= a){
            return; 
        }
        
        int i = a+1;
        int j = b;
        
        while(i<j){
            if((comparador.compare(arreglo[i], arreglo[a]) > 0) && (comparador.compare(arreglo[j],arreglo[a]) <= 0)){
                
                intercambia(arreglo, i, j);
                i++;
                j--;
            }else if(comparador.compare(arreglo[i], arreglo[a]) <= 0){
                
                i++;
            }else{
                
                j--;
            }
            
        }
        
        if(comparador.compare(arreglo[i], arreglo[a]) > 0) {
            i--;
        }
        
        intercambia(arreglo, a, i);
        quickSort(arreglo, comparador, a, i-1);
        quickSort(arreglo, comparador, i+1, b);
        
    }
    
    /**
    * Ordena el arreglo recibido usando QickSort.
    * @param <T> tipo del que puede ser el arreglo.
    * @param arreglo un arreglo cuyos elementos son comparables.
    */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }
    
    private static <T> void intercambia(T[] arreglo, int i, int j){
        T aux;
        
        aux = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = aux;
    }
    
    /**
    * Ordena el arreglo recibido usando SelectionSort.
    * @param <T> tipo del que puede ser el arreglo.
    * @param arreglo el arreglo a ordenar.
    * @param comparador el comparador para ordernar el arreglo.
    */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        int length = arreglo.length;
        int m;
        
        for(int i = 0; i < length; i++){
            m = i; 
            
            for(int j = i+1; j < length; j++){
                if((comparador.compare(arreglo[j],arreglo[m])) < 0){
                    
                    m=j;
                }
            }
            
            T iesimo = arreglo[i];
            arreglo[i] = arreglo[m];
            arreglo[m] = iesimo;
            
        }
    }
    
    /**
    * Ordena el arreglo recibido usando SelectionSort.
    * @param <T> tipo del que puede ser el arreglo.
    * @param arreglo un arreglo cuyos elementos son comparables.
    */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }
    
    /**
    * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
    * del elemento en el arreglo, o -1 si no se encuentra.
    * @param <T> tipo del que puede ser el arreglo.
    * @param arreglo el arreglo dónde buscar.
    * @param elemento el elemento a buscar.
    * @param comparador el comparador para hacer la búsqueda.
    * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
    */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        return busquedaBinaria(arreglo, elemento, comparador, 0, arreglo.length-1);
    }
    
    private static <T> int busquedaBinaria(T[] arreglo, T elemento,  Comparator<T> comparador, int a, int b){
        if(b<a) {
            
            return (-1);
        }
        
        if(elemento == null){
            
            return (-1);
        }
        
        int m = (int)((a+b)/2);
        
        if(comparador.compare(arreglo[m], elemento ) == 0){
            
            return m;
        }
        
        if(comparador.compare(arreglo[m], elemento ) < 0){
            
            return busquedaBinaria(arreglo, elemento, comparador, m+1, b);
        }
        
        else{
            return busquedaBinaria(arreglo, elemento, comparador, a, m-1);
        }
        
    }
    
    /**
    * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
    * del elemento en el arreglo, o -1 si no se encuentra.
    * @param <T> tipo del que puede ser el arreglo.
    * @param arreglo un arreglo cuyos elementos son comparables.
    * @param elemento el elemento a buscar.
    * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
    */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
