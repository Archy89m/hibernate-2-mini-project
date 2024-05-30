package com.javarush;

import com.javarush.domain.*;


public class Main {

    public static void main(String[] args) {

        MovieRentalStore movieRentalStore = new MovieRentalStore();

        Customer customer = movieRentalStore.createCustomer();
        movieRentalStore.ReturnInvToStore();
        movieRentalStore.rentInventory(customer);
        movieRentalStore.newMovieHasBeenReleased();
    }
}