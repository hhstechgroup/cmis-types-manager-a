package com.engagepoint.acceptancetest.steps;


import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.annotations.findby.By;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.WebElement;

/**
 * Created with IntelliJ IDEA.
 * User: ivan.yakubenko
 * Date: 12/10/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelperSteps extends ScenarioSteps {
    private static final String XPATH_SELCTOR_SUFIX = "')]";
    private UIBootstrapBasePage uIBootstrapBasePage;

    public HelperSteps(Pages pages) {
        super(pages);
        uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
    }

    @When("clicks on element with class '$className' with text '$text'")
    @Alias("the user clicks on element with class '$className' with text '$text'")
    public void clickByText(String className, String text){
        for (WebElement webElement : uIBootstrapBasePage.getDriver().findElements(By.className(className))) {
            if (webElement.getText().equalsIgnoreCase(text)) {
                webElement.click();
            }
        }
    }
}
