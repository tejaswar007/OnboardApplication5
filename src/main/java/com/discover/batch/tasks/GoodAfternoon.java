package com.discover.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class GoodAfternoon implements Tasklet {
	private static final Logger  logger = LoggerFactory.getLogger(GoodMorning.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("Good Afternoon ....");
		return RepeatStatus.FINISHED;
	}

}