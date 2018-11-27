import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
    /**
     * establish a neural network architecture without any training
     * **/
    public BasicNetwork estNNStructure(double[][] inputs, double[][] y){
        // create a neural network, without using a factory
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, inputs[Numbers.ZERO].length));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, Numbers.UNITES));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, y[Numbers.ZERO].length));
        network.getStructure().finalizeStructure();
        network.reset();
        Encog.getInstance().shutdown();
        return network;
    }
    /**
     * train a neural network by the bp algorithm
     * **/
    public BasicNetwork trainNN(BasicNetwork network, MLDataSet trainingSet){
        // train the neural network
        final Backpropagation train = new Backpropagation(network, trainingSet, Numbers.LEARNINGRATE, Numbers.MOMENTUM);
        int epoch = Numbers.ZERO;
        double subError[] = new double[Numbers.ERRORSIZE];
        StringBuffer error = new StringBuffer();
        for(;;){
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            subError[epoch%Numbers.ERRORSIZE] = train.getError();
            epoch++;
            if (epoch > Numbers.MAXITERS || train.getError() < Numbers.THRESHOLDS){
                break;
            } else if(epoch>=Numbers.ERRORSIZE){
                double diff =stopRule2(subError);
                if (diff <= Numbers.MIN){
                    break;
                }
            }
            error.append(train.getError());
        }
        train.finishTraining();
        return network;
    }
    /**
     * if the sum of nearby N errors's differences are too small,
     * the training progress will stop
     * **/
    public double stopRule2(double[] error){
        double difference = Numbers.ZERO2;
        for (int i = Numbers.ZERO; i < error.length - Numbers.ONE; i++){
            difference = difference + Math.abs(error[i + Numbers.ONE] - error[i]);
        }
        return difference;
    }
    /**
     * make predictions by a neural network,
     * produce binary multidimensional outputs with probabilities
     * **/
    public double[][] predictionCodeProb(BasicNetwork network, double[][] inputs, double[][] y){
        // create testing data
        MLDataSet testingSet = new BasicMLDataSet(inputs, y);
        // test the neural network
        double[][] data = new double[y.length][y[Numbers.ZERO].length];
        int row = Numbers.ZERO;
        for (MLDataPair pair : testingSet) {
            final MLData output = network.compute(pair.getInput());
            for (int i= Numbers.ZERO; i< y[Numbers.ZERO].length; i++){
                data[row][i] = output.getData(i);
            }
            row ++;
        }
        return data;
    }
    /**
     * convert binary multidimensional predictions(double[][]) with probabilities into binary(0/1) multidimensional predictions
     * **/
    public double[][] predictionCodeInt(double[][] num){
        int row = num.length, col = num[Numbers.ZERO].length;
        double[][] ingPre = new double[row][col];
        int[] index = new int[row];
        for (int i = Numbers.ZERO; i < row; i++){
            double currMax = Numbers.ZERO2;
            for (int j = Numbers.ZERO; j < col; j++){
                if (num[i][j]>currMax){
                    currMax = num[i][j];
                    index[i] = j;
                }
            }
        }
        for (int i = Numbers.ZERO; i < row; i++){
            ingPre[i][index[i]] = Numbers.ONE2;
        }
        return ingPre;
    }
    /**
     * print the training results
     * **/
    public void printTrainResults(BasicNetwork network, MLDataSet trainingSet){
        System.out.println("Neural Network Results:");
        StringBuffer strb = new StringBuffer();
        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getInput()
                    + ", actual=" + output + ",ideal=" + pair.getIdeal());
            strb.append(output+ "\n");
        }
    }
    /**
     * print the testing results, which include:
     * (1) prediction rate;
     * **/
    public double printTestResults(String[][] y, String[][] yhat) {
        int row = y.length;
        int count = Numbers.ZERO;
        for (int i = Numbers.ZERO; i < row; i++) {
            String str1 = y[i][Numbers.ZERO];
            String str2 = yhat[i][Numbers.ZERO];
            if (str2.contains(str1)) {
                count++;
            }
        }
        double predRate = (double) count / row;
        return predRate;
    }
    public int[] setTrainingIndex(int amount, double percent){
        int size = (int)Math.floor((double)amount*percent);
        int[] trainIndex = new int[size];
        List<Integer> list;
        Random r = new Random();
        list = new ArrayList<>();
        int i;
        while(list.size() < size){
            i = r.nextInt(amount);
            if(!list.contains(i)){
                list.add(i);
            }
        }
        for (int m = Numbers.ZERO; m < list.size(); m++){
            trainIndex[m] = list.get(m);
        }
        return trainIndex;
    }
    public int[] setTestingIndex(int[] trainindex, int amount){
        int[] index = new int[amount-trainindex.length];
        int flag = Numbers.ZERO, count = Numbers.ZERO;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = Numbers.ZERO; i < trainindex.length; i++){
            list.add(trainindex[i]);
        }
        for (int i = Numbers.ZERO; i < amount; i++){
            if (!list.contains(flag)){
                index[count] = flag;
                count++;
            }
            flag++;
        }
        return index;
    }
    public double[][] getDoubleSets(int[] index, double[][] data){
        int row = data.length, newrow = index.length;
        int col = data[Numbers.ZERO].length;
        int[] newIndex = sort(index);
        double[][] newData = new double[newrow][col];
        int count = Numbers.ZERO;
        for (int i = Numbers.ZERO; i<row; i++){
            if (count < newrow && i == newIndex[count]){
                for (int j = Numbers.ZERO; j< col; j++){
                    newData[count][j] = data[i][j];
                }
                count++;
            }
        }
        return newData;
    }public String[][] getStrSet(int[] index, String[][] data){
        int row = data.length, newrow = index.length;
        int col = data[Numbers.ZERO].length;
        int[] newIndex = sort(index);
        String[][] newData = new String[newrow][col];
        int count = Numbers.ZERO;
        for (int i = Numbers.ZERO; i<row; i++){
            if (count < newrow && i == newIndex[count]){
                for (int j = Numbers.ZERO; j< col; j++){
                    newData[count][j] = data[i][j];
                }
                count++;
            }
        }
        return newData;
    }
    private int[] sort(int[] arr){
        for(int i=arr.length-1;i>0;i--){
            for(int j=1;j<=i;j++){
                if(arr[j-1]>arr[j]){
                    int tmp = arr[j-1];
                    arr[j-1] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    }
}
