package com.finora.finora.Repository;

import com.finora.finora.Model.JenisKategori;
import com.finora.finora.Model.Kategori;
import com.finora.finora.Model.Pemasukan;
import com.finora.finora.Model.Pengeluaran;
import com.finora.finora.Model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class KategoriRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private KategoriRepository kategoriRepository;

    @Test
    void findAvailableByUserId_includesLegacyCategoriesReferencedByUsersTransactionsOnly() {
        User firstUser = persistUser("first");
        User secondUser = persistUser("second");

        Kategori ownedCategory = persistCategory("Gaji", JenisKategori.PEMASUKAN, firstUser);
        Kategori legacyIncomeCategory = persistCategory("Freelance", JenisKategori.PEMASUKAN, null);
        Kategori legacyExpenseCategory = persistCategory("Makan", JenisKategori.PENGELUARAN, null);
        Kategori otherUsersLegacyCategory = persistCategory("Other", JenisKategori.PEMASUKAN, null);
        persistCategory("Unused legacy", JenisKategori.PEMASUKAN, null);
        persistCategory("Other user's category", JenisKategori.PEMASUKAN, secondUser);

        persistIncome(firstUser, legacyIncomeCategory);
        persistExpense(firstUser, legacyExpenseCategory);
        persistIncome(secondUser, otherUsersLegacyCategory);

        List<Kategori> categories = kategoriRepository.findAvailableByUserId(firstUser.getId());

        assertThat(categories)
                .extracting(Kategori::getNama)
                .containsExactlyInAnyOrder("Gaji", "Freelance", "Makan");
        assertThat(categories)
                .extracting(Kategori::getId)
                .contains(ownedCategory.getId(), legacyIncomeCategory.getId(), legacyExpenseCategory.getId())
                .doesNotContain(otherUsersLegacyCategory.getId());
    }

    private User persistUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        entityManager.persist(user);
        return user;
    }

    private Kategori persistCategory(String name, JenisKategori type, User owner) {
        Kategori category = new Kategori();
        category.setNama(name);
        category.setJenis(type);
        category.setUser(owner);
        entityManager.persist(category);
        return category;
    }

    private void persistIncome(User user, Kategori category) {
        Pemasukan income = new Pemasukan();
        income.setUser(user);
        income.setKategori(category);
        income.setTanggal(LocalDate.now());
        income.setJumlah(BigDecimal.ONE);
        entityManager.persist(income);
    }

    private void persistExpense(User user, Kategori category) {
        Pengeluaran expense = new Pengeluaran();
        expense.setUser(user);
        expense.setKategori(category);
        expense.setTanggal(LocalDate.now());
        expense.setJumlah(BigDecimal.ONE);
        entityManager.persist(expense);
    }
}
