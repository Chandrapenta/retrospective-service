# Retrospective Service

The Retrospective Service is a RESTful web service built using Java with Spring Boot, designed to facilitate retrospective (SCRUM ceremony) management for development teams.

## Key Features:

- **Create New Retrospective:** Create retrospectives with details such as name, summary, date, participants, and feedback items.
- **Add Feedback Items:** Add feedback items to existing retrospectives, including the name of the person providing feedback, body, and type of feedback (positive, negative, idea, praise).
- **Update Feedback Items:** Modify feedback items' body and type for better clarity and accuracy.
- **Search Retrospectives:** Search retrospectives based on date, with pagination support for efficient retrieval.
- **Logging:** Basic logging for debugging and error handling purposes.
- **Unit Testing:** Unit tests ensure the reliability and correctness of the application.

## Usage:

1. **Prerequisites:**
   - Java Development Kit (JDK) 8 or higher installed on your machine.
   - Apache Maven installed on your machine.
   - Git installed on your machine.

2. **Clone the Repository:**
   ```bash
   git clone <repository-url>
3. **Build the Project:**
   ```bash
   cd retrospectiveservice
   mvn clean install
 4. **Run the Application:**
   ```bash
   java -jar target/retrospectiveservice-<version>.jar6. **Access the Endpoints:**

  5. **Create New Retrospective:**
     
     POST /retrospectives
     

   - **Add Feedback Item:**
     
     POST /retrospectives/{retrospectiveName}/feedback
     

   - **Update Feedback Item:**
     
     PUT /retrospectives/{retrospectiveName}/feedback/{feedbackItemId}
    

   - **Search Retrospectives By Date:**
     
     GET /retrospectives/search?date={date}&page={page}&pageSize={pageSize}
     

   - **Get All Retrospectives:**
     
     GET /retrospectives?page={page}&pageSize={pageSize}
     
