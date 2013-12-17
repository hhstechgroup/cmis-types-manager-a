
Scenario: when user login and click on tree node and select don't select some element on the tree, user will see page with attributes

When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='leftMenuForm:updateTypeSection']/a'
And clicks on element by './/*[@id='leftMenuForm:placeholdersItem']'
And clicks on element by './/*[@id='deleteBtn']'
Then the text 'You haven't chosen type' should be in the page source


