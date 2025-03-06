# Email-Automation-for-Test-Invitations
This project automates the process of checking a Gmail inbox for new test invitations, extracting the test link, and automatically accepting the invitation using Selenium WebDriver.
This project automates the process of checking a Gmail inbox for new test invitations, extracting the test link, and automatically accepting the invitation using Selenium WebDriver.

🚀 Features

Automated Email Checking: Scans for test invitations every 5 minutes.

Filters Recent Emails: Ignores invitations older than 10 minutes.

Extracts Test Links Dynamically: Supports different invitation formats.

Opens the Invitation and Accepts It: Uses Selenium to interact with the webpage.

📌 Prerequisites

Before running the script, ensure you have the following installed:

Java Development Kit (JDK 8 or later)

Maven (for managing dependencies)

Google Chrome Browser (Ensure it's up-to-date)

Chrome WebDriver (Managed automatically by WebDriverManager)

🛠 Setup Guide

1️⃣ Clone the Repository

 git clone https://github.com/yourusername/email-automation.git
 cd email-automation

2️⃣ Add Dependencies to pom.xml (For Maven Users)

<dependencies>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>javax.mail</artifactId>
        <version>1.6.2</version>
    </dependency>
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.3.1</version>
    </dependency>
</dependencies>

3️⃣ Update Gmail Settings

Since we are connecting to Gmail using JavaMail, you must:

Enable IMAP in your Gmail settings.

Generate an App Password from Google if you're using 2-Step Verification.

Replace the email and password in the script:

store.connect("imap.gmail.com", "your-email@gmail.com", "your-app-password");

4️⃣ Run the Project

👨‍💻 Contributors

Mohamed A Taha - My Profile:/mohamedpyth
