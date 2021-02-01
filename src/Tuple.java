import java.util.List;

/**
 * The type Tuple.
 *This method is used to be a helper method allowing for the creation of a
 * Tuple like in python also can be used like the apashe pairs libary allowing for a return of two differnt parameters from a method
 * @param <T> the type parameter
 */
// Cameron Hutchings
 class Tuple<T> {
    /**
     * The Training. Training data
     */
    List<List<Float>> training;
    /**
     * The Testing. testing data
     */
    List<List<Float>> testing;
    /**
     * The Helper tester.
     */
    List<List<List<Float>>> helper_tester;
    /**
     * The Helper training.
     */
    List<List<List<Float>>> helper_training;


    /**
     * Instantiates a new Tuple.
     */
    public Tuple() {

    }

    /**
     * Instantiates a new Tuple.
     *
     * @param training the training
     * @param testing  the testing
     */
    protected Tuple(List<List<Float>> training, List<List<Float>> testing) {
        this.training = training;
        this.testing = testing;

    }


    /**
     * Set other lists.
     *
     * @param something   the something
     * @param testing_set the testing set
     */
    public void set_other_lists(List<List<List<Float>>> something, List<List<List<Float>>> testing_set){
        this.helper_training = something;
        this.helper_tester = testing_set;
    }
}
