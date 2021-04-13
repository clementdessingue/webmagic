package us.codecraft.webmagic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Pooled Proxy Object
 * 
 * @author yxssfxwzy@sina.com <br>
 * @since 0.5.1
 */

public class ProxyUtils {
	
	private ProxyUtils() {}

	private static final Logger logger = LoggerFactory.getLogger(ProxyUtils.class);

	public static boolean validateProxy(Proxy p) {
		try (Socket socket = new Socket();) {
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(p.getHost(), p.getPort());
			socket.connect(endpointSocketAddr, 3000);
			return true;
		} catch (IOException e) {
			logger.warn("FAILURE - CAN not connect!  remote: %s", p);
			return false;
		}
	}

}
