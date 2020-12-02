package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }
        
        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }
 
         /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null) throw new IllegalArgumentException();
        Vertice buscado = buscaVertice(elemento);
        
        if(buscado != null) throw new IllegalArgumentException();
        vertices.agrega(new Vertice(elemento)); 
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        //no necesito buscarlo para saber si me pide conectar dos vertices iguales.
        if(a.equals(b)) throw new IllegalArgumentException("No puedes crear loops.");
        Vertice aa = buscaVertice(a);
        Vertice bb = buscaVertice(b);
        if(aa == null || bb == null) throw new NoSuchElementException("No es posible conectar dos elementos que no son elementos de la gráfica.");
        if(sonVecinos(a, b)) throw new IllegalArgumentException("No puedes crear multiaristas.");
        
        aristas++;
        aa.vecinos.agrega(bb);
        bb.vecinos.agrega(aa);
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice aa = buscaVertice(a);
        Vertice bb = buscaVertice(b);
        
        if(aa == null || bb == null) throw new NoSuchElementException("No es posible desconectar dos vertices que no son elementos de la gráfica.");
        if(!sonVecinos(a, b)) throw new IllegalArgumentException("No puedes desconectar dos vertices que no están conectados.");

        aristas--;
        aa.vecinos.elimina(bb);
        bb.vecinos.elimina(aa);
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return buscaVertice(elemento) != null;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice buscado = buscaVertice(elemento);

        if(buscado == null) throw new NoSuchElementException();
        vertices.elimina(buscado);

        for(Vertice v: buscado.vecinos){
            v.vecinos.
            elimina(buscado);
            aristas--;
        }
        
    }   

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice aa = buscaVertice(a);
        Vertice bb = buscaVertice(b);
        
        if(aa == null || bb == null) throw new NoSuchElementException("No es posible conectar dos elementos que no son elementos de la gráfica.");
        Iterator<Vertice> ita = aa.vecinos.iterator();
        Iterator<Vertice> itb = bb.vecinos.iterator();

        return aa.vecinos.contiene(bb) && bb.vecinos.contiene(aa);
    }


    private Vertice buscaVertice(T elemento){
        Iterator<Vertice> it = vertices.iterator();
        for(Vertice v: vertices)
            if(v.elemento == elemento) 
                return v;
        return null;
        
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice aux, buscada;
        buscada = null;
        for(Vertice v:vertices)
            if(v.elemento.equals(elemento))
                buscada = v;

        if(buscada == null) throw new NoSuchElementException("El elemento buscado no está en la gráfica.");

        return (VerticeGrafica)buscada;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if(vertice.getClass() != Vertice.class) throw new IllegalArgumentException("No es posible cambiar el color a un objeto que no es de tipo Vertice.");
        ((Vertice)vertice).color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        //vacuidad
        if(vertices.esVacia()) return false;

        boolean esConexa = true;
        pintaVertices(Color.ROJO);

        recorrido(vertices.get(0), v -> {}, (MeteSaca)new Pila<Vertice>());

        for(Vertice v: vertices)
            if(v.color == Color.ROJO) esConexa = false;

        return esConexa;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice v: vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice buscado = buscaVertice(elemento);
        
        if(buscado == null) throw new NoSuchElementException();

        recorrido(buscado, accion, (MeteSaca)new Cola<Vertice>());
        pintaVertices(Color.NINGUNO);
    }

    private void recorrido(Vertice v, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> implementacion){
        if(accion == null || v == null || implementacion == null) return;

        pintaVertices(Color.ROJO);
        Vertice aux = null;
        
        v.color = Color.NEGRO;
        implementacion.mete(v);
        
        while(!implementacion.esVacia()){
            aux = implementacion.saca();
            accion.actua((VerticeGrafica)aux);
            for(Vertice aRecorrer: aux.vecinos)
                if(aRecorrer.color == Color.ROJO){
                    aRecorrer.color = Color.NEGRO;
                    implementacion.mete(aRecorrer);
                }
        }
        
    }


    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice buscado = buscaVertice(elemento);
        
        if(buscado == null) throw new NoSuchElementException();
        
        recorrido(buscado, accion, (MeteSaca)new Pila<Vertice>());
        pintaVertices(Color.NINGUNO);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices = new Lista<>();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String toString = "{";
        for(Vertice v: vertices)
            toString += v.elemento+", ";
        toString += "}, ";
        toString += "{";
        pintaVertices(Color.ROJO);
        for(Vertice v: vertices){
            v.color = Color.NEGRO;
            for(Vertice w: v.vecinos){
                if(w.color == Color.ROJO){
                toString += "("+v.elemento.toString()+", "+w.elemento.toString()+"), ";
                }
            }
        }
        toString += "}";
        return toString;
    }

    private void pintaVertices(Color color){
        for(Vertice v: vertices)
            v.color = color;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;

        System.out.println("pasado 0");
        if(aristas != grafica.aristas || vertices.getElementos() != grafica.vertices.getElementos()){
            System.out.println("caso1");
            return false;
        }

        for(Vertice v: vertices)
            if(! grafica.contiene(v.elemento)){
                System.out.println("caso2");
                return false;
            }
        
        System.out.println("pasado1");

        for(Vertice v: vertices)
            for(Vertice w: grafica.vertices)
                if(sonVecinos(v.elemento, w.elemento) ^ grafica.sonVecinos(v.elemento, w.elemento)) return false;
        return true;

    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
