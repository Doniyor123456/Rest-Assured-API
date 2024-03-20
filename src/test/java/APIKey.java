import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class APIKey {
    @Test
    public void weatherAPI(){
  Response response=  given()
                .param("key","17a50e40ad8b422891003444241503")
                .param("q","Buffalo")
                .log().uri()
                .when()
                .get("http://api.weatherapi.com/v1/current.json")
                .then()
                .log().body()
            .extract().response();
  
  double tempC=response.jsonPath().getDouble("current.temp_c");
  double tempF=response.jsonPath().getDouble("current.temp_f");

        System.out.println("tempF = " + tempF);
        System.out.println("tempC = " + tempC);
  
    

    }
}
