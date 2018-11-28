import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.IOException;

public class Main {
    /**
     * for calling methods and manage the program
     * **/
    public static void main(String args[]) throws IOException {
        String filepath1 = "network1.eg";
        String filepath2 = "descode.csv";
        // declaration
        DataProcess dp = new DataProcess();
        Destinations des = new Destinations();
        Model nnmodel = new Model();
        StringBuffer filecontent = dp.readFile(filepath2);
        double[][] descodeBinary = dp.inputProcessor(filecontent);
        String[][] descodeStr = dp.outputProcessor(filecontent, descodeBinary.length);
        // count how many destinations in the file
        String[] list = des.countDes(descodeStr);
        //convert the des into binary multidimensional arrays
        double[][] code = des.desCoding(list);
        User user = new User();
        double[] parameters = user.askQuestions(filepath1, list, code);
        BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(filepath1));
        double[][] predOne = nnmodel.predictOne(parameters, network, code[Numbers.ZERO].length);
        String desOne = des.convert22DStr(predOne, list)[Numbers.ZERO][Numbers.ZERO];
        user.answer(desOne);
        }
}
