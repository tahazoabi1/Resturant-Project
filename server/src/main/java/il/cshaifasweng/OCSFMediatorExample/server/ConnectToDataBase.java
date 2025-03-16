package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ConnectToDataBase {
    private static Session session;
    private static List<MenuItem> menuItems;
    private static SessionFactory sessionFactory = null;

    public ConnectToDataBase() {
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            configuration.addAnnotatedClass(MenuItem.class);
            configuration.addAnnotatedClass(Branch.class);
            configuration.addAnnotatedClass(Complaint.class);
            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(Dietitian.class);
            configuration.addAnnotatedClass(Hostess.class);
            configuration.addAnnotatedClass(HostingArea.class);
            configuration.addAnnotatedClass(Manager.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(Report.class);
            configuration.addAnnotatedClass(Request.class);
            configuration.addAnnotatedClass(ReservationReport.class);
            configuration.addAnnotatedClass(ServiceWorker.class);
            configuration.addAnnotatedClass(Tables.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Worker.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    public static void addBranch(String name, String location, String description, String imageUrl) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Create a new Branch object
            Branch branch = new Branch(name, location, description, imageUrl);

            // Save the branch
            session.save(branch);

            transaction.commit();
            System.out.println("Branch added successfully: " + name);

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error adding branch: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }


    public static List<Branch> getAllBranches() throws Exception {
        System.out.println("Getting all branches from database...");
        Session session = null;
        List<Branch> branches;

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Branch> query = builder.createQuery(Branch.class);
            Root<Branch> root = query.from(Branch.class);
            query.select(root);

            branches = session.createQuery(query).getResultList();
            System.out.println("Found " + (branches != null ? branches.size() : 0) + " branches");

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in getAllBranches: " + e.getMessage());
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return branches;
    }

    public static List<MenuItem> getAllMenuItems() throws Exception {
        System.out.println("Getting all menu items from database...");
        Session session = null;
        List<MenuItem> items;

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MenuItem> query = builder.createQuery(MenuItem.class);
            Root<MenuItem> root = query.from(MenuItem.class);
            query.select(root);

            items = session.createQuery(query).getResultList();
            System.out.println("Found " + (items != null ? items.size() : 0) + " items");

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in getAllMenuItems: " + e.getMessage());
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return items;
    }


    public static void updateMenuItemPrice(int id, double newPrice) throws Exception {
        System.out.println("Updating price for item ID: " + id);
        Session session = null;

        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();

            MenuItem item = session.get(MenuItem.class, id);
            if (item != null) {
                item.setPrice(newPrice);
                session.update(item);
                session.getTransaction().commit();
                System.out.println("Price updated successfully");
            } else {
                System.out.println("MenuItem with ID " + id + " not found");
                if (session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating price: " + e.getMessage());
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static void updatePrice(String name, double price) throws Exception {
        System.out.println("Updating price for item name: " + name);
        Session session = null;

        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MenuItem> query = builder.createQuery(MenuItem.class);
            Root<MenuItem> root = query.from(MenuItem.class);
            query.select(root).where(builder.equal(root.get("name"), name));

            List<MenuItem> menuItems = session.createQuery(query).getResultList();
            if (!menuItems.isEmpty()) {
                MenuItem item = menuItems.get(0);
                item.setPrice(price);
                session.saveOrUpdate(item);
                session.getTransaction().commit();
                System.out.println("Price updated successfully for: " + name);
            } else {
                System.out.println("MenuItem with name " + name + " not found");
                if (session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating price: " + e.getMessage());
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public static void addWorker(String name, String phoneNumber, String email, String password, double salary, Branch branch) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Create new worker object
            Worker worker = new Worker(salary, branch, name, phoneNumber, email, password);

            // Save worker to the users table (due to SINGLE_TABLE inheritance)
            session.save(worker);

            transaction.commit();
            System.out.println("Worker added successfully!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error adding worker: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    public static Branch getBranchByName(String branchName) {
        Session session = null;
        Branch branch = null;

        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();

            branch = (Branch) session.createQuery("FROM Branch WHERE name = :branchName")
                    .setParameter("branchName", branchName)
                    .uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error finding branch: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return branch;
    }


    public static void initializeDatabase() throws HibernateException {
        Session session = null;
        Transaction transaction = null;
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            transaction.commit();  // Commit changes to make sure they are applied
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();  // Ensure rollback on error
            throw new HibernateException("Failed to initialize database", e);
        } finally {
            if (session != null && session.isOpen()) session.close();  // Ensure session is closed
        }
    }

    public static void closeSession() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public static User Login(String email, String password) {
        Session session = null;
        Transaction transaction = null;
        User user = null;
        try {
            // Get the session from the session factory
            session = getSessionFactory().openSession();

            // Start a transaction
            transaction = session.beginTransaction();

            // Query to find the user by email and password
            String hql = "FROM User u WHERE u.email = :email AND u.password = :password";
            user = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();

            if (user != null) {
                // User found, return user information (or process as needed)
                System.out.println("Login successful! Welcome, " + user.getName());
                user.signIn();
            } else {
                // No user found with the provided email and password
                System.out.println("Error: No user found with the provided email and password.");
            }

            // Commit the transaction if everything goes well
            transaction.commit();
        } catch (Exception e) {
            // Rollback the transaction in case of an error
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Close the session to release resources
            if (session != null) {
                session.close();
            }
        }
        return user;
    }
}