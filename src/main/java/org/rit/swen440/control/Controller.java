package org.rit.swen440.control;

import org.rit.swen440.dataLayer.Category;
import org.rit.swen440.dataLayer.Product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Controls access to data, on start-up scans directories and builds internal
 * representation of categories and items within each category.  Isolates the
 * categories and products from information on the underlying file system.
 */
public class Controller {
  private Path dirPath;

  public  enum PRODUCT_FIELD {
    NAME,
    DESCRIPTION,
    COST,
    INVENTORY
  };
  
  private final String jdbcDriverStr = "com.mysql.jdbc.Driver";
  private final String jdbcURL = "jdbc:mysql://localhost/javaTestDB?user=javauser&password=javapass";
  
  private Connection connection;
  private Statement statement;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;

  public Controller(String directory) {
    //loadCategories(directory);
  }

  /**
   * Get a list of all category names
   *
   * @return list of categories
   */
  public List<String> getCategories() {
	try {
		List<String> categories = new ArrayList<>();
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select * from category");
		while(resultSet.next()) {
			categories.add(resultSet.getString("Name"));
		}
		return categories;
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
      return null;
  }

  public List<String> getLogs() {
	try {
		List<String> categories = new ArrayList<>();
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select * from log inner join product on product.SKU_code = log.SKU_code");
		while(resultSet.next()) {
			categories.add(resultSet.getString("Title"));
			categories.add(resultSet.getString("quantity"));
			categories.add(resultSet.getString("Type"));
		}
		return categories;
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
      return null;
  }

  /**
   * Get the description of the named category
   * @param category name
   * @return description
   */
  public String getCategoryDescription(String category) {
    try {
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select * from category WHERE Name='" + category + "'");
		while(resultSet.next()) {
			return resultSet.getString("description");
		}
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
      return null;
  }

  /**
   * Return a list of Products based on the provided category.
   *
   * @param categoryName Name of Category to use
   * @return List of Products in the category
   */
  public List<String> getProducts(String categoryName) {
    try {
		ArrayList<String> products = new ArrayList<>();
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
		resultSet = statement.executeQuery("SELECT * FROM category inner join product on category.id = product.category where category.name = '" + categoryName + "'");
		while(resultSet.next()) {
			products.add(resultSet.getString("Title"));
		}
		return products;
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
	return null;
  }


  public String getProductInformation(String category, String product, PRODUCT_FIELD field) {
   switch (field) {
     case NAME:
	   try {
		connection = DriverManager.getConnection(jdbcURL);
		preparedStatement = connection.prepareStatement("select * from product WHERE Title=?");
		preparedStatement.setString(1, product);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("Title");
		}
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
     case DESCRIPTION:
         try {
		connection = DriverManager.getConnection(jdbcURL);
		preparedStatement = connection.prepareStatement("select * from product WHERE Title=?");
		preparedStatement.setString(1, product);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("description");
		}
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}

     case COST:
	   try {
		connection = DriverManager.getConnection(jdbcURL);
		preparedStatement = connection.prepareStatement("select * from product WHERE Title=?");
		preparedStatement.setString(1, product);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("cost");
		}
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}

     case INVENTORY:
       try {
		connection = DriverManager.getConnection(jdbcURL);
		preparedStatement = connection.prepareStatement("select * from product WHERE Title=?");
		preparedStatement.setString(1, product);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			return resultSet.getString("item_count");
		}
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
   }

   return null;
  }

  /**
   * Get the category that matches the provided category name
   *
   * @param name
   * @return Category, if present
   */
//  public Optional<Category> findCategory(String name) {
//    return categories.stream()
//        .filter(c -> c.getName().equalsIgnoreCase(name))
//        .findFirst();
//  }

  /**
   * Loop through all our categories and write any product records that
   * have been updated.
   */
//  public void writeCategories() {
//    for (Category category: categories) {
//      writeProducts(category.getProducts());
//    }
//  }

  /* -----------------------------------
   *
   * Private Methods
   */

  /**
   * Get the category object for this directory
   *
   * @param path directory
   * @return Category object, if .cat file exists
   */
  private Optional<Category> getCategory(Path path) {
    DirectoryStream.Filter<Path> catFilter = new DirectoryStream.Filter<Path>() {
      @Override
      public boolean accept(Path path) throws IOException {
        return path.toString().toLowerCase().endsWith("cat");
      }
    };

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, catFilter)) {
      for (Path file : stream) {
        // read the file
        BufferedReader reader = Files.newBufferedReader(file, Charset.forName("US-ASCII"));
        Category category = new Category();

        category.setName(reader.readLine());
        category.setDescription(reader.readLine());
        category.setProducts(loadProducts(path));

        return Optional.of(category);
      }
    } catch (IOException | DirectoryIteratorException e) {
      System.err.println(e);
    }

    return Optional.empty();
  }

//  private Optional<Product> getProduct(String category, String product) {
//    return findCategory(category).map(c -> c.findProduct(product)).orElse(null);
//  }

  /**
   * Parse a subdirectory and create a product object for each product within it
   *
   * @param path the subdirectory we're working in
   * @return a set of products
   */
  private Set<Product> loadProducts(Path path) {
    DirectoryStream.Filter<Path> productFilter = new DirectoryStream.Filter<Path>() {
      @Override
      public boolean accept(Path path) throws IOException {
        return !Files.isDirectory(path) && !path.toString().toLowerCase().endsWith("cat");
      }
    };

    Set<Product> products = new HashSet<>();

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, productFilter)) {
      for (Path productFile : stream) {
        // Read the product file
        try (BufferedReader reader = Files.newBufferedReader(productFile, Charset.forName("US-ASCII"))){
          Product product = new Product();
          product.setSkuCode(Integer.valueOf(reader.readLine()));
          product.setItemCount(Integer.valueOf(reader.readLine()));
          product.setThreshold(Integer.valueOf(reader.readLine()));
          product.setReorderAmount(Integer.valueOf(reader.readLine()));
          product.setTitle(reader.readLine());
          product.setDescription(reader.readLine());
          product.setCost(new BigDecimal(reader.readLine()));

          product.setPath(productFile);

          products.add(product);
        } catch (Exception e) {
          // Failed to read a product.  Log the error and continue
          System.err.println("Failed to read file: " + path.toString());
        }
      }
    } catch (IOException | DirectoryIteratorException e) {
      System.err.println(e);
    }

    return products;
  }

  /**
   * Loop through the set of products and write out any updated products
   *
   * @param products set of products
   */
  private void writeProducts(Set<Product> products) {
    for (Product product : products) {
      if (product.isUpdated()) {
        updateProduct(product);
      }
    }
  }

  /**
   * Write an updated product
   *
   * @param product the product
   */
  private void updateProduct(Product product) {
    try (BufferedWriter writer = Files.newBufferedWriter(product.getPath(), Charset.forName("US-ASCII"))){
      writer.write(String.valueOf(product.getSkuCode()));
      writer.newLine();
      writer.write(String.valueOf(product.getItemCount()));
      writer.newLine();
      writer.write(String.valueOf(product.getThreshold()));
      writer.newLine();
      writer.write(String.valueOf(product.getReorderAmount()));
      writer.newLine();
      writer.write(product.getTitle());
      writer.newLine();
      writer.write(product.getDescription());
      writer.newLine();
      writer.write(product.getCost().toString());
    } catch(IOException e) {
      System.err.println("Failed to write product file for:" + product.getTitle());
    }
  }
  
  private void close(){
	try {
	    if(resultSet!=null) resultSet.close();
	    if(statement!=null) statement.close();
	    if(connection!=null) connection.close();
	  } catch(Exception e){}
	}

  public void buyItems(int amount, String name){
	try {
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
		preparedStatement = connection.prepareStatement("update product set Item_Count = Item_Count - ? WHERE Title=?");
		preparedStatement.setInt(1, amount);
		preparedStatement.setString(2, name);
		preparedStatement.executeUpdate();
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
  }
  public void supplyItems(int amount, String name){
	try {
		connection = DriverManager.getConnection(jdbcURL);
		statement = connection.createStatement();
		preparedStatement = connection.prepareStatement("update product set Item_Count = Item_Count + ? WHERE Title=?");
		preparedStatement.setInt(1, amount);
		preparedStatement.setString(2, name);
		preparedStatement.executeUpdate();
	} catch(Exception e) {
		System.err.println(e);
	} finally {
		close();
	}
  }
    public void addProduct(int sku, int count, int threshold, int amount, String title, String description, double cost, int category) {
        try {
            connection = DriverManager.getConnection(jdbcURL);
            preparedStatement = connection.prepareStatement("INSERT INTO product VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, sku);
            preparedStatement.setInt(2,count);
            preparedStatement.setInt(3, threshold);
            preparedStatement.setInt(4, amount);
            preparedStatement.setString(5, title);
            preparedStatement.setString(6, description);
            preparedStatement.setDouble(7,cost);
            preparedStatement.setInt(8, category);
            preparedStatement.executeUpdate();
            logAction(sku, count, "Supplier", "supply");
        } catch(Exception e) {
            System.err.println(e);
        } finally {
            close();
        }
    }

    public void logAction(int sku, int quantity, String Utype, String type){
        try {
            connection = DriverManager.getConnection(jdbcURL);
            preparedStatement = connection.prepareStatement("INSERT INTO log VALUES (NULL, ?, ?, ?, ?, ?)");
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss");
            String dateS = ft.format(dNow);
            preparedStatement.setString(1, dateS);
            preparedStatement.setInt(2, sku);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, Utype);
            preparedStatement.setString(5, type);
            preparedStatement.executeUpdate();
        } catch(Exception e) {
            System.err.println(e);
        } finally {
            close();
        }
    }
}
