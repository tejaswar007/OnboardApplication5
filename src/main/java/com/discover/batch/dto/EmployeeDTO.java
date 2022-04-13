package com.discover.batch.dto;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
	@Id
	private Long employeeId;
	private String employeeName;
	private  Double employeeSalary;
}
