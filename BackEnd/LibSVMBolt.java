import java.io.File;
import java.io.IOException;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;
import weka.core.Debug.Random;
import weka.core.converters.LibSVMLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class LibSVMBolt extends BaseBasicBolt {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {

		LibSVMLoader libsvm = new LibSVMLoader();

		try {
			libsvm.setFile(new File("LIBSVM.arff"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// read from libsvm format files
		Instances instances = null;
		try {
			instances = libsvm.getDataSet();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// convert to nominal class argument to avoid
		// exception in weka's smo :-/
		NumericToNominal filter = new NumericToNominal();
		try {
			filter.setInputFormat(instances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			instances = Filter.useFilter(instances, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Random ran = new Random(System.currentTimeMillis());
		instances.randomize(ran);

		int max = 10;
		double[] acc = new double[max];
		while (max > 0) {

			// copy and randomize instances
			Instances full = new Instances(instances);
			full.randomize(ran);

			// using 5 fold CV to emulate the 80-20 random split of jkms
			Instances train = full.trainCV(5, 0);
			Instances test = full.testCV(5, 0);

			// new svm with rbf kernel
			SMO smo = new SMO();
			String[] options = { "-L 1e-15", "-P 1e-15", "-N 2" };
			try {
				smo.setOptions(options);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			smo.setC(1.0); // same as default value for jkms
			RBFKernel rbf = new RBFKernel();
			rbf.setGamma(0.1); // same as default value for jkms
			smo.setKernel(rbf);

			try {
				smo.buildClassifier(train);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Evaluation eval = null;
			try {
				eval = new Evaluation(train);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				eval.evaluateModel(smo, test);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(eval.toSummaryString("results:\n", false));
			try {
				System.out.println(eval.toMatrixString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			acc[acc.length - max] = eval.pctCorrect();
			System.out.println("accuracy: " + eval.pctCorrect());

			max--;
		}

		double mu = 0;
		for (double d : acc) {
			mu += d;
		}
		mu /= acc.length;

		double std = 0;
		for (double d : acc) {
			std += (d - mu) * (d - mu);
		}
		std = Math.sqrt(std / acc.length);

		// print comparable score :-)
		System.out.println("mean accuracy : " + mu + " +/- " + std);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text"));
	}

}
