package com.example.gceolmcqs.datamodels

data class TransactionInfo(val packageName: String,
                           val amountToPay: String,
                           val depositAccountNumber: String,
                           val depositAccountName: String,
                           val transactionUUid: String,
                           val transactionStatus: String)
