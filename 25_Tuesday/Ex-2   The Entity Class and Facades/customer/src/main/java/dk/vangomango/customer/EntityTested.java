package dk.vangomango.customer;


import dbfacade.CustomerFacade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityTested {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        CustomerFacade facade = CustomerFacade.getCustomerFacade(emf);

        // Add
        Customer c1 = facade.addCustomer("Oliver","vang");
        Customer c2 = facade.addCustomer("Ja","nej");

        System.out.println("--- Find by id");
        System.out.println("Customer c1: " + facade.findByID(c1.getId()).getFirstName());
        System.out.println("Customer c1: " + facade.findByID(c2.getId()).getFirstName());

        System.out.println("--- Find by lastname");
        System.out.println(facade.findByLastName("vang").getFirstName());


        System.out.println("--- Find All");
        for (Customer customer : facade.getAllCustomers())
        {
            System.out.println("Customer: " + facade.findByID(customer.getId()).getFirstName());
        }

        System.out.println("--- Get total count");
        System.out.println("Count: " + facade.getNumberOfCustomers());

    }


}

