import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.sql.rowset.serial.SerialBlob;

/**
 * Group 8 - Phase III
 * This program is meant to be run after the phaseTwoDB has been created by the sql script. 
 * This code will add 10k records to all tables except CDC_Departments and Means_of_Transmission, to reduce visual clutter.
 */

public class PhaseThree {

	public static void main(String args[]) {

		
		String[] departmentList = new String[] {"Office of Infectious Diseases","Office of Minority Health", "Office of Noncommunicable Diseases", "Office of Public Health Preparedness",
			"Office of Public Health Science Services",	"Office of Mental Health and Disorders", "Office of Made-Up Diseases", "Office of Victims of Random Names", "The Department",
			"Another Department", "One More Department", "Last Department"};
		
		String[] meansList = new String[] {"air", "food", "water", "birds", "mosquitoes", "ticks", "none"}; 
		
		String[] tempDrugList = new String[1000];
		
		String[] tempDiseaseList = new String[1000];
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		try {	
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("Please enter your database username");
			
			String username = scanner.nextLine();
			
			System.out.println("\nPlease enter your database password");
			
			String password = scanner.nextLine();
			
			scanner.close();
			
			System.out.println("Connecting database...");
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:mysql://localhost:3306/phasethreedb";
			String connectionUser = username;
			String connectionPassword = password; 
			Connection conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);
			System.out.println("Database connected!");
			
			PreparedStatement preparedStmt = null;
			
			Random rand = new Random();
			
			boolean testing = false;
			
			if (testing) {
				
				String query = "SELECT location.country, location.state, location.city, COUNT(outbreak.location) as number_of_outbreaks " +
						"FROM location JOIN outbreak ON location.id = outbreak.location " +
						"WHERE outbreak.casualties > 500 " +
						"GROUP BY location.id ORDER BY Number_of_Outbreaks desc";
				
				preparedStmt = conn.prepareStatement(query);
				
				Date start = new Date();
				preparedStmt.executeQuery();
				Date finish = new Date();
				
				System.out.println("Time taken: " + (finish.getTime() - start.getTime()));
				
				query = "SELECT disease.name as disease, count(distinct(tableA.id)) as number_locations_affected " +
						"FROM disease JOIN " +
						"(SELECT location.id, outbreak.disease, outbreak.mortality_rate FROM location JOIN outbreak ON location.id = outbreak.location) as tableA " +
						"ON disease.name = tableA.disease WHERE cause LIKE '%a%%%a%d%a%' GROUP BY disease.name ORDER BY number_locations_affected desc";
				
				preparedStmt = conn.prepareStatement(query);
				
				start = new Date();
				preparedStmt.executeQuery();
				finish = new Date();
				
				System.out.println("Time taken: " + (finish.getTime() - start.getTime()));
				
				query = "SELECT o.id, o.disease, o.mortality_rate, o.start, o.end, o.affected, o.casualties, o.location " +
						"FROM location l, outbreak o, disease d " +
						"WHERE l.id = o.location AND o.disease = d.name AND o.mortality_rate > d.mortality_rate AND o.start > '1950-01-01' ORDER BY d.name";
						
				preparedStmt = conn.prepareStatement(query);
						
				start = new Date();
				preparedStmt.executeQuery();
				finish = new Date();
				
				System.out.println("Time taken: " + (finish.getTime() - start.getTime()));
						
				
				query = "SELECT name FROM Disease AS d WHERE NOT EXISTS " +
						"( SELECT country FROM Location AS l" +
						"JOIN Outbreak AS o ON l.id = o.location " +
						"JOIN Disease AS d ON o.disease = d.name " +
						"WHERE l.country NOT IN (‘United States’) )";
			
				
			} else {
				System.out.println("Populating tables...");
			    for (int i = 0; i < 10000; i++) {
			    	
//Research_team//
			    	
			    	String query = "INSERT INTO research_team (name) " + 
							"VALUES (?)";
			    	
			    	preparedStmt = conn.prepareStatement(query);
					preparedStmt.setString(1, "team".concat(Integer.toString(i+1)));
					
					preparedStmt.executeUpdate();

			    	
//Drug//
			    	String drug = "";
			    	for (int j = 0; j < 5 + rand.nextInt(6); j++) {
			    		drug += Character.toString( (char) (rand.nextInt(26) + 97) );
			    	}
			    	
			    	if (i < tempDrugList.length) {
			    		tempDrugList[i] = drug;
			    	} else {
			    		tempDrugList[rand.nextInt(tempDrugList.length)] = drug;
			    	}
			    	
			    	query = "INSERT INTO drug (name) " + 
			    			"VALUES (?)";

			    	preparedStmt = conn.prepareStatement(query);
			    	preparedStmt.setString(1, drug);
			    	
					preparedStmt.executeUpdate();

//Prescription//
			    	String description = "";
			    	for (int j = 0; j < 10 + rand.nextInt(11); j++) {
			    		description += Character.toString( (char) (rand.nextInt(26) + 97) );
			    	}
			    	
			    	if (i >= tempDrugList.length) {
			    		//randomize drug name if the temp list has been filled, otherwise it carries from drug set-up
			    		drug = tempDrugList[rand.nextInt(tempDrugList.length)];
			    	}
			    	
			    	query = "INSERT INTO prescription (description, drug) " +
			    			"VALUES (?, ?)";
			    			
			    	preparedStmt = conn.prepareStatement(query);
			    	preparedStmt.setBlob(1, new SerialBlob(description.getBytes()));
			    	preparedStmt.setString(2, drug);
			    	
			    	preparedStmt.executeUpdate();
			    	
//Person//
			    	String firstName = "";
			    	String lastName = "";
			    	String phone = "1";
			    	String email = "";
			    	for (int j = 0; j < 5 + rand.nextInt(6); j++) {
			    		firstName += Character.toString((char) (rand.nextInt(26) + 97));
			    		
			    		lastName += Character.toString((char) (rand.nextInt(26) + 97));
			    	
			    		email += Character.toString((char) (rand.nextInt(26) + 97));
			    	}
			    	
			    	email += "@" + (Character.toString((char) (rand.nextInt(26) + 97)));
			    	
			    	
			    	for (int j = 0; j < 10; j++) {
			    		if (rand.nextInt(2) == 1) {
				    		email += Character.toString((char) (rand.nextInt(26) + 97));
			    		}
			    		phone += Integer.toString(rand.nextInt(10));
			    	}
			    	
			    	
			    	query = "INSERT INTO person (name, phone, email, department, research_team) " + 
							"VALUES (?, ?, ?, ?, ?)";
				    
			    	preparedStmt = conn.prepareStatement(query);
			    	preparedStmt.setString(1, firstName.concat(" " + lastName));
			    	preparedStmt.setString(2, phone);
			    	preparedStmt.setString(3, email.concat(".com"));
			    	preparedStmt.setString(4, departmentList[rand.nextInt(departmentList.length)]);
			    	preparedStmt.setInt(5, rand.nextInt(i+1)+1);
			    	
			    	preparedStmt.executeUpdate();
			    	
				    
//Location//
				    String country = "", state = "", city = "";
				    
				    String[] locales = Locale.getISOCountries();
				    int countryID = rand.nextInt(locales.length);
				    country =  new Locale("", locales[countryID]).getDisplayCountry();
				    
				    for (int j = 0; j < 6 + rand.nextInt(6); j++) {
			    		state += Character.toString((char) (rand.nextInt(26) + 97));
			    		
			    		city += Character.toString((char) (rand.nextInt(26) + 97));
				    }
				    
				    query = "INSERT INTO location (country, state, city) " + 
							"VALUES (?, ?, ?)";
				    
				    preparedStmt = conn.prepareStatement(query);
			    	preparedStmt.setString(1, country);
			    	preparedStmt.setString(2, state);
			    	preparedStmt.setString(3, city);
			    	
			    	preparedStmt.executeUpdate();
				  
				    
//Disease//
				    String name = "", cause = "";
				    double mortalityRate;
				    
				    for (int j = 0; j < 10 + rand.nextInt(6); j++) {
				    	name += Character.toString( (char) (rand.nextInt(26) + 97) );
				    	cause += Character.toString( (char) (rand.nextInt(26) + 97) );
				    }
				    
				    mortalityRate = rand.nextDouble();
				    if (mortalityRate > .9) {
				    	mortalityRate = .89;
				    }
				    
				    if (i < tempDiseaseList.length) {
				    	tempDiseaseList[i] = name;
				    } else {
				    	tempDiseaseList[rand.nextInt(tempDiseaseList.length)] = name;
				    }
				    
				    query = "INSERT INTO disease (name, cause, mortality_rate, transmission, department, prescription) " + 
				    		"VALUES (?, ?, ?, ?, ?, ?)";
				    
				    preparedStmt = conn.prepareStatement(query);
			    	preparedStmt.setString(1, name);
			    	preparedStmt.setBlob(2, new SerialBlob(cause.getBytes()));
			    	preparedStmt.setDouble(3, Double.parseDouble(df.format(mortalityRate*100)));
			    	preparedStmt.setString(4, meansList[rand.nextInt(meansList.length)]);
			    	preparedStmt.setString(5, departmentList[rand.nextInt(departmentList.length)]);
			    	preparedStmt.setInt(6, rand.nextInt(i+1)+1);
			    	
			    	preparedStmt.executeUpdate();
				    
				    
//Publication//
				    String yearPublished = "", disease = "";
				    
				    if (i < tempDiseaseList.length) {
				    	disease = tempDiseaseList[i];
				    } else {
				    	disease = tempDiseaseList[rand.nextInt(tempDiseaseList.length)];
				    }
				    
				    if (rand.nextInt(2) == 1) {
				    	yearPublished = "19";
				    	yearPublished += Integer.toString(rand.nextInt(10));
				    	yearPublished += Integer.toString(rand.nextInt(10));
				    } else {
				    	yearPublished = "20";
				    	yearPublished += Integer.toString(rand.nextInt(2));
				    	yearPublished += Integer.toString(rand.nextInt(6));
				    }
			
				    
				    query = "INSERT INTO publication (year_published, disease, research_team) " +
				    		"VALUES (?, ?, ?)";
				    
				    preparedStmt = conn.prepareStatement(query);
			    	preparedStmt.setDate(1, new java.sql.Date(formatter.parse(yearPublished.concat("-01-01")).getTime()));
			    	preparedStmt.setString(2, disease);
			    	preparedStmt.setInt(3, rand.nextInt(i+1)+1);
			    	
			    	preparedStmt.executeUpdate();
				    
//Outbreak//
				    String start = "", end = "";
				    
				    if (i > tempDiseaseList.length) {
				    	//randomize disease name if the temp list has been filled, otherwise it carries from drug set-up
				    	disease = tempDiseaseList[rand.nextInt(tempDiseaseList.length)];
				    }
				    
				    //
				    if (rand.nextInt(2) == 1) {
				    	start = "19";
				    	end = "19";
				    	
				    	String tens = Integer.toString(rand.nextInt(10));
				    	String ones = Integer.toString(rand.nextInt(10));
				    	
				    	start += tens; start += ones;
				    	if (rand.nextInt(2) == 1) {
				    		end += (Integer.parseInt(tens) + rand.nextInt(9 - (Integer.parseInt(tens)-1))); //
				    		end += Integer.toString(rand.nextInt(10));
				    	} else {
				    		end += tens;
				    		end += (Integer.parseInt(ones) + rand.nextInt(9 - (Integer.parseInt(ones)-1)));
				    	}
				    	
				    } else {
				    	start = "20";
				    	end = "20";
				    	String tens = Integer.toString(rand.nextInt(2));
				    	String ones = Integer.toString(rand.nextInt(6));
				    	
				    	start += tens; start += ones;
				    	if (rand.nextInt(2) == 1) {
				    		end += (Integer.parseInt(tens) + rand.nextInt(9 - Integer.parseInt(tens)));
				    		end += Integer.toString(rand.nextInt(10));
				    	} else {
				    		end += tens;
				    		end += (Integer.parseInt(ones) + rand.nextInt(9 - Integer.parseInt(ones)));
				    	}
				    }
				    
				    start += "-";
				    end += "-";
				    
			    	String s = Integer.toString(rand.nextInt(2));
			    	if (s.equals("0")) {
			    		start += s;
			    		start += Integer.toString(rand.nextInt(10));
			    	} else {
			    		start += s;
			    		start += Integer.toString(rand.nextInt(3));
			    	}
			    	
			    	s = Integer.toString(rand.nextInt(2));
			    	if (s.equals("0")) {
			    		end += s;
			    		end += Integer.toString(rand.nextInt(10));
			    	} else {
			    		end += s;
			    		end += Integer.toString(rand.nextInt(3));
			    	}
			    	
			    	start += "-";
				    end += "-";
				    
				    s = Integer.toString(rand.nextInt(4));
				    if (s.equals("0") | s.equals("1") | s.equals("2")) {
				    	start += s;
				    	start += Integer.toString(rand.nextInt(10));
				    } else {
				    	start += s;
				    	start += Integer.toString(rand.nextInt(2));
				    }
				    
				    s = Integer.toString(rand.nextInt(4));
				    if (s.equals("0") | s.equals("1") | s.equals("2")) {
				    	end += s;
				    	end += Integer.toString(rand.nextInt(10));
				    } else {
				    	end += s;
				    	end += Integer.toString(rand.nextInt(2));
				    }
				    //
				    
				    mortalityRate = rand.nextDouble();
				    if (mortalityRate > .9) {
				    	mortalityRate = .89;
				    }
				    
				    query = "INSERT INTO outbreak (disease, start, end, mortality_rate, affected, casualties, location)" +
				    		"VALUES (?, ?, ?, ?, ?, ?, ?)";

				    preparedStmt = conn.prepareStatement(query);
				    preparedStmt.setString(1, disease);
			    	preparedStmt.setDate(2, new java.sql.Date(formatter.parse(start).getTime()));
			    	preparedStmt.setDate(3, new java.sql.Date(formatter.parse(end).getTime()));
			    	preparedStmt.setDouble(4, Double.parseDouble(df.format(mortalityRate*100)));
			    	preparedStmt.setInt(5, rand.nextInt(1000000));
			    	preparedStmt.setInt(6, rand.nextInt(50000));
			    	preparedStmt.setInt(7, rand.nextInt(i+1)+1);
			    	
			    	preparedStmt.executeUpdate();
			    }
			}
		  
		} catch (Exception e) {
		  	System.out.println(e.getClass().toString() + " " + e.getMessage());
		  	e.printStackTrace();
		}
		
		System.out.println("Finished!");
		
	}
	
}