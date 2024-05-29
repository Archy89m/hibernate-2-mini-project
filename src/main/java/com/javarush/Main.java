package com.javarush;

import com.javarush.dao.*;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
        main.ReturnInvToStore();
        main.rentInventory(customer);
        main.newMovieHasBeenReleased();

    }

    private void newMovieHasBeenReleased() {

        try (Session session = sessionFactory.getCurrentSession()) {

            session.beginTransaction();

            Language language = languageDAO.getItems(0, 5).stream().findFirst().get();
            List<Category> categories = categoryDAO.getItems(0, 5);
            List<Actor> actors = actorDAO.getItems(0, 10);

            Film film = new Film();
            film.setLanguage(language);
            film.setActors(new HashSet<>(actors));
            film.setRating(Rating.PG);
            film.setSpecialFeatures(Set.of(Feature.BEHIND_THE_SCENES, Feature.TRAILERS));
            film.setLength((short) 123);
            film.setReplacementCost(BigDecimal.TEN);
            film.setRentalRate(BigDecimal.ONE);
            film.setDescription("This is a film");
            film.setTitle("The planet of Apes");
            film.setRentalDuration((byte) 44);
            film.setOriginalLanguage(language);
            film.setCategories(new HashSet<>(categories));
            film.setYear(Year.now());
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setId(film.getId());
            filmText.setFilm(film);
            filmText.setDescription("This is a film");
            filmText.setTitle("The planet of Apes");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        }
    }

    private void rentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()){

            session.beginTransaction();

            Film film = filmDAO.getFirstAvailableFilmForRent();
            Store store = storeDAO.getItems(0,1).get(0);

            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Staff staff = store.getStaff();

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rentalDAO.save(rental);

            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setCustomer(customer);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setAmount(BigDecimal.valueOf(33.12));
            payment.setStaff(staff);
            paymentDAO.save(payment);

            session.getTransaction().commit();
        }
    }

    public Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()){

            session.beginTransaction();

            Store store = storeDAO.getItems(0,1).get(0);
            City city = cityDAO.getByName("Abu Dhabi");

            Address address = new Address();
            address.setAddress("New street");
            address.setPhone("+12345678910");
            address.setCity(city);
            address.setDistrict("New district");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setFirstName("Mark");
            customer.setLastName("Zuckerberg");
            customer.setActive(true);
            customer.setEmail("123@gmail.com");
            customer.setAddress(address);
            customer.setStore(store);
            customerDAO.save(customer);

            session.getTransaction().commit();

            return customer;
        }
    }

    private void ReturnInvToStore() {

        try (Session session = sessionFactory.getCurrentSession()){

            session.beginTransaction();

            Rental rental = rentalDAO.getUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());

            rentalDAO.save(rental);

            session.getTransaction().commit();
        }
    }
}