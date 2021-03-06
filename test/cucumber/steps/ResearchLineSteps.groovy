import rgms.publication.ResearchLine
import steps.TestDataAndOperations
import steps.TestDataAndOperationsResearchLine
import pages.ResearchLineCreatePage
import pages.ResearchLinePage
import pages.LoginPage
import pages.PublicationsPage
import pages.ResearchLineVisualizePage
import pages.ResearchLineEditPage

import static cucumber.api.groovy.EN.*

Given(~'^the system has a research line named "([^"]*)" with a description "([^"]*)"$') { String name, description ->
	TestDataAndOperationsResearchLine.insertsResearchLine(name, description)
	research_line = ResearchLine.findByName(name)
	assert research_line != null
}

When(~'^I remove the research line "([^"]*)"$') { String name ->
	research_line = ResearchLine.findByName(name)
	TestDataAndOperationsResearchLine.deleteResearchLine(research_line.id)
}

Then(~'^the research line "([^"]*)" is properly removed by the system'){String name ->
	research_line = ResearchLine.findByName(name)
	assert research_line == null
}


When(~'^I update the research line "([^"]*)" with a description "([^"]*)"$') { String name,description ->
	TestDataAndOperationsResearchLine.updateResearchLine(name,description)
}

Then(~'^the research line "([^"]*)" has the description updated to "([^"]*)"$'){String name, description ->
	research_line = ResearchLine.findByName(name)
	assert research_line != null && research_line.description == description
}

Given(~'^the system has no research line named "([^"]*)"$') { String name ->
	research_line = ResearchLine.findByName(name)
	assert research_line == null
}

When(~'^I create the research line named "([^"]*)" with empty description$') { String name ->
	TestDataAndOperationsResearchLine.createResearchLine(name)
}

Then(~'^the research line "([^"]*)" is not stored, because is invalid$'){String name ->
	research_line = ResearchLine.findByName(name)
	assert research_line == null
}

When (~'I create the research line "([^"]*)" with description "([^"]*)" with no member assigned'){String name, description ->
    TestDataAndOperationsResearchLine.createResearchLine(name)
}
Then (~'the research line "([^"]*)" is properly saved with no error'){String name ->
    research_line = ResearchLine.findByName(name)
    assert research_line != null
}

When(~'^I select the new research line option at the research line page$') {->
	at ResearchLinePage
	page.selectNewResearchLine()
}

Then(~'^I can fill the research line details$') {->
	at ResearchLineCreatePage
	page.fillResearchLineDetails()
}

When(~'^I click the research line "([^"]*)" at the research line list$') {String name ->
    at ResearchLinePage
    page.visualizeResearchLine(name)
}
Then(~'^I can visualize the research line "([^"]*)" details$') {String name->
    at ResearchLineVisualizePage
    page.checkResearchLineDetails(name)
}

Given (~'^I am logged as admin$'){->
    to LoginPage
    at LoginPage
    page.fillLoginData("admin", "adminadmin")


}
Given(~'^the system has a research line named as "([^"]*)"$') { String name ->

    at PublicationsPage
    page.select("Linha de pesquisa")
    at ResearchLinePage
    page.selectNewResearchLine()
    at ResearchLineCreatePage
    page.createsResearchLine(name)
    research_line = ResearchLine.findByName(name)
    assert research_line != null
}

When(~'^I click the edit button$') { ->
    at ResearchLineVisualizePage
    page.editResearchLine()
}

Then(~'^I can change the research line "([^"]*)" details$') {String name->
    at ResearchLineEditPage
    page.changeResearchLineDetails(name)
}
