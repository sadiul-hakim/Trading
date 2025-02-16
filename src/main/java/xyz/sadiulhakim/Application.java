package xyz.sadiulhakim;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.sadiulhakim.stock.Stock;
import xyz.sadiulhakim.stock.StockRepository;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

import java.util.Optional;

@SpringBootApplication
public class Application {

    private final PasswordEncoder passwordEncoder;

    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    public Application(PasswordEncoder passwordEncoder, StockRepository stockRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ApplicationRunner createStocks() {
        return args -> {

            Optional<Stock> stock = stockRepository.findBySymbol("HK");
            if (stock.isEmpty()) {

                Stock ht = new Stock(null, "Hakim Trading", "HT", 200, 0);
                stockRepository.save(ht);
            }

            Optional<User> user = userRepository.findByUsername("sadiulhakim@gmail.com");
            if (user.isEmpty()) {

                User hakim = new User(null, "Sadiul Hakim", "sadiulhakim@gmail.com", 10_000,
                        passwordEncoder.encode("hakim@123"), "ROLE_ADMIN");
                userRepository.save(hakim);
            }
        };
    }
}
