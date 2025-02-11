package xyz.sadiulhakim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    private final StockRepository stockRepository;
//    private final UserRepository userRepository;
//
//    public Application(StockRepository stockRepository, UserRepository userRepository) {
//        this.stockRepository = stockRepository;
//        this.userRepository = userRepository;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    ApplicationRunner createStocks() {
//        return args -> {
//            Stock hk = new Stock(null, "Hakim Stock", "HK", 150, 0);
//            Stock ashk = new Stock(null, "Ashik Stock", "ASHK", 120, 0);
//            Stock rkb = new Stock(null, "Rakib Stock", "RKB", 140, 0);
//
//            stockRepository.saveAll(List.of(hk, ashk, rkb));
//        };
//    }

//    @Bean
//    ApplicationRunner createUser(){
//        return args -> {
//            User hakim = new User(null, "Sadiul Hakim", 10_000);
//            userRepository.save(hakim);
//        };
//    }

//    @Bean
//    ApplicationRunner passwordGenerator(){
//        return args -> {
//            System.out.println(passwordEncoder.encode("hakim@123"));
//        };
//    }
}
