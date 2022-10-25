package es.joseluisgs.dam;

import es.joseluisgs.dam.database.DataBaseController;
import es.joseluisgs.dam.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        checkServer();

        initData();

        // Operaciones CRUD

        selectAllUsers();

        selectUserById(2);

        User user = User.builder()
                .nombre("Nombre " + Instant.now().toString())
                .email("user" + Math.random() + "@mail.com")
                .password("1234")
                .fechaRegistro(LocalDate.now())
                .build();

        insertUser(user);

        user = User.builder()
                .id(6L)
                .nombre("Update " + Instant.now().toString())
                .email("update" + Math.random() + "@mail.com")
                .password("2345")
                .fechaRegistro(LocalDate.now())
                .build();

        updateUser(user);

        deleteUser(user);


    }

    private static void deleteUser(User user) {
        System.out.println("Eliminando usuario con id: " + user.getId());
        String query = "DELETE FROM user WHERE id = ?";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            int res = db.delete(query, user.getId());
            db.close();
            if (res > 0)
                System.out.println(user.toString());
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario" + e.getMessage());
        }
    }

    private static void updateUser(User user) {
        System.out.println("Actualizando usuario con id: " + user.getId());
        String query = "UPDATE user SET nombre = ?, email = ? WHERE id = ?";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            int res = db.update(query, user.getNombre(), user.getEmail(), user.getId());
            db.close();
            if (res > 0)
                System.out.println(user.toString());
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario" + e.getMessage());
        }

    }

    private static void insertUser(User user) {
        System.out.println("Insertando usuario");
        String query = "INSERT INTO user VALUES (null, ?, ?, ?, ?)";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            ResultSet res = db.insert(query, user.getNombre(), user.getEmail(),
                    user.getPassword(), user.getFechaRegistro()).orElseThrow(() -> new SQLException("Error insertar Usuario"));
            // Para obtener su ID
            if (res.first()) {
                user.setId(res.getLong(1));
                // una vez insertado comprobamos que esta correcto para devolverlo
                db.close();
                System.out.println(user.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar Usuario" + e.getMessage());
        }
    }

    private static void selectUserById(int id) {
        System.out.println("Obteniendo usuario con id: " + id);
        String query = "SELECT * FROM user WHERE id = ?";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            ResultSet result = db.select(query, id).orElseThrow(() -> new SQLException("Error al consultar usuario con ID " + id));
            if (result.first()) {
                User user = new User(
                        result.getLong("id"),
                        result.getString("nombre"),
                        result.getString("email"),
                        result.getString("password"),
                        result.getDate("fecha_registro").toLocalDate()
                );
                db.close();
                System.out.println(user.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario con id: " + id + " - " + e.getMessage());
        }
    }

    private static void selectAllUsers() {
        System.out.println("Obteniendo todos los usuarios");
        String query = "SELECT * FROM user";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            ResultSet result = db.select(query).orElseThrow(() -> new SQLException("Error al consultar registros de Usuarios"));
            ArrayList<User> list = new ArrayList<User>();
            while (result.next()) {
                list.add(
                        new User(
                                result.getLong("id"),
                                result.getString("nombre"),
                                result.getString("email"),
                                result.getString("password"),
                                result.getDate("fecha_registro").toLocalDate()
                        )
                );
            }
            db.close();
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }
    }

    private static void initData() {
        System.out.println("Iniciamos los datos");
        DataBaseController controller = DataBaseController.getInstance();
        String sqlFile = System.getProperty("user.dir") + File.separator + "sql" + File.separator + "blog.sql";
        System.out.println(sqlFile);
        try {
            controller.open();
            controller.initData(sqlFile);
            controller.close();
        } catch (SQLException e) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.getMessage());
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println("Error al leer el fichero de datos iniciales: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void checkServer() {
        System.out.println("Comprobamos la conexión al Servidor BD");
        DataBaseController controller = DataBaseController.getInstance();
        try {
            controller.open();
            Optional<ResultSet> rs = controller.select("SELECT 'Hello world'");
            if (rs.isPresent()) {
                rs.get().first();
                controller.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.getMessage());
            System.exit(1);
        }
    }
}
