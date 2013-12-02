Scenario: Login user
When the user opens the default page
And the user fills 'url' field with 'localhost'
And the user fills 'port' field with '8080'
And press 'Login' button
Then wait for element 'form' is errorVisible