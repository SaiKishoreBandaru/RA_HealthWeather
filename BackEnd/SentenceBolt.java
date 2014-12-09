package com.learningstorm.kafka;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;


import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class SentenceBolt extends BaseBasicBolt {

	private List<String> words = new ArrayList<String>();

	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String word = tuple.getString(0);
		if (StringUtils.isBlank(word)) {
			return;
		}
		System.out.println("Recieved Word:"+word);
		words.add(word);
		if(word.endsWith(".")){
			collector.emit(ImmutableList.of((Object)StringUtils.join(words,' ')));
			words.clear();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sentence"));
	}

}
