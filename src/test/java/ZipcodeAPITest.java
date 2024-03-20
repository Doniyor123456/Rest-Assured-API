import POJOClasses.Location;
import POJOClasses.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ZipcodeAPITest {
    @Test
    public void test1(){
        given()
                .when()
                .then();
    }

    @Test
    public void test2(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }
    @Test
    public void contentTypeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON);
    }
    @Test
    public void countryInformationTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country",equalTo("United States"));
    }
    @Test
    public void stateInformationTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state",equalTo("California"));
    }

    @Test
    public void stateAbbreviationTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'state abbreviation'",equalTo("CA"));
    }
    @Test
    public void test4(){

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body();

    }
    @Test
    public void test5(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'",hasItem("Büyükdikili Köyü"));
    }
    @Test
    public void test6() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'", hasSize(71));
    }
    @Test
    public void test7() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("places.'place name'", hasSize(71))
                .body("places.'place name'",hasItem("Büyükdikili Köyü"))
                .body("country",equalTo("Turkey"));
    }
    @Test
    public void parametresTest(){
        String countryCode="us";
        String zipCode="90210";

        given()

                .pathParam("country",countryCode)
                .pathParam("zip",zipCode)
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{country}/{zip}")
                .then()
                .statusCode(200);

    }
    @Test
    public void test8() {
        for (int i = 90210; i <= 90213; i++) {
            given()

                    .pathParam("zip", i)

                    .when()
                    .get("http://api.zippopotam.us/us/{zip}")
                    .then()
                    .log().body()
                    .body("places",hasSize(1));

        }
    }
    @Test
    public void test9(){

        given()

                .param("page",2)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .statusCode(200);



    }
    @Test
    public void test10(){
        for(int i=1;i<=10;i++) {
            given()

                    .param("page", i)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .statusCode(200)
            .body("meta.pagination.page",equalTo(i));
        }
    }
    @Test(dataProvider = "parameters")
    public void queryParametresNameWithDataProvider(int pageNumber,String apiName, String version){
        given()

                .param("page",pageNumber)
                .pathParam("apiName",apiName)
                .pathParam("version",version)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/{version}/{apiName}")
                .then()
                .log().body()
                .statusCode(200)
        .body("meta.pagination.page",equalTo(pageNumber));
    }
    @DataProvider
    public Object[][]parameters(){
        Object[][]parameterList = {
                {1,"users","v1"},
                {2,"users","v1"},
                {3,"users","v1"},
                {4,"users","v1"},
                {5,"users","v1"},
                {6,"users","v1"},
                {7,"users","v1"},
                {8,"users","v1"},
                {9,"users","v1"},
                {10,"users","v1"}
        };
        return parameterList;
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setup(){
        baseURI="https://gorest.co.in/public";
        requestSpecification=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .addPathParam("apiName","users")
                .addPathParam("version","v1")
                .addParam("page",3)
                .build();

        responseSpecification=new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("meta.pagination.page",equalTo(3))
                .build();

    }
    @Test
    public void baseURITest(){
        given()
                .param("page",3)
                .log().uri()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test
    public void specificationTest(){
        given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification);

    }
    @Test
    public void test11(){
      String placeName=  given()

                .pathParam("country","us")
                .pathParam("zip","90210")

                .when()
                .get("http://api.zippopotam.us/{country}/{zip}")
                .then()
                .log().body()
                .extract().path("places[0].'place name'");

        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractIntTest(){
      int pageNumber=  given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("meta.pagination.page");
        System.out.println("pageNumber = " + pageNumber);
    }
    @Test
    public void extractListTest1(){
      List<String> nameList = given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("data.name");
        System.out.println("nameList = " + nameList.size());
        System.out.println("nameList = " + nameList.get(4));

    }
    @Test
    public void extractListTest2() {
        List<String> nameList = given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("data.email");
        System.out.println("nameList = " + nameList);
        Assert.assertTrue(nameList.contains("patel_atreyee_jr@gottlieb.test"));
    }
    @Test
    public void extractListTest3() {
        String nameList = given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().path("meta.pagination.links.next");
        System.out.println("nameList = " + nameList);
        Assert.assertTrue(nameList.contains("page=4"));
    }
    @Test
    public void extractRepsonseTest(){
      Response response= given()
                .spec(requestSpecification)
                .when()
                .get("/{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().response();
        //The entire request returns the entire response as Response object
        //By using this object we are able to extract multiple values with one request
        
        int page=response.path("meta.pagination.page");
        System.out.println("page = " + page);
        
        String currentUrl=response.path("meta.pagination.links.current");
        System.out.println("currentUrl = " + currentUrl);
        
        String name=response.path("data[1].name");
        System.out.println("name = " + name);
        
        List<String >emailList=response.path("data.email");
        System.out.println("emailList = " + emailList);

    }
    //POJO-->Plain Old Java Object
    @Test
    public void extactJSONPOJO(){
     Location location=   given()

                .pathParam("country","us")
                .pathParam("zip","90210")

                .when()
                .get("http://api.zippopotam.us/{country}/{zip}")
                .then()
                .log().body()
                .extract().as(Location.class);
        System.out.println("location.getPostCode() = " + location.getPostCode());
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getPlaces() = " + location.getPlaces().get(0).getPlaceName());
        System.out.println("location.getPlaces().get(0).getState() = " + location.getPlaces().get(0).getState());
     

    }
    @Test
    public void extractWithJson(){
      User user=  given()
                .spec(requestSpecification)
                .when()
                .get("{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().jsonPath().getObject("data[0]", User.class);

        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.getEmail() = " + user.getEmail());
        System.out.println("user.getName() = " + user.getName());
        System.out.println("user.getGender() = " + user.getGender());
        System.out.println("user.getStatus() = " + user.getStatus());
    }
    @Test
    public void extractWithJson2() {
        String name = given()
                .spec(requestSpecification)
                .when()
                .get("{version}/{apiName}")
                .then()
                .spec(responseSpecification)
                .extract().jsonPath().getString("data[1].name");
        System.out.println("name = " + name);

    }
    @Test
    public void extractWithJson3() {
       Response response = given()
                .spec(requestSpecification)
                .when()
                .get("{version}/{apiName}")
                .then()
                .spec(responseSpecification)
               .extract().response();
                int page=response.jsonPath().getInt("meta.pagination.page");
        System.out.println("page = " + page);
        String currentLink=response.jsonPath().getString("meta.pagination.links.current");
        System.out.println("currentLink = " + currentLink);

        User user=response.jsonPath().getObject("data[2]", User.class);
        System.out.println("user.getName() = " + user.getName());

        List<User>userList= response.jsonPath().getList("data", User.class);
        System.out.println("userList.size() = " + userList.size());




    }
    

}
