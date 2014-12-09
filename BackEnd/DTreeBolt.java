
import java.io.Serializable;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class DTreeBolt extends BaseBasicBolt implements Serializable {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		DataSource trainingFile;
		DataSource testingFile;

		try {
			trainingFile = new DataSource("/Users/jagadishpd/Desktop/ARFF.arff");
			testingFile = new DataSource("/Users/jagadishpd/Desktop/ttt.arff");
			J48 tree = new J48();

			try {
				Instances train = trainingFile.getDataSet();
				Instances test = testingFile.getDataSet();
				train.setClassIndex(train.numAttributes() - 1);
				test.setClassIndex(test.numAttributes() - 1);
				System.out.println("Hi");
				tree.buildClassifier(train);
				Evaluation eval = new Evaluation(train);
				eval.evaluateModel(tree, test);
				System.out.println("bh");
				System.out.println(eval.toSummaryString("\nResults\n======\n",
						false));
				System.out.println(eval.toMatrixString());

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

		declarer.declare(new Fields("tweet_id", "tweet_text"));
	}

}
