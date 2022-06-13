package com.trx.auth.controllers;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trx.auth.model.Account;
import com.trx.auth.model.Request;
import com.trx.auth.model.Response;
import com.trx.auth.model.Transaction;
import com.trx.auth.services.TrxService;

@RestController
@RequestMapping("api/account")
public class ApiController {

	Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private TrxService trxService;

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Account>> consultAccounts() {
		try {
			return ResponseEntity.ok(trxService.consultAccounts());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/{id}/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Transaction>> consultTrx(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(trxService.consultTrx(id));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> executeOperation(@RequestBody Request inRequest) {

		Response out = null;
		if (inRequest.getAccount() != null) {
			out = trxService.accountCreate(inRequest.getAccount());
		}
		if (inRequest.getTransaction() != null) {
			if (inRequest.getTransaction() != null && inRequest.getTransaction().getAmount() > 0) {
				out = trxService.authorizeTrx(inRequest.getTransaction());
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		return ResponseEntity.ok(out);
	}

	@DeleteMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> restart() {
		try {
			trxService.resetAccounts();
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}

}
