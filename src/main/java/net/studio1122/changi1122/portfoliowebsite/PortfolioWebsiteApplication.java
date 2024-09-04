package net.studio1122.changi1122.portfoliowebsite;

import net.studio1122.changi1122.portfoliowebsite.domain.customer.Customer;
import net.studio1122.changi1122.portfoliowebsite.domain.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioWebsiteApplication.class, args);
    }

}
