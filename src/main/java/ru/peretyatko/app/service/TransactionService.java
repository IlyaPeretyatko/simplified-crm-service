package ru.peretyatko.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.repository.SellerRepository;
import ru.peretyatko.app.repository.TransactionRepository;
import ru.peretyatko.app.util.TransactionNotFoundException;
import ru.peretyatko.app.util.SellerNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final SellerRepository sellerRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, SellerRepository sellerRepository) {
        this.transactionRepository = transactionRepository;
        this.sellerRepository = sellerRepository;
    }

    @Transactional
    public Transaction add(Transaction transaction) {
        Seller seller = transaction.getSeller();
        if (sellerRepository.existsById(seller.getId())) {
            return transactionRepository.save(transaction);
        } else {
            throw new SellerNotFoundException();
        }
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(long id) {
        return transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    @Transactional
    public Transaction update(long id, Transaction updatedTransaction) {
        Transaction transaction = findById(id);
        if (updatedTransaction.getAmount() != null) {
            transaction.setAmount(updatedTransaction.getAmount());
        }
        if (updatedTransaction.getPaymentType() != null) {
            transaction.setPaymentType(updatedTransaction.getPaymentType());
        }
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void delete(long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
        } else {
            throw new TransactionNotFoundException();
        }
    }

}
