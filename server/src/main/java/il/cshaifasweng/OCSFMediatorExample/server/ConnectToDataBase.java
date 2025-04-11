package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ConnectToDataBase {
    private static Session session;
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
            configuration.addAnnotatedClass(Manager.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(Report.class);
            configuration.addAnnotatedClass(Request.class);
            configuration.addAnnotatedClass(Reservation.class);
            configuration.addAnnotatedClass(ServiceWorker.class);
            configuration.addAnnotatedClass(Tables.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Worker.class);
            configuration.addAnnotatedClass(Request.class);
            configuration.addAnnotatedClass(NetworkManager.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
    public static void addBranch(String name, String location, String description, String imageUrl,String openingHours) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Create a new Branch object
            Branch branch = new Branch(name, location, description, imageUrl,openingHours);

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

    public static void addCustomer(String name, String phoneNumber, String address, String email, String password, String preferredPaymentMethod) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Ensure columns match the correct order in the users table
            Customer customer = new Customer(name, phoneNumber, address, email, password, preferredPaymentMethod);

            session.save(customer); // Save to users table

            transaction.commit();
            System.out.println("Customer added successfully!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error adding customer: " + e.getMessage());
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

    public static MenuItem getMenuItemByName(String name) {
        Session session = null;
        MenuItem menuItem = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();

            Query<MenuItem> query = session.createQuery("FROM MenuItem WHERE name = :name", MenuItem.class);
            query.setParameter("name", name);
            menuItem = query.uniqueResult();

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error fetching menu item: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return menuItem;
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
    public static void updateServeTable(int id) throws Exception {
        System.out.println("Updating serve status for table ID: " + id);
        Session session = null;

        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();

            Tables table = session.get(Tables.class, id);
            if (table != null) {
                table.setReserved();
                session.update(table);
                session.getTransaction().commit();
                System.out.println("Serve update successfully");
            } else {
                System.out.println("table with ID " + id + " not found");
                if (session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating table: " + e.getMessage());
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

    public static int createOrder(int branchId, double totalPrice, int customerId) {
        int orderId = -1;
        Transaction transaction = null;

        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            System.out.println("✅ Creating order: branchId = " + branchId + ", price = " + totalPrice + ", customerId = " + customerId);

            // ✅ Correct SQL syntax
            String sql = "INSERT INTO orders (branch_id, total_price, is_accepted, status, customer_id) VALUES (?, ?, ?, ?, ?)";
            SQLQuery query = session.createSQLQuery(sql);

            // ✅ Set parameters (Starting from 1)
            query.setParameter(1, branchId);            // Branch ID
            query.setParameter(2, totalPrice);          // Total Price
            query.setParameter(3, (byte) 0);            // ✅ Use (byte) 0 for is_accepted
            query.setParameter(4, "Pending");           // Status
            query.setParameter(5, customerId);          // Customer ID

            int result = query.executeUpdate(); // EXECUTE THE INSERT COMMAND

            System.out.println("✅ Query executed, affected rows: " + result);

            if (result > 0) {
                // ✅ Retrieve the last inserted ID
                Query resultIdQuery = session.createSQLQuery("SELECT LAST_INSERT_ID()");
                orderId = ((Number) resultIdQuery.uniqueResult()).intValue();
                System.out.println("✅ Order created successfully with ID: " + orderId);
            } else {
                System.err.println("❌ Order was not inserted into the database.");
            }

            transaction.commit();  // COMMIT THE TRANSACTION TO MAKE CHANGES PERMANENT

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Error inserting order: " + e.getMessage());
            e.printStackTrace();
        }
        return orderId;
    }




    public static void insertItemIntoOrder(int orderId, int menuItemId) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            String sql = "INSERT INTO order_menu_items (order_id, menu_item_id) VALUES (?, ?)";
            Query query = session.createSQLQuery(sql);
            query.setParameter(1, orderId);
            query.setParameter(2, menuItemId);
            query.executeUpdate();

            transaction.commit();
            System.out.println("✅ Item inserted into order ID: " + orderId + ", MenuItem ID: " + menuItemId);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Error inserting item into order: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }


    public static void updateOrderTotal(int orderId, double newTotal) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // ✅ Use native SQL with positional parameters (?)
            String sql = "UPDATE orders SET total_price = ? WHERE id = ?";
            Query query = session.createSQLQuery(sql);
            query.setParameter(1, newTotal);
            query.setParameter(2, orderId);
            query.executeUpdate();

            transaction.commit();
            System.out.println("✅ Order total updated to: " + newTotal);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Error updating order total: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }





    public static void insertItemIntoOrderWithDetails(int orderId, int menuItemId, int quantity, String preferences) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            String sql = "INSERT INTO order_menu_items (order_id, menu_item_id, quantity, preferences) VALUES (?, ?, ?, ?)";
            Query query = session.createSQLQuery(sql);
            query.setParameter(1, orderId);
            query.setParameter(2, menuItemId);
            query.setParameter(3, quantity);
            query.setParameter(4, preferences);
            query.executeUpdate();

            transaction.commit();
            System.out.println("✅ Item inserted into order ID: " + orderId + ", MenuItem ID: " + menuItemId);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Error inserting item into order: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }


    public static void insertOrderItems(int orderId, String itemsWithPreferences) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            String sql = "INSERT INTO order_menu_items (order_id, menu_item_id, preferences) VALUES (?, ?, ?)";
            Query query = session.createSQLQuery(sql);

            // נניח שהפורמט של itemsWithPreferences הוא כך: "{מנה1=העדפה1, מנה2=העדפה2}"
            String[] items = itemsWithPreferences.substring(1, itemsWithPreferences.length() - 1).split(", ");

            for (String item : items) {
                String[] keyValue = item.split("=");
                String itemName = keyValue[0];
                String preference = keyValue[1];

                MenuItem menuItem = getMenuItemByName(itemName);
                if (menuItem != null) {
                    query.setParameter(1, orderId);
                    query.setParameter(2, menuItem.getId());
                    query.setParameter(3, preference);
                    query.executeUpdate();
                }
            }

            transaction.commit();
            System.out.println("✅ Order items saved successfully for Order ID: " + orderId);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Error saving order items: " + e.getMessage());
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

            System.out.println("📌 Fetching branch: " + branchName);  // ✅ Debugging line

            branch = (Branch) session.createQuery("FROM Branch WHERE name = :branchName")
                    .setParameter("branchName", branchName)
                    .uniqueResult();

            session.getTransaction().commit();
            if (branch != null) {
                System.out.println("✅ Found branch: " + branch.getName());
            } else {
                System.out.println("❌ No branch found with name: " + branchName);
            }
        } catch (Exception e) {
            System.err.println("❌ Error finding branch: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        return branch;
    }


    public static List<MenuItem> getMenuItemsByBranch(String branchName) {
        Session session = null;
        List<MenuItem> items = null;

        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();

            Query<MenuItem> query = session.createQuery(
                    "SELECT DISTINCT mi FROM MenuItem mi JOIN mi.branches b WHERE b.name = :branchName",
                    MenuItem.class
            );
            query.setParameter("branchName", branchName);
            items = query.getResultList();

            session.getTransaction().commit();
            System.out.println("Found " + (items != null ? items.size() : 0) + " items for branch " + branchName);
        } catch (Exception e) {
            System.err.println("Error retrieving menu items: " + e.getMessage());
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

    public static void addMenuItem(String name, String ingredients, String preferences, Double price,
                                   Boolean IsChainAvailable, Boolean IsDeliveryAvailable,
                                   String branchesStr) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            MenuItem menuitem = new MenuItem(name, ingredients, preferences, price, IsDeliveryAvailable, IsChainAvailable);

            if (branchesStr != null && !branchesStr.isEmpty()) {
                String[] branchIds = branchesStr.split("@");
                for (String idStr : branchIds) {
                    try {
                        int branchId = Integer.parseInt(idStr.trim());
                        Branch branch = session.get(Branch.class, branchId);
                        if (branch != null) {
                            menuitem.getBranches().add(branch);
                            branch.getItems().add(menuitem);
                        } else {
                            System.err.println("⚠️ Branch not found with ID: " + branchId);
                        }
                        session.save(branch);
                    } catch (NumberFormatException e) {
                        System.err.println("⚠️ Invalid branch ID: " + idStr);
                    }
                }
            }

            session.save(menuitem);
            transaction.commit();
            System.out.println("✅ Dish with branches added successfully!");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Error adding dish: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
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

    public static void removeItem(int id) {
        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        MenuItem itemToRemove = session.get(MenuItem.class, id);

        if (itemToRemove != null) {
            // Manually disassociate the item from each branch
            for (Branch branch : itemToRemove.getBranches()) {
                branch.getItems().remove(itemToRemove);
            }
            // Remove the item itself
            session.remove(itemToRemove);
        }

        tx.commit();
        session.close();
    }

    public static User LogOut(String email, String password) {
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
                // User found, proceed to log them out by setting is_signed_in to false
                user.signOut(); // Mark the user as logged out
                session.update(user); // Update the user in the database
                System.out.println("Logout successful for user: " + email);
            } else {
                System.out.println("Error: No user found with the provided email and password.");
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
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
                // User found, check if they are already signed in
                if (user.isSignedIn()) {
                    // User is already signed in
                    System.out.println("Cannot log in. The user is already logged in.");
                } else {
                    // User is not signed in, proceed with login
                    user.signIn();  // Set signedIn flag to true
                    session.update(user);  // Update user in the database
                    session.flush();  // Ensure the update is committed to the database

                    // Re-fetch the user to make sure we have the latest state
                    user = session.get(User.class, user.getId());  // Or use the appropriate identifier

                    System.out.println("Login successful! Welcome, " + user.getName());
                    return user;
                }
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
        return null;
    }

    public static void updateIngredients(int itemId, String newIngredients) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            // Retrieve the MenuItem object by ID
            MenuItem itemToUpdate = session.get(MenuItem.class, itemId);

            if (itemToUpdate != null) {
                // Update the ingredients
                itemToUpdate.setIngredients(newIngredients);

                // Save the changes to the database
                session.update(itemToUpdate);

                // Commit the transaction
                tx.commit();

                System.out.println("Ingredients for item ID " + itemId + " updated successfully.");
            } else {
                System.err.println("Menu item with ID " + itemId + " not found.");
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();  // Rollback in case of error
            }
            e.printStackTrace();
        } finally {
            session.close();  // Ensure the session is closed even in case of error
        }
    }

    public static void updateTypeItem(int itemId) {
        session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        // Fetch the menu item by ID
        MenuItem itemToUpdate = session.get(MenuItem.class, itemId);

        if (itemToUpdate != null) {
            // Toggle the chainDish value
            itemToUpdate.setChainDish(!itemToUpdate.getChainDish());
            session.update(itemToUpdate);
        }

        tx.commit();
        session.close();
    }
}