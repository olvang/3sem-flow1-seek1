package dbfacade;

import dk.vangomango.customer.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerFacade {
    private static EntityManagerFactory emf;
    private static CustomerFacade instance;

    private CustomerFacade() {}

    public static CustomerFacade getCustomerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CustomerFacade();
        }
        return instance;
    }

    public Customer findByID(int id){
        EntityManager em = emf.createEntityManager();
        try{
            Customer customer = em.find(Customer.class,id);
            return customer;
        }finally {
            em.close();
        }
    }

    public Customer findByLastName(String lName){
        EntityManager em = emf.createEntityManager();
        try{
            Customer customer = (Customer) em.createQuery("SELECT c FROM Customer c where c.lastName = :lastname")
                    .setParameter("lastname", lName).getSingleResult();
            return customer;
        }finally {
            em.close();
        }
    }

    public Long getNumberOfCustomers(){
       EntityManager em = emf.createEntityManager();
        try{
            Query q1 = em.createQuery("SELECT COUNT(c) FROM Customer c");
            return ((Long) q1.getSingleResult());
        }finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers(){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Customer> query =
                    em.createQuery("Select customer from Customer customer",Customer.class);
            return query.getResultList();
        }finally {
            em.close();
        }
    }

    public Customer addCustomer(String fName, String lName){
        Customer customer = new Customer(fName, lName);
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return customer;
        }finally {
            em.close();
        }
    }
}

