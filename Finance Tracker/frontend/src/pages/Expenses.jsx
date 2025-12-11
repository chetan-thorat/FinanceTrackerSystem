import React, { useEffect, useState } from 'react'
import api from '../services/api'
import ExpenseForm from '../components/ExpenseForm'
import { format } from 'date-fns'

const Expenses = () => {
  const [expenses, setExpenses] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editingExpense, setEditingExpense] = useState(null)

  useEffect(() => {
    fetchExpenses()
    fetchCategories()
  }, [])

  const fetchExpenses = async () => {
    try {
      const response = await api.get('/expenses')
      setExpenses(response.data)
    } catch (error) {
      console.error('Error fetching expenses:', error)
    } finally {
      setLoading(false)
    }
  }

  const fetchCategories = async () => {
    try {
      const response = await api.get('/categories')
      setCategories(response.data)
    } catch (error) {
      console.error('Error fetching categories:', error)
    }
  }

  const handleCreate = () => {
    setEditingExpense(null)
    setShowForm(true)
  }

  const handleEdit = (expense) => {
    setEditingExpense(expense)
    setShowForm(true)
  }

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this expense?')) {
      try {
        await api.delete(`/expenses/${id}`)
        fetchExpenses()
      } catch (error) {
        console.error('Error deleting expense:', error)
        alert('Failed to delete expense')
      }
    }
  }

  const handleFormClose = () => {
    setShowForm(false)
    setEditingExpense(null)
  }

  const handleFormSubmit = () => {
    fetchExpenses()
    handleFormClose()
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Expenses</h1>
        <button
          onClick={handleCreate}
          className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          + Add Expense
        </button>
      </div>

      {showForm && (
        <ExpenseForm
          expense={editingExpense}
          categories={categories}
          onClose={handleFormClose}
          onSubmit={handleFormSubmit}
        />
      )}

      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <ul className="divide-y divide-gray-200">
          {expenses.length === 0 ? (
            <li className="px-6 py-4 text-center text-gray-500">
              No expenses found. Add your first expense!
            </li>
          ) : (
            expenses.map((expense) => (
              <li key={expense.id} className="px-6 py-4">
                <div className="flex items-center justify-between">
                  <div className="flex items-center flex-1">
                    <div className="text-2xl mr-4">{expense.categoryIcon}</div>
                    <div className="flex-1">
                      <div className="flex items-center">
                        <p className="text-sm font-medium text-gray-900">
                          {expense.categoryName}
                        </p>
                        <span className="ml-2 text-sm text-gray-500">
                          {format(new Date(expense.expenseDate), 'MMM dd, yyyy')}
                        </span>
                      </div>
                      <p className="text-sm text-gray-500 mt-1">
                        {expense.description || 'No description'}
                      </p>
                      {expense.paymentMethod && (
                        <p className="text-xs text-gray-400 mt-1">
                          Payment: {expense.paymentMethod}
                        </p>
                      )}
                    </div>
                  </div>
                  <div className="flex items-center space-x-4">
                    <span className="text-lg font-semibold text-gray-900">
                      ${expense.amount.toFixed(2)}
                    </span>
                    <button
                      onClick={() => handleEdit(expense)}
                      className="text-blue-600 hover:text-blue-800 text-sm font-medium"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(expense.id)}
                      className="text-red-600 hover:text-red-800 text-sm font-medium"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </li>
            ))
          )}
        </ul>
      </div>
    </div>
  )
}

export default Expenses

