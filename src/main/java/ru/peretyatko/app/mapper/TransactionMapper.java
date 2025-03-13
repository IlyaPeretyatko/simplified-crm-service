package ru.peretyatko.app.mapper;

import org.mapstruct.Mapper;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.model.Transaction;

@Mapper
public interface TransactionMapper {
    Transaction toTransaction(TransactionPostRequest transactionPostRequest);
    TransactionPostRequest toTransactionPostRequest(Transaction transaction);
    TransactionPatchRequest toTransactionPatchRequest(Transaction transaction);
}
