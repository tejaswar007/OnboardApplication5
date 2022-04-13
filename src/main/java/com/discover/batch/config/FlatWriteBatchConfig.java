package com.discover.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;

import com.discover.batch.dto.EmployeeDTO;

@Configuration
public class FlatWriteBatchConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;
	
	  @Bean
	  @Scheduled(fixedRate = 1000)
	    public ItemReader<EmployeeDTO> readEmpData() {
	        FlatFileItemReader<EmployeeDTO> empData = new FlatFileItemReader<EmployeeDTO>();
	        empData.setResource(new ClassPathResource("/data/employee.csv"));
	        empData.setLineMapper(new DefaultLineMapper<EmployeeDTO>() {{
	            setLineTokenizer(new DelimitedLineTokenizer() {{
	                setNames(new String[] {"employeeId", "employeeName", "employeeSalary" });
	            }});
	            setFieldSetMapper(new BeanWrapperFieldSetMapper<EmployeeDTO>() {{
	                setTargetType(EmployeeDTO.class);
	            }});
	        }});
	  return empData; 
	 }
	  
	 @ SuppressWarnings({ "rawtypes", "unchecked" })
		@Bean
		public JdbcBatchItemWriter<EmployeeDTO> writeEmpData() {
			JdbcBatchItemWriter<EmployeeDTO> itemWriter = new JdbcBatchItemWriter<>();
			itemWriter.setDataSource(this.dataSource);
			itemWriter.setSql("INSERT INTO EMPLOYEE VALUES (:employeeId, :employeeName, :employeeSalary)");
			itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
			itemWriter.afterPropertiesSet();

			return itemWriter;
		}
	  
	  @Bean
		public Step step1() {
			return stepBuilderFactory.get("step1")
					.<EmployeeDTO, EmployeeDTO>chunk(10)
					.reader(readEmpData())
					.writer(writeEmpData())
					.build();
		}

		@Bean
		public Job job() {
			return jobBuilderFactory.get("job")
					.start(step1())
					.build();
		}
}
