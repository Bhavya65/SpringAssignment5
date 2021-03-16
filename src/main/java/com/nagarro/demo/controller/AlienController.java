package com.nagarro.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.nagarro.demo.dao.AlienRepo;
import com.nagarro.demo.model.Alien;

@Controller
public class AlienController {

	@Autowired
	AlienRepo repo;

	@RequestMapping("/")
	public String home() {
		return "home.jsp";
	}
	
	@RequestMapping("/upload")
	public String upload() {
		System.out.println("hello");
		return "AddEntry.jsp";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "home.jsp";
	}
	
	@RequestMapping("/login")
	public ModelAndView login(@RequestParam String uname, @RequestParam String pass) {
		ModelAndView mv;
		if(uname.equals("Bhavya") && pass.equals("password")) {
			List<Alien> login = repo.findAll();
			mv = new ModelAndView("showData.jsp");
			mv.addObject("login", login);
		}
		else {
			mv = new ModelAndView("ErrorLogin.jsp");
		}
		return mv;
	}
	
	@RequestMapping("/add")
	public ModelAndView add(@RequestParam int employeeCode, @RequestParam String employeeName,@RequestParam String email, @RequestParam String location, @RequestParam String dateOfBirth) {
		Alien alien = new Alien();
		alien.setDateOfBirth(dateOfBirth);
		alien.setEmail(email);
		alien.setEmployeeName(employeeName);
		alien.setLocation(location);
		alien.setEmployeeCode(employeeCode);
		repo.save(alien);
		List<Alien> login = repo.findAll();
		ModelAndView mv = new ModelAndView("showData.jsp");
		mv.addObject("login", login);
		return mv;
	}
	
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam int employeeCode) {
		repo.deleteById(employeeCode);
		ModelAndView mv = new ModelAndView("AddEntry.jsp");
		return mv;
	}
	
	@RequestMapping("/download")
	public void downloadToCsv(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		String headerkey = "Content-Disposition";
		String headervalue = "attachment;filename = HrData.csv";
		response.setHeader(headerkey, headervalue);
		List<Alien> aliens = repo.findAll();
		String[] csvHeader = {"Employee Code", "Date of birth", "Email", "Employee Name", "Location"};
		String[] nameMapping = {"employeeCode", "dateOfBirth", "email", "employeeName", "location"};
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		csvWriter.writeHeader(csvHeader);
		for(Alien alien:aliens) {
			csvWriter.write(alien, nameMapping);
		}
		csvWriter.close();
	}
}