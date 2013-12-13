
Scenario: Login user

When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='treeForm:tree:2']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element by './/*[@id='treeForm:tree:2_0']//*[contains(@class,'ui-tree-toggler')]'

