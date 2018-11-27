import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
public class Main {
    /**
     * for calling methods and manage the program
     * **/
    public static void main(String args[]) {
        // file path
        String filepath = "D:\\java\\a3\\src\\trip2.csv";
        // declaration
        DataProcess dp = new DataProcess();
        Destinations des = new Destinations();
        Model nnmodel = new Model();
        // read the file
        StringBuffer buffer = dp.readFile(filepath);
        /*
        * generate outputs and inputs according to the file (without training and testing sets)
        * */
        double[][] inputs = dp.inputProcessor(buffer);
        int row = inputs.length;
        String[][] results = dp.outputProcessor(buffer, row);
        // count how many destinations in the file
        String[] list = des.countDes(results);
        //convert the des into binary multidimensional arrays
        double[][] code = des.desCoding(list);
        // show the infor
        printDesCode(code, list);
        // convert all outputs into relative binary codes
        double[][] y = des.conver2doubleArray(results, code, list);
        // create8 training data (with a training and a testing set)
        int[] index1 = nnmodel.setTrainingIndex(y.length, Numbers.TRAINPER);
        int[] index2 = nnmodel.setTestingIndex(index1, y.length);
//        dp.printKIntArray(index2);
        double[][] x_train = nnmodel.getDoubleSets(index1, inputs);
        double[][] y_train = nnmodel.getDoubleSets(index1, y);
        double[][] x_test = nnmodel.getDoubleSets(index2, inputs);
        double[][] y_test = nnmodel.getDoubleSets(index2, y);
        String[][] y_testStr = nnmodel.getStrSet(index2, results);
        MLDataSet trainingSet = new BasicMLDataSet(x_train, y_train);
        // build an architecture of the nn
        BasicNetwork arich = nnmodel.estNNStructure(x_train, y_train);
        // train the nn by bp
        BasicNetwork network = nnmodel.trainNN(arich, trainingSet);
        // have a look at the training results
//        nnmodel.printTrainResults(network, trainingSet);
        // make predictions according to given training set
        String[][] predicitons = des.convert22DStr(nnmodel.predictionCodeInt(nnmodel.predictionCodeProb(network, x_test, y_test)),
                list);
        double preRate = nnmodel.printTestResults(y_testStr, predicitons);
        System.out.println("Prediction Rate: " + preRate * Numbers.PERCENT + " %");
//        System.out.println(preBuffer);
        }
        /**
         * print current mappings from string destination to multidimensional binary destination
         * **/
    public static void printDesCode(double[][] descode, String[] des){
        StringBuffer strbu = new StringBuffer();
        for (int i = Numbers.ZERO;i<des.length;i++){
            strbu.append(des[i] + ": ");
            for (int j= Numbers.ZERO; j< descode[Numbers.ZERO].length; j++){
                strbu.append(descode[i][j]+ " ");
            }
            strbu.append("\n");
        }
        System.out.print(strbu);
    }

}
