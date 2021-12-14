package com.srgc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srgc.model.Student;

@RestController
public class MailController {

	// Method to save student

	@PostMapping("student/save")
	public Student saveStudent(@RequestBody Student student) {

		try {
			String myDriver = "com.mysql.cj.jdbc.Driver";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");

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
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
		}
		return student;
	}

	@GetMapping("student/fetch")
	public ArrayList<Student> fetchStudents() {
		ArrayList<Student> listOfStudents = new ArrayList<>();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from student");

			while (rs.next()) {
				Student student = new Student();
				student.setFirstName(rs.getString(1));
				student.setLastName(rs.getString(2));
				student.setAddress(rs.getString(3));
				student.setUsername(rs.getString(4));
				student.setEmailId(rs.getString(5));

				listOfStudents.add(student);
			}
			System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + " " + rs.getString(4)
					+ " " + rs.getString(5));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return listOfStudents;
	}
}
