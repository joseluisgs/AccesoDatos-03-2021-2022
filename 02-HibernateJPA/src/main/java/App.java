
import hibernate.Departamento;
import hibernate.Empleado;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        // Creamos las EntityManagerFactory para manejar las entidades y transacciones
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {

            // OPERACIONES CRUD

            // Listados
            System.out.println("Listar todos los Departamentos con consulta sobre la marcha");
            Query queryDepartamento = entityManager.createQuery("SELECT d FROM Departamento d");
            List<Departamento> departamentos = queryDepartamento.getResultList();
            departamentos.forEach(System.out::println);

            //System.out.println("Listar todos los Departamentos con consulta NamedQuery");
            //TypedQuery<Departamento> namedQueryDepartamento = entityManager.createNamedQuery("Departamento.finAll", Departamento.class);
            //departamentos = namedQueryDepartamento.getResultList();
            //departamentos.forEach(System.out::println);

            System.out.println("Buscar el Departamento 1");
            Departamento departamento = entityManager.find(Departamento.class, 1L);
            System.out.println(departamento.toString());

            System.out.println("Listar empleados del Departamento uno 1 usando la relación inversa");
            System.out.println("Cuidado con la navegabilidad");
            departamento.getEmpleados().forEach(System.out::println);

            System.out.println("Listar empleados del Departamento uno 1 usando NamedQuery parametrizada ");
            List<Empleado> empleados = entityManager.createNamedQuery("Empleado.porDepartamentoNamed", Empleado.class)
                    .setParameter(1, "TypeScript Departamento")
                    .getResultList();
            empleados.forEach(System.out::println);

            System.out.println("Listar empleados del Departamento uno 1 usando NativeQuery parametrizada ");
            empleados = entityManager.createNamedQuery("Empleado.porDepartamentoNative", Empleado.class)
                    .setParameter(1, "TypeScript Departamento")
                    .getResultList();

            empleados.forEach(System.out::println);

            transaction.begin();
            Departamento dep = new Departamento();
            dep.setNombre("Prueba " + LocalDateTime.now().toString());
            entityManager.persist(dep);
            transaction.commit();
            System.out.println(dep);

            // Insertando
            System.out.println("Insertando Empleado");
            transaction.begin();
            Empleado insert = new Empleado();
            // pedro.setId(6);
            insert.setNombre("Insert " + LocalDateTime.now().toString());
            insert.setApellidos(LocalDateTime.now().toString());
            insert.setDepartamento(departamento);
            entityManager.persist(insert);
            transaction.commit();
            System.out.println(insert);

            // Actualizando
            System.out.println("Actualizando Empleado 6");
            // Puedo buscarlo o usar inser
            Empleado update = entityManager.find(Empleado.class, 5L);
            update.setNombre("Update " + LocalDateTime.now().toString());
            update.setApellidos("Update " + LocalDateTime.now().toString());
            transaction.begin();
            entityManager.merge(update);
            transaction.commit();
            System.out.println(update);

            // Eliminando
            System.out.println("Eliminado Empleado 6");
            // Puedo buscarlo o usar inser
            transaction.begin();
            entityManager.remove(insert);
            transaction.commit();
            System.out.println(insert);
        } catch (Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}
