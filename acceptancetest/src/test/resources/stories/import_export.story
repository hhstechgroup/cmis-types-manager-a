
Scenario: import
When the user opens the default page
And the user fills 'url' field with 'http://lab18:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='leftMenuForm:importexport']/a'
And the user uploads the fileName/filePath 'src/test/resources/files/xmlns.xml' to field with 'form:import_input'
And clicks on element by './/*[@id='push']'

When the user opens the default page
And clicks on element by './/*[@id='mainTab:treeForm:tree:2']/span/span[1]'
And clicks on element by './/*[@id='mainTab:treeForm:tree:2_5:lblNode']'
Then wait until all animations on page completed

When clicks on element by './/*[@id='leftMenuForm:updateTypeSection']/a'
And clicks on element by './/*[@id='leftMenuForm:placeholdersItem']'
And clicks on element by './/*[@id='deleteBtn']'
And clicks on element by './/*[@id='dltbtn']'
