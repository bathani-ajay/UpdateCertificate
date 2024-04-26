package updatePolicyNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdatePolicyNumber {
public static String updatePolicy(String url, String source, String medium, String campaign, String proposalNumber, String policyNumber) {
    try {
        String endpoint = url;
        String request = ""+
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gcs=\"http://stub.tagicesb.com/wsdl/GCSyncService/\">\n"+
        "<soapenv:Header/>\n"+
        		"<soapenv:Body>\n"+
        			"<gcs:updateCertificate>\n"+
        				"<gcs:Source>" + source + "</gcs:Source>\n" + 
				        " <gcs:Medium>" + medium + "</gcs:Medium>\n" + 
				        " <gcs:Campaign>" + campaign + "</gcs:Campaign>\n" + 
				        " <gcs:AuthenticationToken></gcs:AuthenticationToken>\n" + 
				        " <gcs:ReferenceNo>" + proposalNumber + "</gcs:ReferenceNo>\n" + 
				        " <gcs:CertificateNo></gcs:CertificateNo>\n" + 
				        " <gcs:PolicyNo>" + policyNumber + "</gcs:PolicyNo>\n" + 
				    " </gcs:updateCertificate>\n" + 
				 " </soapenv:Body>\n" + 
        "</soapenv:Envelope>";

        System.out.println("Sending SOAP request to: " + endpoint);
        String soapResponse = sendSoapRequest(endpoint, request);
        System.out.println("SOAP Response received.");
        return soapResponse;
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<root>\r\n\t<Status>FAILED</Status>\r\n\t<MessageDesc>"+e.getMessage()+"</MessageDesc>\r\n</root>";
    }
}

public static String sendSoapRequest(String endpoint, String request) throws IOException {
    HttpURLConnection connection = null;
    OutputStream outputStream = null;
    InputStream inputStream = null;
    BufferedReader reader = null;
    StringBuilder response = new StringBuilder();
    try {
        URL url = new URL(endpoint);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setDoOutput(true);

        outputStream = connection.getOutputStream();
        byte[] requestBytes = request.getBytes();
        outputStream.write(requestBytes, 0, requestBytes.length);

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } else {
            throw new IOException("Failed to send SOAP request. HTTP error code: " + responseCode);
        }
    } finally {
        if (outputStream != null) {
        	try{
            outputStream.close();
        	}
        	catch(Exception e){
        		
        		e.printStackTrace();
        	}
        }
        if (inputStream != null) {
        	try{
        		inputStream.close();
        	}
			catch(Exception e){
			        		
			      e.printStackTrace();
			 }
        }
        if (reader != null) {
            reader.close();
        }
        if (connection != null) {
            connection.disconnect();
            System.out.println("Connection disconnected.");
        }
    }
    return response.toString();
}

public static void main(String[] args) {
    String msg = updatePolicy("http://172.20.251.126:8182/cxf/GCSyncService?WSDL", "TATA-AIG", "PACE_UAT", "PACE_UAT", "202403060033527", "12345");
    
    System.out.println(msg);
}
}