package com.blz.EmployeePayroll;

import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private EmployeePayrollDBService employeePayrollDBService;

	/* Welcome Message */
	public void printWelcomeMessage() {
		System.out.println("Welcome to the Employee PayRoll Service Program");
	}

	private static List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public static void main(String[] args) {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
	}

	/* Read Employee Payroll data from console */
	public void readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter Employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter Employee Name ");
		String name = consoleInputReader.next();
		System.out.println("Enter Employee Salary ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	/* Write Employee Payroll data to console */
	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO))
			System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
		else if (ioService.equals(IOService.FILE_IO)) {
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
		}
	}

	/* Print Employee Payroll */
	public void printData(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			new EmployeePayrollFileIOService().printData();
		}

	}

	public long countEntries(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO)) {
			return new EmployeePayrollFileIOService().countEntries();
		}
		return 0;
	}

	public List<EmployeePayrollData> readPayrollData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO))
			this.employeePayrollList = new EmployeePayrollFileIOService().readData();
		return employeePayrollList;
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws PayrollServiceException {
		if (ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readData();
		return employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) throws PayrollServiceException {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.salary = salary;

	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name)).findFirst().orElse(null);
		return employeePayrollData;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));

	}

}