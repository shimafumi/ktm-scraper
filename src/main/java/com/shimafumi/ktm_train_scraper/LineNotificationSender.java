package com.shimafumi.ktm_train_scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class LineNotificationSender {

	public void send(String token, String message) {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost request = new HttpPost("https://notify-api.line.me/api/notify/");
		List<NameValuePair> requestParams = new ArrayList<>();
		requestParams.add(new BasicNameValuePair("message", message));

		CloseableHttpResponse response = null;
		try {
			request.setHeader("Authorization", "Bearer " + token);
			request.setEntity(new UrlEncodedFormEntity(requestParams, "UTF-8"));
			response = httpclient.execute(request);

			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new RuntimeException("HttpStatus is not OK");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
