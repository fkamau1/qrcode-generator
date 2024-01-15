# QR Code Generator

This is a QR code generator application implemented using Spring Boot and the ZXing API library. The application provides a RESTful API endpoint to generate QR codes based on user input.

## Getting Started
### Prerequisites

   - Java Development Kit (JDK) 17 or higher
   - Gradle

### Installation

1. Clone the repository

   ```bash
   git clone https://github.com/yourusername/qrcode-generator.git
   ```

2. Navigate to the project directory:

   ```bash
   cd qrcode-generator
   ```

3. Build the project

   ```bash
   gradle build
   ```

4. Run the application:

   ```bash
   java -jar build/libs/qrcode-generator-0.0.1-SNAPSHOT.jar
   ```
The application will start, and you can access it at ```http://localhost:8080```

## API Endpoints
### Health Check
Description: Check the health status of the application.
  - Endpoint: /api/health
  - Method: GET  
  - Response:

    ```
    OK
    ```
### Generate QR Code
Description: Generate a QR code based on the provided parameters.
   - Endpoint: /api/qrcode
   - Method: GET
   - Parameters:
       - contents (required): The data to be encoded in the QR code.
       - size (optional): The size of the QR code (default: 250 pixels).
       - correction (optional): Error correction level (L, M, Q, H - default: L).
       - type (optional): Image type (png, jpeg, gif - default: png).      
   - Response: QR code image in the specified format.

     ```
      http://localhost:8181/api/qrcode?contents=https://www.google.com/&size=250&type=png
     ```
## Error Handling
   - The API provides informative error messages for invalid requests.

## Configuration
   - The default configuration can be modified in the ```application.properties``` file.

## Dependencies
  - [Spring boot](https://spring.io/projects/spring-boot/) 
  - [ZXing Api](https://github.com/zxing/zxing)

## License 
  - This project is licensed under the MIT License - see the [LICENSE](https://github.com/fkamau1/qrcode-generator/blob/main/LICENSE) file for details.

