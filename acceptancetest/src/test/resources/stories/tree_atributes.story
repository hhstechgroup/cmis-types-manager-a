
Scenario: when user login and click on tree node, user will see not empty atribute fields

When the user opens the default page
And the user fills 'url' field with 'localhost'
And the user fills 'port' field with '8080'
And press 'Login' button
Then wait for element 'form' is visible
When clicks on element with id/name/className 'form:tree:0:lblNode'
Then element 'atributesForm:atribute' has attribute value 'Relationship'



