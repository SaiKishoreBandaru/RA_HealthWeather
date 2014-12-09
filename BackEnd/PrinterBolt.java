package com.learningstorm.kafka;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseBasicBolt {

	PrinterBolt() {

	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String sentence = input.getString(0);
		System.out.println("Received Sentence: " + sentence);

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

}
