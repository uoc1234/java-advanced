package com.vti.backend.repository;

import com.vti.entity.Account;
import com.vti.entity.Role;
import com.vti.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AccountRepositoryV2 implements IAccountRepository{
    private Session session;
    @Override
    public List<Account> getAllAccount() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        try {
            session = HibernateUtils.buildSessionFactory().openSession();
            Query<Account> query = session.createQuery("From Account");
            accounts.addAll(query.list());
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return accounts;
    }

    @Override
    public void createAccount(Account account) throws SQLException {
        try {
            session = HibernateUtils.buildSessionFactory().openSession();
            account.setRole(Role.EMPLOYEE);
            session.save(account);
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void updateAccount(Account account) throws SQLException {
        try {
            session = HibernateUtils.buildSessionFactory().openSession();
            Account accountDb = session.get(Account.class, account.getId());
            accountDb.setPassword(account.getPassword());
            accountDb.setFirstName(account.getFirstName());
            accountDb.setLastName(account.getLastName());
            session.getTransaction().begin();
            session.update(accountDb);
            session.getTransaction().commit();
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public Account findById(int id) throws SQLException {
        Account account = null;
        try {
            session = HibernateUtils.buildSessionFactory().openSession();
            account = session.get(Account.class, id);
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return account;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        try {
            String hql = "DELETE FROM Account Where id = :accountID";
            session = HibernateUtils.buildSessionFactory().openSession();
            Query<Account> query = session.createQuery(hql);
            query.setParameter("accountID", id);

            session.getTransaction().begin();
            int affectedRows = query.executeUpdate();
            session.getTransaction().commit();

            if (affectedRows < 1){
                System.err.println("Xoá khôgn thành công!");
            } else {
                System.out.println("Xoá thành công!");
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public Account findByName(String name) throws SQLException {
        return null;
    }
}
