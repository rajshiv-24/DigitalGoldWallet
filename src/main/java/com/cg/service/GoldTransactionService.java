package com.cg.service;

import com.cg.dto.*;

import java.util.List;

public interface GoldTransactionService {

    
    TransactionHistoryResponseDTO buyGold(BuyGoldRequestDTO request);

    
    TransactionHistoryResponseDTO sellGold(SellGoldRequestDTO request);

    
    PhysicalGoldTransactionResponseDTO convertToPhysical(ConvertToPhysicalRequestDTO request);

    List<VirtualGoldHoldingResponseDTO> getHoldingsByUser(Integer userId);

    List<TransactionHistoryResponseDTO> getTransactionHistoryByUser(Integer userId);

    List<PhysicalGoldTransactionResponseDTO> getPhysicalTransactionsByUser(Integer userId);

    List<TransactionHistoryResponseDTO> getAllTransactionHistory();
}
