# File Server App with Spring Boot

This is a file server application built with Spring Boot that allows users to view, download, preview and send files as emails from a remote server. The app provides a secure, scalable, and efficient way to share files with other users.

## Features

File preview: Users can preview files on the server using a simple web interface. The app supports multiple file formats, including images, videos, audio files, and documents.

File Download: Users can download files from the server by clicking on the download link. 

Send Emails: Users can send files by adding recepient email address.

Upload Files: Admins can upload files with the file name and description

View Files: Admins can view download and sent email count.

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

1. Clone the repository to your local machine using git clone https://github.com/3ga01/fileserver.git.
2. Install Java 8 and MySQL 8.0.23.
3. Create a new MySQL database and update the application properties file (src/main/resources/application.properties) with your database credentials.
4. Build the app using Maven: mvn clean install.
5. Run the app using the command: mvn spring-boot:run
6. Open your web browser and navigate to http://localhost:8080 to access the file server.


This project was inspired by the Spring Boot File Upload and Download .
    
![Screenshot from 2023-04-08 01-36-46](https://user-images.githubusercontent.com/107252455/230697800-40c65f9a-9dfe-4eb7-b8e5-6b3f77c23a5d.png)

![Screenshot from 2023-04-11 22-51-47](https://user-images.githubusercontent.com/107252455/231306096-c6e427a7-cd79-45a5-98c7-5aa48d9d357a.png)

![Screenshot from 2023-04-11 22-52-08](https://user-images.githubusercontent.com/107252455/231306213-33c9c836-7671-421d-99fa-be2200a540f3.png)

![Screenshot from 2023-04-11 22-52-19](https://user-images.githubusercontent.com/107252455/231306263-5e87adda-c504-46c5-affc-76d170882607.png)

![Screenshot from 2023-04-11 22-52-41](https://user-images.githubusercontent.com/107252455/231306317-3672b62b-fbed-44f4-95ce-4c3afbf4dce2.png)

![Screenshot from 2023-04-11 22-54-10](https://user-images.githubusercontent.com/107252455/231306348-bc906436-f739-4367-9c34-181e10c891d3.png)

![Screenshot from 2023-04-12 02-46-36](https://user-images.githubusercontent.com/107252455/231335135-6aed60f5-1d9b-4472-a212-48b4b5b5ba08.png)






