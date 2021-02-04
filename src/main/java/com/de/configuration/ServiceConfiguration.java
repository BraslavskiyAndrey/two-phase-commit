package com.de.configuration;

import com.de.services.AccountManagementService;
import com.de.services.BookingService;
import com.de.services.FlyBookingService;
import com.de.services.HotelBookingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ServiceConfiguration {

    @Bean
    public AccountManagementService accountManagementService(@Qualifier("account_db") DataSource accountDbDatasource) {
        return new AccountManagementService(accountDbDatasource);
    }

    @Bean
    public FlyBookingService flyBookingService(@Qualifier("fly_db") DataSource flyDbDatasource) {
        return new FlyBookingService(flyDbDatasource);
    }

    @Bean
    public HotelBookingService hotelBookingService(@Qualifier("hotel_db") DataSource hotelDbDatasource) {
        return new HotelBookingService(hotelDbDatasource);
    }

    @Bean
    public BookingService bookingService(FlyBookingService flyBookingService,
                                         HotelBookingService hotelBookingService,
                                         AccountManagementService accountManagementService) {
        return new BookingService(flyBookingService, hotelBookingService, accountManagementService);
    }
}
