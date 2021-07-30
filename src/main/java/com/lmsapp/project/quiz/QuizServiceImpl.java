package com.lmsapp.project.quiz;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

	private QuizRepository repo;

	@Override
	public List<Quiz> findAll() {
		
		return repo.findAll();
	}

	@Override
	public Quiz findById(int id) {
		Optional<Quiz> result = repo.findById(id);
		
		Quiz quiz = null;
		
		if (result.isPresent()) {
			quiz = result.get();
		}
		else {
			// not found
			throw new RuntimeException("Did not find quiz id - " + id);
		}
		
		return quiz;
	}

	@Override
	public void save(Quiz quiz) {
		repo.save(quiz);
		
	}

	@Override
	public void deleteById(int id) {
		repo.deleteById(id);
		
	}

	

}
