import POJOClasses.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class GoRestUsers {

    public String randomName(){
        return RandomStringUtils.randomAlphabetic(10);
    }
    public String randomEmail(){
        return RandomStringUtils.randomAlphanumeric(7)+ "@techno.com";
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    @BeforeClass
    public void setUp(){
        baseURI="https://gorest.co.in/public/v2/users";

        requestSpecification=new RequestSpecBuilder()
                .addHeader("Authorization","Bearer 542443dcbc3d51d1f56f81f749e5af68cd8ee62f644b88e213f5d39d5a80b29a ")
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification=new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();


    }
    @Test
    public void getUserList(){
        given()
                .when()
                .get()
                .then()
//                .log().body()
                .statusCode(200)
//                .contentType(ContentType.JSON)
                .spec(responseSpecification)
                .body("",hasSize(10));
    }

    @Test
    public void createNewUser(){
        given()
                .header("Authorization","Bearer 542443dcbc3d51d1f56f81f749e5af68cd8ee62f644b88e213f5d39d5a80b29a ")
                .body("{\"name\":\""+randomName()+"\",\"gender\":\"male\",\"email\":\""+randomEmail()+"\",\"status\":\"active\"}")
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON);

    }
    @Test
    public void createNewUserWithMaps(){
        Map<String,String >user=new HashMap<>();
        user.put("name",randomName());
        user.put("gender","male");
        user.put("email",randomEmail());
        user.put("status","active");
        given()
                .spec(requestSpecification)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(201)
                .spec(responseSpecification)
                .body("email",equalTo(user.get("email")))
                .body("name",equalTo(user.get("name")));

    }
    User user;
    User userFromResponse;
    @Test
    public void createNewUserWithObjects(){
         user=new User();
        user.setName(randomName());
        user.setEmail(randomEmail());
        user.setGender("male");
        user.setStatus("active");

     userFromResponse=    given()
                .spec(requestSpecification)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(201)
                .spec(responseSpecification)
                .body("email",equalTo(user.getEmail()))
                .body("name",equalTo(user.getName()))
                .extract().as(User.class);



    }
    @Test(dependsOnMethods = "createNewUserWithObjects")
    public void createUserNegativeTest(){
        User userNegative=new User(randomName(),user.getEmail(),"male","active");
        given()
                .spec(requestSpecification)
                .body(userNegative)
                .when()
                .post()
                .then()
                .statusCode(422)
                .spec(responseSpecification);

    }
    @Test(dependsOnMethods = "createNewUserWithObjects")
    public void getUserById(){
              given()
                      .pathParam("userId",userFromResponse.getId())
                      .spec(requestSpecification)
                      .when()
                      .get("{userId}")
                      .then()
                      .spec(responseSpecification)
                      .body("id",equalTo(userFromResponse.getId()))
                      .body("name",equalTo(userFromResponse.getName()))
                      .body("email",equalTo(userFromResponse.getEmail()));
    }
    @Test(dependsOnMethods = "createNewUserWithObjects")
    public void updateUser(){
        User updateUser=new User(randomName(),randomEmail(),"male","active");
//        userFromResponse.setName(randomName());
//        userFromResponse.setEmail(randomEmail());
        given()
                .spec(requestSpecification)
                .pathParam("userId",userFromResponse.getId())
                .body(userFromResponse)
                .when()
                .put("{userId}")
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .body("id",equalTo(userFromResponse.getId()))
                .body("email",equalTo(updateUser.getEmail()))
                .body("name",equalTo(updateUser.getName()));


    }
    @Test(dependsOnMethods = "createNewUserWithObjects")
    public void deleteUser(){
        given()
                .spec(requestSpecification)
                .pathParam("userId",userFromResponse.getId())
                .when()
                .delete("{userId}")
                .then()
                .statusCode(204);


    }
    @Test(dependsOnMethods = {"createNewUserWithObjects","deleteUser"})
    public void deleteUserNegativeTest(){
        given()
                .spec(requestSpecification)
                .pathParam("userId",userFromResponse.getId())
                .when()
                .delete("{userId}")
                .then()
                .statusCode(404);

    }
    @Test(dependsOnMethods = {"createNewUserWithObjects","deleteUser"})
    public void getUserByIdNegativeTest(){
        given()
                .spec(requestSpecification)
                .pathParam("userId",userFromResponse.getId())
                .when()
                .get("{userId}")
                .then()
                .statusCode(404);

    }
}
