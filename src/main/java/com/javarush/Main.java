package com.javarush;

import com.javarush.dao.*;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class Main {

    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public Main() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "12345");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new  AddressDAO(sessionFactory);
        categoryDAO = new  CategoryDAO(sessionFactory);
        cityDAO = new  CityDAO(sessionFactory);
        countryDAO = new  CountryDAO(sessionFactory);
        customerDAO = new  CustomerDAO(sessionFactory);
        filmDAO = new  FilmDAO(sessionFactory);
        filmTextDAO = new  FilmTextDAO(sessionFactory);
        inventoryDAO = new  InventoryDAO(sessionFactory);
        languageDAO = new  LanguageDAO(sessionFactory);
        paymentDAO = new  PaymentDAO(sessionFactory);
        rentalDAO = new  RentalDAO(sessionFactory);
        staffDAO = new  StaffDAO(sessionFactory);
        storeDAO = new  StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {
        Main main = new Main();
        Customer customer = main.createCustomer();
    }

    public Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()){
            session.beginTransaction();




            session.getTransaction().commit();


        }
    }
}