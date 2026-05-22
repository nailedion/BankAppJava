package repository;

import model.Card;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {
    private static class Holder { private static final CardRepository INSTANCE = new CardRepository(); }
    public static CardRepository getInstance() { return Holder.INSTANCE; }
    private CardRepository() {}

    @Override public Card save(Card entity) { return entity; }
    @Override public Optional<Card> findById(String id) { return Optional.empty(); }
    @Override public List<Card> findAll() { return new ArrayList<>(); }
    @Override public void update(Card entity) {}
    @Override public void delete(String id) {}
}