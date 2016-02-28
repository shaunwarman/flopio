package io.flop.system;

import org.apache.samza.Partition;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.SystemStreamPartition;
import org.apache.samza.util.BlockingEnvelopeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BallMaker extends BlockingEnvelopeMap {

	private static final Logger logger = LoggerFactory.getLogger(BallMaker.class);
	private final List<String> streams;
	private final String systemName;

	public BallMaker(String systemName) {
		this.systemName = systemName;
		this.streams = new ArrayList<>();
	}

	@Override
	public void register(SystemStreamPartition systemStreamPartition, String startingOffset) {
		super.register(systemStreamPartition, startingOffset);
		streams.add(systemStreamPartition.getStream());
	}

	@Override
	public void start() {
		String ball = "<<[[{}]]>>";
		logger.info("Ball --> {}", ball);
		for(String stream : streams) {
			SystemStreamPartition systemStreamPartition = new SystemStreamPartition(systemName, stream, new Partition(0));
			try {
				put(systemStreamPartition, new IncomingMessageEnvelope(systemStreamPartition, null, null, ball));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		// NOOP
	}
}
