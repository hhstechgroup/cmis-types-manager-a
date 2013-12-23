
Scenario: when user login and click on tree node and select some element on the tree, user will see page with attributes

When the user opens the default page
And the user fills 'url' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='mainTab:treeForm:tree:2:lblNode']'
Then wait until all animations on page completed
Then element 'displayName' has attribute value 'Document'




