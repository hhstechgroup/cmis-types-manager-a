
Scenario: user creates type

When the user opens the default page
And the user fills 'url' field with 'http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/'
And press 'Get repo list' button
And press 'Login' button

And clicks on element by './/*[@id='leftMenuForm:updateTypeSection']/a'
And clicks on element by './/*[@id='mainForm:tree:2']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element by './/*[@id='mainForm:tree:2_0']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element with id/name/className 'mainForm:tree:2_0:lblNode'
And press 'Next' button

And the user fills 'mainForm:displayName' field with 'TestType'
And the user fills 'mainForm:id' field with 'test'
And the user fills 'mainForm:description' field with 'just for testing'
And the user fills 'mainForm:queryName' field with 'one'
And the user fills 'mainForm:localName' field with 'two'
And the user fills 'mainForm:localNamespace' field with 'three'
And press 'Next' button

And press 'Add metadata' button
And the user fills 'id' field with 'TestMetadata'
And the user fills 'localname' field with 'just for testing'
And the user fills 'localnamespace' field with 'testMeta'
And the user fills 'displayname' field with 'oneMeta'
And the user fills 'queryname' field with 'twoMeta'
And clicks on element with id/name/className 'add'
And press 'Create' button

Scenario: user updates type
When the user opens the default page
And clicks on element by './/*[@id='leftMenuForm:updateTypeSection']/a'
And clicks on element by './/*[@id='leftMenuForm:historyItem']'
And clicks on element by './/*[@id='mainForm:tree:1']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element with id/name/className 'mainForm:tree:1_0:lblNode'
And press 'Next' button

And the user fills 'mainForm:displayName' field with 'UpdatedTestType'
And the user fills 'mainForm:description' field with 'just for testing'
And the user fills 'mainForm:queryName' field with 'one'
And the user fills 'mainForm:localName' field with 'two'
And press 'Next' button
And clicks on element with id/name/className 'mainForm:addType'
Then wait for element 'mainForm:tree:1_0:lblNode' is visible

Scenario: user delete type
When the user opens the default page
And clicks on element by './/*[@id='leftMenuForm:updateTypeSection']/a'
And clicks on element by './/*[@id='leftMenuForm:placeholdersItem']'
And clicks on element by './/*[@id='treeForm:tree:2']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element by './/*[@id='treeForm:tree:2_0']//*[contains(@class,'ui-tree-toggler')]'
And clicks on element with id/name/className 'treeForm:tree:2_0_2:lblNode'
And press 'Delete' button
Then the text 'Are you sure?' should be in the page source
When press 'Continue' button