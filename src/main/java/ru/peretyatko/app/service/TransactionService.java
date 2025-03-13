package ru.peretyatko.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.error.exception.ServiceException;
import ru.peretyatko.app.mapper.TransactionMapper;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Transaction wasn't found."));
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions() {
        return transactionRepository.findAll().stream().map(transactionMapper::toTransactionResponse).collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionPostRequest transactionPostRequest) {
        Transaction transaction = transactionMapper.toTransaction(transactionPostRequest);
        Transaction createdTransaction = transactionRepository.save(transaction);
        createdTransaction.setTransactionDate(LocalDateTime.now());
        return transactionMapper.toTransactionResponse(createdTransaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(long id, TransactionPatchRequest transactionPatchRequest) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Transaction wasn't found."));
        transactionMapper.updateTransaction(transactionPatchRequest, transaction);
        return transactionMapper.toTransactionResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(long id) {
        if (transactionRepository.existsById(id)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Transaction wasn't found.");
        }
        transactionRepository.deleteById(id);
    }


}
