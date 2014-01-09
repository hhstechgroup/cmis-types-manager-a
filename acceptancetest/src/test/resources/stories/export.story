
Scenario: 8,10 Export types and tree of types
When the user opens the default page
And the user fills 'url' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/'
And press 'Get repo list' button
And press 'Login' button
And clicks on element by './/*[@id='leftMenuForm:importexport']/a'
And clicks on element by './/*[@id='treeForm:tree:2:lblNode']'
And clicks on element by './/*[@id='selectOne:export']/div[3]'
And clicks on element by './/*[@id='selectOne:export_panel']/div/div/div[1]/ul/li[2]'
And the user clicks on 'selectOne:save' download file
Then verify that file is downloaded

When clicks on element by './/*[@id='selectOne:export']/div[3]'
And clicks on element by './/*[@id='selectOne:export_panel']/div/div/div[1]/ul/li[3]'
And the user clicks on 'selectOne:save' download file
Then verify that file is downloaded

When clicks on element by './/*[@id='selectOne:export']/div[3]'
And clicks on element by './/*[@id='selectOne:export_panel']/div/div/div[1]/ul/li[4]'
And the user clicks on 'selectOne:save' download file
Then verify that file is downloaded