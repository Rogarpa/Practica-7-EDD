package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
* acotados a la interfaz {@link Comparable}.</p>
*
* <p>Un árbol instancia de esta clase siempre cumple que:</p>
* <ul>
*   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
*       descendientes por la izquierda.</li>
*   <li>Cualquier elemento en el árbol es menor o igual que todos sus
*       descendientes por la derecha.</li>
* </ul>
*/
public class ArbolBinarioOrdenado<T extends Comparable<T>>
extends ArbolBinario<T> {
    
    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {
        
        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;
        
        /* Inicializa al iterador. */
        public Iterador() {
            pila = new Pila<Vertice>();
            Vertice aux = raiz;
            
            while(aux != null){
                pila.mete(aux);
                aux = aux.izquierdo;
            }
        }
        
        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(pila.esVacia());
        }
        
        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            
            Vertice  aux;
            T ret;
            aux= pila.saca();
            ret = aux.elemento;
            
            if(aux.derecho != null){
aux = aux.derecho;
                while(aux != null){
                pila.mete(aux);
                    aux = aux.izquierdo;
                }
            }
            return ret;
            
        }
    }
    
    /**
    * El vértice del último elemento agegado. Este vértice sólo se puede
    * garantizar que existe <em>inmediatamente</em> después de haber agregado
    * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
    * árbol se ejecuta después de haber agregado un elemento, el estado de esta
    * variable es indefinido.
    */
    protected Vertice ultimoAgregado;
    
    /**
    * Constructor sin parámetros. Para no perder el constructor sin parámetros
    * de {@link ArbolBinario}.
    */
    public ArbolBinarioOrdenado() { super(); }
    
    /**
    * Construye un árbol binario ordenado a partir de una colección. El árbol
    * binario ordenado tiene los mismos elementos que la colección recibida.
    * @param coleccion la colección a partir de la cual creamos el árbol
    *        binario ordenado.
    */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }
    
    /**
    * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
    * @param elemento el elemento a agregar.
    */
    @Override public void agrega(T elemento) {
        if(elemento == null) throw new IllegalArgumentException();
        
        Vertice v = nuevoVertice(elemento);
        ultimoAgregado = v;
        elementos++;
        
        if(raiz == null){ 
            raiz = v;
            return;
        }
        
        agrega(raiz, v);
        
    }
    
    private void agrega(Vertice actual, Vertice agregar){
        
        if(agregar.elemento.compareTo(actual.elemento) <= 0){
            if(actual.izquierdo == null){
                
                actual.izquierdo = agregar;
                agregar.padre = actual;
                return;
            }else agrega(actual.izquierdo, agregar);
        }else{
            if(actual.derecho == null){
                
                actual.derecho = agregar;
                agregar.padre = actual;
                return;
            }else agrega(actual.derecho, agregar);
        }
    }
    
    /**
    * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
    * está varias veces, elimina el primero que encuentre (in-order). El árbol
    * conserva su orden in-order.
    * @param elemento el elemento a eliminar.
    */
    @Override public void elimina(T elemento) {
        
        if(elemento == null) return;
       
        //casting innecesario
        VerticeArbolBinario<T> v = busca(elemento);
        Vertice aux = null;
        if(v != null)aux = vertice(v);

        if(aux == null) return;
        
        elementos--;
        
        if(aux.izquierdo != null && aux.derecho != null){
            eliminaVertice(intercambiaEliminable(aux));
        }else eliminaVertice(aux);
        
    }
    
    private Vertice maximoensubarbol(Vertice subarbol){
        if(subarbol.derecho == null) return subarbol;
        
        return maximoensubarbol(subarbol.derecho);
        
    }
    
    /**
    * Intercambia el elemento de un vértice con dos hijos distintos de
    * <code>null</code> con el elemento de un descendiente que tenga a lo más
    * un hijo.
    * @param vertice un vértice con dos hijos distintos de <code>null</code>.
    * @return el vértice descendiente con el que vértice recibido se
    *         intercambió. El vértice regresado tiene a lo más un hijo distinto
    *         de <code>null</code>.
    */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice max = maximoensubarbol(vertice.izquierdo);

        T aux;
        aux = max.elemento;
        max.elemento = vertice.elemento;
        vertice.elemento = aux;
        return max;

    }
    
    /**
    * Elimina un vértice que a lo más tiene un hijo distinto de
    * <code>null</code> subiendo ese hijo (si existe).
    * @param vertice el vértice a eliminar; debe tener a lo más un hijo
    *                distinto de <code>null</code>.
    */
    protected void eliminaVertice(Vertice vertice) {
        if(vertice == null) return;
        
        Vertice hijo;
        if (vertice.derecho == null) hijo = vertice.izquierdo;
        else hijo = vertice.derecho;
        
        if(vertice.padre == null){
            raiz = hijo;       
            if(hijo != null)    hijo.padre = null;
            return;
        }
        
        if(vertice == vertice.padre.izquierdo) vertice.padre.izquierdo = hijo;
        else vertice.padre.derecho = hijo;
        
        if(hijo != null) hijo.padre = vertice.padre;  
    }
    
    /**
    * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
    * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
    * @param elemento el elemento a buscar.
    * @return un vértice que contiene al elemento buscado si lo
    *         encuentra; <code>null</code> en otro caso.
    */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        if(raiz == null) return null;
        
        return busca(raiz, elemento);
    }
    
    private VerticeArbolBinario<T> busca(Vertice v, T elemento){
        if(v == null) return null;
        
        if(v.elemento.compareTo(elemento) == 0) return v;
        if(elemento.compareTo(v.elemento) < 0) return busca(v.izquierdo, elemento);
        return busca(v.derecho, elemento);
    }    
    /**
    * Regresa el vértice que contiene el último elemento agregado al
    * árbol. Este método sólo se puede garantizar que funcione
    * <em>inmediatamente</em> después de haber invocado al método {@link
        * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
        * ejecuta después de haber agregado un elemento, el comportamiento de este
        * método es indefinido.
        * @return el vértice que contiene el último elemento agregado al árbol, si
        *         el método es invocado inmediatamente después de agregar un
        *         elemento al árbol.
        */
        public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
            return ultimoAgregado;
        }
        
        /**
        * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
        * tiene hijo izquierdo, el método no hace nada.
        * @param vertice el vértice sobre el que vamos a girar.
        */
        public void giraDerecha(VerticeArbolBinario<T> vertice) {
            if(vertice == null) return;

            Vertice k = vertice(vertice);
            
            if(k.izquierdo == null) return;
           
            k.izquierdo.padre = k.padre;
            
            if(k.padre != null){
                if(k.padre.izquierdo != null && k.padre.izquierdo == k) k.padre.izquierdo = k.izquierdo;
                else k.padre.derecho = k.izquierdo;
            }else raiz = k.izquierdo;
            
            k.padre = k.izquierdo;

            k.izquierdo= k.padre.derecho;
            
            if(k.izquierdo != null) k.izquierdo.padre = k;
            
            k.padre.derecho = k;
            
        }
        
        /**
        * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
        * tiene hijo derecho, el método no hace nada.
        * @param vertice el vértice sobre el que vamos a girar.
        */
        public void giraIzquierda(VerticeArbolBinario<T> vertice) {
            if(vertice == null) return;
            Vertice j = vertice(vertice);
            
            if((j.derecho == null)) return;
            
            j.derecho.padre = j.padre;
            if(j.padre != null){
                if(j.padre.izquierdo != null && j.padre.izquierdo.elemento.equals(j.elemento)) j.padre.izquierdo = j.derecho;
                else j.padre.derecho = j.derecho;
            }else raiz = j.derecho;
            
            j.padre = j.derecho;
            j.derecho = j.padre.izquierdo;
            
            if(j.derecho != null) j.derecho.padre = j;
            
            j.padre.izquierdo = j;

            
        }
        
        /**
        * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
        * acción recibida en cada elemento del árbol.
        * @param accion la acción a realizar en cada elemento del árbol.
        */
        public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
            if(raiz == null) return;
            dfsPreOrder(accion, raiz);
        }
        
        private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v){
            if(v == null) return;
            accion.actua(v);
            dfsPreOrder(accion, v.izquierdo);
            dfsPreOrder(accion, v.derecho);
        }
        
        
        /**
        * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
        * acción recibida en cada elemento del árbol.
        * @param accion la acción a realizar en cada elemento del árbol.
        */
        public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
            if(raiz == null) return;
            dfsInOrder(accion, raiz);
            
        }
        private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice v){
            if(v == null) return;
            dfsInOrder(accion, v.izquierdo);
            accion.actua(v);
            dfsInOrder(accion, v.derecho);
        }
        
        /**
        * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
        * acción recibida en cada elemento del árbol.
        * @param accion la acción a realizar en cada elemento del árbol.
        */
        public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
            if(raiz == null) return;
            dfsPostOrder(accion, raiz);
            
        }
        private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v){
            if(v == null) return;
            dfsPostOrder(accion, v.izquierdo);
            dfsPostOrder(accion, v.derecho);
            accion.actua(v);
        }
        
        /**
        * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
        * @return un iterador para iterar el árbol.
        */
        @Override public Iterator<T> iterator() {
            return new Iterador();
        }
    }
    