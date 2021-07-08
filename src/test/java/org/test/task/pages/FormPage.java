package org.test.task.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FormPage {
    private WebDriver driver;
    private String url = "https://www.coffee-qa.perfect-sitebank.com/contact/";

    private By submitButton = By.xpath("//div[@id ='success']/following-sibling::button");
    private By txtMessage = By.id("success");
    private By fieldFullName = By.id("p_name");
    private By fieldEmail = By.id("p_email");
    private By fieldAdress = By.id("p_subject");
    private By fieldPhone = By.id("p_phone");
    private By selectMenu = By.xpath("//*[@for = \"p_message\"]/following::select");
    private By txtNameMessage = By.xpath("//*[@id=\"p_name\"]/following-sibling::*//li");
    private By txtEmailMessage = By.xpath("//*[@id=\"p_email\"]/following-sibling::*//li");

    public FormPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(url);
    }

    public void pressSubmitButton() {
        driver.findElement(submitButton).submit();
    }

    public Boolean isTextForMessagePresent(String text) {
        return isTextPresent(txtMessage, text);
    }

    public Boolean isTextForNamePresent(String text) {
        return isTextPresent(txtNameMessage, text);
    }

    public Boolean isTextForEmailPresent(String text) {
        return isTextPresent(txtEmailMessage, text);
    }

    private Boolean isTextPresent(By elementLocator, String text) {
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.textToBe(elementLocator, text));
    }

    public void selectBoxMenu(int index) {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(selectMenu));
        Select dropdown = new Select(element);
        dropdown.selectByIndex(index);
    }

    public void fillOutFullName(String name) {
        driver.findElement(fieldFullName).sendKeys(name);
    }

    public void fillOutEmail(String email) {
        driver.findElement(fieldEmail).sendKeys(email);
    }

    public void fillOutAdress(String adress) {
        driver.findElement(fieldAdress).sendKeys(adress);
    }

    public void fillOutPhone(String phone) {
        driver.findElement(fieldPhone).sendKeys(phone);
    }
}
