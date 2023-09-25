package com.example.gh4456;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest
class Gh4456ApplicationTests {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Autowired
	private JobRepository jobRepository;

	@BeforeEach
	public void setUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	// Create a standard job that will .end() on priorityExpression
	// and .fail() on secondaryExpression
	private Job createJob(String priorityExpression, String secondaryExpression, String flowStatus) {

		JobExecutionDecider completed = (jobExecution, stepExecution) -> new FlowExecutionStatus(flowStatus);
		Flow flow = new FlowBuilder<SimpleFlow>("step")
				.start(completed)
				.on(priorityExpression).end()
				.on(secondaryExpression).fail()
				.build();

		Step step = new StepBuilder("MyStep", jobRepository)
				.flow(flow)
				.build();

		Job job = new JobBuilder("job", jobRepository)
				.start(step)
				.build();

		return job;

	}

	// ****************************************************************************************************
	// ** DOCUMENTATION TESTS **
	// ****************************************************************************************************

	@Test
	void testJobExecution_documentation1() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "foo*";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation2() throws Exception {
		String priorityExpression = "foo*";
		String secondaryExpression = "???";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation3() throws Exception {
		String priorityExpression = "???";
		String secondaryExpression = "fo?";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation4() throws Exception {
		String priorityExpression = "fo?";
		String secondaryExpression = "foo";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation5() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "???";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation6() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "fo?";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation7() throws Exception {
		String priorityExpression = "foo*";
		String secondaryExpression = "fo?";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation8() throws Exception {
		String priorityExpression = "foo*";
		String secondaryExpression = "foo";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_documentation9() throws Exception {
		String priorityExpression = "???";
		String secondaryExpression = "foo";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}


	// ****************************************************************************************************
	// ** DEFAULT STATE TRANSITION COMPARATOR TESTS **
	// ****************************************************************************************************

	@Test
	void testJobExecution_dstc1() throws Exception {
		String priorityExpression = "CONTIN???LE";
		String secondaryExpression = "CONTINUABLE";

		String flowStatus = "CONTINUABLE";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_dstc2() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "CONTINUABLE";

		String flowStatus = "CONTINUABLE";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_dstc3() throws Exception {
		String priorityExpression = "CONTIN*";
		String secondaryExpression = "CONTINUABLE";

		String flowStatus = "CONTINUABLE";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_dstc4() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "C?";

		String flowStatus = "CO";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_dstc5() throws Exception {
		String priorityExpression = "CON*";
		String secondaryExpression = "CON?";

		String flowStatus = "CONT";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_dstc6() throws Exception {
		String priorityExpression = "**";
		String secondaryExpression = "*";

		String flowStatus = "CONTINUABLE";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}



	// ****************************************************************************************************
	// ** USER TESTS **
	// ****************************************************************************************************



	@Test
	void testJobExecution_rjm1() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "*foo";

		String flowStatus = "afoo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_rjm2() throws Exception {
		String priorityExpression = "*";
		String secondaryExpression = "?";

		String flowStatus = "C";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_rjm3() throws Exception {
		String priorityExpression = "??";
		String secondaryExpression = "*";

		String flowStatus = "CO";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}


	@Test
	void testJobExecution_rjm4() throws Exception {
		String priorityExpression = "foo*";
		String secondaryExpression = "????";

		String flowStatus = "foo_";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_rjm5() throws Exception {
		String priorityExpression = "fo?";
		String secondaryExpression = "foo";

		String flowStatus = "foo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_rjm6() throws Exception {
		String priorityExpression = "????";
		String secondaryExpression = "foo?";

		String flowStatus = "foo_";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Test
	void testJobExecution_rjm7() throws Exception {
		String priorityExpression = "*foo";
		String secondaryExpression = "?foo";

		String flowStatus = "afoo";

		// given
		Job job = createJob(priorityExpression, secondaryExpression, flowStatus);

		// when
		jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		// then
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

}
