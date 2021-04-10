package us.codecraft.webmagic.utils;

import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.junit.Test;

/**
 * @author code4crafer@gmail.com
 */
public class IPUtilsTest {

    @Test
    public void testGetFirstNoLoopbackIPAddresses() throws Exception {
        System.out.println(IPUtils.getFirstNoLoopbackIPAddresses());
        InetAddress addrTest = InetAddress.getByName(IPUtils.getFirstNoLoopbackIPAddresses());
        assertTrue(!addrTest.isLoopbackAddress());
    }
}
