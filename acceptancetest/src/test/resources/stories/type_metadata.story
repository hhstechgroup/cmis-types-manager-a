
Scenario: when user login and click on tree node and select some element on the tree, user will see page with metadata

When the user opens the default page
When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='treeForm:tree:2']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element by './/*[@id='treeForm:tree:2_0']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element with id/name/className 'treeForm:tree:2_0:lblNode'
And clicks on element with id/name/className 'treeMetadataItem'
And clicks on element with id/name/className 'allProperties:0:fullInfo'
Then element with './/*[@id='fullPropertyGrid']/tbody/tr[1]/td[2]' has text 'cmis:contentStreamLength'


