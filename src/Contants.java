/**
 * The type Contants.
 * This class is used for allow a user to tune values
 * Also this class elminates Magic numbers from the other class
 * This class contains the hyperparameters as well as the activation functions for the Neural Network
 */
// Cameron Hutchings
 class Contants {
    /**
     * The Hidden layers. Number of hidden layers to use
     */
    static  int HIDDEN_LAYERS = 10;
    /**
     * The Epoch. # of Epochs to use
     */
    static  int EPOCH = 50;
    /**
     * The Learning rate. # for the learning rate to use
     */
    static  float LEARNING_RATE = (float) 0.1f;
    /**
     * The Size. amount of iterations to run, mainly for testing perposes
     */
    static  int SIZE = 30;
    /**
     * The Momentum. Hyperparameter used by neural network
     */
    static float MOMENTUM = 0.9f;
    /**
     * The Training percent. amount of training data to be used
     */
    static float TrainingPercent;
    /**
     * The Testing percemt. amount of testing data to be used
     */
    static float TestingPercemt;
    /**
     * The Network types. Which type of network to use
     */
    static int NETWORK_TYPES;
    /**
     * The Validation percent.
     */
    static float validationPercent;
    /**
     * The K. # of K for k-fold cross over validation
     */
    static float k;
    /**
     * The Filepath. was used for writing to a file for testing
     */
    static final String filepath="Crossover " + LEARNING_RATE + " Hidden_layers  " + HIDDEN_LAYERS + "Epoch " + EPOCH+ ".txt";
    /**
     * The constant negativeETA. gives the decrease of learning rate used in Rprop algorithim
     */
    public static float negativeETA = 0.5f;
    /**
     * The constant positiveETA. gives the increase of learning rate used in Rprop algorithim
     */
    public static float positiveETA = 1.2f;
    /**
     * The constant tolerance. This is the threshold of the smallest rProp can become
     */
    public static float tolerance = 1e-16f;

    static int activationChosen = 0;

    /**
     * Sigmoid float.
     *
     * @param x the x input from a neuron
     * @return the float returns output of a neuron
     */
    public static float sigmoid(float x) {

        float temp = (float)(1 + (Math.exp(-x)));

        return 1/temp;
    }

    /**
     * Dsigmoid float.
     *
     * @param y the y
     * @return the float returns the gradient WRT to the error
     */
    public static float dsigmoid(float y) {
        return (float) (y * (1.0 - y));
    }

    /**
     * Tanh function float.
     *
     * @param x the x
     * @return the float
     */
    public static float tanh_function(float x){
        return (float) Math.tanh(x);
    }

    /**
     * Dtanh function float.
     *
     * @param x the x
     * @return the float
     */
    public static float dtanh_function(float x){
        return (float) ((float) 1-(Math.pow(x,2)));
    }

    public static float activationFunction(float x){
       if (activationChosen == 1){
          return sigmoid(x);
       }
       else if (activationChosen ==2){
          return tanh_function(x);
       }
       return -1;
    }

   public static float dactivationFunction(float y){
      if (activationChosen == 1){
         return dsigmoid(y);
      }
      else if (activationChosen ==2){
         return dtanh_function(y);
      }
      return -1;
   }

}


