package ru.peretyatko.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.peretyatko.app.mapper.TransactionMapper;
import ru.peretyatko.app.repository.TransactionRepository;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;



}
