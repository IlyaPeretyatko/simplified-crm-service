package ru.peretyatko.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toTransaction(TransactionPostRequest transactionPostRequest);
    TransactionResponse toTransactionResponse(Transaction transaction);
    void updateTransaction(TransactionPatchRequest transactionPatchRequest, @MappingTarget Transaction transaction);
}
