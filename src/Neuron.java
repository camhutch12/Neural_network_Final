import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Neuron.
 * This is the class representing a neuron in the neural network
 */
// Cameron Hutchings
 class Neuron implements Serializable {
    /**
     * The Ei. This is the Gradient of the error WRT to hidden layers
     */
    public float ei;
    /**
     * The Delta weight updates.
     * Delta weight wj
     */
    public List<Float> delta_weight_updates = new ArrayList<>();
    /**
     * The Id.
     * This is the name of each neuron used for keeping track of there location in the neuron network
     */
    int id;
    /**
     * The Delta.
     * This is a list of all the Delta for each wieght connected to the neuron
     */
    List<Float> delta;
    /**
     * The Previous deltas.
     * This is a list of all the Previous Deltas for each wieght connected to the neuron
     */
    List<Float> previous_deltas;
    /**
     * The Connected to.
     * This is used for check which neuron is connected to each other
     */
    Map<Integer, Float> connectedTo;
    /**
     * The Input feature.
     * Input value for the neuron at the input layer
     */
    float input_feature;
    /**
     * The Output expected.
     * the output that the neuron expect at the output layer
     */
//float delta;
    float output_expected;
    /**
     * The Output.
     * The correspoond value after the neuron input was fed though the activation function*
     */
    float output;
    /**
     * The Output feature.
     * Output expected at the output layer neuron
     */
    float output_feature;
    /**
     * The Input.
     * input expected which is the previous nodes output
     */
    float input;
    /**
     * The Weight sum.
     */
    float weight_sum;
    /**
     * The Error.
     */
    float error;
    /**
     * The Error at hidden layer.
     */
    float error_at_hidden_layer;
    /**
     * The Learning rates per weight.
     * used by Rprop to adaptively change the weight values
     */
    List<Float> learning_rates_per_weight;
    /**
     * The Batch deltas.
     * used for storing each delta weight for the neuron
     * used during the batch algorithims
     */
    List<List<Float>> batch_deltas;
    /**
     * The Error list.
     */
    List<Float> error_list = new ArrayList<>();
    /**
     * The Current update values.
     */
    List<Float> currentUpdateValues;;

    /**
     * Instantiates a new Neuron.
     *
     * @param id the id
     */
    public Neuron(int id) {
        this.id = id;
        this.connectedTo = new HashMap<>();
        this.delta= new ArrayList<>();
        this.batch_deltas = new ArrayList<>();
        this.learning_rates_per_weight = new ArrayList<>();
        this.previous_deltas = new ArrayList<>();

    }

    /**
     * Add neighbour.
     *
     * @param nbr    the nbr
     * @param weight the weight
     */
    public void addNeighbour(Neuron nbr, float weight){
        this.connectedTo.put(nbr.id,weight);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets connected to.
     *
     * @return the connected to
     */
    public Map<Integer, Float> getConnectedTo() {
        return connectedTo;
    }

    /**
     * Sets connected to.
     *
     * @param connectedTo the connected to
     */
    public void setConnectedTo(Map<Integer, Float> connectedTo) {
        this.connectedTo = connectedTo;
    }

    /**
     * Gets intput feature.
     *
     * @return the intput feature
     */
    public float getInput_feature() {
        return input_feature;
    }

    /**
     * Sets intput feature.
     *
     * @param input_feature the intput feature
     */
    public void setInput_feature(float input_feature) {
        this.input_feature = input_feature;
    }

    /**
     * Gets output expected.
     *
     * @return the output expected
     */
    public float getOutput_expected() {
        return output_expected;
    }

    /**
     * Sets output expected.
     *
     * @param output_expected the output expected
     */
    public void setOutput_expected(float output_expected) {
        this.output_expected = output_expected;
    }

    /**
     * Gets output.
     *
     * @return the output
     */
    public float getOutput() {
        return output;
    }

    /**
     * Sets output.
     *
     * @param output the output
     */
    public void setOutput(float output) {
        this.output = output;
    }

    /**
     * Gets output feature.
     *
     * @return the output feature
     */
    public float getOutput_feature() {
        return output_feature;
    }

    /**
     * Sets output feature.
     *
     * @param output_feature the output feature
     */
    public void setOutput_feature(float output_feature) {
        this.output_feature = output_feature;
    }

    /**
     * Gets input.
     *
     * @return the input
     */
    public float getInput() {
        return input;
    }

    /**
     * Sets input.
     *
     * @param input the input
     */
    public void setInput(float input) {
        this.input = input;
    }

    /**
     * Gets weight sum.
     *
     * @return the weight sum
     */
    public float getWeight_sum() {
        return weight_sum;
    }

    /**
     * Sets weight sum.
     *
     * @param weight_sum the weight sum
     */
    public void setWeight_sum(float weight_sum) {
        this.weight_sum = weight_sum;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public float getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(float error) {
        this.error = error;
    }

    /**
     * Contains boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean contains(Integer item){
        if (this.connectedTo.containsKey(item))
            return true;

        return false;
    }
}
