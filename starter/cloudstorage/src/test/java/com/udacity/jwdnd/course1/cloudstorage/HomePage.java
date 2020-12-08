package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {
    @FindBy(id = "logout")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "add-new-note")
    private WebElement addNoteButton;

    @FindBy(id = "noteModal")
    private WebElement noteModal;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "save-note")
    private WebElement saveNoteButton;

    @FindBy(id = "goto-home")
    private WebElement gotoHome;

    @FindBy(id = "note-table")
    private WebElement noteTable;

    @FindBy(className = "note-row")
    private List<WebElement> noteRow;

    @FindBy(id = "row-note-description")
    private WebElement rowNoteDescription;

    @FindBy(id = "back-to-home")
    private WebElement backToHome;

    @FindBy(id = "edit-note")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteButton;

    WebDriverWait wait;
    JavascriptExecutor jse;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 20);
        jse = (JavascriptExecutor) driver;
    }

    public WebElement getEditNoteButton() {
        return editNoteButton;
    }

    public List<WebElement> getNoteRow() {
        return noteRow;
    }

    public WebElement getNoteTable() {
        return noteTable;
    }

    public WebElement getAddNoteButton() {
        return addNoteButton;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle.sendKeys(noteTitle);
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription.sendKeys(noteDescription);
    }

    public WebElement getNotesTab() {
        return notesTab;
    }

    public WebElement getLogoutButton() {
        return logoutButton;
    }

    public void logout(){
        logoutButton.click();
    }

    public void gotoNotesTab() throws InterruptedException {
        jse.executeScript("arguments[0].click()", notesTab);
    }

    public void clickAddNote(){
        addNoteButton.click();
        wait.until(ExpectedConditions.visibilityOf(noteModal));
    }

    public void clickEditNote(WebElement editNoteButton){
        editNoteButton.click();
        wait.until(ExpectedConditions.visibilityOf(noteModal));
    }

    public void clickDeleteNote(WebElement deleteNoteButton){
        deleteNoteButton.click();
    }

    public void saveNote(String title, String description){
        clickAddNote();
        setNoteTitle(title);
        setNoteDescription(description);
        saveNoteButton.click();
    }

    public void goBackToNotes() throws InterruptedException {
        jse.executeScript("arguments[0].click()", backToHome);
        wait.until(ExpectedConditions.visibilityOf(notesTab));
        gotoNotesTab();
        wait.until(ExpectedConditions.visibilityOf(rowNoteDescription));
    }

    public void editNote(String description){
        noteDescription.clear();
        setNoteDescription(description);
        saveNoteButton.click();
    }
}
