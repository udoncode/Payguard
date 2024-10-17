package com.payguard.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.payguard.domain.Member;
import com.payguard.domain.Transaction;
import com.payguard.service.TransactionService;
import com.payguard.transaction.dto.TransactionDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
	
	private final TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@GetMapping
	public String showTransaction(@SessionAttribute("member")Member member, @RequestParam(value="page", defaultValue="0") int page ,Model model) {
		if(member != null) {
			Page<TransactionDTO> paging = transactionService.getAllTransactions(page);
			model.addAttribute("list", paging);
			return "/transaction/transaction";
		} else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/form")
	public String showTransactionForm(Model model) {
		model.addAttribute("transactionDTO", new TransactionDTO());
		return "transaction/transaction-form";
	}
	
	@GetMapping("/form/{id}")
	public String getTransactionDetail(@PathVariable Long id, Model model) {
		Transaction transaction = transactionService.getTransactionById(id);
		TransactionDTO transactionDTO = new TransactionDTO(transaction);
		model.addAttribute("transactionDTO", transactionDTO);
		return "transaction/transaction-edit";
	}
	
	@PostMapping("/add")
	public String insertTransaction(@Valid @ModelAttribute TransactionDTO transactionDTO, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			System.out.println("Errors found: " + bindingResult.getAllErrors());
			return "transaction/transaction-form";
		}
		
		Transaction transaction = transactionDTO.toEntity();
		transactionService.insertTransaction(transaction);
		return "redirect:/transaction";
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateTransaction(@PathVariable Long id, @RequestBody @Valid TransactionDTO transactionDTO) {
//        try {
//            // DTO를 엔티티로 변환
//            Transaction transactionDetails = transactionDTO.toEntity();
//            
//        	// 서비스 메서드 호출
//        	Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
//        	
//        	// DTO로 변환하여 반환
//        	TransactionDTO updatedTransactionDTO = new TransactionDTO(updatedTransaction);
//        	return ResponseEntity.ok(updatedTransactionDTO);
//        } catch (RuntimeException e) {
//        	// 예외 발생 시 HTTP 상태 코드와 메시지를 반환
//        	// 에러 메시지를 JSON 형태로 반환
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        }
		// DTO를 엔티티로 변환
	    Transaction transactionDetails = transactionDTO.toEntity();

	    // 서비스 메서드 호출
	    Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);

	    // DTO로 변환하여 반환
	    TransactionDTO updatedTransactionDTO = new TransactionDTO(updatedTransaction);
	    return ResponseEntity.ok(updatedTransactionDTO);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
	    try {
	        transactionService.deleteTransaction(id);
	        return ResponseEntity.ok("거래 내역 삭제 성공");
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("거래가 존재하지 않습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
	    }
	}
}
