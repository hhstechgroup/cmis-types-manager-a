
Scenario: Login user

When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element with id/name/className 'ui-tree-toggler'
Then wait for element 'formTree:tree:0_0:lblNode' is visible