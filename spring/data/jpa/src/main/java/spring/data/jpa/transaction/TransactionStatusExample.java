package spring.data.jpa.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@Service
@RequiredArgsConstructor
public class TransactionStatusExample {

    private final PlatformTransactionManager transactionManager;
    private final ExampleEntityRepository exampleEntityRepository;

    public ExampleEntity createExampleEntity(int count) {
        ExampleEntity exampleEntity = new ExampleEntity(0L, count);
        return exampleEntityRepository.save(exampleEntity);
    }

    public int increaseCountSetRollbackOnly(ExampleEntity exampleEntity) {
        TransactionDefinition defaultTransactionDefinition = TransactionDefinition.withDefaults();
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        try {
            exampleEntity.increaseCount();

            status.setRollbackOnly();

            exampleEntityRepository.save(exampleEntity);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }

        return exampleEntityRepository.findById(exampleEntity.getId()).get().getCount();
    }

    public int increaseCount(ExampleEntity exampleEntity) {
        TransactionDefinition defaultTransactionDefinition = TransactionDefinition.withDefaults();
        TransactionStatus status = transactionManager.getTransaction(defaultTransactionDefinition);

        try {
            exampleEntity.increaseCount();

            exampleEntityRepository.save(exampleEntity);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }

        return exampleEntityRepository.findById(exampleEntity.getId()).get().getCount();
    }

}
