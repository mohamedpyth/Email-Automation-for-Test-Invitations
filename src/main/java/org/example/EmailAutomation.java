package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.mail.*;
import javax.mail.search.SubjectTerm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class EmailAutomation {
    public static void main(String[] args) {
        while (true) {
            try {
                Properties props = new Properties();
                props.put("mail.store.protocol", "imaps");
                props.put("mail.imaps.ssl.trust", "*");
                props.put("mail.imaps.ssl.enable", "true");
                Session session = Session.getInstance(props);

                Store store = session.getStore("imaps");
                store.connect("imap.gmail.com", "your-email@gmail.com", "your-app-password");

                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                Message[] messages = inbox.search(new SubjectTerm("Test Invitation"));
                for (Message message : messages) {
                    Date receivedDate = message.getReceivedDate();
                    LocalDateTime emailTime = receivedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();
                    if (now.minusMinutes(10).isBefore(emailTime)) {
                        Object content = message.getContent();
                        if (content instanceof String) {
                            String emailContent = (String) content;
                            String invitationLink = extractLink(emailContent);

                            WebDriverManager.chromedriver().setup();
                            WebDriver driver = new ChromeDriver();
                            driver.get(invitationLink);
                            Thread.sleep(5000); // Wait for page to load
                            WebElement acceptButton = driver.findElement(By.xpath("//button[contains(text(),'Accept')]"));
                            acceptButton.click();
                            driver.quit();
                        }
                    }
                }
                inbox.close(false);
                store.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(300000); // Wait 5 minutes before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String extractLink(String content) {
        if (content.contains("http")) {
            return content.split("http")[1].split("\"")[0];
        }
        return "";
    }
}
