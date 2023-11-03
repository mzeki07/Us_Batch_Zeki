import POJOClasses.TODO;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Practice {
    /*
     * Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Convert Into POJO
     */
    @Test
    void test(){
       TODO todo = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
//                .log().body()
                .extract().as(TODO.class);
        System.out.println("todo = " + todo);

        System.out.println("todo.getTitle() = " + todo.getTitle());
        System.out.println("todo.getUserId() = " + todo.getUserId());
        System.out.println("todo.isCompleted() = " + todo.isCompleted());
        System.out.println("todo.getId() = " + todo.getId());
       }




    /**
     * Task 2
     * create a get request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    void test3(){
//        given()
//                .when()
//                .get("https://jsonplaceholder.typicode.com/todos/2")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().body()
//                .body("title",equalTo("quis ut nam facilis et officia qui"));

       String title = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .extract().path("title");

        Assert.assertEquals(title,"quis ut nam facilis et officia qui");
    }


    /**
     * Task 3
     * create a get request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */

    @Test
    void test4() {
//        given()
//                .when()
//                .get("https://jsonplaceholder.typicode.com/todos/2")
//                .then()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().body()
//                .body("completed",equalTo(false));
       Boolean completed = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .extract().path("completed");

       Assert.assertFalse(completed);
    }

}
