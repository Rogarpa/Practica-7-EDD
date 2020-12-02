package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* <p>Clase genérica para listas doblemente ligadas.</p>
*
* <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
* eliminar elementos de la lista, comprobar si un elemento está o no en la
* lista, y otras operaciones básicas.</p>
*
* <p>Las listas no aceptan a <code>null</code> como elemento.</p>
*
* @param <T> El tipo de los elementos de la lista.
*/
public class Lista<T> implements Coleccion<T> {
    
    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;
        
        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }
    
    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;
        
        /* Construye un nuevo iterador. */
        public Iterador() {
            siguiente = cabeza;
        }
        
        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(siguiente == null);
        }
        
        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(siguiente == null){
                
                throw new NoSuchElementException();
            }
            
            anterior = siguiente;
            siguiente = anterior.siguiente;
            
            return anterior.elemento;
        }
        
        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return !(anterior == null);
        }
        
        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(anterior == null) {
                throw new NoSuchElementException();
            }
            
            siguiente = anterior;
            anterior = siguiente.anterior;
            
            return siguiente.elemento;
        }
        
        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }
        
        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            siguiente = null;
            anterior = rabo;
        }
    }
    
    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;
    
    
    private void afaltadeconstructor (Nodo cabeza, Nodo rabo, int longitud){
        this.cabeza = cabeza;
        this.rabo = rabo;
        this.longitud = longitud;
        
        
    }
    
    /**
    * Regresa la longitud de la lista. El método es idéntico a {@link
        * #getElementos}.
        * @return la longitud de la lista, el número de elementos que contiene.
        */
        public int getLongitud() {
            return longitud;
        }
        
        /**
        * Regresa el número elementos en la lista. El método es idéntico a {@link
            * #getLongitud}.
            * @return el número elementos en la lista.
            */
            @Override public int getElementos() {
                return longitud;
            }
            
            /**
            * Nos dice si la lista es vacía.
            * @return <code>true</code> si la lista es vacía, <code>false</code> en
            *         otro caso.
            */
            @Override public boolean esVacia() {
                return cabeza == null;
            }
            
            /**
            * Agrega un elemento a la lista. Si la lista no tiene elementos, el
            * elemento a agregar será el primero y último. El método es idéntico a
            * {@link #agregaFinal}.
            * @param elemento el elemento a agregar.
            * @throws IllegalArgumentException si <code>elemento</code> es
            *         <code>null</code>.
            */
            @Override public void agrega(T elemento) {
                agregaFinal(elemento);      
            }
            
            /**
            * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
            * el elemento a agregar será el primero y último.
            * @param elemento el elemento a agregar.
            * @throws IllegalArgumentException si <code>elemento</code> es
            *         <code>null</code>.
            */
            public void agregaFinal(T elemento) {
                if(elemento == null){
                    throw new IllegalArgumentException();
                }
                
                Nodo nodoaagregar = new Nodo(elemento);
                longitud++;
                
                if (rabo == null){
                    cabeza = rabo = nodoaagregar;
                }else{
                    nodoaagregar.anterior = rabo;
                    rabo.siguiente = nodoaagregar;
                    rabo = nodoaagregar;
                }
                
            }
            
            /**
            * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
            * el elemento a agregar será el primero y último.
            * @param elemento el elemento a agregar.
            * @throws IllegalArgumentException si <code>elemento</code> es
            *         <code>null</code>.
            */
            public void agregaInicio(T elemento) {
                
                if(elemento == null){
                    throw new IllegalArgumentException();
                }
                
                longitud++;
                Nodo nodoaagregar = new Nodo(elemento);
                
                if(esVacia()){
                    cabeza = rabo = nodoaagregar;
                }else{
                    cabeza.anterior = nodoaagregar;
                    nodoaagregar.siguiente = cabeza;
                    cabeza = nodoaagregar; 
                }
            }
            
            /**
            * Inserta un elemento en un índice explícito.
            *
            * Si el índice es menor o igual que cero, el elemento se agrega al inicio
            * de la lista. Si el índice es mayor o igual que el número de elementos en
            * la lista, el elemento se agrega al fina de la misma. En otro caso,
            * después de mandar llamar el método, el elemento tendrá el índice que se
            * especifica en la lista.
            * @param i el índice dónde insertar el elemento. Si es menor que 0 el
            *          elemento se agrega al inicio de la lista, y si es mayor o igual
            *          que el número de elementos en la lista se agrega al final.
            * @param elemento el elemento a insertar.
            * @throws IllegalArgumentException si <code>elemento</code> es
            *         <code>null</code>.
            */
            public void inserta(int i, T elemento) {
                
                if(elemento == null){
                    throw new IllegalArgumentException();
                }
                
                if(i<=0){
                    agregaInicio(elemento);
                }else if(i>=longitud){
                    agregaFinal(elemento);
                }else{
                    
                    longitud++;
                    Nodo n = cabeza;
                    Nodo nodoainsertar = new Nodo(elemento);
                    
                    while(i>0){
                        n = n.siguiente;   
                        i--;
                    }
                    
                    n.anterior.siguiente = nodoainsertar;
                    nodoainsertar.anterior = n.anterior;
                    nodoainsertar.siguiente = n;
                    n.anterior = nodoainsertar;                
                    
                }
                
            }
            
            /**
            * Elimina un elemento de la lista. Si el elemento no está contenido en la
            * lista, el método no la modifica.
            * @param elemento el elemento a eliminar.
            */
            @Override public void elimina(T elemento) {
                
                Nodo n = cabeza;
                
                while(n != null){
                    if(n.elemento.equals(elemento)){
                        break;
                    }
                    n = n.siguiente;
                }
                
                if(n == null){
                    return;
                }else{
                    longitud -= 1;
                    if(n.equals(cabeza) && n.equals(rabo)){
                        cabeza = rabo = null;
                    }else if(n.equals(cabeza)){
                        cabeza = n.siguiente;
                        cabeza.anterior = null;
                    }else if(n.equals(rabo)){
                        rabo = n.anterior;
                        rabo.siguiente = null;
                    }else{
                        n.anterior.siguiente = n.siguiente;
                        n.siguiente.anterior = n.anterior;
                    }
                }
            }
            
            /**
            * Elimina el primer elemento de la lista y lo regresa.
            * @return el primer elemento de la lista antes de eliminarlo.
            * @throws NoSuchElementException si la lista es vacía.
            */
            public T eliminaPrimero() {
                
                if(cabeza == null){
                    throw new NoSuchElementException();
                }else{
                    longitud--;
                    T primerelemento;
                    primerelemento = cabeza.elemento;
                    cabeza = cabeza.siguiente;
                    
                    if(cabeza == null){
                        rabo = null;
                    }else{
                        cabeza.anterior = null;
                    }
                    
                    return primerelemento;
                }
            }
            
            /**
            * Elimina el último elemento de la lista y lo regresa.
            * @return el último elemento de la lista antes de eliminarlo.
            * @throws NoSuchElementException si la lista es vacía.
            */
            public T eliminaUltimo() {
                if(rabo == null){
                    throw new NoSuchElementException();
                }else{
                    
                    longitud --;
                    T ultimoelemento;
                    
                    ultimoelemento = rabo.elemento;
                    rabo = rabo.anterior;
                    
                    if(rabo == null){
                        cabeza = null;
                    }else{
                        rabo.siguiente = null;
                    }
                    
                    return ultimoelemento;
                }
                
            }
            
            /**
            * Nos dice si un elemento está en la lista.
            * @param elemento el elemento que queremos saber si está en la lista.
            * @return <code>true</code> si <code>elemento</code> está en la lista,
            *         <code>false</code> en otro caso.
            */
            @Override public boolean contiene(T elemento) {
                Nodo n = cabeza;
                
                while(n != null){
                    
                    if(n.elemento.equals(elemento) ){
                        return true;
                    }
                    
                    n = n.siguiente;
                }
                
                
                return false;
                
            }
            
            /**
            * Regresa la reversa de la lista.
            * @return una nueva lista que es la reversa la que manda llamar el método.
            */
            public Lista<T> reversa() {
                Lista<T> reversa = new Lista<T>();
                Nodo n = rabo;
                
                while(n != null){
                    reversa.agregaFinal(n.elemento);
                    n = n.anterior;                    
                }
                
                reversa.longitud = longitud;
                return reversa;
                
            }
            
            /**
            * Regresa una copia de la lista. La copia tiene los mismos elementos que la
            * lista que manda llamar el método, en el mismo orden.
            * @return una copiad de la lista.
            */
            public Lista<T> copia() {
                Lista<T> copia = new Lista<T>();
                Nodo n = cabeza;
                
                while(n != null){
                    copia.agregaFinal(n.elemento);
                    n = n.siguiente;
                }
                
                copia.longitud = longitud;
                return copia;
            }
            
            /**
            * Limpia la lista de elementos, dejándola vacía.
            */
            @Override public void limpia() {
                cabeza = null; 
                rabo = null;
                longitud = 0;
            }
            
            /**
            * Regresa el primer elemento de la lista.
            * @return el primer elemento de la lista.
            * @throws NoSuchElementException si la lista es vacía.
            */
            public T getPrimero() {
                if(cabeza == null){
                    throw new NoSuchElementException();
                }
                return cabeza.elemento;
                
            }
            
            /**
            * Regresa el último elemento de la lista.
            * @return el primer elemento de la lista.
            * @throws NoSuchElementException si la lista es vacía.
            */
            public T getUltimo() {
                if(rabo == null){
                    throw new NoSuchElementException();
                }
                return rabo.elemento;
            }
            
            /**
            * Regresa el <em>i</em>-ésimo elemento de la lista.
            * @param i el índice del elemento que queremos.
            * @return el <em>i</em>-ésimo elemento de la lista.
            * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
            *         igual que el número de elementos en la lista.
            */
            public T get(int i) {
                if(i<0 || i>=longitud){
                    throw new ExcepcionIndiceInvalido();
                }
                
                Nodo n = cabeza;
                
                while(i>0){
                    n = n.siguiente;
                    i--;
                }
                return n.elemento;
            }
            
            /**
            * Regresa el índice del elemento recibido en la lista.
            * @param elemento el elemento del que se busca el índice.
            * @return el índice del elemento recibido en la lista, o -1 si el elemento
            *         no está contenido en la lista.
            */
            public int indiceDe(T elemento) {
                int indice = 0;
                Nodo n = cabeza;
                
                while(n != null){
                    if(n.elemento.equals(elemento)){
                        return indice;
                    }
                    n = n.siguiente;
                    indice++;
                }
                
                return (-1);
            }
            
            /**
            * Regresa una representación en cadena de la lista.
            * @return una representación en cadena de la lista.
            */
            @Override public String toString() {
                
                
                if(cabeza == null){
                    return "[]";
                }else{
                    
                    Nodo n;
                    String listtoString;
                    
                    listtoString = "[";
                    listtoString += cabeza.elemento.toString();
                    n = cabeza.siguiente;
                    
                    while(n != null){
                        listtoString += ", " + n.elemento.toString();
                        n = n.siguiente;
                    }
                    
                    listtoString += "]";
                    return listtoString;
                    
                }
            }
            
            /**
            * Nos dice si la lista es igual al objeto recibido.
            * @param objeto el objeto con el que hay que comparar.
            * @return <code>true</code> si la lista es igual al objeto recibido;
            *         <code>false</code> en otro caso.
            */
            @Override public boolean equals(Object objeto) {
                if (objeto == null || getClass() != objeto.getClass()){
                    return false;
                }
                
                @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
                
                if(lista.longitud != longitud){
                    return false;
                }
                
                Iterator<T> it;
                it = lista.iterator();
                Nodo n = cabeza;
                
                while(it.hasNext()){
                    if(!(it.next().equals(n.elemento))){
                        return false;
                    }
                    n = n.siguiente;
                }
                
                return true;
                
            }
            
            /**
            * Regresa un iterador para recorrer la lista en una dirección.
            * @return un iterador para recorrer la lista en una dirección.
            */
            @Override public Iterator<T> iterator() {
                return new Iterador();
            }
            
            /**
            * Regresa un iterador para recorrer la lista en ambas direcciones.
            * @return un iterador para recorrer la lista en ambas direcciones.
            */
            public IteradorLista<T> iteradorLista() {
                return new Iterador();
            }
            
            
            
            
            /**
            * Regresa una copia de la lista, pero ordenada. Para poder hacer el
            * ordenamiento, el método necesita una instancia de {@link Comparator} para
            * poder comparar los elementos de la lista.
            * @param comparador el comparador que la lista usará para hacer el
            *                   ordenamiento.
            * @return una copia de la lista, pero ordenada.
            */
            public Lista<T> mergeSort(Comparator<T> comparador) {
                return mergeSort(comparador, copia());
            }
            
            //falta arreglar listas null
            //arreglar complejidad con coṕia
            private Lista<T> mergeSort(Comparator<T> comparador, Lista<T> lista){
                Lista<T> l1 = new Lista<T>();
                Lista<T> l2 = new Lista<T>();
                int k;
                
                int lon = lista.getLongitud();
                
                if(lon < 2){
                    return lista;
                }
                
                if((lon % 2) == 1){
                    k = ((lon-1)/2);
                }else{
                    k = (lon/2);
                }
                
                
                
                Nodo n = lista.cabeza, prev;
                int i = 0; 
                
                while((i++ < k)){
                    n = n.siguiente;
                }
                
                prev = n.anterior;
                prev.siguiente = null;
                
                l1.afaltadeconstructor(lista.cabeza, prev, k);
                
                n.anterior = null;
                
                l2.afaltadeconstructor(n, lista.rabo, lon - k);
                
                return mezcla(comparador,mergeSort(comparador, l1),mergeSort(comparador, l2));
                
                
            }
            
            private Lista<T> mezcla(Comparator<T> comparador, Lista<T> l1, Lista<T> l2){
                
                Lista<T> l = new Lista<T>();
                
                Nodo n1,n2;
                
                n1 = l1.cabeza;
                n2 = l2.cabeza;
                
                
                while((n1 != null) && (n2 != null)){
                    
                    
                    
                    if(comparador.compare(n1.elemento,n2.elemento) <= 0) {
                        l.agregaFinal(n1.elemento);
                        n1 = n1.siguiente;
                    }
                    else{
                        
                        l.agregaFinal(n2.elemento);
                        n2 = n2.siguiente;
                    }
                    
                }
                
                while(n1 != null) {
                    l.agrega(n1.elemento);
                    n1 = n1.siguiente;
                }
                
                while(n2 != null) {
                    l.agrega(n2.elemento);
                    n2 = n2.siguiente;
                    
                }
                
                return l;
                
            }
            
            /**
            * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
            * tiene que contener nada más elementos que implementan la interfaz {@link
                * Comparable}.
                * @param <T> tipo del que puede ser la lista.
                * @param lista la lista que se ordenará.
                * @return una copia de la lista recibida, pero ordenada.
                */
                public static <T extends Comparable<T>>
                Lista<T> mergeSort(Lista<T> lista) {
                    return lista.mergeSort((a, b) -> a.compareTo(b));
                }
                
                /**
                * Busca un elemento en la lista ordenada, usando el comparador recibido. El
                * método supone que la lista está ordenada usando el mismo comparador.
                * @param elemento el elemento a buscar.
                * @param comparador el comparador con el que la lista está ordenada.
                * @return <code>true</code> si el elemento está contenido en la lista,
                *         <code>false</code> en otro caso.
                */
                public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
                    if(cabeza == null || elemento == null || comparador == null){
                        return false;
                    }
                    
                    if(((comparador.compare(elemento, cabeza.elemento)) < 0) || ((comparador.compare(rabo.elemento, elemento)) < 0  )){
                        return false;
                    }
                    
                    Nodo it = cabeza;
                    while(it != null){
                        if(comparador.compare(it.elemento, elemento) == 0){
                            return true;
                        }
                        it = it.siguiente;
                    }
                    
                    return false;
                    
                }
                
                /**
                * Busca un elemento en una lista ordenada. La lista recibida tiene que
                * contener nada más elementos que implementan la interfaz {@link
                    * Comparable}, y se da por hecho que está ordenada.
                    * @param <T> tipo del que puede ser la lista.
                    * @param lista la lista donde se buscará.
                    * @param elemento el elemento a buscar.
                    * @return <code>true</code> si el elemento está contenido en la lista,
                    *         <code>false</code> en otro caso.
                    */
                    public static <T extends Comparable<T>>
                    boolean busquedaLineal(Lista<T> lista, T elemento) {
                        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
                    }
                }
                