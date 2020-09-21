package com.example.demo.forum.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for QuestionsEntity.
 * Defines method to get all Questions which are not replies.
 *
 * @author  Sathish Pendem
 */
public interface QuestionsRepository  extends JpaRepository<QuestionsEntity, Long> {
    List<QuestionsEntity> findByParentQuestionIdIsNull();
}
