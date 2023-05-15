# Spring Boot FullStack

This full-stack application is a customer dashboard where users can register, login, upload profile pictures, manage customer information, and access a small section of dashboard insights with customer info charts. The application was built using a variety of technologies and best practices to ensure high quality, maintainability, and scalability. 
<img src="https://i.imgur.com/QGepQZc.png" align="left" width="370px"/>

A strong emphasis has been placed on the DevOps side of the project, ensuring seamless integration, testing, and deployment.

<br clear="left"/>
DEMO: https://www.full-stack-react.awsfernandopresa.com/
## DevOps
One of the main highlights of this project is the significant emphasis on DevOps and continuous integration/continuous deployment (CI/CD) practices:

- <ins>Docker</ins>: Used for containerization of the application, ensuring the application runs uniformly across all environments.
<img src="https://i.imgur.com/9zpGfcw.png" align="right" width="600px"/>

- <ins>Local Testing Environment</ins>: Docker is used to create a local testing environment that matches the production environment as closely as possible. This allows for thorough testing and reduces the chances of environment-specific bugs.

- <ins>Automated Testing</ins>: Both the backend and frontend of the application have a suite of unit and integration tests. These tests are automatically run in GitHub Actions whenever a push is made to the repository, helping to ensure code quality and catch potential issues early.

- <ins>Automatic Deployment</ins>: The application is set up to automatically deploy after any push to the main branch, utilizing AWS Amplify for the frontend and AWS EC2 for the backend. This means any validated changes can be rapidly reflected in the live application.

- <ins>Database Migrations</ins>: Flyway is used to handle database migrations, ensuring that changes to the database schema are properly managed and versioned.
<br clear="right"/>

#### Secure Access and Deployment
- <ins>AWS IAM Policies</ins>: Implemented strict AWS IAM policies to manage access to AWS services and resources securely.

- <ins>SSH Key Usage</ins>: Utilized SSH keys for secure connection to production servers, demonstrating an understanding of best practices in secure server access.

## Technologies Used

With Spring Boot 3 and Spring Boot Security in mind, the application was built using the following technologies:

<img src="https://i.imgur.com/j2CXKrG.png" align="right" width="600px"/>

- <ins>HTTP & API Development</ins>: The application exposes an API that the frontend consumes.

- <ins>Database & PostgreSQL</ins>: Used for efficient data storage and retrieval.

- <ins>Spring Data JPA and JDBC</ins>:Used for handling database operations.

- <ins>Flyway</ins>: For seamless database migrations.

- <ins>Unit Tests and Integration Tests</ins>: Tests have been written to ensure the functionality and integrity of the application.

- <ins>Spring Security 6</ins>: Used for building secure and authenticated applications.

- <ins>JWT</ins>: JWT tokens are used for maintaining user sessions and authentication.

- <ins>AWS S3</ins>: For storing user and website images.

- <ins>Docker</ins>: Used for containerization and deployment.

<br clear="right"/>

- <ins>AWS EC2 and Elastic Beanstalk AWS Amplify</ins>: Used for hosting the application in the cloud.

- <ins>GitHub Actions</ins>: Used for CI/CD, automated testing and deployment.

- <ins>User Registration & Login</ins>: Users can create an account and log in.

## Application Design
The application is divided into several key components:

- <ins>Authentication</ins>: The AuthenticationService class handles the core authentication logic, using JWT for token generation and management.
- <ins>Customer Management</ins>: The CustomerService class is responsible for managing customer information. The customer images are stored in AWS S3.
- <ins>Security</ins>: The SecurityConfig class handles the application's security configuration, ensuring that all endpoints are properly authenticated and CORS policies are respected.

## Project Structure
- <ins>Backend</ins>: The backend is organized into different packages for authentication, customer management, JWT token management, S3 file handling, and security configuration.
- <ins>Frontend</ins>: The frontend is designed with React and JavaScript, following best practices for component structure and state management.

## Conclusion
This project showcases a robust full-stack application built with a variety of technologies and best practices. The application places a strong emphasis on the DevOps side, using automated workflows for testing and deployment with incorporation of best security practices, like AWS IAM policies and SSH keys
