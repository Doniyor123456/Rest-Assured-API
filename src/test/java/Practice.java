import POJOClasses.Location;
import POJOClasses.ToDo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class Practice {
    @Test
    public void test1() {
        ToDo toDo = given()


                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(ToDo.class);
        System.out.println("toDo = " + toDo);
    }
    @Test
    public void test2() {
        given()


                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"));



    }
}
