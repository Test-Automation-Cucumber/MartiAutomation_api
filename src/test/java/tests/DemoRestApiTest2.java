package tests;

import utilities.TestBase;

import static org.hamcrest.core.IsEqual.equalTo;

import org.testng.Assert;
import org.testng.annotations.Test;

import dbmodel.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;;

public class DemoRestApiTest2 extends TestBase {

	Provider provider;
	
	
	
	 /* 1. Verify that the API starts with an empty store.
	  • At the beginning of a test case, there should be no books stored on the server.*/
	  @Test(priority = 0)
	  public void TestBookCheck() throws Exception {
		  Response response = RestAssured.given()
					.contentType("application/json")
			        .when()
			        .get(this.ENDPOINT)
			        .then()
			        .statusCode(200)
			        .body("size()",equalTo(0))
			        .extract().response();
					System.out.println(response.getBody().asString());
					//başlangıcta hiç bir kayıtlı kitap olmaması durumunda case pass olur.
	  }
	
	
	  /*2. Verify that title and author are required fields.
	  • PUT on /api/books/ should return an error Field '<field_name>' is
	  required.*/
	  @Test(priority = 1)
	  public void VerifyRequiredFields() {

		  Response responseAuthor = RestAssured.given()
					.contentType("application/json")
			        .body("{ \"title\": \"Reliability of late night deployments\" }")
			        .when()
			        .put(this.ENDPOINT)
			        .then()
			        .statusCode(500)
			        .body("error", equalTo("Field 'author' is required."))
			        .extract().response();
					System.out.println(responseAuthor.getBody().asString());
					//author yok
								
		Response responseTitle = RestAssured.given()
					.contentType("application/json")
			        .body("{ \"author\": \"John Smith\" }")
			        .when()
				    .put(this.ENDPOINT)
					.then()
					.statusCode(500)
					.body("error", equalTo("Field 'title' is required."))
					.extract().response();
					System.out.println(responseTitle.getBody().asString());
					//title yok	

	  }
	  	  
	  /*3. Verify that title and author cannot be empty.
	  • PUT on /api/books/ should return an error Field '<field_name>' cannot
	  be empty.*/
	  @Test(priority = 2)
	  public void VerifyEmptyFields() {

		  Response responseAuthor = RestAssured.given()
					.contentType("application/json")
			        .body("{ \"author\": \"\", \"title\": \"\" }")
			        .when()
			        .put(this.ENDPOINT)
			        .then()
			        .statusCode(500)
			        .body("error", equalTo("Field 'title' cannot be empty."))
			        .extract().response();
					System.out.println(responseAuthor.getBody().asString());
					//title bos				
					
		Response responseTitle = RestAssured.given()
					.contentType("application/json")
				    .body("{ \"author\": \"\", \"title\": \"Reliability of late night deployments\" }")
				    .when()
				    .put(this.ENDPOINT)
					.then()
					.statusCode(500)
					.body("error", equalTo("Field 'author' cannot be empty."))
					.extract().response();
					System.out.println(responseTitle.getBody().asString());
					//author yok	

	  }	  
	  
	  /*4. Verify that the id field is read−only.
	  • You shouldn't be able to send it in the PUT request to /api/books/.*/
	  @Test(priority = 3)
	  public void idFieldReadOnly() {

		  Response response = RestAssured.given()
					.contentType("application/json")
			        .body("{ \"id\": 2, \"author\": \"John Smith\", \"title\": \"Reliability of late night deployments\" }")
			        .when()
			        .put(this.ENDPOINT)
			        .then()
			        .statusCode(500)
			        .extract().response();
					System.out.println(response.getBody().asString());
					//id read only	
	  }
	  	  
	  /*5. Verify that you can create a new book via PUT.
	  • The book should be returned in the response.
	  • GET on /api/books/<book_id>/ should return the same book.*/
	  @Test(priority = 4)
	  public void verifyCreateNewBook() {

		  Response createdBookResponse = RestAssured.given()
					.contentType("application/json")
			        .body("{ \"author\": \"John Smith\", \"title\": \"Reliability of late night deployments\" }")
			        .when()
			        .put(this.ENDPOINT)
			        .then()
			        .statusCode(201)
					.extract().response();
					System.out.println(createdBookResponse.getBody().asString());
					//book created
									
					int createdBookId = createdBookResponse.path("id");
					String createdAuthor = createdBookResponse.path("author");
					String createdTitle = createdBookResponse.path("title");
										
	     Response GetResponse = RestAssured.given()
					.contentType("application/json")
					.when()
					.get(this.ENDPOINT + "/" + createdBookId)
					.then()
					.statusCode(200)
					.extract().response();
					System.out.println(GetResponse.getBody().asString());
					//book created	
					
					String ExpectedAuthor = GetResponse.path("author");
					String ExpectedTitle = GetResponse.path("title");
					
					Assert.assertEquals(createdAuthor, ExpectedAuthor);
					Assert.assertEquals(createdTitle, ExpectedTitle);
					//author ve title kontrol
					
	  }
	   
	  /* 6.PUT on /api/books/ should return an error: Another book with similar
title and author already exists.*/
	  @Test(priority = 5)
	  public void smilar() {

		  Response createdBookResponse = RestAssured.given()
					.contentType("application/json")
			        .body("{ \"author\": \"John Smith\", \"title\": \"Reliability of late night deployments\" }")
			        .when()
			        .put(this.ENDPOINT)
			        .then()
			        .statusCode(201)
					.extract().response();
					System.out.println(createdBookResponse.getBody().asString());
					//book created
						
		  Response createdBookResponse2 = RestAssured.given()
					.contentType("application/json")
					.body("{ \"author\": \"John Smith\", \"title\": \"Reliability of late night deployments\" }")
					.when()
					.put(this.ENDPOINT)
					.then()
					.statusCode(500)
					.body("error", equalTo("Another book with similar title and author already exists."))
					.extract().response();
					System.out.println(createdBookResponse2.getBody().asString());
								//book created
	  }
	
}
