import java.io.Serializable;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;

import java.util.Random;

import javax.swing.*;

import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.evaluation.*;
import weka.gui.visualize.*;

public class RocBolt extends BaseBasicBolt implements Serializable {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// load data
		DataSource source = null;
		try {
			source = new DataSource("dataset/data.arff");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Instances data = null;
		try {
			data = source.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.setClassIndex(data.numAttributes() - 1);
		// train classifier
		Classifier cl = new J48();
		Evaluation eval = null;
		try {
			eval = new Evaluation(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			eval.crossValidateModel(cl, data, 10, new Random(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// generate curve
		ThresholdCurve tc = new ThresholdCurve();
		int classIndex = 0;
		Instances result = tc.getCurve(eval.predictions(), classIndex);
		// plot curve
		ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
		vmc.setROCString("(Area under ROC = "
				+ Utils.doubleToString(tc.getROCArea(result), 4) + ")");
		vmc.setName(result.relationName());
		PlotData2D tempd = new PlotData2D(result);
		tempd.setPlotName(result.relationName());
		tempd.addInstanceNumberAttribute();
		// specify which points are connected
		boolean[] cp = new boolean[result.numInstances()];
		for (int n = 1; n < cp.length; n++)
			cp[n] = true;
		
			try {
				tempd.setConnectPoints(cp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				vmc.addPlot(tempd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			   // display curve
			   JFrame frame = new javax.swing.JFrame("ROC Curve");
			   frame.setSize(800, 500);
			   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			   frame.getContentPane().add(vmc);
			   frame.setVisible(true);

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet_id", "tweet_text"));
	}

}
