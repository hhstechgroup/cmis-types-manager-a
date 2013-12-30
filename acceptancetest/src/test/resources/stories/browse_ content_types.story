Scenario: 2. Browse the system content types and their attributes

When the user opens the default page
And the user fills 'url' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='mainTab:treeForm:tree:2']/span/span[1]'
And clicks on element by './/*[@id='mainTab:treeForm:tree:2_0']/span/span[1]'
And clicks on element by './/*[@id='mainTab:treeForm:tree:3']/span/span[1]'
And clicks on element by './/*[@id='mainTab:treeForm:tree:2:lblNode']'
Then wait until all animations on page completed
When clicks on element by './/*[@id='mainTab']/ul/li[2]/a'
And clicks on element with id/name/className 'allProperties:0:fullInfo'
Then element with './/*[@id='fullPropertyGrid']/tbody/tr[1]/td[2]' has text 'cmis:contentStreamLength'