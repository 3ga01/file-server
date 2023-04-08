# File Server App with Spring Boot

This is a file server application built with Spring Boot that allows users to view, download, and preview files from a remote server. The app provides a secure, scalable, and efficient way to share files with other users.

## Features

    File preview: Users can preview files on the server using a simple web interface. The app supports multiple file formats, including images, videos, audio files, and documents.

    File Download: Users can download files from the server using a secure link. The app generates a unique download link for each file, which expires after a specified time period.

    User Authentication: The app provides a secure login mechanism that verifies user credentials against a database. Only authenticated users can access the file server.

    Access Control: The app provides role-based access control, allowing administrators to restrict user access to specific files and folders.

## Technologies Used

    Java 8
    Spring Boot 2.5.0
    MySQL 8.0.23
    Thymeleaf 2.5.11
    Bootstrap
    Spring-Boot-devtools
    Apache Commons FileUpload
    mysql-connector-java
    jakarta.platform
    spring-data-jpa
    spring-boot-maven-plugin
    javax.mail
	html
	css

## Getting Started

To run the app locally, follow these steps:

    Clone the repository to your local machine using git clone https://github.com/3ga01/fileserver.git.
    Install Java 8 and MySQL 8.0.23.
    Create a new MySQL database and update the application properties file (src/main/resources/application.properties) with your database credentials.
    Build the app using Maven: mvn clean install.
    Run the app using the command: mvn spring-boot:run
    Open your web browser and navigate to http://localhost:8080 to access the file server.


    This project was inspired by the Spring Boot File Upload and Download .
    
![Screenshot from 2023-04-08 01-36-46](https://user-images.githubusercontent.com/107252455/230697800-40c65f9a-9dfe-4eb7-b8e5-6b3f77c23a5d.png)

![Screenshot from 2023-04-08 01-46-07](https://user-images.githubusercontent.com/107252455/230698057-86d8a8ae-040e-49e4-91c0-9c32f83cf066.png)

![Screenshot from 2023-04-08 01-53-29](https://user-images.githubusercontent.com/107252455/230698163-4b71716b-3f64-4fc3-972e-8bc3b1637b47.png)

![Screenshot from 2023-04-08 01-56-36](https://user-images.githubusercontent.com/107252455/230698304-942d3d33-9fb7-4673-aa2b-76f6b00c8eb6.png)

![Screenshot from 2023-04-08 01-51-02](https://user-images.githubusercontent.com/107252455/230698106-00cd7a98-69a3-453c-8f44-11f841d88d4d.png)



