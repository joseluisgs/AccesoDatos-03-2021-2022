import database.DataBaseController
import hibernate.Departamento
import hibernate.Empleado
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.io.File
import java.io.FileNotFoundException
import java.sql.SQLException
import java.time.LocalDateTime
import javax.persistence.Persistence


@DisplayName("JUnit5 Test CRUD Empleados")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class EmpleadoCRUDTest {
    val entityManagerFactory = Persistence.createEntityManagerFactory("default")
    val entityManager = entityManagerFactory.createEntityManager()
    val transaction = entityManager.transaction

    @BeforeAll
    fun beforeAll() {
        // Cargamos nuestro fichero sql con los datos de test
        // Obviamente este fichero debería tener una base de datos distinta o ser un server de desarrollo
        val sqlFile = System.getProperty("user.dir") + File.separator + "sql" + File.separator + "mydb.sql"
        println(sqlFile)
        try {
            DataBaseController.open()
            DataBaseController.initData(sqlFile)
            DataBaseController.close()
        } catch (e: SQLException) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.message)
            System.exit(1)
        } catch (e: FileNotFoundException) {
            System.err.println("Error al leer el fichero de datos iniciales: " + e.message)
            System.exit(1)
        }
    }

    @AfterAll
    fun afterAll() {
        if (transaction.isActive) {
            transaction.rollback()
        }
        entityManager.close()
        entityManagerFactory.close()

        // Al terminar todo s los tests, borramos la base de datos y cargamos la otra
        val sqlFile = System.getProperty("user.dir") + File.separator + "sql" + File.separator + "mydb.sql"
        println(sqlFile)
        try {
            DataBaseController.open()
            DataBaseController.initData(sqlFile)
            DataBaseController.close()
        } catch (e: SQLException) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.message)
            System.exit(1)
        } catch (e: FileNotFoundException) {
            System.err.println("Error al leer el fichero de datos iniciales: " + e.message)
            System.exit(1)
        }
    }


  /*  @BeforeEach
    fun setUp() {
        println("Me ejecuto antes de cada test. No soy Obligatorio");
    }

    @AfterEach
    fun setDown() {
        println("Me ejecuto después de cada test. No soy Obligatorio");
    }*/

    @Test
    @Order(1)
    @DisplayName("Get All Empleados")
    fun getAllTest() {
        // Consultamos
        val empleados = entityManager.createNamedQuery("Empleado.findAll", Empleado::class.java)
            .resultList
        val empleado =empleados.get(0);
        Assertions.assertAll(
            { assertNotNull(empleados) },
            { assertEquals(1, empleado.id) },
            { assertEquals("Pepe", empleado.nombre) },
            { assertEquals("Perez", empleado.apellidos) },
        )
    }

    @Test
    @Order(2)
    @DisplayName("Find Empleado by Id")
    fun findByIdTest() {
        // Consultamos
        val empleado = entityManager.find(Empleado::class.java, 1L)
        Assertions.assertAll(
            { assertNotNull(empleado) },
            { assertEquals(1, empleado.id) },
            { assertEquals("Pepe", empleado.nombre) },
            { assertEquals("Perez", empleado.apellidos) },
        )
    }

    @Test
    @Order(3)
    @DisplayName("Save Empleado")
    fun saveTest() {
        val departamento = entityManager.find(Departamento::class.java, 1L)

        val insert = Empleado()
        insert.nombre = "Insert " + LocalDateTime.now().toString()
        insert.apellidos = LocalDateTime.now().toString()
        insert.departamento = departamento

        transaction.begin()
        entityManager.persist(insert)
        transaction.commit()

        val empleado = entityManager.find(Empleado::class.java, insert.id)
        Assertions.assertAll(
            { assertNotNull(insert) },
            { assertEquals(insert, empleado) },
        )
    }

    @Test
    @Order(4)
    @DisplayName("Update Empleado")
    fun updateTest() {
        val update = entityManager.find(Empleado::class.java, 4L)
        update.nombre = "update " + LocalDateTime.now().toString()
        update.apellidos = LocalDateTime.now().toString()

        transaction.begin()
        entityManager.merge(update)
        transaction.commit()

        val empleado = entityManager.find(Empleado::class.java, update.id)
        Assertions.assertAll(
            { assertNotNull(update) },
            { assertEquals(update, empleado) },
        )
    }

    @Test
    @Order(4)
    @DisplayName("Delete Empleado")
    fun deleteTest() {
        val delete = entityManager.find(Empleado::class.java, 4L)

        transaction.begin()
        entityManager.remove(delete)
        transaction.commit()

        val empleado = entityManager.find(Empleado::class.java, delete.id)
        Assertions.assertAll(
            { assertNotNull(delete) },
            { assertNull(empleado) },
        )
    }

}