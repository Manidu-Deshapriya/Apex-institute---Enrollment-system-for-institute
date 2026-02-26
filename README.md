========================================================================
             APEX INSTITUTE STUDENT ENROLLMENT SYSTEM
========================================================================

Authors: Manidu Deshapriya and Rashmitha Nimsara
Project Type: Java Swing Desktop Application
Data Structure: Custom Doubly Linked List

------------------------------------------------------------------------
1. PROJECT OVERVIEW
------------------------------------------------------------------------
This is a Student Enrollment Management System developed for Apex Institute.
It uses a custom-built Doubly Linked List to manage student records (ID,
Name, NIC, Course, GPA, District, etc.) with persistent storage in a 
flat-text file database.

------------------------------------------------------------------------
2. PREREQUISITES
------------------------------------------------------------------------
Before running the application, ensure you have:
- Java Development Kit (JDK) 8 or higher installed.
- A Java IDE such as IntelliJ IDEA, NetBeans, or Eclipse.
- The project files must remain in their current folder structure.

------------------------------------------------------------------------
3. STEP-BY-STEP EXECUTION INSTRUCTIONS
------------------------------------------------------------------------

Follow these steps to run the application correctly:

STEP 1: Open the Project
- Open your IDE (e.g., IntelliJ or NetBeans).
- Select "Open Project" and navigate to the "Apex institute" folder.

STEP 2: Configure the Workspace
- Ensure the 'src' folder is marked as the "Sources Root".
- Check that the 'EnrollmentRecords.txt' file is located in the root 
  project directory (outside the src folder).

STEP 3: Compile the Code
- Build or Compile the project (Click the Build icon or press Ctrl+F9).
- Ensure there are no compilation errors.

STEP 4: Run the Main File
- Navigate to the 'src' folder in your project explorer.
- Locate the file named 'Main.java'.
- Right-click 'Main.java' and select "Run 'Main.main()'".

------------------------------------------------------------------------
4. IMPORTANT USAGE NOTES
------------------------------------------------------------------------
- FILE ACCESS ERROR: If you see an error saying "The process cannot access 
  the file because it is being used by another process," make sure you do 
  NOT have 'EnrollmentRecords.txt' open in Notepad or Excel while the 
  program is running.
- DATA SAVING: The system saves data automatically when you add,Update, or
  sort records.
- SEARCHING: Use the search bar to find students by ID, Name, or Course.

------------------------------------------------------------------------
5. FILE STRUCTURE
------------------------------------------------------------------------
- src/Main.java             : The entry point of the application.
- src/user/EnrollmentUI.java : The Graphical User Interface (GUI).
- src/data/enrollDB.java    : The custom Doubly Linked List logic.
- src/business/Enrollment.java: The student data model class.
- EnrollmentRecords.txt     : The text database where records are saved.

------------------------------------------------------------------------
                          END OF README
------------------------------------------------------------------------
