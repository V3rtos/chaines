package me.moonways.bridgenet.test.jdbc.dao;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.security.BasicCredentials;
import me.moonways.bridgenet.jdbc.dao.EntityAccessCondition;
import me.moonways.bridgenet.jdbc.dao.EntityDao;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.jdbc.entity.Status;
import me.moonways.bridgenet.test.engine.jdbc.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@Log4j2
@RunWith(BridgenetJUnitTestRunner.class)
public class UserDaoTest {

    private static final Status STATUS_1 = new Status("CEO_1");
    private static final Status STATUS_2 = new Status("CEO_2");
    private static final Status STATUS_3 = new Status("CEO_3");

    private static final User USER_1 = User.builder().firstName("Oleg").lastName("Saburov").status(STATUS_1).build();
    private static final User USER_2 = User.builder().firstName("Piter").lastName("Parker").status(STATUS_2).build();
    private static final User USER_3 = User.builder().firstName("Mark").lastName("Zukerberg").status(STATUS_3).build();

    @Inject
    private DatabaseProvider provider;
    @Inject
    private DatabaseConnection connection;

    private EntityDao<Status> statusDao;
    private EntityDao<User> userDao;

    @Before
    public void setUp() {
        this.statusDao = provider.createDao(Status.class, connection);
        this.userDao = provider.createDao(User.class, connection);

        doInsertUsers();
    }

    public void doInsertUsers() {
        List<Long> insertedUsersIdList = userDao.insertMany(USER_1, USER_2, USER_3);
        log.debug(insertedUsersIdList);

        assertEquals(3, insertedUsersIdList.size());
        assertEquals(1L, (long)insertedUsersIdList.get(0));
        assertEquals(2L, (long)insertedUsersIdList.get(1));
        assertEquals(3L, (long)insertedUsersIdList.get(2));
    }

    @Test
    public void test_findStatusesAll() {
        List<Status> statusList = statusDao.findAll();
        log.debug(statusList);

        assertEquals(3, statusList.size());

        Status firstStatus = statusList.get(0);

        assertStatus(firstStatus, STATUS_1);
    }

    @Test
    public void test_findUsersAll() {
        List<User> usersList = userDao.findAll();
        log.debug(usersList);

        assertEquals(3, usersList.size());

        User firstUser = usersList.get(0);

        assertUser(firstUser, USER_1);
    }

    @Test
    public void test_findUserById() {
        long expectedUserId = 2;

        Optional<User> userByIdOptional = userDao.findMonoById(expectedUserId);
        User userById = userByIdOptional.orElse(null);

        log.debug(userById);

        assertNotNull(userById);
        assertUser(userById, USER_2);
    }

    @Test
    public void test_findUserByFirstName() {
        String expectedUserFirstName = USER_2.getFirstName();

        Optional<User> userByFirstNameOptional = userDao.findMono(EntityAccessCondition.createMono("first_name", expectedUserFirstName));
        User userByFirstName = userByFirstNameOptional.orElse(null);

        log.debug(userByFirstName);

        assertNotNull(userByFirstName);
        assertStatus(userByFirstName.getStatus(), USER_2.getStatus());
    }

    private void assertUser(User expected, User actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertStatus(expected.getStatus(), actual.getStatus());
    }

    private void assertStatus(Status expected, Status actual) {
        assertEquals(expected.getName(), actual.getName());
    }
}
