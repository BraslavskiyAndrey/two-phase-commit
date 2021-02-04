package com.de.services;

public class BookingService {

    private final FlyBookingService flyBookingService;
    private final HotelBookingService hotelBookingService;
    private final AccountManagementService accountManagementService;

    public BookingService(FlyBookingService flyBookingService,
                          HotelBookingService hotelBookingService,
                          AccountManagementService accountManagementService) {
        this.flyBookingService = flyBookingService;
        this.hotelBookingService = hotelBookingService;
        this.accountManagementService = accountManagementService;
    }

    public void book(String transactionId) {
        flyBookingService.bookFlyTicket(transactionId);
        hotelBookingService.bookHotel(transactionId);
        try {
            accountManagementService.withdrawAccount(transactionId);
        } catch (RuntimeException ex) {
            flyBookingService.rollbackBooking(transactionId);
            hotelBookingService.rollbackBooking(transactionId);
            return;
        }
        flyBookingService.commitBooking(transactionId);
        hotelBookingService.commitBooking(transactionId);
        accountManagementService.commitBooking(transactionId);
    }
}
