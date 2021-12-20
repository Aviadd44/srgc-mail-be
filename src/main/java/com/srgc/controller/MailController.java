package com.srgc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.srgc.model.EmailContent;
import com.srgc.model.Response;
import com.srgc.model.Student;

@RestController
@CrossOrigin("*")
public class MailController {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
//	final String dbUrl = "jdbc:mysql://mysql-local:3306/srgc";
	final String localDbUrl = "jdbc:mysql://localhost:3306/srgc";

	// Method to save student

	@PostMapping("student/save")
	public Student saveStudent(@RequestBody Student student) throws Exception {

		try {
			String myDriver = "com.mysql.cj.jdbc.Driver";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(localDbUrl, "root", "root");

			String query = " INSERT INTO student (firstName, lastName, address, username, emailId)"
					+ " values (?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, student.getFirstName());
			preparedStmt.setString(2, student.getLastName());
			preparedStmt.setString(3, student.getAddress());
			preparedStmt.setString(4, student.getUsername());
			preparedStmt.setString(5, student.getEmailId());
			preparedStmt.execute();

			conn.close();
		} catch (Exception e) {
			throw e;
		}
		return student;
	}

	@GetMapping("student/fetch")
	public ArrayList<Student> fetchStudents() throws Exception {
		ArrayList<Student> listOfStudents = new ArrayList<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(localDbUrl, "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select firstName, lastName, username, address, emailId, id from student");

			while (rs.next()) {
				Student student = new Student();
				student.setFirstName(rs.getString(1));
				student.setLastName(rs.getString(2));
				student.setUsername(rs.getString(3));
				student.setAddress(rs.getString(4));
				student.setEmailId(rs.getString(5));
				student.setId(rs.getInt(6));
				
				listOfStudents.add(student);
			}
			con.close();
		} catch (Exception e) {
			throw e;
		}
		return listOfStudents;
	}

	@DeleteMapping("student/delete/{studentId}")
	public Response deleteStudent(@PathVariable int studentId) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(localDbUrl, "root", "root");
			Statement stmt = con.createStatement();
			int queryResult = stmt.executeUpdate("delete from student where id = "+ studentId);
			con.close();
			if(queryResult > 0)
				return new Response("Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response("Failed");
	}
	
	@PostMapping("student/send/email")
	public EmailContent sendMail(@RequestBody EmailContent content) {
		
		String subject = "Email from Avi's Postal System project";
		
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(content.getReceiver());

        msg.setSubject(subject);
        msg.setText(content.getMessage());

        javaMailSender.send(msg);
        return content;
	}
}
