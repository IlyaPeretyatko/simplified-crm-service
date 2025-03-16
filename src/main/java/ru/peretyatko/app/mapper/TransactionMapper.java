package ru.peretyatko.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.model.Transaction;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    Transaction toTransaction(TransactionPostRequest transactionPostRequest);

    @Mapping(source = "seller.id", target = "sellerId")
    TransactionResponse toTransactionResponse(Transaction transaction);

    void updateTransaction(TransactionPatchRequest transactionPatchRequest, @MappingTarget Transaction transaction);


}
