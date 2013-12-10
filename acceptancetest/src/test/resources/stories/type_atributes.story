
Scenario: when user login and click on tree node and select some element on the tree, user will see page with attributes

When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element with id/name/className 'ui-tree-toggler'
And clicks on element with id/name/className 'formTree:tree:0_0:lblNode'
And clicks on element with id/name/className 'treeAttributesItem'
Then wait for element 'displayName' is visible



