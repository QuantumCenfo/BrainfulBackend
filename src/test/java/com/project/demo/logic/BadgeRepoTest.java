package com.project.demo.logic;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BadgeRepoTest {

    @Autowired
    private BadgeRepository badgeRepository;

    @Test
    public void testSaveBadge() {
        // Arrange: Crea un Badge con datos por defecto
        Badge badge = new Badge();
        badge.setTitle("Medalla de Oro");
        badge.setDescription("Descripción de la medalla de oro");
        badge.setUrl("http://example.com/badge.png");

        // Act: Guarda el Badge en el repositorio
        Badge savedBadge = badgeRepository.save(badge);

        // Assert: Verifica que el Badge fue guardado correctamente
        assertThat(savedBadge).isNotNull();
        assertThat(savedBadge.getBadgeId()).isGreaterThan(0);
        assertThat(savedBadge.getTitle()).isEqualTo("Medalla de Oro");
        assertThat(savedBadge.getDescription()).isEqualTo("Descripción de la medalla de oro");
        assertThat(savedBadge.getUrl()).isEqualTo("http://example.com/badge.png");
    }

    @Test
    public void testUpdateBadge() {
        // Arrange: Crea y guarda un Badge con datos por defecto
        Badge badge = new Badge();
        badge.setTitle("Medalla de Bronce");
        badge.setDescription("Descripción de la medalla de bronce");
        badge.setUrl("http://example.com/badge-bronze.png");

        Badge savedBadge = badgeRepository.save(badge);

        // Act: Recupera el Badge, actualiza el título y guarda los cambios
        Badge updatedBadge = badgeRepository.findById(savedBadge.getBadgeId()).orElse(null);
        assertThat(updatedBadge).isNotNull();

        updatedBadge.setTitle("Medalla de Plata");
        updatedBadge.setDescription("Descripción de la medalla de plata");
        updatedBadge.setUrl("http://example.com/badge-silver.png");
        badgeRepository.save(updatedBadge);

        // Act: Recupera el Badge actualizado
        Badge reloadedBadge = badgeRepository.findById(savedBadge.getBadgeId()).orElse(null);

        // Assert: Verifica que el Badge fue actualizado correctamente
        assertThat(reloadedBadge).isNotNull();
        assertThat(reloadedBadge.getTitle()).isEqualTo("Medalla de Plata");
        assertThat(reloadedBadge.getDescription()).isEqualTo("Descripción de la medalla de plata");
        assertThat(reloadedBadge.getUrl()).isEqualTo("http://example.com/badge-silver.png");
    }

    @Test
    public void testDeleteBadge() {
        // Arrange: Crea y guarda un Badge con datos por defecto
        Badge badge = new Badge();
        badge.setTitle("Medalla de Platino");
        badge.setDescription("Descripción de la medalla de platino");
        badge.setUrl("http://example.com/badge-platinum.png");

        Badge savedBadge = badgeRepository.save(badge);

        // Act: Elimina el Badge
        badgeRepository.delete(savedBadge);

        // Act: Intenta recuperar el Badge eliminado
        Badge deletedBadge = badgeRepository.findById(savedBadge.getBadgeId()).orElse(null);

        // Assert: Verifica que el Badge ya no esté presente en la base de datos
        assertThat(deletedBadge).isNull();
    }

}
