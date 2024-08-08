package com.project.demo.logic;

import com.project.demo.logic.entity.forum.Forum;
import com.project.demo.logic.entity.forum.ForumRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ForumRepoTest {

    @Autowired
    private ForumRepository forumRepository;

    @Test
    public void testReturnSave() {
        // Arrange: Crea un Role y un User con datos por defecto
        Role role = new Role();
        role.setId(1);  // Asigna un ID ficticio
        role.setName(RoleEnum.USER);  // Asigna el rol de usuario
        role.setDescription("Usuario estándar");

        User user = new User();
        user.setId(1L);
        user.setName("NombreFalso");
        user.setLastname("ApellidoFalso");
        user.setEmail("falso@correo.com");
        user.setImage("ruta/imagen.png");
        user.setPassword("password123");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setBirthDate(new Date());
        user.setRole(role);

        // Crea un forum con el user falso
        Forum forum = new Forum();
        forum.setTitle("Título de prueba");
        forum.setDescription("Descripción de prueba");
        forum.setAnonymous(false);
        forum.setCreationDate(new Date());
        forum.setUser(user);

        // Act: Guarda el forum en el repositorio
        Forum savedForum = forumRepository.save(forum);

        // Assert: Verifica que el forum fue guardado correctamente
        assertThat(savedForum).isNotNull();
        assertThat(savedForum.getForumId()).isGreaterThan(0);
        assertThat(savedForum.getTitle()).isEqualTo("Título de prueba");
        assertThat(savedForum.getUser()).isEqualTo(user);
    }

    @Test
    public void testUpdateForumTitle() {
        // Arrange: Crea un Role y un User con datos por defecto
        Role role = new Role();
        role.setId(1);  // Asigna un ID ficticio
        role.setName(RoleEnum.USER);  // Asigna el rol de usuario
        role.setDescription("Usuario estándar");

        User user = new User();
        user.setId(1L);
        user.setName("NombreFalso");
        user.setLastname("ApellidoFalso");
        user.setEmail("falso@correo.com");
        user.setImage("ruta/imagen.png");
        user.setPassword("password123");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setBirthDate(new Date());
        user.setRole(role);

        // Crea un forum con el user falso
        Forum forum = new Forum();
        forum.setTitle("Título original");
        forum.setDescription("Descripción original");
        forum.setAnonymous(false);
        forum.setCreationDate(new Date());
        forum.setUser(user);

        // Act: Guarda el forum en el repositorio
        Forum savedForum = forumRepository.save(forum);

        // Act: Recupera el forum, actualiza el título y guarda los cambios
        Forum updatedForum = forumRepository.findById(savedForum.getForumId()).orElse(null);
        assertThat(updatedForum).isNotNull();

        updatedForum.setTitle("Título actualizado");
        forumRepository.save(updatedForum);

        // Act: Recupera el forum actualizado
        Forum reloadedForum = forumRepository.findById(savedForum.getForumId()).orElse(null);

        // Assert: Verifica que el título del forum fue actualizado correctamente
        assertThat(reloadedForum).isNotNull();
        assertThat(reloadedForum.getTitle()).isEqualTo("Título actualizado");
        assertThat(reloadedForum.getDescription()).isEqualTo("Descripción original");  // Verifica que otros campos no se hayan modificado
    }

    @Test
    public void shouldRetrieveAllForumsSuccessfully() {
        // Arrange: Crea varios Roles y Users con datos por defecto
        // Arrange: Crea un Role y un User con datos por defecto
        Role role = new Role();
        role.setId(1);  // Asigna un ID ficticio
        role.setName(RoleEnum.USER);  // Asigna el rol de usuario
        role.setDescription("Usuario estándar");

        User user = new User();
        user.setId(1L);
        user.setName("NombreFalso");
        user.setLastname("ApellidoFalso");
        user.setEmail("falso@correo.com");
        user.setImage("ruta/imagen.png");
        user.setPassword("password123");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setBirthDate(new Date());
        user.setRole(role);

        Forum forum1 = new Forum();
        forum1.setTitle("Título de prueba 1");
        forum1.setDescription("Descripción de prueba 1");
        forum1.setAnonymous(false);
        forum1.setCreationDate(new Date());
        forum1.setUser(user);

        Forum forum2 = new Forum();
        forum2.setTitle("Título de prueba 2");
        forum2.setDescription("Descripción de prueba 2");
        forum2.setAnonymous(true);
        forum2.setCreationDate(new Date());
        forum2.setUser(user);

        // Act: Guarda los foros en el repositorio
        forumRepository.save(forum1);
        forumRepository.save(forum2);

        // Act: Recupera todos los foros
        List<Forum> forums = forumRepository.findAll();

        // Assert: Verifica que todos los foros fueron recuperados correctamente
        assertThat(forums).isNotEmpty();
        assertThat(forums.size()).isEqualTo(2); // Asegúrate de que la cantidad es la esperada
        assertThat(forums.get(0).getTitle()).isEqualTo("Título de prueba 1");
        assertThat(forums.get(1).getTitle()).isEqualTo("Título de prueba 2");
    }

    @Test
    public void testReturnDelete() {
        // Arrange: Crea un Role y un User con datos por defecto
        Role role = new Role();
        role.setId(1);  // Asigna un ID ficticio
        role.setName(RoleEnum.USER);  // Asigna el rol de usuario
        role.setDescription("Usuario estándar");

        User user = new User();
        user.setId(1L);
        user.setName("NombreFalso");
        user.setLastname("ApellidoFalso");
        user.setEmail("falso@correo.com");
        user.setImage("ruta/imagen.png");
        user.setPassword("password123");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setBirthDate(new Date());
        user.setRole(role);

        // Crea un forum con el user falso
        Forum forum = new Forum();
        forum.setTitle("Título de prueba");
        forum.setDescription("Descripción de prueba");
        forum.setAnonymous(false);
        forum.setCreationDate(new Date());
        forum.setUser(user);

        // Act: Guarda el forum en el repositorio
        Forum savedForum = forumRepository.save(forum);
        Long forumId = savedForum.getForumId();

        // Act: Delete the forum
        forumRepository.deleteById(forumId);

        // Assert: Verify that the forum was deleted
        assertThat(forumRepository.findById(forumId)).isEmpty();
    }

}
