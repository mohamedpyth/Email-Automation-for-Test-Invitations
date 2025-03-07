package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SubjectTerm;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class EmailAutomation {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new CheckEmailTask(), 0, 300000); // Run every 5 minutes (300,000 ms)
    }
}

class CheckEmailTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Checking for new email invitations...");
        try {
            // Email properties
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.ssl.trust", "*");
            props.put("mail.imaps.ssl.enable", "true");
            Session session = Session.getInstance(props);

            // Connect to email inbox
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "mohamedtahahdic123@gmail.com", "fmqd riya xkip hnsw");

            // Search for the latest email
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new SubjectTerm("Test Invitation")); // Filter by subject

            if (messages.length > 0) {
                for (Message message : messages) {
                    Date receivedDate = message.getReceivedDate();
                    if (isEmailRecent(receivedDate)) {
                        System.out.println("New email found! Extracting content...");
                        String emailContent = getTextFromMessage(message);

                        if (emailContent != null) {
                            String acceptanceLink = extractLink(emailContent);
                            if (acceptanceLink != null) {
                                System.out.println("Opening invitation link: " + acceptanceLink);
                                acceptInvitation(acceptanceLink);
                                break; // Open only the latest valid email
                            }
                        }
                    } else {
                        System.out.println("Skipping old email received at: " + receivedDate);
                    }
                }
            } else {
                System.out.println("No matching emails found.");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailRecent(Date receivedDate) {
        if (receivedDate == null) return false;
        long emailTime = receivedDate.getTime();
        long currentTime = System.currentTimeMillis();
        return (currentTime - emailTime) <= 600000; // 10 minutes in milliseconds
    }

    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return null;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                return bodyPart.getContent().toString();
            } else if (bodyPart.isMimeType("text/html")) {
                return bodyPart.getContent().toString();
            } else if (bodyPart.getContent() instanceof InputStream) {
                BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) bodyPart.getContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
        }
        return result.toString();
    }

    private String extractLink(String content) {
        try {
            System.out.println("Extracting link from content...");
            if (content.contains("http://") || content.contains("https://")) {
                String[] parts = content.split("http");
                for (String part : parts) {
                    if (part.startsWith("s://") || part.startsWith("://")) {
                        String link = "http" + part.split("[\\s\"<>]")[0];
                        if (link.contains("test.io/tests/")) {
                            return link; // Return only test invitation links
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error extracting link: " + e.getMessage());
        }
        return null;
    }

    private void acceptInvitation(String url) {
        try {
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver();
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Find the Accept button dynamically
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Click this link for more details')]")));

            acceptButton.click();
            System.out.println("Invitation accepted successfully!");

            // Close the browser
            driver.quit();
        } catch (Exception e) {
            System.out.println("Failed to accept the invitation: " + e.getMessage());
        }
    }
}
