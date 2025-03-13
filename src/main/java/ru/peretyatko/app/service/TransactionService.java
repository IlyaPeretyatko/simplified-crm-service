package ru.peretyatko.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.mapper.TransactionMapper;
import ru.peretyatko.app.repository.TransactionRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(long id) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions() {
        return null;
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionPostRequest transactionPostRequest) {
        return null;
    }

    @Transactional
    public TransactionResponse updateTransaction(long id, TransactionPatchRequest transactionPatchRequest) {
        return null;
    }

    @Transactional
    public void deleteTransaction(long id) {

    }


}
