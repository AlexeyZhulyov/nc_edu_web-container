### CGI module
- Create more specific exception without call a format message. Three particular exceptions which are inherit CgiException
- Variables should be defined in blocks where they are using! Right now everything is storing in `@Before` method
- Remove `System.out.println` from tests

### Configuration module
- `AccessContainersTest` tests aren't **полноценные** because collections `ALLOW` and `DENY` are empty. 

### Web module
- Create more specific exception without call a format message
```java
    throw new WebException(format(CANNOT_FIND_READ_FILE, page.getName()), e);
```

