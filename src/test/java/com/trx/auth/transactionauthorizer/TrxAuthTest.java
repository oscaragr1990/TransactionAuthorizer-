package com.trx.auth.transactionauthorizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trx.auth.controllers.ApiController;
import com.trx.auth.enums.ViolationsEnum;
import com.trx.auth.model.Request;
import com.trx.auth.model.Response;

@SpringBootTest
class TrxAuthTest {

	@Autowired
	private ApiController apiController;

	@Autowired
	private ObjectMapper objectMapper;

	private ResponseEntity<Response> printTrz(List<String> lines) {
		apiController.restart();
		ResponseEntity<Response> response = null;
		List<Response> listResponse = new ArrayList<>();
		try {
			System.out.println("Input");
			for (String l : lines) {
				Request request = objectMapper.readValue(l, Request.class);
				System.out.println("\t" + request.toString());
				response = apiController.executeOperation(request);
				listResponse.add(response.getBody());
			}
			System.out.println("Output");
			for (Response r : listResponse) {
				if (r != null) {
					System.out.println("\t" + r.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("----------------------------------------");
		return response;
	}

	@Test
	void test01_AccountCreate() {
		System.out.println("test01_AccountCreate");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true, \"available-limit\": 100}}");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().isEmpty());
	}

	@Test
	void test02_authorizeTrx() {
		System.out.println("test02_authorizeTrx");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true,	 \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().isEmpty());
	}

	@Test
	void test03_authorizeTrx_Amount_Ng() {
		System.out.println("test03_authorizeTrx_Amount_Ng");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true,	 \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": -20, \"time\":\"2019-02-13T10:00:00.000Z\"}}");
		ResponseEntity<Response> responseBody = printTrz(lines);
		// Response response = responseBody.getBody();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getStatusCode());
	}

	@Test
	void testBR_01_AccountNotInitialized() {
		System.out.println("testBR_01_AccountNotInitialized");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 2, \"active-card\": false,	 \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().contains(ViolationsEnum.ACCOUNT_NOT_INITIALIZED.getDescription()));

	}

	@Test
	void testBR_02_CardNotActive() {
		System.out.println("testBR_02_CardNotActive");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": false, \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().contains(ViolationsEnum.CARD_NOT_ACTIVE.getDescription()));

	}

	@Test
	void testBR_03_InsufficientLimit() {
		System.out.println("testBR_03_InsufficientLimit");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true, \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Habbib's\", \"amount\": 90, \"time\": \"2019-02-13T11:00:00.000Z\"}})");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().contains(ViolationsEnum.INSUFFICIENT_LIMIT.getDescription()));

	}

	@Test
	void testBR_04_HighFrequencySmallInterval() {
		System.out.println("testBR_04_HighFrequencySmallInterval");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true, \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:10.000Z\"}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"CL 01\", \"amount\": 10, \"time\": \"2019-02-13T10:00:15.000Z\"}})");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"CL 02\", \"amount\": 10, \"time\": \"2019-02-13T10:01:20.000Z\"}})");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"CL 03\", \"amount\": 10, \"time\": \"2019-02-13T10:00:20.000Z\"}})");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().contains(ViolationsEnum.HIGHT_FREQUENCY_SMALL_INTERVAL.getDescription()));
	}

	@Test
	void testBR_05_DoubledRransaction() {
		System.out.println("testBR_05_DoubledRransaction");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true, \"available-limit\": 100}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"Burger King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:10.000Z\"}}");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"CL 01\", \"amount\": 10, \"time\": \"2019-02-13T10:00:15.000Z\"}})");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"CL 01\", \"amount\": 10, \"time\": \"2019-02-13T10:01:15.000Z\"}})");
		lines.add(
				"{\"transaction\": {\"id\": 1, \"merchant\": \"CL 01\", \"amount\": 10, \"time\": \"2019-02-13T10:02:15.000Z\"}})");

		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().contains(ViolationsEnum.DOUBLED_TRANSACTION.getDescription()));

	}

	@Test
	void testBR_06_AccountAlreadyInitialized() {
		System.out.println("testBR_06_AccountAlreadyInitialized");
		List<String> lines = new ArrayList<>();
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true, \"available-limit\": 100}}");
		lines.add("{\"account\": {\"id\": 2, \"active-card\": true, \"available-limit\": 350}}");
		lines.add("{\"account\": {\"id\": 1, \"active-card\": true, \"available-limit\": 350}}");
		ResponseEntity<Response> responseBody = printTrz(lines);
		Response response = responseBody.getBody();
		assertTrue(response.getViolations().contains(ViolationsEnum.ACCOUNT_ALREADY_INITIALIZED.getDescription()));
	}
}
