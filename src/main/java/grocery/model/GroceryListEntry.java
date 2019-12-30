package grocery.model;

import javax.persistence.*;

@Entity
@Table(name = "grocerylistentries")
public class GroceryListEntry {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grocerylist_id")
    private GroceryList groceryList;

    @Column(unique = false, nullable = false)
    private String name;

    private Double quantity;

    // TODO quantity unit

    public Long getId() {
        return id;
    }

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(GroceryList groceryList) {
        this.groceryList = groceryList;
    }
}