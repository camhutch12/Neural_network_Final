import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This Class is used for Implement a feed foreward nerual network
 */
// Cameron Hutchings
 class NeuralNetwork implements Serializable {
    /**
     * The Inputs.
     * The Training example placed into array list
     */
    List<Float> inputs;
    /**
     * The Amount of nodes in hidden layer.
     */
    int amount_of_nodes_in_hidden_layer;
    /**
     * The Output.
     */
    List<Float> output;
    /**
     * The Neuron list.
     */
    List<Neuron> neuronList;
    /**
     * The Layers.
     * Each layer of a neural netowork which consists of Neurons
     */
    List<List<Neuron>> layers;
    /**
     * The Correct guess.
     */
    double correct_guess =0;
    /**
     * The Total.
     */
    double  total = 0;
    /**
     * The Total testing error.
     */
    float total_testing_error;
    /**
     * The Digraph.
     */
    DirectedGraph digraph;
    /**
     * The Counter.
     */
    int counter = 0;

    /**
     * Instantiates a new Neural network.
     *
     * @param inputs        the inputs passed from App class
     * @param hidden_layers the hidden layers # number of hidden Neurons in the network
     * @param output        the output expected output from neural network
     */
    public NeuralNetwork(List<Float> inputs, int hidden_layers, List<Float> output) {
        this.inputs = inputs;
        this.amount_of_nodes_in_hidden_layer = hidden_layers;
        layers = new ArrayList<>();
        this.output = output;
        setupGraph();
        if (Contants.NETWORK_TYPES == 3){
        init_rprop_weights();
        }

    }

    /*Sets up the graph arhitecture of the nerual network */
    private void setupGraph(){
        digraph = new DirectedGraph();
        neuronList = new ArrayList<>();


        // sets up the input nodes
        for (float i : this.inputs) {
            counter++;
            digraph.addNode(counter,i);
            neuronList.add(digraph.getNode(counter));

        }
        List<Neuron> lay_lis = new ArrayList<>(neuronList);
        layers.add(new ArrayList<>(lay_lis));
        neuronList.clear();
        lay_lis.clear();

        // sets up the hidden layer nodes
        for (int i = 0; i < amount_of_nodes_in_hidden_layer; i++) {
            counter++;
            digraph.addNode(counter, null);
            neuronList.add(digraph.getNode(counter));
        }

        lay_lis.addAll(neuronList);
        layers.add(new ArrayList<>(lay_lis));
        neuronList.clear();
        lay_lis.clear();

        // setout output edges
        for (float i : this.output) {
            counter++;
            this.digraph.addNode(counter,i);
            neuronList.add(digraph.getNode(counter));

        }

        lay_lis.addAll(neuronList);
        layers.add(new ArrayList<>(lay_lis));
        neuronList.clear();
        lay_lis.clear();
        this.setupEdges();
    }
    /*Sets up the Edges between each neuron */
    private void setupEdges(){
        for (int i = 0; i < layers.size()-1; i++) {
               List<Neuron> current_layer = this.layers.get(i);
            for (Neuron neuron : current_layer) {

                for (int j = 0; j < layers.get(i + 1).size(); j++) {
                    Random random = new Random();
                    double d = random.doubles(-0.5,0.5).findFirst().getAsDouble();

                    Neuron to = layers.get(i+1).get(j);
                    digraph.addEdge(neuron,to,d);
            }
            }

            }

        }

    /**
     * Train float.
     *
     * @param input_features   the input features
     * @param expected_outputs the expected outputs
     * @param sumError         the sum error
     * @return the float
     */
    public float train(List<Float> input_features, List<Float> expected_outputs, float sumError) {

        // give each neuron a new input feature
        for (int i = 0; i < input_features.size(); i++) {
            this.layers.get(0).get(i).input_feature = input_features.get(i);
        }

        for (int i = 0; i < expected_outputs.size(); i++) {
            this.layers.get(this.layers.size()-1).get(i).output_feature = expected_outputs.get(i);

        }

        List<Float> output = this.feed_foreward();

        if (Contants.NETWORK_TYPES == 3){
            this.backProp_batch();
        }
        else {
        this.backProp();

        }

        return 0;
    }

    /*Does the Back prop algorithim for Neural network */
    private void backProp() {

        // calculate error between calculared and expected error in the output nodes
        for (Neuron outputLayer_neuron : this.layers.get(this.layers.size() - 1)) {
            // Formula used is dj = cj(1-cj)(cj-cjk)
            outputLayer_neuron.error = (outputLayer_neuron.output_feature - outputLayer_neuron.output) * Contants.dsigmoid(outputLayer_neuron.output);
        }
        // calculate hidden area error
        // The side for loop does the summation of wi*dj
        // then the outside for loop multiplies sumation with bi(1-bi)
        float sum_wij_time_dj = 0;
        for (Neuron hidden_layer_neuron : this.layers.get(1)) {
            hidden_layer_neuron.error = 0;
            for (Neuron output_layer_neuron : this.layers.get(2)) {
                float wj_times_dj = hidden_layer_neuron.connectedTo.get(output_layer_neuron.id) * output_layer_neuron.error;
                sum_wij_time_dj += wj_times_dj;
                hidden_layer_neuron.error += hidden_layer_neuron.connectedTo.get(output_layer_neuron.id) * output_layer_neuron.error;

            }
            // ei = b1*(1-bi)*sum(wij*dj)
            // bi is the hidden layer output value
            // is the gradient of the error WRT the input to the hidden layer
            hidden_layer_neuron.ei = sum_wij_time_dj * Contants.dsigmoid(hidden_layer_neuron.output);
        }


        // this block of code gets delta wij
        // for each weight in the fb to fc we multiply the (a * bi * dj)
        // and add into the array
        for (Neuron hidden_layer_neuron : this.layers.get(1)) {

            for (Neuron output_layer_neuron : this.layers.get(2)) {
                if (hidden_layer_neuron.contains(output_layer_neuron.id)) {
                    hidden_layer_neuron.delta.add(Contants.LEARNING_RATE * hidden_layer_neuron.output * output_layer_neuron.error);

                }
            }

        }
        // this block of code calculates the delta from input to the hidden layer
        // for each weight in the fb to fc we multiply the (a * ah * ei)
        // and add into the array
        for (Neuron input_layer_neurons : this.layers.get(0)) {

            for (Neuron hidden_layer_neurons : this.layers.get(1)) {
                if (input_layer_neurons.contains(hidden_layer_neurons.id)) {
                    input_layer_neurons.delta.add((Contants.LEARNING_RATE * input_layer_neurons.input_feature * hidden_layer_neurons.ei));


                    //System.out.println(input_layer_neurons.delta);
                    //input_layer_neurons.connectedTo.replace(hidden_layer_neurons.id, input_layer_neurons.connectedTo.get(hidden_layer_neurons.id) + (Contants.LEARNING_RATE * hidden_layer_neurons.ei * input_layer_neurons.intput_feature));
                }
            }
        }

        // updates the weights that are connected from the input layer to the hidden layer
        for (Neuron i : this.layers.get(0)) {
            int c = 0;
            for (Neuron j : this.layers.get(1)) {
                if (i.contains(j.id)) {
                    if (!i.previous_deltas.isEmpty()){
                    i.connectedTo.replace(j.id, i.connectedTo.get(j.id) + (i.delta.get(c) + (Contants.MOMENTUM * i.previous_deltas.get(c))));

                    }
                    else {
                    i.connectedTo.replace(j.id, i.connectedTo.get(j.id) + i.delta.get(c));

                    }
                }
                    c++;
            }
            i.previous_deltas = i.delta.stream().collect(Collectors.toList());
            i.delta.clear();
        }

        // updates the weights that are connected from the hidden layer to the output layer
        for (Neuron i : this.layers.get(1)) {
            int c = 0;
            for (Neuron j : this.layers.get(2)) {
                if (i.contains(j.id)) {
                    if (!i.previous_deltas.isEmpty()){
                    float temp = (i.connectedTo.get(j.id) + (i.delta.get(c) + (Contants.MOMENTUM * i.previous_deltas.get(c))));
                    i.connectedTo.replace(j.id, temp);

                    }
                    else
                        {
                    float temp = (i.connectedTo.get(j.id) + i.delta.get(c));
                    i.connectedTo.replace(j.id, temp);

                    }
                    // System.out.println(i.delta);

                }
                    c++;
            }
            i.previous_deltas = i.delta.stream().collect(Collectors.toList());
            i.delta.clear();
        }

    }
    /*Calls the feed forward algorithim
    * This Method does a single pass from Each layer calculating the output of each neuron
    **/
    private List feed_foreward() {
            int count = 0;
            List<Float> output_l = new ArrayList();

            /*Adds input value * the weight value connected between the edges
            * sums those values to get the input value of the neuron at the preceding layer  */
        for (Neuron output : this.layers.get(1)) {
            output.input =0;
            for (Neuron input : this.layers.get(0)) {
                float value = input.connectedTo.get(output.id);
                output.input += value * input.input_feature;
            }
            /*Activation function is called and out input is from that neuron is passed into the Activation function */
            output.output = Contants.activationFunction(output.input);
        }
        /*Calculates the sum of weights and output values from the previous layer */
        for (Neuron output : this.layers.get(2)) {
            output.input =0;
            for (Neuron input : this.layers.get(1)) {
                float value = input.connectedTo.get(output.id);
                output.input += value * input.output;
            }
            /*Activation function is called and out input is from that neuron is passed into the Activation function */
            output.output = Contants.activationFunction(output.input);
        }

        for (Neuron neuron : this.layers.get(this.layers.size() - 1)) {
            output_l.add(neuron.output);
        }
        /*Returns the output at the output layer neurons */
        return output_l;



    }

    /**
     * Test float.
     *
     * @param input_features   the input features
     * @param expected_outputs the expected outputs
     * @return The RMSE
     */
    public float test(List<Float> input_features, List<Float> expected_outputs) {
        // give each neuron a new input feature
        for (int i = 0; i < input_features.size(); i++) {
            this.layers.get(0).get(i).input_feature = input_features.get(i);
        }

        for (int i = 0; i < expected_outputs.size(); i++) {
            this.layers.get(this.layers.size()-1).get(i).output_feature = expected_outputs.get(i);

        }

        List<Float> output = this.feed_foreward();
        float mse_per_example =0;
        for (int i = 0; i < output.size(); i++) {
            mse_per_example += Math.pow(expected_outputs.get(i) - output.get(i),2);
        }
        this.total_testing_error += (float) Math.sqrt(mse_per_example/output.size());



        List<Float> newOutput = this.get_results(output,expected_outputs);
//        System.out.print("The expected ouput is: ");
//        for (float expected_output : expected_outputs) {
//            System.out.print(expected_output);
//            System.out.print(" ");
//
//        }
//
//        System.out.print(" and The output gotten back is: ");
//        for (Float v : newOutput) {
//            System.out.print(v + " ");
//
//        }
//        System.out.println();


        this.get_accuracy(newOutput,expected_outputs);
        return root_mean_sqaured(output,expected_outputs, expected_outputs.size());


    }

    /**
     * Validate float.
     *
     * @param list_of_lists the list of lists This is the input features being validated
     * @param l2            the l 2
     * @return the float returns the RMS For the input
     */
    public float validate(List<List<Float>> list_of_lists, List<List<Float>> l2) {
        // give each neuron a new input feature
        for (int i = 0; i < list_of_lists.get(0).size(); i++) {
            this.layers.get(0).get(i).input_feature = list_of_lists.get(0).get(i);
        }
//
        for (int i = 0; i < list_of_lists.get(1).size(); i++) {
            this.layers.get(this.layers.size() - 1).get(i).output_feature = list_of_lists.get(1).get(i);

        }
//
        List<Float> output = this.feed_foreward();
        List<Float> newOutput = this.get_results(output, list_of_lists.get(1));


        this.get_accuracy(newOutput, list_of_lists.get(1));

        return root_mean_sqaured(output, list_of_lists.get(1), (float)list_of_lists.get(1).size());
    }

    /**
     * Get results list.
     *Calculates the results
     * @param output           the output
     * @param expected_outputs the expected outputs
     * @return the list returns a which decides and classifys which number was chosen
     */
    public List<Float> get_results(List<Float> output, List<Float> expected_outputs){
        float max_value = Collections.max(output);
        //System.out.println(max_value);
        List<Float> newOutput = new ArrayList<>();
        List<Float> all_zeros = new ArrayList<>(Arrays.asList(0f,0f,0f,0f,0f,0f,0f,0f,0f));
        if (expected_outputs.equals(all_zeros)){
            newOutput = expected_outputs;
            return expected_outputs;
        }
        else {
            for (Float value : output) {
                if (value == max_value){
                    newOutput.add(1f);
                }
                else {
                    newOutput.add(0f);
                }
            }
            return newOutput;
        }

    }

    /**
     * Get accuracy.
     * calculates the total correctness for each training example increasing a count
     * @param output          the output
     * @param expected_output the expected output
     */
    public void get_accuracy(List<Float> output, List<Float> expected_output){
        List<Integer> tow_values = new ArrayList<>();


        if (output.equals(expected_output)){
            this.correct_guess++;
        }

        this.total++;
    }

    /**
     * Gets accuracy.
     *
     * @param output          the output
     * @param expected_output the expected output
     * @return the accuracy
     */
    public int get_accuracy_(List<Float> output, List<Float> expected_output) {
        List<Integer> tow_values = new ArrayList<>();


        if (output.equals(expected_output)) {
            return 1;
        }

        return 0;
    }

    /**
     * Root mean sqaured float.
     *
     * @param output      the output
     * @param execptedOut the execpted out
     * @param length      the length
     * @return the float
     */
    public float root_mean_sqaured(List<Float> output, List<Float> execptedOut, float length){
        float sum = 0;
        for (int i = 0; i < execptedOut.size(); i++) {
            sum += Math.pow(output.get(i) - execptedOut.get(i),2);
        }
        return (float) Math.sqrt((sum/length));
    }
    private void backProp_batch() {

        // calculate error between calculared and expected error in the output nodes
        // calculates the gradient of the error WRT to each output node
        for (Neuron outputLayer_neuron : this.layers.get(this.layers.size() - 1)) {
            // Formula used is dj = cj(1-cj)(cj-cjk)
            outputLayer_neuron.error = (outputLayer_neuron.output_feature - outputLayer_neuron.output) * Contants.dactivationFunction(outputLayer_neuron.output);
            //System.out.println(outputLayer_neuron.output_feature +  " - " + outputLayer_neuron.output);
            // rprop needs to go the oppisite direction of the gradient;


        }
        // calculate hidden area error
        // The side for loop does the summation of wi*dj
        // then the outside for loop multiplies sumation with bi(1-bi)
        float sum_wij_time_dj = 0;
        for (Neuron hidden_layer_neuron : this.layers.get(1)) {
            hidden_layer_neuron.error = 0;
            for (Neuron output_layer_neuron : this.layers.get(2)) {
                float wj_times_dj = hidden_layer_neuron.connectedTo.get(output_layer_neuron.id) * output_layer_neuron.error;
                sum_wij_time_dj += wj_times_dj;
                //hidden_layer_neuron.error += hidden_layer_neuron.connectedTo.get(output_layer_neuron.id) * output_layer_neuron.error;

            }
            // ei = b1*(1-bi)*sum(wij*dj)
            hidden_layer_neuron.ei = sum_wij_time_dj * Contants.dactivationFunction(hidden_layer_neuron.output);
        }


        // this block of code gets delta wij
        // for each weight in the fb to fc we multiply the (a * bi * dj)
        // and add into the array
        for (Neuron hidden_layer_neuron : this.layers.get(1)) {
            int c = 0;
            for (Neuron output_layer_neuron : this.layers.get(2)) {
                if (hidden_layer_neuron.contains(output_layer_neuron.id)) {
                    // this is the individual gradient error for weights wij
                    hidden_layer_neuron.delta.add( hidden_layer_neuron.learning_rates_per_weight.get(c)
                            * hidden_layer_neuron.output * output_layer_neuron.error);
                }
                c++;
            }
            hidden_layer_neuron.batch_deltas.add(new ArrayList<>(hidden_layer_neuron.delta));
            //hidden_layer_neuron.previous_deltas.add(new ArrayList<Float>(hidden_layer_neuron.delta));
            hidden_layer_neuron.delta.clear();

        }
        // this block of code calculates the delta from input to the hidden layer
        // for each weight in the fb to fc we multiply the (a * ah * ei)
        // and add into the array
        for (Neuron input_layer_neurons : this.layers.get(0)) {
            int c=0;
            for (Neuron hidden_layer_neurons : this.layers.get(1)) {
                if (input_layer_neurons.contains(hidden_layer_neurons.id)) {
                    input_layer_neurons.delta.add(input_layer_neurons.learning_rates_per_weight.get(c) * input_layer_neurons.input_feature * hidden_layer_neurons.ei);
//                    System.out.println(input_layer_neurons.intput_feature +
//                            " * " + hidden_layer_neurons.ei + " = " + (input_layer_neurons.intput_feature * hidden_layer_neurons.ei));
//                    System.out.println(input_layer_neurons.delta);
                }
                c++;
            }

            input_layer_neurons.batch_deltas.add(new ArrayList<>(input_layer_neurons.delta));
            input_layer_neurons.delta.clear();
        }

    }

    /**
     * Back prop batch update.
     * does the Backprop Batch Algorithim for the entire batch size of training examples
     */
    public void backProp_batch_update() {

        // updates the weights that are connected from the input layer to the hidden layer
        for (Neuron i : this.layers.get(0)) {
            List<Float> sum_delta_weights = rpropSum(i.batch_deltas);
            int c = 0;
            for (Neuron j : this.layers.get(1)) {
                if (i.contains(j.id)) {
                    i.delta.add( sum_delta_weights.get(c));
                    //i.connectedTo.replace(j.id, i.connectedTo.get(j.id) +  sum_delta_weights.get(c));
                }
                c++;
            }
            //i.batch_deltas.clear();
        }
        // updates the weights that are connected from the hidden layer to the output layer
        // gives the gradient for the sum of values in the batch
        for (Neuron i : this.layers.get(1)) {
            int c = 0;
            List<Float> sum_delta_weights = rpropSum(i.batch_deltas);
            for (Neuron j : this.layers.get(2)) {
                if (i.contains(j.id)) {
                    i.delta.add(sum_delta_weights.get(c));
                    //float newWeight = i.connectedTo.get(j.id) + sum_delta_weights.get(c);
                    //i.connectedTo.replace(j.id, newWeight);
                }

                c++;
            }
            //i.batch_deltas.clear();
        }

        for (Neuron i : this.layers.get(0)) {
            int m = 0;
            for (Neuron j : this.layers.get(1)) {
                if (i.contains(j.id)) {
                    if (i.previous_deltas.isEmpty()){
                        for (int k = 0; k <i.delta.size(); k++) {
                            i.previous_deltas.add(0f);
                        }
                    }
                    int c = sign(i.delta.get(m), i.previous_deltas.get(m));

                    // calculates the actual weight change of the weight delta
                    if (c > 0){
                        // negative increment delta

                        float newWeight = (i.delta.get(m) * (i.learning_rates_per_weight.get(m))) + i.connectedTo.get(j.id);
                        i.connectedTo.replace(j.id,newWeight);
                        i.learning_rates_per_weight.set(m,1.2f * i.learning_rates_per_weight.get(m));
                    }

                    // positive delta
                    else if (c < 0){
                        float newWeight = (i.delta.get(m) * i.learning_rates_per_weight.get(m)) + i.connectedTo.get(j.id);
                        i.connectedTo.replace(j.id,newWeight);
                        i.learning_rates_per_weight.set(m,0.5f * i.learning_rates_per_weight.get(m));
                    }
                    else {
                        // dont change the learning rate
                        float newWeight = (i.delta.get(m) * i.learning_rates_per_weight.get(m)) + i.connectedTo.get(j.id);
                        i.connectedTo.replace(j.id,newWeight);
                    }
                }

                m++;
            }
            i.previous_deltas.clear();
            i.previous_deltas = i.delta.stream().collect(Collectors.toList());
            i.delta.clear();
            i.batch_deltas.clear();
        }

        for (Neuron i : this.layers.get(1)) {
            int m = 0;
            for (Neuron j : this.layers.get(2)) {
                if (i.contains(j.id)) {
                    if (i.previous_deltas.isEmpty()){
                        for (int k = 0; k <i.delta.size(); k++) {
                            i.previous_deltas.add(0f);
                        }
                    }
                    int c = sign(i.delta.get(m), i.previous_deltas.get(m));

                    if (i.learning_rates_per_weight.get(m) > Contants.positiveETA){
                        i.learning_rates_per_weight.set(m, 0.01f);
                    }
                    else if (i.learning_rates_per_weight.get(m) < Contants.negativeETA){
                        i.learning_rates_per_weight.set(m, 0.01f);

                    }

                    // calculates the actual weight change of the weight delta
                    if (c > 0){
                        // negative increment delta
                        float newWeight = (i.delta.get(m) * (i.learning_rates_per_weight.get(m))) + i.connectedTo.get(j.id);
                        i.connectedTo.replace(j.id,newWeight);
                        i.learning_rates_per_weight.set(m,1.2f * i.learning_rates_per_weight.get(m));
                    }

                    // positive delta
                    else if (c < 0){
                        float newWeight = (i.delta.get(m) * i.learning_rates_per_weight.get(m)) + i.connectedTo.get(j.id);
                        i.connectedTo.replace(j.id,newWeight);
                        i.learning_rates_per_weight.set(m,0.5f * i.learning_rates_per_weight.get(m));

                    }
                    else {
                        // dont change the learning rate
                        float newWeight = (i.delta.get(m) * i.learning_rates_per_weight.get(m)) + i.connectedTo.get(j.id);
                        i.connectedTo.replace(j.id,newWeight);
                    }
                }

                m++;
            }
            i.previous_deltas.clear();
            i.previous_deltas = i.delta.stream().collect(Collectors.toList());
            i.delta.clear();
            i.batch_deltas.clear();
        }

    }

    /**
     * Sign int.
     *
     * @param current_grad the current gradient for weight
     * @param pre_grad     the pre gradient WRT error for the weight
     * @return the int     returns a number either +, -
     */
    public int sign(float current_grad, float pre_grad){
        if ((current_grad * pre_grad) < 0){
            return -1;
        }
        if ((current_grad * pre_grad) == 0 ){
            return 0;
        }

        if ((current_grad * pre_grad) > 0){
            return 1;
        }
        return 99;
    }

    /**
     * Init rprop weights.
     * used to intilaize weights of the rprop algorithim
     */
    public void init_rprop_weights(){
        for (Neuron i : this.layers.get(0)) {
            int c = 0;
            for (Neuron j : this.layers.get(1)) {
                if (i.contains(j.id)) {
                    i.learning_rates_per_weight.add(0.1f);
                }
                c++;
            }
            //i.batch_deltas.clear();
        }

        // updates the weights that are connected from the hidden layer to the output layer
        for (Neuron i : this.layers.get(1)) {
            int c = 0;

            for (Neuron j : this.layers.get(2)) {
                if (i.contains(j.id)) {
                    i.learning_rates_per_weight.add(0.1f);
                }
                c++;
            }

        }
    }
    /*Sums up all the Delta values for each weight */
    private List<Float> rpropSum(List<List<Float>> batch_delta_weight_lists) {

        List<Float> delta_sum_list = new ArrayList<>();
        for (int i = 0; i < batch_delta_weight_lists.get(0).size(); i++) {
            float sum = 0f;
            for (int j = 0; j < batch_delta_weight_lists.size(); j++) {
                //System.out.println(batch_delta_weight_lists.get(j).get(i));

                sum += batch_delta_weight_lists.get(j).get(i);



            }
            // makes the gradient negative
            delta_sum_list.add(sum);
        }
        return delta_sum_list;

    }
}

