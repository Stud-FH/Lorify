package fh.server.service;

import fh.server.entity.Account;
import fh.server.repository.PollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuestionService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionService.class);
//
//
//    private final PollRepository pollRepository;
//
//    public QuestionService(
//            @Qualifier("questionRepository") PollRepository pollRepository
//    ) {
//        this.pollRepository = pollRepository;
//    }
//
//    public Question fetchById(String id) {
//        return pollRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found"));
//    }
//
//    public void verifyOwner(Question question, Account account) {
//        if (!question.hasOwner(account))
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you do not own this resource");
//    }
//
//    public Question createQuestion(Account owner) {
//        Question question = new Question();
//        question.addOwner(owner);
//        question = pollRepository.saveAndFlush(question);
//        LOGGER.info(String.format("question created (id=%s)", question.getId()));
//        return question;
//    }
//
//    public Question updateFormulation(String id, String formulation) {
//        Question question = fetchById(id);
//        String previous = question.getFormulation();
//        question.setFormulation(formulation);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s formulation updated: %s (previous: %s)", id, formulation, previous));
//        return question;
//    }
//
//    public Question updateDesc(String id, String desc) {
//        Question question = fetchById(id);
//        String previous = question.getDesc();
//        question.setDesc(desc);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s desc updated: %s (previous: %s)", id, desc, previous));
//        return question;
//    }
//
//    public Question addOwner(String id, Account owner) {
//        Question question = fetchById(id);
//        question.addOwner(owner);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s owner added: %s", id, owner));
//        return question;
//    }
//
//    public Question updateQuestionType(String id, QuestionType questionType) {
//        Question question = fetchById(id);
//        QuestionType previous = question.getQuestionType();
//        question.setQuestionType(questionType);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s type updated: %s (previous: %s)", id, questionType, previous));
//        return question;
//    }
//
//    public Question addOption(String id, String option) {
//        Question question = fetchById(id);
//        if (option == null || option.isEmpty())
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "cannot add empty option");
//        if (question.getOptions().contains(option))
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "duplicate option");
//        question.addOption(option);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s option added: %s", id, option));
//        return question;
//    }
//
//    public Question removeOption(String id, String option) {
//        Question question = fetchById(id);
//        question.removeOption(option);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s option removed: %s", id, option));
//        return question;
//    }
//
//    public Question updateLimit(String id, Integer limit) {
//        Question question = fetchById(id);
//        if (limit != null && limit < 0)
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "limit must be positive");
//        Integer previous = question.getLimit();
//        question.setLimit(limit);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s limit updated: %s (previous: %s)", id, limit, previous));
//        return question;
//    }
//
//    public Question updateGuard(String id, Guard guard) {
//        Question question = fetchById(id);
//        Guard previous = question.getGuard();
//        question.setGuard(guard);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s guard updated: %s (previous: %s)", id, guard, previous));
//        return question;
//    }
//
//    public Question putOptionValue(String id, String option, Integer value) {
//        Question question = fetchById(id);
//        if (option == null || option.isEmpty())
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "option required");
//        Integer previous = question.weighOption(option);
//        question.putOptionValue(option, value);
//        pollRepository.flush();
//        value = question.weighOption(option);
//        LOGGER.info(String.format("question %s option %s value updated: %s (previous: %s)", id, option, value, previous));
//        return question;
//    }
//
//    public Question submit(String id, String input, Account account) {
//        Question question = fetchById(id);
//        question.submit(account, input);
//        pollRepository.flush();
//        LOGGER.info(String.format("question %s: %s submitted \"%s\"", id, account, input));
//        return question;
//    }
//
//    private void checkConsistency(Question question) {
//        if (question.getQuestionType() == null)
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "question type is required");
//        String formulation = question.getFormulation();
//        if (formulation == null || formulation.isEmpty())
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "formulation is required");
//        if (question.getOptions().isEmpty())
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "at least one option is required");
//    }
}
