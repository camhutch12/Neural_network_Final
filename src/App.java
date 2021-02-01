import java.io.*;
import java.util.*;


/**
 * The type App.
 * This class is used for allowing a user to run the program
 */
// Cameron Hutchings 6427892
class App {
    /**
     * The Validation set correctness.
     */
    static List<Double> validation_set_correctness = new ArrayList<>();
    /**
     * The Percent.
     */
    static float percent;
    /**
     * The T cross percent.
     */
    static double t_crossPercent = 0;
    /**
     * The Has momentum.
     */
    static boolean hasMomentum = false;
    /**
     * The Cross over percent.
     */
    static List<Double> crossOverPercent = new ArrayList<>();
    /**
     * The Scanner.
     */
    static Scanner scanner = new Scanner(System.in);
    private static boolean crossoverChose;

    /**
     * The entry point of application.
     *User input for allowing different varitions to be tuned
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {

        // implement a small menu allowing for the user to which neural network to run

        Random random = new Random();
        random.setSeed(20);


        int network = 0;
        do {
            System.out.println("" +
                    "Please enter a number \n " +
                    "1. Basic Neural Network \n" +
                    "2. Neural Network With Momentum \n" +
                    "3. Neural Network With RProp Extension");
            try {
            network = scanner.nextInt();
            Contants.NETWORK_TYPES = network;
                setHyperParameters(network);
            }catch (Exception e){
                network = 0;
            }

        }
        while (network != 1 && network != 2 && network != 3);

        // choose weather to do hold out or k-fold cross validation
        int testingTypes =0;
        do {
            System.out.println("Please enter \n" +
                    "1: Holdout \n" +
                    "2: K-fold Crossover");
            try{
            testingTypes = scanner.nextInt();

            }catch (Exception e){
                System.out.println("Please enter a valid response");
                System.out.println("Please enter \n" +
                        "1: Holdout \n" +
                        "2: K-fold Crossover");
                testingTypes = scanner.nextInt();
            }
        }while (testingTypes != 1 && testingTypes != 2);


        if (testingTypes == 1){
            crossoverChose = false;
            do {
                try{
            System.out.println("Please enter your training percent like (50)");
            Contants.TrainingPercent = (int) Math.floor(scanner.nextInt());
                    System.out.println("Please enter your testing percent");
            Contants.TestingPercemt = (int) Math.floor(scanner.nextInt());

                }catch (Exception e){
                    System.out.println("Please pick a valid number");
                    System.out.println("Please enter your training percent like (50)");
                    Contants.TrainingPercent = (int) Math.floor(scanner.nextInt());
                    System.out.println("Please enter your testing percent");
                    Contants.TestingPercemt = (int) Math.floor(scanner.nextInt());
                }

            }while (((Contants.TrainingPercent + Contants.TestingPercemt) != 100) || (Contants.TestingPercemt > Contants.TrainingPercent));
            // do hold out
             run_code();
        }

        if (testingTypes == 2){
            // do cross validation
            crossoverChose = true;
            do {

                System.out.println("Please enter the amount of K-Folds: (This will be your K value");
                try {
                    Contants.k = scanner.nextInt();

                } catch (Exception e) {
                    System.out.println("Choice was invalid please try again");
                }
            }while (Contants.k < 1 && Contants.k > 10);
        }

        if (crossoverChose){
            if (network ==  3){
                runCrossOver(network);
            }
            else if (network == 1 || network == 2){
                runCrossOver(network);
            }
        }
        else {

            run_code();
        }

    }

    /**
     * Run cross over.
     *Used for running the crossover algorithim main for testing
     * @param network the network Type of neural network to use
     * @throws IOException the io exception
     */
    public static void runCrossOver(int network) throws IOException {
        List<Float> crossOver_p = new ArrayList<>();
        List<Float> mse_list_total = new ArrayList<>();
        for (int i = 0; i < Contants.SIZE; i++) {
            Bigger_Tuple tuple = PreprocessingFile.setupt();
            mse_list_total.add(crossOver(tuple, (int) Contants.k,network));
            crossOver_p.add((float) t_crossPercent);
            t_crossPercent = 0;
            crossOverPercent.clear();
        }
        double max = Collections.max(crossOver_p);
        double min = Collections.min(crossOver_p);
        double p = 0;
        for (Float v : crossOver_p) {
            p += v;
        }
        p /= crossOver_p.size();
        double sd = stand_deviation(crossOver_p, (float) p);
        float avg_MSE = 0;
        for (Float v : mse_list_total) {
            avg_MSE += v;
        }
        avg_MSE /= mse_list_total.size();
        File file = new File("Basic NeuralNetwork with momentum" + Contants.HIDDEN_LAYERS + ".txt");
        try {

            PrintStream out = new PrintStream(new FileOutputStream(file, true));
            out.print(avg_MSE + ",");
            out.print(p + ",");
            out.print(max + ",");
            out.print(min + ",");
            out.print(sd + ",");
            out.println();

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        File file3 = new File("Basic NeuralNetwork_MSE" + Contants.HIDDEN_LAYERS + ".txt");
        try {
            PrintStream out = new PrintStream(new FileOutputStream(Contants.filepath, true));
            for (Float mse : mse_list_total) {
                out.println(mse);

            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        crossOverPercent.clear();
        File file2 = new File("Basic NeuralNetwork_percent" + Contants.HIDDEN_LAYERS + ".txt");
        try {
            PrintStream out = new PrintStream(new FileOutputStream(file2, true));
            for (float mse : crossOver_p) {
                out.println(mse);

            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        while (Contants.HIDDEN_LAYERS < 100){
//            Contants.LEARNING_RATE  = 0.1f;
//            while (Contants.LEARNING_RATE < 1){
//                run_code();
//                Contants.LEARNING_RATE += 0.1;
//
//            }
//            Contants.HIDDEN_LAYERS += 10;
//        }

    }

    /**
     * Sets hyper parameters.
     *
     * @param network the network
     */
    public static void setHyperParameters(int network) {
        do {
            try {
                System.out.println("Please enter # of Hidden Neurons:");
                Contants.HIDDEN_LAYERS = (int) Math.floor(scanner.nextInt());

            } catch (Exception e) {
                System.out.println("Please enter a Integer number that is Positive");
                Contants.HIDDEN_LAYERS = -1;
            }

        } while (Contants.HIDDEN_LAYERS <= 0);


        do {
            try {
                System.out.println("Please enter the # of Epochs ");
                Contants.EPOCH = (int) Math.floor(scanner.nextInt());

            } catch (Exception e) {
                System.out.println("Please enter a Integer number that is Positive");
                Contants.EPOCH = -1;
            }

        } while (Contants.EPOCH <= 0);


        if (network == 1 || network ==2) {
            boolean isLearningRateCorrect;
            do {
                try {
                    System.out.println("Please enter the Learning Rate ");
                    Contants.LEARNING_RATE = scanner.nextFloat();
                    isLearningRateCorrect = true;

                } catch (Exception e) {
                    isLearningRateCorrect = false;
                    System.out.println("Please enter  number that is Positive");
                    Contants.LEARNING_RATE = -1f;
                    Contants.LEARNING_RATE = scanner.nextFloat();
                }

            } while (!isLearningRateCorrect);

                if (network == 2){
                    boolean hasMomentum;
                    do {
                        try {
                            System.out.println("Please enter the Momentum ");
                            Contants.LEARNING_RATE = scanner.nextFloat();
                            hasMomentum = true;

                        } catch (Exception e) {
                            hasMomentum = false;
                            System.out.println("Please enter number that is Positive");
                            Contants.MOMENTUM = -1f;
                        }

                    } while (!hasMomentum && Contants.MOMENTUM < -0.99999f);
                }
                else {
                    Contants.MOMENTUM =0f;
                }

        }


        do {
            try {
                System.out.println("Please Choose an activation function \n" +
                        "1: Sigmoid \n" +
                        "2: Hyperbolic Tan ");
                Contants.activationChosen = scanner.nextInt();
            }catch(Exception e){
                System.out.println("Invalid entry ");
                Contants.activationChosen = scanner.nextInt();
            }

        }while (Contants.activationChosen != 1 && Contants.activationChosen!= 2);

    }
    /*Calculates the standard deviation */
    private static float stand_deviation(List<Float> runs_percents, float mean) {
        float total_sum = 0;
        for (Float i : runs_percents) {
            total_sum += ((float) Math.pow((i - mean), 2));
        }
        total_sum = total_sum / runs_percents.size();
        total_sum = (float) Math.sqrt(total_sum);
        return total_sum;
    }

    /**
     * Holdout tuple.
     *
     * @param testing_percent  the testing percent
     * @param training_percent the training percent
     * @param data             the data Consists of the both and training and testing data
     * @return the tuple       returns 2 lists one for training and one for testing
     * @throws IOException the io exception
     */
    public static Tuple holdout(float testing_percent, float training_percent, Bigger_Tuple data) throws IOException {
        List<List<List<Float>>> something = new ArrayList<>();
        List<List<List<Float>>> testing_set = new ArrayList<>();
        Tuple results = new Tuple();
        for (int i = 0; i < data.one.size(); i++) {
            Tuple t = data.one.get(i);
            for (int j = 0; j < t.training.size(); j++) {
                List<List<Float>> data_list = new ArrayList<>();
                data_list.add((List<Float>) t.training.get(j));
                data_list.add((List<Float>) t.testing.get(j));
                something.add(data_list);
            }
        }
        Collections.shuffle(something);
        Collections.shuffle(something);
        Collections.shuffle(something);
        Collections.shuffle(something);

        int testing_list_size = (int) Math.floor(something.size() * (testing_percent / 100));
        for (int i = 0; i < testing_list_size; i++) {
            try{
            testing_set.add(something.remove(i));

            }catch (Exception e){
                System.out.println();
            }
        }


        results.set_other_lists(something, testing_set);
        return results;

    }

    /**
     * Gets validation set.
     *Used to get validation set for validating each epoch
     * @param testing_percent  the testing percent
     * @param training_percent the training percent
     * @param data             the data
     * @return the validation set
     * @throws IOException the io exception
     */
    public static List<List<List<Float>>> get_validation_set(float testing_percent, float training_percent, List<List<List<Float>>> data) throws IOException {
        List<List<List<Float>>> validation = new ArrayList<>();
        float validation_percent = data.size() * (testing_percent / 100);

        Collections.shuffle(data);
        Collections.shuffle(data);
        Collections.shuffle(data);
        for (int i = 0; i < validation_percent; i++) {
            validation.add(data.remove(i));
        }


        return validation;

    }

    /**
     * Combine sets list.
     *Combines the data and testing data into one bet list then shuffles those lists together.
     *
     * @param data the data
     * @return the list
     * @throws IOException the io exception
     */
    public static List<List<List<Float>>> combine_sets(Bigger_Tuple data) throws IOException {
        List<List<List<Float>>> something = new ArrayList<>();
        for (int i = 0; i < data.one.size(); i++) {
            Tuple t = data.one.get(i);
            for (int j = 0; j < t.training.size(); j++) {
                List<List<Float>> data_list = new ArrayList<>();
                data_list.add((List<Float>) t.training.get(j));
                data_list.add((List<Float>) t.testing.get(j));
                something.add(data_list);
                //System.out.println("hi");

            }
            //System.out.println();

        }
        //System.out.println();
        Collections.shuffle(something);
        Collections.shuffle(something);
        Collections.shuffle(something);
        Collections.shuffle(something);


        return something;

    }

    /**
     * Cross over float.
     *implements the K-fold crossover algorithim
     * @param data    the data
     * @param k       the k The k value expected from the k fold algorithim
     * @param network the network
     * @return the float
     * @throws IOException the io exception
     */
    public static float crossOver(Bigger_Tuple data, int k, int network) throws IOException {
        NeuralNetwork nn;
        List<Float> mse_list = new ArrayList<>();
        List<Float> percenty_list = new ArrayList<>();
        List<List<List<Float>>> big_data = combine_sets(data);
        List<List<List<List<Float>>>> k_fold_list = new ArrayList<>();
        List<List<List<Float>>> overflow = new ArrayList<>();
        if (network == 3){
             nn = new NeuralNetwork(big_data.get(0).get(0), Contants.HIDDEN_LAYERS, big_data.get(0).get(1));
        }
        else {
         nn = new NeuralNetwork(big_data.get(0).get(0), Contants.HIDDEN_LAYERS, big_data.get(0).get(1));

        }
        //System.out.println();
        int counter = 0;
        int b = 0;
        double k_size;
        // checks weather the data is able to be seperated evenly
        // if not takes data out until the k-folds are even then places the extra into the testing set
        do {
            k_size = (big_data.size() + (-1f * b)) / k;
            double x = (int) Math.floor(k_size);
            double y = Math.ceil(k_size);
            if (x == y) {
                break;
            } else {
                b++;
                overflow.add(big_data.remove(b));
            }
        } while (true);

        //System.out.println(k_size);
        for (int i = 0; i < k; i++) {
            List<List<List<Float>>> temp = new ArrayList<>();
            for (int j = 0; j < k_size; j++) {
                if (k_size == big_data.size()) {
                    for (List<List<Float>> big_datum : big_data) {
                        temp.add(big_datum);
                    }
                    break;
                }
                temp.add(big_data.remove(j));


            }
            k_fold_list.add(temp);

        }
        //System.out.println();

        for (int i = 0; i < k; i++) {
            List<List<List<Float>>> testing = k_fold_list.get(i);
            List<List<List<List<Float>>>> training = new ArrayList<>();

            for (int j = 0; j < k; j++) {
                if (j != i) {
                    training.add(k_fold_list.get(j));
                }

            }
//            if (network == 3){
//                NeuralNetworkRProp neuralNetwork = new NeuralNetworkRProp(training.get(0).get(0).get(0), Contants.HIDDEN_LAYERS, training.get(0).get(0).get(1));
//            }
//            else {
//            NeuralNetwork neuralNetwork = new NeuralNetwork(training.get(0).get(0).get(0), Contants.HIDDEN_LAYERS, training.get(0).get(0).get(1));
//
//            }

            for (List<List<List<Float>>> trainer : training) {

                for (int j = 0; j < Contants.EPOCH; j++) {
                    for (List<List<Float>> l : trainer) {
                        nn.train(l.get(0), l.get(1), 0f);
                    }

                    if (network == 3){
                        nn.backProp_batch_update();
                    }
                }



            }

            float value = 0;
            nn.correct_guess = 0;
            nn.total = 0;
            List<Float> m = new ArrayList<>();
            for (List<List<Float>> t : testing) {
                value = nn.test(t.get(0), t.get(1));
                m.add(value);
            }

            float sum = 0;
            double percent = (nn.correct_guess / nn.total) * 100;
            System.out.println(percent);
            crossOverPercent.add(percent);

            for (Float aFloat : m) {
                sum += aFloat;
            }

            sum = sum / testing.size();

            // need to get MSE for the data.
            float mean_sqaured_errror = nn.total_testing_error / testing.size();
            float percenty = sum/testing.size();

            mse_list.add(mean_sqaured_errror);
            percenty_list.add(percenty);
            //System.out.println("The Error is " + sum);
            nn.total_testing_error = 0;
            nn.correct_guess = 0;


        }

        float total_mse = 0;
        for (Float v : mse_list) {
            total_mse += v;
        }
        float total_percent_crossOvers =0;
        for (Float v : percenty_list) {
            total_percent_crossOvers += v;
        }
        total_percent_crossOvers /= percenty_list.size();
        double total_percent_crossOver = 0;
        for (Double v : crossOverPercent) {
            total_percent_crossOver += v;
        }
        total_percent_crossOver /= crossOverPercent.size();
        t_crossPercent = total_percent_crossOver;

        total_mse /= mse_list.size();
        System.out.println("Total MSE is " + total_mse);
        System.out.println("Total Percent is " + total_percent_crossOver);
        t_crossPercent = total_percent_crossOver;
        return total_mse;
    }

    /**
     * Run neural network using holdout float.
     *runs the neural network algorithm using Holdout method
     * @param data           the data Training and testing data
     * @param count          the count
     * @param validation_set the validation set list used for validation
     * @return the float
     */
    public static float run_neural_network_usingHoldout(Tuple data, int count, List<List<List<Float>>> validation_set) {
        float sum_of_sqaures = 0;
        List<Float> mse = new ArrayList<>();
        float rowMeanSqaureError = 10;
        //List<Float> mean_sqaure_per_row = new ArrayList<>();
        List<List<List<Float>>> training_data = data.helper_training;
        List<List<List<Float>>> test_data = data.helper_tester;

        List<Double> converge_list = new ArrayList();
        List<Double> validation_set_correctness = new ArrayList<>();
        int converge_count = 0;
        NeuralNetwork nn = new NeuralNetwork(training_data.get(0).get(0), Contants.HIDDEN_LAYERS, training_data.get(0).get(1));
        int i = 0;
        while (i < Contants.EPOCH && rowMeanSqaureError > 0.01) {
            double correct_valadated = 0;
            double total_validated = 0;
            System.out.println("Epoch: " + i);
            float sumError = 0;
            // calls the train method from the neural network
            for (int j = 0; j < training_data.size(); j++) {
                sumError += nn.train(training_data.get(j).get(0), training_data.get(j).get(1), sumError);
            }
            if (Contants.NETWORK_TYPES == 3){
                nn.backProp_batch_update();
            }
            float value = 0;
            for (int j = 0; j < validation_set.size(); j++) {
                List<List<Float>> v = validation_set.get(j);
                value += nn.validate(v, v);
            }

            // calculates the RMSE as well as the percent correct
            rowMeanSqaureError = value / validation_set.size();
            mse.add(rowMeanSqaureError);
            System.out.println("The MSE is " + rowMeanSqaureError);

            total_validated = validation_set.size();
            //converge_list.add((correct_valadated/total_validated)*100);

            nn.correct_guess = 0;
            nn.total = 0;

            sum_of_sqaures += (float) Math.sqrt(Math.pow(total_validated - correct_valadated, 2) / validation_set.size());
            //System.out.println((correct_valadated/)*100 + "%");
            // used for checking weather the data network got stuck on a local minima
            if (converge_count > 15) {
                converge_list.clear();
                converge_count = -1;
            }
            i++;
            converge_count++;
        }


        sum_of_sqaures += (float) Math.sqrt(sum_of_sqaures / Contants.EPOCH);
        nn.correct_guess = 0;
        nn.total = 0;
        float value = 0;
        for (List<List<Float>> test_datum : test_data) {
            value += nn.test(test_datum.get(0), test_datum.get(1));
        }

        float correct = (float) nn.correct_guess;
        float total = (float) nn.total;
        percent = ((correct / total)) * 100f;

        return value / test_data.size();


    }

    /**
     * Converged boolean.
     *
     * @param percentages the percentages
     * @return the boolean
     */
    public static boolean converged(ArrayList<Double> percentages) {
        int count = 0;
        double num = 0;
        for (int i = 0; i < percentages.size(); i++) {
            if (i == 0) {
                num = percentages.get(i);
            } else if (num == percentages.get(i)) {
                count++;
            }
        }

        if (count >= 10) {
            return true;
        } else
            return false;
    }

    /**
     * Run code.
     *Mainly used for testing but also calls the holdout algorithim
     *
     * @throws IOException the io exception
     */
    public static void run_code() throws IOException {
        int count = 0;

        List<Float> runs_percents = new ArrayList<>();
        List<Float> mseperRun = new ArrayList<>();
        for (int i = 0; i < Contants.SIZE; i++) {
            Bigger_Tuple data_ = PreprocessingFile.setupt();
            Tuple data = holdout(Contants.TestingPercemt, Contants.TrainingPercent, data_);
            List<List<List<Float>>> validation = get_validation_set(20, 80, data.helper_training);
            float mse = run_neural_network_usingHoldout(data, count, validation);
            validation_set_correctness.clear();
            runs_percents.add(percent);
            mseperRun.add(mse);
            count++;

        }
        float total_avg = 0;
        for (Float runs_percent : runs_percents) {
            total_avg += runs_percent;
        }
        total_avg = total_avg / runs_percents.size();
        // gets the max for all 30 runs
        float max = Collections.max(runs_percents);
        // gets the min for all 30 runs
        float min = Collections.min(runs_percents);
        // standard deviation
        float st = stand_deviation(runs_percents, total_avg);
        // get all percents for all 30 runs
        float avg_MSE = 0;
        for (Float v : mseperRun) {
            avg_MSE += v;
        }
        avg_MSE = avg_MSE / mseperRun.size();
        File file = new File("Basic NeuralNetwork with momentum" + Contants.HIDDEN_LAYERS + ".txt");
        try {

            PrintStream out = new PrintStream(new FileOutputStream(file, true));

            out.print(avg_MSE + " ");
            out.print(total_avg + " ");
            out.print(max + " ");
            out.print(min + " ");
            out.print(st + " ");
            out.println();

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file2 = new File("Basic NeuralNetwork with momentum Percents for test hidden layer is " + Contants.HIDDEN_LAYERS + " .txt");
        try {

            PrintStream out = new PrintStream(new FileOutputStream(file2, true));
            out.println("below is for: Learning rate:" + Contants.LEARNING_RATE + "Hidden layers: " + Contants.HIDDEN_LAYERS + " Epochs: " + Contants.EPOCH);
            for (Float runs_percent : runs_percents) {
                out.println(runs_percent);
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file3 = new File("Basic NeuralNetwork with momentum mse hidden layer is " + Contants.HIDDEN_LAYERS + " .txt");
        try {
            PrintStream out = new PrintStream(new FileOutputStream(file3, true));
            out.println("below is for: Learning rate:" + Contants.LEARNING_RATE + "Hidden layers: " + Contants.HIDDEN_LAYERS + " Epochs: " + Contants.EPOCH);
            for (Float ms : mseperRun) {
                out.println(ms);
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
