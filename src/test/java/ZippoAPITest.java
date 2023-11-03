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

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class ZippoAPITest {

    @Test
    void test1(){
    given()  //preparation (token, request body, parameters, cookies...)


            .when() //for url, request method(get,post,put,patch,delete)

            .then(); // response(response body, tests, extract data from the response...)
    }
    @Test
    void statusCodeTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // prints the response body to the console
                .log().status()
                .statusCode(200);
    }

    @Test
    void contentTypeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON); //tests if the response is in Json format
    }

    @Test
    void countryInformationTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country",equalTo("United States"));
    }

    @Test
    void stateInformationTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state",equalTo("California"));
    }

    @Test
    void stateAbbInformationTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'state abbreviation'",equalTo("CA"));
    }
    // Send a request to "http://api.zippopotam.us/tr/01000"
    // and check if the body has "Büyükdikili Köyü"

    @Test
    void bodyContainsItemTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'",hasItem("Büyükdikili Köyü") );
        // When we don't use index it gets all place names from the response and creates an array with them.
        // hasItem checks if that array contains "Büyükdikili Köyü" value in it
    }


    @Test
    void arrayHasSizeTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'longitude'",hasSize(71) );
        // When we don't use index it gets all place names from the response and creates an array with them.
        // hasItem checks if that array contains "Büyükdikili Köyü" value in it
    }


    @Test
    void arrayHasSizeTest2(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places.'place name'",hasSize(1));

    }


    @Test
    void multipleTest(){
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places",hasSize(71))
                .body("places.'place name'",hasItem("Büyükdikili Köyü"))
                .body("country",equalTo("Turkey"))
                .body("places.state",hasItem("Adana"));
    }

    //Parameters
    //There are 2 types of parameters.
    //      1) Path Parameters  -> http://api.zippopotam.us/tr/01000
    //      2) Query Parameters ->

    @Test
    void pathParametersTest1(){
        String countryCode = "us";
        String zipCode = "90210";

        given()
                .pathParam("countryCode",countryCode)
                .pathParam("zipCode",zipCode)
                .log().uri() // prints the requested url
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .statusCode(200)
                ;
    }


    // send a get request for zipcodes between 90210 and 90213 and verify that in all responses the size
    // of the places array is 1
    @Test
    void pathParametersTest2(){
        String countryCode = "us";
        for (int i = 90210; i <90214 ; i++) {

            given()
                    .pathParam("countryCode",countryCode)
                    .pathParam("zipCode",i)
                    .log().uri() // prints the requested url
                    .when()
                    .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places",hasSize(1))
                    .body("'post code'",equalTo(String.valueOf(i)));
        }
    }


    @Test
    void queryParametersTest(){
        given()
                .param("page",3)
                .pathParam("APIName","users")
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200);
    }

    // send the same request for the pages between 1-10 and check if
    // the page number we send from request and page number we get from response are the same
    @Test
    void queryParametersTest2(){
        int object = 1;
        for (int i = 1; i <=10 ; i++) {
            given()
                    .param("page",i)
                    .pathParam("APIName","users")
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/{APIName}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(i));
        }
    }

    // Write the same test with Data Provider
    @Test(dataProvider = "parameters")
    void queryParametersTest3(int pageNumber, String apiName){

            given()
                    .param("page",pageNumber)
                    .pathParam("APIName",apiName)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/{APIName}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(pageNumber));
    }

    @DataProvider
    public Object[][] parameters(){
        Object[][] parameters = {
                {1,"users"},
                {2,"users"},
                {3,"users"},
                {4,"users"},
                {5,"users"},
                {6,"users"},
                {7,"users"},
                {8,"users"},
                {9,"users"},
                {10,"users"},
        };
        return parameters;
    }
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp(){

//        baseURI = "https://gorest.co.in/public/v1";
//        // if the request url in the request method doesn't have http part
//        // rest assured puts baseURI to the beginning of the url in the request method
//
//        requestSpecification1 = new RequestSpecBuilder()
//                .log(LogDetail.URI)
//                .log(LogDetail.BODY)
//                .addPathParam("APIName","users")
//                .addParam("page",3)
//                .setContentType(ContentType.JSON)
//                .build();
//
//        responseSpecification1 = new ResponseSpecBuilder()
//                .log(LogDetail.BODY)
//                .expectStatusCode(200)
//                .expectContentType(ContentType.JSON)
//                .build();

        baseURI = "https://gorest.co.in/public/v1";
        // if the request url in the request method doesn't have http part
        // rest assured puts baseURI to the beginning of the url in the request method

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .log(LogDetail.BODY)
                .addPathParam("APIName", "users")
                .addParam("page",3)
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

    }

    @Test
    void baseURITest(){

        given()
                .param("page",3)
                .pathParam("APIName","users")
                .log().uri()
                .when()
                .get("/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page",equalTo(3));
    }


    @Test
    void requestAndResponseSpecTest(){

        given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("meta.pagination.page",equalTo(3));
    }

    @Test
    void extractStringTest(){

       String placeName = given()
                .pathParam("countryCode","us")
                .pathParam("zipCode","90210")
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("places[0].'place name'");

        System.out.println("place name is "+ placeName);
    }

    @Test
    void extractIntegerValueTest(){
       int page = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("meta.pagination.page",equalTo(3))
                .extract().path("meta.pagination.page");

        System.out.println("page = " + page);
    }


    @Test
    void extractListTest(){
        List<Integer> listOfIDs = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .body("data.id",hasSize(10))
                .extract().path("data.id");

        System.out.println("listOfIDs.size() = " + listOfIDs.size());
        System.out.println("listOfIDs.get(3) = " + listOfIDs.get(3));
        System.out.println("listOfIDs.contains(5507746) = " + listOfIDs.contains(5507746));
        System.out.println("listOfIDs = " + listOfIDs);

        Assert.assertTrue(listOfIDs.contains(5507746));
    }

    @Test
    void extractTest2(){
        List<String> name = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().path("data.name");

        System.out.println("name = " + name);

        for (String names: name) {
            System.out.println("name "+names);
        }
    }


    @Test
    void extractResponse(){
       Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().response();
        // returns the entire response and assigns it to a Response object.
        // By using this object we are able to reach any part of the response

        // extract.path           vs                    extract.response
        // extract.path() can only give us one part of the response. If you need different values from different parts of the response (names and page)
        // you need to write two different request.
        // extract.response() gives us the entire response as an object so if you need different values from different parts of the response (names and page)
        // you can get them with only one request

        System.out.println("response.path(\"meta.pagination.page\") = " + response.path("meta.pagination.page"));
        System.out.println("response.path.links.next = " + response.path("meta.pagination.links.next"));

        int page = response.path("meta.pagination.page");
        System.out.println("page = " + page);

        String nexturl = response.path("meta.pagination.links.next");
        System.out.println("nexturl = " + nexturl);

        String secondName = response.path("data[1].name");
        System.out.println("secondName = " + secondName);

        List<String> nameList = response.path("data.name");
        System.out.println("nameList = " + nameList);

    }

    //POJO (Plain Old Java Object)

    @Test
    void extractJsonPOJO(){
       Location location = given()
                .pathParam("countryCode","us")
                .pathParam("zipCode","90210")
                .when()
                .get("http://api.zippopotam.us/{countryCode}/{zipCode}")
                .then()
                .log().body()
                .extract().as(Location.class);
        // This request extracts the entire response and assigns it to Location class as a Location object
        // We cannot extract the body partially (e.g. cannot extract place object separately)
        System.out.println("location.getPostCode() = " + location.getPostCode());
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getCountryAbbreviation() = " + location.getCountryAbbreviation());
        System.out.println("location.getPlaces().get(0) = " + location.getPlaces().get(0));
        System.out.println("location.getPlaces().get(0).getPlaceName = " + location.getPlaces().get(0).getPlaceName());
        System.out.println("location.getPlaces().get(0).getState = " + location.getPlaces().get(0).getState());
    }

    @Test
    void extractWithJsonPath1(){
       User user = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().jsonPath().getObject("data[1]", User.class);

        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.getName() = " + user.getName());
        System.out.println("user.getEmail() = " + user.getEmail());
    }
    // extract.path() ==> we can extract only one value or list of that values
    //                 String name = extract.path(data[0].name)
    //                 List<String> nameList extract.path(data.name)
    //
    // extract.as() ==> We can extract the entire response body. It doesn't let us to extract one part of the body separately.
    //                  So we need to create classes for the entire body.
    //                  extract.as(Location.class)
    //                  extract.as(Place.class) cannot extract like this
    //                  extract.as(User.class) cannot extract like this
    //
    // extract.jsonPath() ==> We can extract the entire body as well as any part of the body. So if we need only one part of the
    //                        body we don't need to create classes for the entire body
    //                        extract.jsonPath().getObject(Location.class)
    //                        extract.jsonPath().getObject(Place.class)
    //                        extract.jsonPath().getObject(User.class)

    @Test
    void extractWithJsonPath2(){
       List<User> usersList = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().jsonPath().getList("data", User.class);

        System.out.println("usersList.size() = " + usersList.size());
        System.out.println("usersList.get(0).getName() = " + usersList.get(0).getName());
        System.out.println("usersList.get(5).getId() = " + usersList.get(5).getId());
    }

    @Test
    void extractWithJsonPath3(){
       Response response = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .spec(responseSpecification)
                .extract().response();

       System.out.println("response.jsonPath().getInt(\"meta.pagination.page\") = " + response.jsonPath().getInt("meta.pagination.page"));
        System.out.println("response.jsonPath().getString(\"data[2].name\") = " + response.jsonPath().getString("data[2].name"));

        List<User> userList =response.jsonPath().getList("data", User.class);
        System.out.println("userList.size() = " + userList.size());
        System.out.println("userList.get(6).getName() = " + userList.get(6).getName());
    }


}
