## GRASP

GRASP(General Responsibility Assignment Software Patterns(Principles))는 소프트웨어 설계 원칙으로 소프트웨어 개발 프로젝트에서 자주 맞닥뜨리는 객체 설계와 책임 할당에 대한 문제를 해결할 수 있는 9가지 패턴을 제안한다

Information Expert, Creator, Low Coupling, High Cohesion, Controller, Polymorphism, Pure Fabrication, Indirection, Protected Variations

## Information Expert

객체에 책임을 할당해야 하는 기본 원칙은 **정보를 알고 있어야 하거나, 책임을 수행하는 데 필요한 데이터를 잘 알고 있는 객체에게 책임을 할당**하는 것으로 메서드나 필드 등으로 책임을 위임할 위치를 결정하기 위해 사용되는 원칙이다

이는 객체의 데이터와 행동을 일치시켜 응집도를 높이는데 기여한다

**은행 계좌와 잔액 관리 예시 코드**

```java
class Account {
    
    private double balance;
    
    public Account(double balance) {
        this.balance = balance;
    }
    
    public boolean withdraw(double amount) {
        if (balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public double getBalance() {
        return balance;
    }
}
```

```java
class ATM {
    
    public void processWithdraw(Account account, double amount) {
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful. Remaining balance: " + account.getBalance());
        } else {
            System.out.println("Insufficient balance");
        }
    }
}
```

Account는 잔액 정보(balance)를 가지고 있는 객체로서 잔액 관리 책임을 수행하기 충분하므로 withdraw, deposit, getBalance 메서드를 가지고 있다

ATM은 특정 계좌에 대한 잔액 데이터를 가지고 있을 수 없기 때문에 잔액 관리 책임을 수행하기 적절치 않다

따라서 계좌 객체에게 출금 처리를 위임하고, 출금 처리 결과를 출력하는 책임을 가지고 있다

Information Expert 원칙을 적용하면 책임이 적절히 분배돼서 응집도가 높아지며, Account 객체의 내부 구현이 변경되더라도 ATM 객체는 영향을 받지 않는다









