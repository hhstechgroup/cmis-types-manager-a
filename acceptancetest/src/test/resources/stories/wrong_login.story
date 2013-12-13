Scenario: Login input wrong url

When the user opens the default page
And the user fills 'url' field with 'ololololo'
And press 'Get repo list' button
Then the text 'Cannot access ololololo/atom11: no protocol: ololololo/atom11' should be in the page source