package com.cos.bookTest.web;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.bookTest.domain.BookRepository;
import com.cos.bookTest.domain.Book;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookTestController {
	
	private final BookRepository bookRepository;
	
	@GetMapping("/book")
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	@GetMapping("/book/{id}")
	public Book findById(@PathVariable Long id) {
		return bookRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("id를 확인해주세요!!"));
	}
	
	
	@PostMapping("/book")
	public Book save(@RequestBody Book book) {
		return bookRepository.save(book);
	}
	
	
	@PutMapping("/book/{id}")
	public  Book  update(@PathVariable Long id, @RequestBody Book book) {
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("id를 확인해주세요")); 
		bookEntity.setTitle(book.getTitle());
		bookEntity.setRating(book.getRating());
		bookEntity.setPrice(book.getPrice());
		return bookEntity;
	}
	
	@DeleteMapping("/book/{id}")
	public String deleteById(@PathVariable Long id, @RequestBody Book book) {
		bookRepository.deleteById(id);
		return "ok";

	}

}
