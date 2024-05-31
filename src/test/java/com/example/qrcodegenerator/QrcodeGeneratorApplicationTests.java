package com.example.qrcodegenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class QrcodeGeneratorApplicationTests {

    private static WebDriver driver;

    @BeforeAll
    static void setup() {
        // Set the path to the chromedriver executable
        String driverPath = Paths.get("src", "main", "resources", "chromedriver.exe").toAbsolutePath().toString();
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();

        // Set up RestAssured base URI
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void testQRCodeEndpoint() throws IOException {
        String contents = "Hello, World!";
        int size = 250;
        String correction = "L";
        String type = "png";

        // Test the /api/qrcode endpoint
        Response response = given()
                .queryParam("contents", contents)
                .queryParam("size", size)
                .queryParam("correction", correction)
                .queryParam("type", type)
                .when()
                .get("/api/qrcode")
                .then()
                .statusCode(200)
                .contentType("image/png")
                .extract()
                .response();

        // Check that the response body is not empty
        byte[] imageBytes = response.getBody().asByteArray();
        assertThat(imageBytes.length, greaterThan(0));

        // Verify that the image can be read as a BufferedImage
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        assertThat(bufferedImage, notNullValue());
        assertThat(bufferedImage.getWidth(), equalTo(size));
        assertThat(bufferedImage.getHeight(), equalTo(size));
    }

    @Test
    void testQRCodeInvalidParameters() {
        // Test invalid size parameter
        Response response = given()
                .queryParam("contents", "Test")
                .queryParam("size", 100)
                .queryParam("correction", "L")
                .queryParam("type", "png")
                .when()
                .get("/api/qrcode")
                .then()
                .statusCode(400)
                .extract()
                .response();

        String errorMessage = response.getBody().asString();
        assertThat(errorMessage, containsString("Image size must be between 150 and 350 pixels"));

        // Test invalid correction parameter
        response = given()
                .queryParam("contents", "Test")
                .queryParam("size", 250)
                .queryParam("correction", "X")
                .queryParam("type", "png")
                .when()
                .get("/api/qrcode")
                .then()
                .statusCode(400)
                .extract()
                .response();

        errorMessage = response.getBody().asString();
        assertThat(errorMessage, containsString("Permitted error correction levels are L, M, Q, H"));

        // Test invalid type parameter
        response = given()
                .queryParam("contents", "Test")
                .queryParam("size", 250)
                .queryParam("correction", "L")
                .queryParam("type", "bmp")
                .when()
                .get("/api/qrcode")
                .then()
                .statusCode(400)
                .extract()
                .response();

        errorMessage = response.getBody().asString();
        assertThat(errorMessage, containsString("Only png, jpeg and gif image types are supported"));
    }

    @Test
    void testHealthCheckEndpoint() throws InterruptedException{
        // Test the /api/health endpoint using Selenium
        driver.get("http://localhost:8080/api/health");

        // Define an explicit wait with a timeout of 10 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement body = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        assertThat(body.getText(), equalTo("OK"));

        // Delay for observation (5 seconds)
        Thread.sleep(5000);
    }

    // Cleanup after tests
    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}