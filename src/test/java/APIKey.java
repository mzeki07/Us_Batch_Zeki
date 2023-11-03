import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class APIKey {

    /**
     *     Use https://www.weatherapi.com/docs/ as a reference.
     *     First, You need to signup to weatherapi.com, and then you can find your API key under your account
     *     after that, you can use Java to request: http://api.weatherapi.com/v1/current.json?key=[YOUR-APIKEY]&q=Indianapolis&aqi=no
     *     Parse the json and print the current temperature in F and C.
     *
     **/

    @Test
    void weatherAPI(){
       Response response = given()
                .param("key","d771bc00f12e44c283411855230111")
                .param("q","Pittsburgh")
                .param("aqi","no")
                .log().uri()
                .when()
                .get(" http://api.weatherapi.com/v1/current.json")
                .then()
                .log().body()
                .extract().response();

        System.out.println("response.jsonPath().getDouble(\"current.temp_c\") = " + response.jsonPath().getDouble("current.temp_c"));
        System.out.println("response.jsonPath().getDouble(\"current.temp_f\") = " + response.jsonPath().getDouble("current.temp_f"));
    }

    // url: https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user
    // key: x-api-key
    // value: GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra

    @Test
    void interviewQuestion(){
        given()
                .header("x-api-key","GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")
                .when()
                .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")
                .then()
                .log().body()
                .statusCode(200);

    }
}
