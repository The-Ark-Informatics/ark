package au.org.theark.core.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.HttpMethod;

import org.apache.cxf.common.util.Base64Utility;
import org.json.simple.JSONObject;

/**
 * The Ark HTTP REST request and response handler.
 * @author thilina
 *
 */
public class ArkHTTPService {
	
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private OutputStream outputStream;
	private PrintWriter writer;

	/**
	 * This constructor initializes a new HTTP POST request with content type is
	 * set to multipart/form-data
	 * 
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 */
	public ArkHTTPService(String requestURL, String charset, String authHeader, String method) throws IOException {

		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);

		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setRequestMethod(method);

		if (authHeader != null) {
			httpConn.setRequestProperty("Authorization", "Basic " + Base64Utility.encode(authHeader.getBytes()));
		}
		httpConn.setRequestProperty("Accept", "application/json");
		httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
	}
	
	/**
	 * This constructor initializes a new HTTP POST request with content type is
	 * set to multipart/form-data
	 * 
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 */
	public ArkHTTPService(String requestURL, String authHeader) throws IOException {
		
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod(HttpMethod.GET);
		httpConn.setRequestProperty("Accept", "application/json");
		if (authHeader != null) {
			httpConn.setRequestProperty("Authorization", "Basic " + Base64Utility.encode(authHeader.getBytes()));
		}
	}


	
	/**
	 * Add JSON object for the parameter.
	 * @param object Input JSON object
	 * @throws IOException
	 */
	public void addPostParameters(JSONObject object) throws IOException {

		StringWriter sw = new StringWriter();
		object.writeJSONString(sw);
		String data = sw.toString();
		writer.write(data);
		writer.flush();
	}

	/**
	 * Add String object for the parameter.
	 * 
	 * @param parameter Input String parameter
	 * @throws IOException
	 */
	public void addPostParameters(String parameter) throws IOException {
		writer.write(parameter);
		writer.flush();
	}

	
	/**
	 * Adds a header field to the request.
	 * 
	 * @param name
	 *            - name of the header field
	 * @param value
	 *            - value of the header field
	 */
	public void addHeaderField(String name, String value) {
		writer.append(name + ": " + value).append(LINE_FEED);
		writer.flush();
	}

	public int getResponseCode() throws IOException {
		return httpConn.getResponseCode();
	}

	/**
	 * Completes the request and receives response from the server.
	 * 
	 * @return a list of Strings as response in case the server returned status
	 *         OK, otherwise an exception is thrown.
	 * @throws IOException
	 */
	public List<String> finish() throws IOException {
		List<String> response = new ArrayList<String>();

		if(writer !=null){
			writer.close();
		}

		int status = httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.add(line);
				System.out.println(line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			throw new IOException("Server returned non-OK status: " + status);
		}

		return response;
	}
}
