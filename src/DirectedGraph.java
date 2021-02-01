import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Directed graph.
 * This class is used for the creation of a directed graph using the map data structure / collection
 */
// Cameron Hutchings
 class DirectedGraph implements Serializable {
    /**
     * The Node list.
     * The list of all the Neurons in the network
     */
    Map<Integer, Neuron> nodeList;
    /**
     * The Number nodes.
     */
    int numberNodes =0;

    /**
     * Instantiates a new Directed graph.
     */
    public DirectedGraph() {
        this.numberNodes = 0;
        this.nodeList = new HashMap<>();
    }

    /**
     * Gets node list.
     *
     * @return the node list
     */
    public Map<Integer, Neuron> getNodeList() {
        return nodeList;
    }

    /**
     * Sets node list.
     *
     * @param nodeList the node list
     */
    public void setNodeList(Map<Integer, Neuron> nodeList) {
        this.nodeList = nodeList;
    }

    /**
     * Gets number nodes.
     *
     * @return the number nodes
     */
    public int getNumberNodes() {
        return numberNodes;
    }

    /**
     * Sets number nodes.
     *
     * @param numberNodes the number nodes
     */
    public void setNumberNodes(int numberNodes) {
        this.numberNodes = numberNodes;
    }

    /**
     * Get node neuron.
     *
     * @param n the n
     * @return the neuron
     */
    public Neuron getNode(int n){
        if (nodeList.containsKey(n))
            return this.nodeList.get(n);
        return null;
    }

    /**
     * Add node neuron.
     *
     * @param key     the key
     * @param feature the feature
     * @return the neuron
     */
    public Neuron addNode(int key, Float feature){
        Neuron newNode;
        numberNodes++;
        if (feature == null){
            newNode = new Neuron(key);
        }
        else {
            newNode = new Neuron(key);
            newNode.input_feature = feature;
        }
        this.nodeList.put(key,newNode);
        return newNode;
    }

    /**
     * Add edge.
     *adds a edge between input to destination neuron
     * @param neuron the neuron
     * @param j      the j
     * @param d      the d
     */
    public void addEdge(Neuron neuron, Neuron j, double d) {
        this.nodeList.get(neuron.id).addNeighbour(this.nodeList.get(j.id),(float)d);
    }
}
