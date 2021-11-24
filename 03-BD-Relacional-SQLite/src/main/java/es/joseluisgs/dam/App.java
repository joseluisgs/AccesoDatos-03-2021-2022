package es.joseluisgs.dam;

import es.joseluisgs.dam.database.DataBaseController;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        checkServer();

        initData();

        System.out.println("Opbteniendo empleados");
        DataBaseController controller = DataBaseController.getInstance();
        try {
            controller.open();
            Optional<ResultSet> rs = controller.select("SELECT * FROM empleado");
            if (rs.isPresent()) {
                while (rs.get().next()) {
                    // El mapeo ya lo haces tú que lo has visto...
                    System.out.println(rs.get().getString("nombre"));

                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar empleados: " + e.getMessage());
        }

    }

        // Operaciones CRUD

    private static void initData() {
        System.out.println("Iniciamos los datos");
        DataBaseController controller = DataBaseController.getInstance();
        String sqlFile = System.getProperty("user.dir") + File.separator + "sql" + File.separator + "mydb.sql";
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
                rs.get().next();
                controller.close();
                System.out.println("Conexión correcta");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.getMessage());
            System.exit(1);
        }
    }
}
