/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import entity.Animal;

import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Oliver
 */
@Path("animals_db")
public class AnimalFromDB {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AnimalFromDB
     */
    public AnimalFromDB() {
    }

    @Path("animals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnimals() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a", Animal.class);
            List<Animal> animals = query.getResultList();
            return new Gson().toJson(animals);
        } finally {
            em.close();
        }
    }

    @Path("animalbyid/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnimal(@PathParam("id") Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Animal animal = em.find(Animal.class, id);
            if (animal == null) {
                return new Gson().toJson("not found");
            }
            return new Gson().toJson(animal.getType());
        } finally {
            em.close();
        }
    }

    @Path("animalbytype/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnimal(@PathParam("type") String type) {
        EntityManager em = emf.createEntityManager();
        try {
            Query q = em.createQuery("SELECT a FROM Animal a where a.type = :type");
            q.setParameter("type", type);

            return new Gson().toJson((Animal) q.getSingleResult());

        } catch (NoResultException e) {
            return new Gson().toJson("not found");
        } finally {
            em.close();
        }
    }

    @Path("random_animal")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRandomAnimal() {
        EntityManager em = emf.createEntityManager();
        Query q1 = em.createQuery("SELECT COUNT(a) FROM Animal a");
        Long count = (Long) q1.getSingleResult();

        if (count == 0) {
            //No rows
            return new Gson().toJson("not found");
        } else {
            //Get random
            Random random = new Random();
            int id = random.nextInt(count.intValue());

            try {
                Animal animal = em.find(Animal.class, (long)id);
                if (animal == null) {
                    return new Gson().toJson("not found");
                }
                return new Gson().toJson(animal.getType());
            } finally {
                em.close();
            }

        }

    }

}
