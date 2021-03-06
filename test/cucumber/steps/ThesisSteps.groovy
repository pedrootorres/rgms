import rgms.authentication.User
import steps.ThesisTestDataAndOperations
import pages.LoginPage
import pages.thesis.ThesisCreatePage
import pages.thesis.ThesisShowPage
import rgms.publication.Tese
import steps.TestDataAndOperationsPublication
import pages.ThesisPage
import rgms.member.Member

import java.io.File

import static cucumber.api.groovy.EN.*
import org.codehaus.groovy.grails.web.context.ServletContextHolder 

Given(~'^The system has no thesis entitled "([^"]*)"$') {String title ->
    article = Tese.findByTitle(title)
    assert article == null
}

Given(~'^The thesis "([^"]*)" is stored in the system with file name "([^"]*)"$') { 
    String title, filename ->
    ThesisTestDataAndOperations.createTese(title, filename, "UFPE")
    article = Tese.findByTitle(title)
    assert article != null
}

When(~'^I create the thesis "([^"]*)" with file name "([^"]*)" and school "([^"]*)"$') {
    String title, filename, school ->
    ThesisTestDataAndOperations.createTese(title, filename, school)
}

Then(~'^The thesis "([^"]*)" is not stored twice$') { String title ->
    theses = Tese.findAllByTitle(title)
    assert theses.size() == 1
}

Then(~'^The thesis "([^"]*)" is properly stored by the system$') { String title ->
    thesis = Tese.findByTitle(title)
    assert thesis != null
}

Given(~'^I am at the create thesis page$') {->
    to LoginPage
    at LoginPage
    page.fillLoginData("admin", "adminadmin")
    to ThesisCreatePage
    at ThesisCreatePage
}

When(~'^I fill the thesis details with "([^"]*)", "([^"]*)", "([^"]*)", "([^"]*)", "([^"]*)" and "([^"]*)"$') {
    title, pub_day, pub_month, pub_year, school, address ->
    def absolutePath = ServletContextHolder.servletContext.getRealPath("/test/functional/steps/NewthesisGUI.txt")
    absolutePath = absolutePath.replace("\\", "/").replaceAll("/web-app", "")
    page.fillThesisDetails(title, pub_day, pub_month, pub_year, school, address, absolutePath)
}

When(~'^I fill some thesis details with "([^"]*)", "([^"]*)", "([^"]*)", "([^"]*)", "([^"]*)" and "([^"]*)"$') {
    title, pub_day, pub_month, pub_year, school, address ->
    page.fillSomeThesisDetails(title, pub_day, pub_month, pub_year, school, address)
}

Then(~'^I am on the thesis show page$') {->
    at ThesisShowPage
}

Then(~'^I am still on the create thesis page with the error message$') {->
    at ThesisCreatePage
}

/**
 * @author carloscemb
 */
When(~'^I select the new thesis option at the thesis page$') {->
    at ThesisPage
    page.selectNewThesis()
    at ThesisCreatePage
}

/**
 * @author carloscemb
 */
Then(~'^I see my user listed as an author member of thesis by default$') {->
    at ThesisCreatePage
    assert TestDataAndOperationsPublication.containsUser(page.selectedMembers())
}

Then(~'^I see my school name as school of thesis by default$') {->
    at ThesisCreatePage
    userData = User.findByUsername('admin')?.author?.university
    assert page.currentSchool() == userData
}
