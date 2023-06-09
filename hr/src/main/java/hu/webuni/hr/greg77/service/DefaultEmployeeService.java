package hu.webuni.hr.greg77.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.greg77.config.HrConfigProperties;
import hu.webuni.hr.greg77.model.Employee;

@Service
public class DefaultEmployeeService extends AbstractEmployeeService{

	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(Employee employee) {
		return config.getRaise().getDef().getPercent();
	}

}
