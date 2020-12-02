package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
* <p>Clase para árboles binarios completos.</p>
*
* <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
* árbol siempre es lo más cercano posible a estar lleno.</p>
*/
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {
    
    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {
        
        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;
        
        /* Inicializa al iterador. */
        public Iterador() {
            cola = new Cola<Vertice>();
            
            if(raiz != null) cola.mete(raiz);
        }
        
        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(cola.esVacia());
        }
        
        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            
            if(cola.esVacia()) throw new NoSuchElementException("No puedes iterar un Arbol Binario Completo sin elementos siguientes");
            if(cola.mira().izquierdo != null) cola.mete(cola.mira().izquierdo);
            if(cola.mira().derecho != null) cola.mete(cola.mira().derecho);
            
            return cola.saca().elemento;
        }
    }
    
    /**
    * Constructor sin parámetros. Para no perder el constructor sin parámetros
    * de {@link ArbolBinario}.
    */
    public ArbolBinarioCompleto() { super(); }
    
    /**
    * Construye un árbol binario completo a partir de una colección. El árbol
    * binario completo tiene los mismos elementos que la colección recibida.
    * @param coleccion la colección a partir de la cual creamos el árbol
    *        binario completo.
    */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }
    
    /**
    * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
    * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
    * @param elemento el elemento a agregar al árbol.
    * @throws IllegalArgumentException si <code>elemento</code> es
    *         <code>null</code>.
    */
    @Override public void agrega(T elemento) {
        if(elemento == null) throw new IllegalArgumentException();
        
        Vertice h = nuevoVertice(elemento);
        
        /**bfs(v -> {
            if(v.hayIzquierdo() || v.hayDerecho()){  
                //guardar v en algun vertice de arbolbinario para mas adelante agregarle en algún hijo nulo
            }
        });*/
        
        
        elementos++;
        
        if(raiz == null){ 
            raiz = h;
            return;
        }else{
            
            Cola<Vertice> c = new Cola<Vertice>();
            Vertice v; 
            c.mete(raiz);
            while(!(c.esVacia()) ){
                v = c.saca();
                
                if(v.izquierdo == null){
                    v.izquierdo = h;
                    h.padre = v;
                    return;
                }
                if(v.derecho == null){
                    v.derecho = h;
                    h.padre = v;
                    return;    
                }
                
                c.mete(v.izquierdo);
                c.mete(v.derecho);
            }
        }
    }        
    
    /**
    * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
    * el último elemento del árbol al recorrerlo por BFS, y entonces es
    * eliminado.
    * @param elemento el elemento a eliminar.
    */
    @Override public void elimina(T elemento) {
        if(elemento == null) return;
        
        Vertice h = vertice(busca(elemento)), aux = null;
        
        if(h == null) return;
        
        else{
            elementos--;
            if(elementos == 0){ 
                raiz = null;
                return;
            }
            
            int i = 0;
            
            Cola<Vertice> c = new Cola<Vertice>();
            Vertice v; 
            
            c.mete(raiz);
            
            while(!(c.esVacia())){
                v = c.saca();
                i++;
                if(i == elementos + 1) aux = v;
                
                if(v.izquierdo != null)    c.mete(v.izquierdo);
                if(v.derecho != null)    c.mete(v.derecho);
            }
            
            
            h.elemento = aux.elemento;
            if(aux.padre.derecho == null) aux.padre.izquierdo = null;
            else aux.padre.derecho = null;
            
        }
    }
    
    
    /**
    * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
    * cada elemento del árbol.
    * @param accion la acción a realizar en cada elemento del árbol.
    */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(raiz == null || accion == null) return;
        else{
            Cola<Vertice> c = new Cola<Vertice>();
            Vertice v; 
            c.mete(raiz);
            while(!(c.esVacia())){
                v = c.saca();
                accion.actua(v);
                
                if(v.izquierdo != null) c.mete(v.izquierdo);
                if(v.derecho != null) c.mete(v.derecho);
                
            }
            
        }
    }
    /**
    * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
    * @return un iterador para iterar el árbol.
    */
    @Override public Iterator<T> iterator() {
        return new Iterador();
        
    }
}



