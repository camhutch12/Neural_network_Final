import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Preprocessing file.
 * This class is ued for preprocessing and cleaning the data before the data sets are used
 */
// Cameron Hutchings
 class PreprocessingFile {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {

    }

    /*Loads a file by reading a CSV*/
    public static Tuple loadFile(String fileName, int count) throws IOException {
        String fName = "a1digits/a1digits/" + fileName + ".txt";
        BufferedReader csv = new BufferedReader(new FileReader(fName));
        Tuple tuple;
        List<List<Float>> data = new ArrayList<>();
        float[] a;
        List<List<Float>> testing = new ArrayList<>();
        while (csv.readLine() != null) {
            String[] row = csv.readLine().split(",");
            List<Float> num_list = new ArrayList<>();
            for (String s : row) {
                float i = Float.parseFloat(s);
                num_list.add(i);
            }
            data.add(num_list);
            switch (count) {
                case 0:
                       a = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                    break;
                case 1:
                    a = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
                    break;
                case 2:
                    a = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
                    break;
                case 3:
                    a = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
                    break;
                case 4:
                    a = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f };
                    break;
                case 5:
                    a = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                    break;
                case 6:
                    a = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                    break;
                case 7:
                    a = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                    break;
                case 8:
                    a = new float[]{0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                    break;
                case 9:
                    a = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + count);
            }
            ArrayList<Float> b = new ArrayList<Float>();
            for (int i = 0; i < a.length; i++) {
                b.add(a[i]);
            }
            testing.add(b);
        }


        tuple = new Tuple(data, testing);
        return tuple;

    }
    /*Used for collecting all the data into a Bigger tuple */
    public static Bigger_Tuple setupt() throws IOException {
        List<Tuple> tupleList;
        List<Tuple> t_data = new ArrayList<>();
        List<Tuple> test_data = new ArrayList<>();
        Bigger_Tuple bt;
        for (int i = 0; i < 10; i++) {
            String file_name = "digit_test_" + i;
            Tuple single_tuple = loadFile(file_name,i);
            t_data.add(single_tuple);
        }

        for (int i = 0; i < 10; i++) {
            String file_name = "digit_train_" + i;
            Tuple single_tuple = loadFile(file_name,i);
            t_data.add(single_tuple);
        }
        bt = new Bigger_Tuple(t_data);
        return bt;
    }

}
