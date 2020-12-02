package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            if(color == Color.ROJO) return "R{" + elemento.toString() + "}";  
            else return "N{" + elemento.toString() + "}";  

        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;

                return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() {
        //super();
    }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        if(!(vertice instanceof ArbolRojinegro.VerticeRojinegro)) throw new ClassCastException("Obteniendo color de vertice no rojinegro");
        return ((VerticeRojinegro)vertice).color;
    }

    private boolean esRojo(VerticeRojinegro v){
        return (v != null && v.color == Color.ROJO);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        ((VerticeRojinegro)ultimoAgregado).color = Color.ROJO;
        balanceoAgrega(((VerticeRojinegro)ultimoAgregado));
    }

    private void balanceoAgrega(VerticeRojinegro v){
        //VAN A ENTRAR NULLS?
        VerticeRojinegro p = (VerticeRojinegro)v.padre;
        //CASO1
        if(p == null){
            v.color = Color.NEGRO;
            return;
        } 

        //CASO2
        if(!(esRojo(p))) return;

        //CASO3(quitando problema de tener rojos empalmados y pasándolo abuelo).
        VerticeRojinegro a = (VerticeRojinegro)p.padre;
        VerticeRojinegro t = null;
        
        if(a.izquierdo == p) t = (VerticeRojinegro) a.derecho;
        else t = (VerticeRojinegro)a.izquierdo;

        if(esRojo(t)){
            t.color = Color.NEGRO;
            p.color = Color.NEGRO;
            a.color = Color.ROJO;
            balanceoAgrega(a);
            return;
        }
        //CASO4 (ignoramos t y hay cruzados)
        //MEJORABLE
        VerticeRojinegro aux;
        if(a.izquierdo == p && p.derecho == v){
            super.giraIzquierda(p);
            aux = p;
            p = v;
            v = aux;
        }

        if(a.derecho == p && p.izquierdo == v){
            super.giraDerecha(p);
            aux = p;
            p = v;
            v = aux;
        }

        //CASOFINAL (giramos cruzados)
        p.color = Color.NEGRO;
        a.color = Color.ROJO;
        if(p.izquierdo == v) super.giraDerecha(a);
        if(p.derecho == v) super.giraIzquierda(a);

    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        if(elemento == null) return;

        Vertice encontrado = vertice(busca(elemento));
        if(encontrado == null) return;


        elementos--;
        if(encontrado.izquierdo != null && encontrado.derecho != null)
            encontrado = intercambiaEliminable(encontrado);
        if(encontrado.izquierdo == null && encontrado.derecho == null){
            VerticeRojinegro fantasma = new VerticeRojinegro(null);
            fantasma.color = Color.NEGRO;
            encontrado.izquierdo = fantasma;
            fantasma.padre = encontrado;
        }
        
        VerticeRojinegro hijastro;
        if(encontrado.derecho == null) hijastro = (VerticeRojinegro)encontrado.izquierdo;
        else hijastro = (VerticeRojinegro)encontrado.derecho;
        
        eliminaVertice(encontrado);
        
        //casos aislados
        //1
        if(esRojo(hijastro)) hijastro.color = Color.NEGRO;
        
        //3
        else if(!(esRojo((VerticeRojinegro)encontrado))) balanceoElimina((VerticeRojinegro)hijastro);
        //2 no necesario
        

        if(hijastro.elemento == null){
            if(hijastro.padre == null) raiz = null;
            else if(hijastro.padre.izquierdo == hijastro) hijastro.padre.izquierdo = null;
            else hijastro.padre.derecho = null;
        }

        
    }

    private void balanceoElimina(VerticeRojinegro v){
        if(v == null) return;
        //Caso 1 padre vacio

        if(v.padre == null) return;

        //Caso 2 hermano no vacio puesto tenia hermano negro y giramos para dar tela para cortar a v. 
        VerticeRojinegro h,p = (VerticeRojinegro)v.padre;

        //defino h
        if(p.izquierdo == v) h = (VerticeRojinegro)p.derecho;
        else h = (VerticeRojinegro)p.izquierdo;

        if(esRojo(h)){
            p.color =  Color.ROJO;
            h.color = Color.NEGRO;
            if(p.derecho == v) super.giraDerecha(p);
            else super.giraIzquierda(p);
            
            //reactualizando hermano
            if(p.izquierdo == v) h = (VerticeRojinegro)p.derecho;
            else h = (VerticeRojinegro)p.izquierdo;
        }
         
        //Caso 3 caso amplio en el cual quitamos un negro rama h pero compensamos recursivamente en p.
        //no necesita de anteriores.
        VerticeRojinegro hi,hd;
        hi = (VerticeRojinegro)h.izquierdo;
        hd = (VerticeRojinegro)h.derecho;

        if(!esRojo(p) && !esRojo(h) && !esRojo(hi) && !esRojo(hd)){
            h.color = Color.ROJO;
            balanceoElimina(p);
            return;
        }
        //Caso4 p nos da material para compensar y pintar h de rojo así que cambiamos.
        if(!esRojo(h) && !esRojo(hi) && !esRojo(hd) && esRojo(p)){
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }

        //Caso5 quitamos negro de h con táctica conocida.
        if((p.izquierdo == v && esRojo(hi) && !esRojo(hd)) 
        || (p.derecho == v && !esRojo(hi) && esRojo(hd))){
            h.color = Color.ROJO;
            if(esRojo(hi)) hi.color = Color.NEGRO;
            else hd.color = Color.NEGRO;

            if(p.izquierdo == v) super.giraDerecha(h);
            else super.giraIzquierda(h);
           
            //reactualizando hermano
            if(v.padre.izquierdo == v) h = (VerticeRojinegro)v.padre.derecho;
            else h = (VerticeRojinegro)v.padre.izquierdo;
        }

        //Caso 6 
        //solucionar hi y hd
        h.color = p.color;
        p.color = Color.NEGRO;
        if(v.padre.izquierdo == v) if(h.derecho != null) ((VerticeRojinegro)h.derecho).color = Color.NEGRO;
        if(v.padre.derecho == v) if(h.izquierdo != null) ((VerticeRojinegro)h.izquierdo).color = Color.NEGRO;

        if(v.padre.izquierdo == v) super.giraIzquierda(p);
        else super.giraDerecha(p);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
