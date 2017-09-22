package au.org.theark.test.integration.testing;

import au.org.theark.test.integration.BaseIntegrationTest;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ITesting extends BaseIntegrationTest {

    private transient static Logger log = LoggerFactory.getLogger(ITesting.class);

    @Test
    public void testLoadingFromSQL() {
        log.info("Study count: " + iArkCommonService.getCountOfStudies());
        assertEquals(1, iArkCommonService.getCountOfStudies());
        assertEquals("testLoadingFromSQL", iArkCommonService.getStudy(1l).getName());
    }

    @Test
    public void testSuperUserExists() throws LdapException, CursorException {
        LdapConnection connection = getLDAPResource().getLDAPConnection();

        EntryCursor cursor = connection.search("ou=arkUsers,dc=the-ark,dc=org,dc=au", "(objectclass=*)", SearchScope.ONELEVEL, "*");

        int resultCount = 0;

        while (cursor.next()) {
            resultCount+=1;
            cursor.get();
        }

        assertEquals(1, resultCount);
    }

    @Test
    public void testLoadingFromLDIF() throws LdapException, CursorException {

        LdapConnection connection = getLDAPResource().getLDAPConnection();

        EntryCursor cursor = connection.search("ou=arkUsers,dc=the-ark,dc=org,dc=au", "(cn=testLDIF@email.com)", SearchScope.ONELEVEL, "*");

        int resultCount = 0;

        while (cursor.next()) {
            resultCount+=1;
            cursor.get();
        }

        assertEquals(1, resultCount);
    }

}
