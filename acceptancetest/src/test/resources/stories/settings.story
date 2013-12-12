
Scenario: Settings page demonstration

When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element with id/name/className 'topMenuBarForm:j_idt7'
Then element id/name/className 'form:url' has text 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And element id/name/className 'form:repo' has text 'Apache Chemistry OpenCMIS InMemory Repository'