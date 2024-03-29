package remembrall.model.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import remembrall.model.GroceryList;
import remembrall.model.User;

import java.util.List;
import java.util.Optional;

public interface GroceryListRepository extends JpaRepository<GroceryList, Long>, CustomGroceryListRepository {
    List<GroceryList> findByUsers(User user);

    Optional<GroceryList> findByIdAndUsers(Long id, User user);

    @EntityGraph(value = "GroceryList.Users")
    Optional<GroceryList> findEagerUsersById(Long id);
}
