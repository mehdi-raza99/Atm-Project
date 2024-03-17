import java.util.*;

abstract class BankAccount {
    Scanner in = new Scanner(System.in);
    private String accountNumber, accountHolderName;
    private double balance = 0;

    BankAccount(String a, String b) {
        accountHolderName = a;
        accountNumber = b;
    }

    boolean verify(String name, String account_number) {
        if (accountHolderName.equals(name) && accountNumber.equals(account_number))
            return true;
        else
            return false;
    }

    void deposit(double amount) {
        if (amount < 0) {
            System.out.println("Negative number can't be entered, Please enter again: ");
        }
        while (amount < 0) {
            try {
                amount = in.nextDouble();
                in.nextLine();
                if (amount > 0) {
                    break;
                } else {
                    throw new Negative();
                }
            } catch (Negative e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        balance += amount;
        System.out.println("Deposit Successfull\nNew Balance: " + balance);

    }

    abstract void withdraw(double amount);

    double getBalance() {
        return balance;
    }

    void setBalance(double b) {
        balance = b;
    }
}

class SavingsAccount extends BankAccount {
    private double interestRate = 0.03;

    SavingsAccount(String a, String b) {
        super(a, b);
    }

    void withdraw(double amount) {
        double balance = super.getBalance();
        if (amount < 0) {
            System.out.println("Negative number can't be entered, Please enter again: ");
        }
        while (amount < 0) {
            try {
                amount = in.nextDouble();
                in.nextLine();
                if (amount > 0) {
                    break;
                } else {
                    throw new Negative();
                }
            } catch (Negative e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        try {
            if (amount > 0 && amount <= balance) {
                System.out.println("Withdraw Successful.");
                balance -= amount;
                setBalance(balance);
            } else {
                throw new InsufficientFunds();
            }
        } catch (InsufficientFunds e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

class CheckingAccount extends BankAccount {
    static double overdraftlimit = 20000;

    CheckingAccount(String a, String b) {
        super(a, b);
    }

    void withdraw(double amount) {
        double balance = super.getBalance();
        if (amount < 0) {
            System.out.println("Negative number can't be entered, Please enter again: ");
        }
        while (amount < 0) {
            try {
                amount = in.nextDouble();
                in.nextLine();
                if (amount > 0) {
                    break;
                } else {
                    throw new Negative();
                }
            } catch (Negative e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        if (amount > 0 && amount <= balance) {
            System.out.println("Withdraw Successful.");
            balance -= amount;
            setBalance(balance);
        } else {
            System.out.println("Your Balance is insufficient for the Withdraw. Your current balance: " + balance);
            if (amount <= overdraftlimit) {
                System.out.println("Withdraw Successful (From the Overdraft Limit).");
                overdraftlimit -= amount;
                System.out.println("Remaining Overdraft Limit: " + overdraftlimit);
            } else {
                System.out.println("Overdraft Limit has also exceeded, Withdraw not possible.");
                System.out.println("Overdraft Limit: " + overdraftlimit);

            }
        }
    }
}

interface ATMTransaction {
    void withdraw(double amount);

    void deposit(double amount);

    double getBalance();
}

class ATM implements ATMTransaction {
    BankAccount[] acc = new BankAccount[50];
    Scanner in = new Scanner(System.in);
    HashMap<Integer, BankAccount> map = new HashMap<>();
    double depositAmount, withdrawAmount;
    int choice = -1, i = 0, access;

    void start() {
        while (true) {
            try {
                System.out.println("Enter 1 for Creating account and 2 for accessing account");
                choice = in.nextInt();
                if (choice != 1 && choice != 2)
                    throw new InputMismatchException();
                else
                    break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                in.nextLine();
            }
        }
        in.nextLine();
        switch (choice) {
            case 1:
                createAcc();
                break;
            case 2:
                System.out.println("Enter name: ");
                String n = in.nextLine();
                System.out.println("Enter Account number: ");
                String p = in.nextLine();
                Access(n, p);
                break;
        }
    }

    void createAcc() {
        System.out.println("Enter your Name: ");
        String n = in.nextLine();
        Random r = new Random();
        String number = "Acc-" + r.nextInt(9000) + 1000;
        System.out.println("Your Account Number: " + number);
        System.out.println("Choose from the 2 options.");
        System.out.println("1. Checking Accounts\n2. Saving Account");
        System.out.println("Enter 1/2 : ");

        while (true) {
            try {
                choice = in.nextInt();
                if (choice == 1 || choice == 2)
                    break;
                else
                    throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input, Please Enter Again: ");
                in.nextLine();
            }
        }
        map.put(1, new CheckingAccount(n, number));
        map.put(2, new SavingsAccount(n, number));
        acc[i] = map.get(choice);
        i++;
        Menu();
    }

    void Access(String n, String p) {
        if (i == 0) {
            System.out.println("No account has been created yet!");
            start();
        }
        for (int i = 0; i < acc.length; i++) {
            access = -1;
            if (acc[i].verify(n, p)) {
                access = i;
                System.out.println("Login Successfull!");
                Menu();
                break;
            }
        }
        if (access == -1) {
            System.out.println("Not found.");
        }
    }

    void Menu() {
        while (true) {
            System.out.println("Choose from 1 to 3: \n1 - Deposit \n2 - Withdraw \n3 - Check Balance\n4 - Exit");
            while (true) {
                try {
                    choice = in.nextInt();
                    if (choice == 1 || choice == 2 || choice == 3 || choice == 4)
                        break;
                    else
                        throw new InputMismatchException();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input, Please Enter Again: ");
                    in.nextLine();
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("Enter amount to deposit: ");
                    while (true) {
                        try {
                            depositAmount = in.nextDouble();
                            in.nextLine();
                            if (depositAmount < 0 || depositAmount == 0 || depositAmount > 0) {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Input. Please enter again: ");
                            in.nextLine();
                        }
                    }
                    deposit(depositAmount);
                    break;
                case 2:
                    System.out.println("Enter amount to withdraw: ");
                    while (true) {
                        try {
                            withdrawAmount = in.nextDouble();
                            in.nextLine();
                            if (withdrawAmount < 0 || withdrawAmount == 0 || withdrawAmount > 0) {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Input. Please enter again: ");
                            in.nextLine();
                        }
                    }
                    acc[access].withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.println("Account Balance: " + acc[access].getBalance());
                    break;
                case 4:
                    start();
                    break;
                default:
                    System.out.println("Invalid Input.");
            }
        }
    }

    public void withdraw(double amount) {
        acc[access].withdraw(amount);
    }

    public void deposit(double amount) {
        acc[access].deposit(amount);
    }

    public double getBalance() {
        return acc[access].getBalance();

    }
}

class InsufficientFunds extends Exception {
    InsufficientFunds() {
        super("Your Balance is insufficient for the Withdraw.");
    }
}

class Negative extends Exception {
    Negative() {
        super("Negative number can be entered");
    }
}

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ATM atm = new ATM();
        int choice = -1;
        atm.start();
    }
}