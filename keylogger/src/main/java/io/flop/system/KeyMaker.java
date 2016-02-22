package io.flop.system;

import org.apache.samza.Partition;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.SystemStreamPartition;
import org.apache.samza.util.BlockingEnvelopeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class KeyMaker extends BlockingEnvelopeMap {

	private static final Logger logger = LoggerFactory.getLogger(KeyMaker.class);
	private final List<String> streams;
	private final String systemName;
	private Timer timer;

	public KeyMaker(String systemName) {
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
		timer = new Timer();
		final KeyMaker that = this;
		// every second
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				String key = UUID.randomUUID().toString();
				logger.info("Key --> {}", key);
				for(String stream : streams) {
					SystemStreamPartition systemStreamPartition = new SystemStreamPartition(systemName, stream, new Partition(0));
					try {
						that.put(systemStreamPartition, new IncomingMessageEnvelope(systemStreamPartition, null, null, key));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0, 1000);
	}

	@Override
	public void stop() {
		timer.cancel();
	}
}
