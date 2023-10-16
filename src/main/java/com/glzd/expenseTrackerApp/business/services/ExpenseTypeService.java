package com.glzd.expenseTrackerApp.business.services;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import com.glzd.expenseTrackerApp.business.exceptions.ExpenseTypeAlreadyExistsException;
import com.glzd.expenseTrackerApp.data.ExpenseTypeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class ExpenseTypeService {
    private final ExpenseTypeRepository expenseTypeRepository;

    public ExpenseTypeService(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }

    public ExpenseType findById(Long id) {
        return expenseTypeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    
    public ExpenseType save(ExpenseType entity) throws ExpenseTypeAlreadyExistsException {
        if (expenseTypeRepository.existsByExpenseCategoryIgnoreCase(entity.getExpenseCategory())){
            throw new ExpenseTypeAlreadyExistsException("Expense type with name '" + entity.getExpenseCategory() + "' already exists.");
        }
        return expenseTypeRepository.save(entity);

    }


   
    @PostConstruct
    public void init() {
        Iterable<ExpenseType> allExpenses = expenseTypeRepository.findAll();
        if (((Collection<?>) allExpenses).isEmpty()) {
            ExpenseType defaultExpenseType = new ExpenseType();
            expenseTypeRepository.save(defaultExpenseType);
        }
    }


    public Iterable<ExpenseType> findAll() {
        return expenseTypeRepository.findAll();
    }

    public void deleteById(Long id) {
        ExpenseType expenseTypeToBeDeleted = findById(id);
        expenseTypeRepository.delete(expenseTypeToBeDeleted);
    }

}
