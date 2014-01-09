
Scenario: 1. Login user
When the user opens the default page
And the user fills 'url' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='topMenuBarForm:settings']/span'
Then element id/name/className 'form:repo' has text 'A1'
When clicks on element by './/*[@id='logoutForm:logoutBtn']'
And the user fills 'url' field with 'ololololo'
And press 'Get repo list' button
Then the text 'Can't find entered URL address' should be in the page source

