package xyz.sadiulhakim.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.sadiulhakim.stock.Stock;
import xyz.sadiulhakim.user.User;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUserAndStock(User user, Stock stock);

    List<Portfolio> findByUser(User user);
}
