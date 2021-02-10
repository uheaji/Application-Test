package com.cos.bookTest.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.bookTest.domain.BookRepository;
import com.cos.bookTest.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookTestControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
	@Test
	public void save_테스트() throws Exception {
		// given
		Book book = new Book(null, "스프링 따라하기", 4.6, 12.5);
		String content = new ObjectMapper().writeValueAsString(book); // 오브젝트를 json으로 바꾸는 함수
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then 
		resultAction
			.andExpect(jsonPath("$.title").value("스프링 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링 따라하기", 4.6, 12.5));
		books.add(new Book(null, "react 따라하기", 4.6, 11.5));
		bookRepository.saveAll(books);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.[0].title").value("스프링 따라하기"))
		.andDo(MockMvcResultHandlers.print());	
	}
	
	@Test
	public void findById_테스트() throws Exception {
		// given
		Long id = 2L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링 따라하기", 4.6, 12.5));
		books.add(new Book(null, "react 따라하기", 4.6, 12.5));
		bookRepository.saveAll(books);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
		.andExpect(jsonPath("$.title").value("react 따라하기"))
		.andDo(MockMvcResultHandlers.print()); 
	}
	
	
	@Test
	public void delete_테스트() throws Exception {
		// given
		Long id = 1L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링 따라하기", 4.6, 12.5));
		books.add(new Book(null, "react 따라하기", 4.6, 12.5));
		bookRepository.saveAll(books);
		
		
		// when (테스트 실행)
				ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id)
						.accept(MediaType.TEXT_PLAIN));
				
		// then
		resultAction
		.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		
		assertEquals("ok", result);
}
	


}
