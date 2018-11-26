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
        * generate outputs and inputs according to the file
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
        // convert outputs into relative binay codes
        double[][] y = des.conver2doubleArray(results, code, list);
        // create training data
        MLDataSet trainingSet = new BasicMLDataSet(inputs, y);
        // build an architecture of the nn
        BasicNetwork arich = nnmodel.estNNStructure(inputs, y);
        // train the nn by bp
        BasicNetwork network = nnmodel.trainNN(arich, trainingSet);
        // have a look at the training results
//        nnmodel.printTrainResults(network, trainingSet);
        // make predictions according to given training set
        String[][] predicitons = des.convert22DStr(nnmodel.predictionCodeInt(nnmodel.predictionCodeProb(network, inputs, y)),
                list);
        double preRate = nnmodel.printTestResults(results, predicitons);
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
        System.out.println(strbu);
    }
}
