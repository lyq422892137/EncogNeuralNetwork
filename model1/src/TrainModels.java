import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.simple.EncogUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
public class TrainModels {
    public static void main(String args[]){
        // declaration
        DataProcess dp = new DataProcess();
        Destinations des = new Destinations();
        Model nnmodel = new Model();
        Features feature = new Features();
        // file path
        String filepath = "trip2.csv";
        String filepathNN1 = "nn1.eg";
        String filepathNN2 = "nn2.eg";
        String filepathNN3 = "nn3.eg";
        String filepathCode1 = "descode.csv";
        String filepathCode2 = "descode2.csv";
        // read the file
        StringBuffer buffer = dp.readFile(filepath);
        String[] features = feature.getFeatures(buffer);
          /*
        * generate outputs and inputs according to the file (without training and testing sets)
        * */
        double[][] inputs = dp.inputProcessor(buffer);
        int row = inputs.length;
        String[][] results = dp.outputProcessor(buffer, row);
        // count how many destinations in the file
        String[] list = des.countDes(results);
        trainProcess(des, nnmodel, results, inputs, filepathNN1,list, filepathCode1);
        /*
         * add a new nn model with one new feature and one new destination
         * */
        System.out.println("Add a new destination: [yes/no]");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if (answer.equalsIgnoreCase("yes")){
            System.out.println("Please input your new destination");
            String newDes = scanner.next();
            StringBuffer buffer2 = des.addNewDes(buffer, newDes, list, inputs[Numbers.ZERO].length);
            System.out.println(buffer2);
            double[][] inputs2 = dp.inputProcessor(buffer2);
            int row2 = inputs2.length;
            String[][] results2 = dp.outputProcessor(buffer2, row2);
            // count how many destinations in the file
            String[] list2 = des.countDes(results2);
            trainProcess(des, nnmodel, results2, inputs2, filepathNN2, list2, filepathCode2);
        }else {
            System.out.println("Add a new feature: [yes/no]");
            String answer2 = scanner.next();
            if (answer2.equalsIgnoreCase("yes")){
                System.out.println("Please enter the new feature's name:");
                String featureName = scanner.next();
                double[][] inputs3 = feature.addFeature(inputs, features, featureName);
                trainProcess(des, nnmodel, results, inputs3, filepathNN3, list, filepathCode1);
            }
        }
    }
    public static void trainProcess(Destinations des, Model nnmodel, String[][] results, double[][] inputs, String filename, String[] list, String codefilename){
        //convert the des into binary multidimensional arrays
        double[][] code = des.desCoding(list);
        // show the infor
        StringBuffer descodebuffer = saveDesCode(code, list, codefilename);
        printDesCode(descodebuffer);
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
        BasicNetwork network;
        double currRate = Numbers.ZERO2;
        for (;;){
            // train the nn by bp
            network = nnmodel.trainNN(arich, trainingSet);
            // make predictions according to given training set
            String[][] predictions = des.convert22DStr(nnmodel.predictionCodeInteger(nnmodel.predictionTestCodeProb(network, x_test, y_test)),
                    list);
            double preRate = nnmodel.printTestResults(y_testStr, predictions);
            System.out.println("Prediction Rate: " + preRate * Numbers.PERCENT + " %");
            System.out.println("Errors:" + EncogUtility.calculateClassificationError(network,new BasicMLDataSet(x_test,y_test)));
            if (preRate>=Numbers.STOPTRAINING || Math.abs(currRate-preRate)<Numbers.MIN){
                break;
            }
            currRate = preRate;
        }
        nnmodel.saveNN(network, filename);
    }
    /**
     * print current mappings from string destination to multidimensional binary destination
     * **/
    public static StringBuffer saveDesCode(double[][] descode, String[] des, String filename) {
        StringBuffer strbu = new StringBuffer();
        for (int i = Numbers.ZERO; i < des.length; i++) {
            for (int j = Numbers.ZERO; j < descode[Numbers.ZERO].length; j++) {
                strbu.append(descode[i][j] + ",");
            }
            strbu.append(des[i] + ",");
            strbu.append("\n");
        }
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            if (file.createNewFile()){
                PrintWriter p = new PrintWriter(new FileOutputStream(file.getAbsolutePath()));
                p.write(strbu.toString());
                p.close();
            } else {
                System.out.println("Creating destination code failed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strbu;
    }
    public static void printDesCode(StringBuffer stringBuffer){
        System.out.print(stringBuffer);
    }
}
