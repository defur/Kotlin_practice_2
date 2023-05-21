package ua.digijed2.android.kotlinpractice2

//Task 1

class TimeSpan(private var hours: Int, private var minutes: Int) {

    init {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw IllegalArgumentException("Invalid time interval")
        }
    }

    fun getHours(): Int {
        return hours
    }

    fun getMinutes(): Int {
        return minutes
    }

    fun add(hours: Int, minutes: Int): TimeSpan {
        val totalMinutes = this.hours * 60 + this.minutes + hours * 60 + minutes
        val newHours = totalMinutes / 60
        val newMinutes = totalMinutes % 60
        return TimeSpan(newHours, newMinutes)
    }

    fun addTimeSpan(timeSpan: TimeSpan): TimeSpan {
        return add(timeSpan.hours, timeSpan.minutes)
    }

    fun getDecimalHours(): Double {
        return hours + minutes / 60.0
    }

    fun getTotalMinutes(): Int {
        return hours * 60 + minutes
    }

    fun subtract(timeSpan: TimeSpan): TimeSpan {
        val totalMinutes = getTotalMinutes() - timeSpan.getTotalMinutes()
        if (totalMinutes < 0) {
            throw IllegalArgumentException("Cannot subtract a larger time interval")
        }
        val newHours = totalMinutes / 60
        val newMinutes = totalMinutes % 60
        return TimeSpan(newHours, newMinutes)
    }

    fun multiply(factor: Double): TimeSpan {
        if (factor <= 0) {
            throw IllegalArgumentException("Factor must be greater than zero")
        }
        val totalMinutes = (hours * 60 + minutes) * factor
        val newHours = totalMinutes.toInt() / 60
        val newMinutes = (totalMinutes.toInt() % 60)
        return TimeSpan(newHours, newMinutes)
    }
}

//Task  2

open class BankAccount(private var balance: Double = 0.0, private var transactionFee: Double = 1.0) {

    init {
        require(transactionFee > 0) { "Комісія повинна бути більше 0" }
    }

    fun deposit(amount: Double) {
        require(amount > 0) { "Сума для внесення повинна бути більше 0" }
        balance += amount
    }

    open fun withdraw(amount: Double) {
        require(amount > 0) { "Сума для зняття повинна бути більше 0" }
        val totalAmount = amount + transactionFee
        require(balance >= totalAmount) { "На рахунку недостатньо коштів" }
        balance -= totalAmount
    }

    open fun transfer(amount: Double, recipientAccount: BankAccount) {
        require(amount > 0) { "Сума для переказу повинна бути більше 0" }
        val totalAmount = amount + transactionFee
        require(balance >= totalAmount) { "На рахунку недостатньо коштів для переказу" }
        balance -= totalAmount
        recipientAccount.deposit(amount)
    }

    fun setTransactionFee(fee: Double) {
        require(fee > 0) { "Комісія повинна бути більше 0" }
        transactionFee = fee
    }
    fun getTransactionFee(): Double {
        return transactionFee
    }
    fun getBalance(): Double {
        return balance
    }
}

//Task 3
class MinMaxAccount(balance: Double = 0.0, transactionFee: Double = 1.0) : BankAccount(balance, transactionFee) {
    private var minBalance: Double = balance
    private var maxBalance: Double = balance

    fun getMinBalance(): Double {
        return minBalance
    }

    fun getMaxBalance(): Double {
        return maxBalance
    }

    fun withdrawWithMinMax(amount: Double) {
        if (amount <= getBalance()) {
            withdraw(amount)
            updateMinMaxBalance()
        } else {
            throw IllegalArgumentException("Insufficient funds to withdraw.")
        }
    }

    fun transferWithMinMax(amount: Double, recipient: BankAccount) {
        if (amount <= getBalance()) {
            transfer(amount, recipient)
            updateMinMaxBalance()
        } else {
            throw IllegalArgumentException("Insufficient funds to transfer.")
        }
    }

    private fun updateMinMaxBalance() {
        val currentBalance = getBalance()
        if (currentBalance < minBalance) {
            minBalance = currentBalance
        }
        if (currentBalance > maxBalance) {
            maxBalance = currentBalance
        }
    }
}



fun main() {
    println("Демонстрація роботи класу TimeSpan:")
    // створення об'єкту TimeSpan
    val timeSpan = TimeSpan(2, 30)

    // години та хвилини
    println("Години: ${timeSpan.getHours()}")
    println("Хвилини: ${timeSpan.getMinutes()}")

    // Додавання до інтервалу годин та хвилин
    val addedTimeSpan = timeSpan.add(1, 45)
    println("Після додавання: ${addedTimeSpan.getHours()} год. ${addedTimeSpan.getMinutes()} хв.")

    // Додавання іншого об'єкту TimeSpan
    val otherTimeSpan = TimeSpan(3, 15)
    val addedTimeSpan2 = timeSpan.addTimeSpan(otherTimeSpan)
    println("Після додавання іншого інтервалу: ${addedTimeSpan2.getHours()} год. ${addedTimeSpan2.getMinutes()} хв.")

    // Кількість годин у вигляді дробового числа
    val decimalHours = timeSpan.getDecimalHours()
    println("Кількість годин (дробове число): $decimalHours")

    // Кількість хвилин у поточному періоді
    val totalMinutes = timeSpan.getTotalMinutes()
    println("Загальна кількість хвилин: $totalMinutes хв.")

    // Віднімання від поточного інтервалу
    val timeSpan1 = TimeSpan(2, 30)
    val timeSpan2 = TimeSpan(1, 45)

    val subtractedTimeSpan = timeSpan1.subtract(timeSpan2)
    println("Після віднімання: ${subtractedTimeSpan.getHours()} год. ${subtractedTimeSpan.getMinutes()} хв.")

    // Збільшення інтервалу у factor разів
    val multipliedTimeSpan = timeSpan.multiply(2.5)
    println("Після збільшення у 2.5 рази: ${multipliedTimeSpan.getHours()} год. ${multipliedTimeSpan.getMinutes()} хв.")



    println("\n")
    println("Демонстрація роботи класу BankAccount:")

    val account1 = BankAccount()
    val account2 = BankAccount(1000.0, 2.0)

    println("Рахунок 1: ${account1.getBalance()} грн")
    println("Рахунок 2: ${account2.getBalance()} грн")

    account1.deposit(500.0)
    println("Після внесення на рахунок 1: ${account1.getBalance()} грн")

    account1.withdraw(200.0)
    println("Після зняття з рахунку 1: ${account1.getBalance()} грн")

    account1.transfer(100.0, account2)
    println("Після переказу з рахунку 1 на рахунок 2:")
    println("Рахунок 1: ${account1.getBalance()} грн")
    println("Рахунок 2: ${account2.getBalance()} грн")

    account2.setTransactionFee(3.0)
    println("Після зміни комісії на рахунку 2: ${account2.getTransactionFee()} грн")




    println("\n")
    println("Демонстрація роботи класу MinMaxAccount:")

    val myAccount = MinMaxAccount(1000.0) //початковий баланс 1000
    val recipientAccount = BankAccount(500.0) // початковий баланс 500

    println("Початковий баланс мого рахунку: ${myAccount.getBalance()}")
    println("Мінімальний баланс мого рахунку: ${myAccount.getMinBalance()}")
    println("Максимальний баланс мого рахунку: ${myAccount.getMaxBalance()}\n")

    println("Знімаємо 300 грн:")
    myAccount.withdrawWithMinMax(300.0)

    println("Поточний баланс мого рахунку: ${myAccount.getBalance()}")
    println("Мінімальний баланс мого рахунку: ${myAccount.getMinBalance()}")
    println("Максимальний баланс мого рахунку: ${myAccount.getMaxBalance()}\n")

    println("Переводимо 500 грн на інший рахунок:")
    myAccount.transferWithMinMax(500.0, recipientAccount)

    println("Поточний баланс мого рахунку: ${myAccount.getBalance()}")
    println("Мінімальний баланс мого рахунку: ${myAccount.getMinBalance()}")
    println("Максимальний баланс мого рахунку: ${myAccount.getMaxBalance()}")
    println("Поточний баланс рахунку одержувача: ${recipientAccount.getBalance()}")
}
