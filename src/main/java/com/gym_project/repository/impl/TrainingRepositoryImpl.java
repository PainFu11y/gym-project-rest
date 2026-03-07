package com.gym_project.repository.impl;

import com.gym_project.entity.Trainee;
import com.gym_project.entity.Trainer;
import com.gym_project.entity.Training;
import com.gym_project.repository.TrainingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Training training) {
        entityManager.persist(training);
    }

    @Override
    @Transactional
    public Training update(Training training) {
        return entityManager.merge(training);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findByTrainee(Trainee trainee) {
        return entityManager.createQuery(
                        "SELECT t FROM Training t WHERE t.trainee = :trainee", Training.class)
                .setParameter("trainee", trainee)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findByTrainer(Trainer trainer) {
        return entityManager.createQuery(
                        "SELECT t FROM Training t WHERE t.trainer = :trainer", Training.class)
                .setParameter("trainer", trainer)
                .getResultList();
    }
}