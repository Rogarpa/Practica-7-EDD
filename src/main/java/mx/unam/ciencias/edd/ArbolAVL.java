package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            int alturaIzquierdo = -1;
            int alturaDerecho = -1;
            if(izquierdo != null) alturaIzquierdo = izquierdo.altura();
            if(derecho != null) alturaDerecho = derecho.altura();

            return super.toString() + " " + altura + "/" + (alturaIzquierdo - alturaDerecho);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            
            return (altura == vertice.altura && super.equals(vertice));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalanceo((VerticeAVL)(ultimoAgregado.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        //puerco con casting
        Vertice encontrado = (Vertice)(busca(elemento));
        if(encontrado == null) return;

        elementos--;
        if(encontrado.izquierdo != null && encontrado.derecho != null)
            encontrado = intercambiaEliminable(encontrado);
        
        eliminaVertice(encontrado);
        
        rebalanceo((VerticeAVL)(encontrado.padre));
    }

    private void rebalanceo(VerticeAVL v){
        if(v == null) return;

        v.altura = alturaVertice(v);
        
        int balance = balanceVertice(v);
        VerticeAVL p,q,x,y,aux;
        int H = v.altura;

        p = (VerticeAVL)v.izquierdo;
        q = (VerticeAVL)v.derecho;


        
        if(balance == -2){
            x = (VerticeAVL)q.izquierdo;
            y = (VerticeAVL)q.derecho;

            //caso cuando p es vacio (-1)
            if(balanceVertice(q) == 1) {
                super.giraDerecha(q);
                q.altura--;
                if(x != null)x.altura++;
                
                q = (VerticeAVL)v.derecho;
                x = (VerticeAVL)q.izquierdo;
                y = (VerticeAVL)q.derecho; 
            }
            super.giraIzquierda(v);
            
            if(x != null && x.altura == H - 2) v.altura = H - 1;
            else v.altura = H - 2;

            if(v.altura == H - 1) q.altura = H;
            else q.altura = H - 1;

        }

        if(balance == 2){
            x = (VerticeAVL)p.izquierdo;
            y = (VerticeAVL)p.derecho;
            //caso cuando p es vacio (-1)
            if(balanceVertice(p) == -1) {
                super.giraIzquierda(p);
                p.altura--;
                if(y != null) y.altura++;
                
                p = (VerticeAVL)v.izquierdo;
                x = (VerticeAVL)p.izquierdo;
                y = (VerticeAVL)p.derecho; 
            }
            super.giraDerecha(v);
            
            if(y != null && y.altura == H - 2) v.altura = H - 1;
            else v.altura = H - 2;

            if(v.altura == H - 1) p.altura = H;
            else p.altura = H - 1;

        }

        rebalanceo((VerticeAVL)v.padre);
    }


    private int alturaVertice(VerticeAVL v){
        int alturaHijoIzquierdo, alturaHijoDerecho;
        alturaHijoIzquierdo = -1;
        alturaHijoDerecho = -1;
        if(v.izquierdo != null) alturaHijoIzquierdo = v.izquierdo.altura();
        if(v.derecho != null) alturaHijoDerecho = v.derecho.altura();
        
        return 1 + Math.max(alturaHijoIzquierdo, alturaHijoDerecho);
    }

    private int balanceVertice(VerticeAVL v){
        int alturaIzquierdo = -1;
        int alturaDerecho = -1;

        if(v.izquierdo != null) alturaIzquierdo = v.izquierdo().altura();
        if(v.derecho != null) alturaDerecho = v.derecho().altura();
        return alturaIzquierdo - alturaDerecho;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
