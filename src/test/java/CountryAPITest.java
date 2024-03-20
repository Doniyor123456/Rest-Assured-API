package APITest;

import APITest.POJOClasses.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryAPITest {
    Cookies cookies;
    RequestSpecification rs;

    @BeforeClass
    public void loginTest(){
        baseURI="https://test.mersys.io/school-service/api/countries";
        Map<String, Object> credentials=new HashMap<>();
        credentials.put("username","turkeyts");
        credentials.put("password","TechnoStudy123");
        credentials.put("rememberMe",true);
        cookies= given()
                .body(credentials)
                .contentType(ContentType.JSON)
                .when()
                .post("https://test.mersys.io/auth/login")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }
    @Test(dependsOnMethods = "loginTest")
    public void getListOfCountries(){
        given()
                .body("{}")
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .post("https://test.mersys.io/school-service/api/countries/search")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name",hasItem("Batch 10"));
    }
    public String randomCountry(){
        return RandomStringUtils.randomAlphabetic(8);
    }
    public String randomCountryCode(){
        return RandomStringUtils.randomAlphanumeric(8);
    }
    Country requestCountry;
    Country responseCountry;
    @Test
    public void createCountry(){

        requestCountry=new Country();
        requestCountry.setName(randomCountry());
        requestCountry.setCode(randomCountryCode());
        requestCountry.setTranslateName(new ArrayList<>());
        requestCountry.setId(null);
        requestCountry.setHasState(false);
        responseCountry=   given()
                .body(requestCountry)
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .post()
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .log().body()
                .extract().jsonPath().getObject("", Country.class);

        Assert.assertEquals(requestCountry.getName(),responseCountry.getName());
        Assert.assertEquals(requestCountry.getCode(),responseCountry.getCode());

    }
    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegativeTest(){
//        Country country=new Country();
//        country.setId(null);
//        country.setName(responseCountry.getName());
//        country.setCode(responseCountry.getCode());
//        country.setTranslateName(new ArrayList<>());
//        country.setHasState(false);

        given()
                .body(requestCountry)
//                .body(country)
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createCountry")
    public void getCountryByName(){
        given()
                .body("{\"name\":\"" + responseCountry.getName() + "\"}")
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .when()
                .post("/search")
                .then()
                .statusCode(200);
    }



    @Test(dependsOnMethods = "createCountry")
    public void updateCountry(){
//        responseCountry.setName(randomCountry());
//        responseCountry.setCode(randomCountryCode());
        requestCountry.setId(responseCountry.getId());
        requestCountry.setName(randomCountry());
        requestCountry.setCode(randomCountryCode());
        given()
//                .body(responseCountry)
                .body(requestCountry)
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .when()
                .put()
                .then()
                .statusCode(200)
//                .body("id",equalTo(responseCountry.getId()));
                .body("id",equalTo(requestCountry.getId()));


    }
    @Test
    public void deleteCountry(){
        given()
                .cookies(cookies);
    }
}