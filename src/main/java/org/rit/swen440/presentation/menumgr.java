package org.rit.swen440.presentation;

import org.rit.swen440.control.Controller;
import org.rit.swen440.dataLayer.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Objects;
import java.util.Optional;


public class menumgr
{
    int currentLevel = 0;
    String currentCategoryName;
    String currentItemName;
    category currentCategory;
    item currentItem;
    private Controller controller;

    public menumgr()
    {
        controller = new Controller(System.getProperty("fileSystemRoot"));

    }

    public boolean loadLevel(int level)
    {
       // System.out.println("Loading level:" + currentLevel);
       System.out.println("\n -------------------------------\n");
        switch (currentLevel)
        {
            case -1:
                return true;
            case 0:
                UserMenu();
                break;
            case 1:
                PasswordMenu();
                break;
            case 2:
                SupplierMenu();
                break;
            case 3:
                CustomerMenu();
                break;
            case 4:
                SupplierProductMenu();
                break;
            case 5:
                CustomerProductMenu();
                break;
            default:
                System.out.println("Returning to main org.rit.swen440.presentation.menu");
                currentLevel = 0;
                UserMenu();
                break;
        }

        return false;
    }

    public void UserMenu()
    {
      menu m = new menu();
      List<String> l = new ArrayList<>();
      m.loadMenu(l);
      m.addMenuItem("Supplier");
      m.addMenuItem("Customer");
      m.addMenuItem("'q' to Quit");
      System.out.println("Are you a customer or a supplier?");
      m.printMenu();
      String result = "0";
      try
      {
          result = m.getSelection();
      }
      catch (Exception e)
      {
          result = "q";
      }
      if (Objects.equals(result,"q"))
      {
          currentLevel--;
      }
      if (Objects.equals(result,"0"))
      {
          System.out.println("\nYour Selection was: Supplier");
      }
      if (Objects.equals(result,"1"))
      {
          currentLevel+=3;
          System.out.println("\nYour Selection was: Customer");
      }
      else
      {
          currentLevel++;

          int iSel = Integer.parseInt(result);
          System.out.println("\nYour Selection was: " + result);

      }
    }

    public void PasswordMenu()
    {
      Scanner reader = new Scanner(System.in);

      System.out.println("\nEnter your password (press 'q' to quit)");

      String s = reader.next();

      if (Objects.equals(s,"1234")){
        System.out.println("\nAuthenticated!");
        currentLevel++;
      }
      else if (Objects.equals(s,"q")){
        currentLevel--;
      }
      else {
        System.out.println("\nYour password was incorrect");
      }

    }

    public void SupplierMenu()
    {
        menu m = new menu();
        List<String> categories = controller.getCategories();
        m.loadMenu(categories);
        m.addMenuItem("'q' to Quit");
        System.out.println("These are your available supplier categories");
        m.printMenu();
        System.out.println("Type 'new category' if you would you like to add a new category");
        String result = "0";
        try
        {
            result = m.getSelection();
        }
        catch (Exception e)
        {
            result = "q";
        }
        if (Objects.equals(result,"q"))
        {
            currentLevel-=2;
        }
        else if (Objects.equals(result,"new category")){
            System.out.println("Please enter the following category details");
            System.out.println("Name: ");
            String name = m.getSelection();
            System.out.println("Description: ");
            String description = m.getSelection();
            //call a controller funtion to insert into category table
            //make sure to normalize category into foreign key
            controller.addCategory(name, description);
        }
        else
        {
            currentLevel+=2;
            int iSel = Integer.parseInt(result);

            currentCategoryName = categories.get(iSel);
            System.out.println("\nYour Selection was:" + currentCategoryName);
        }
    }

    public void CustomerMenu()
    {
        menu m = new menu();
        List<String> categories = controller.getCategories();
        m.loadMenu(categories);
        m.addMenuItem("'q' to Quit");
        System.out.println("\nThe following categories are available");
        m.printMenu();
        String result = "0";
        try
        {
            result = m.getSelection();
        }
        catch (Exception e)
        {
            result = "q";
        }
        if (Objects.equals(result,"q"))
        {
            currentLevel-=3;
        }
        else
        {
            currentLevel+=2;
            int iSel = Integer.parseInt(result);

            currentCategoryName = categories.get(iSel);
            System.out.println("\nYour Selection was:" + currentCategoryName);
        }
    }

    public void SupplierProductMenu()
    {
        menu m = new menu();

        //items it = new items("orderSys/" + currentCategory.getName());

        // List<item> itemList = controller.getProducts(currentCategoryName);
        List<String> itemList = controller.getProducts(currentCategoryName);
        List<String> l = new ArrayList<>();
        System.out.println("");
        for (String itm: itemList)
            l.add(controller.getProductInformation(currentCategoryName, itm, Controller.PRODUCT_FIELD.NAME)
             + "($" + controller.getProductInformation(currentCategoryName, itm, Controller.PRODUCT_FIELD.COST) + ")");

        m.loadMenu(l);
        m.addMenuItem("'q' to quit");
        System.out.println("The following supplier items are available for update");
        m.printMenu();
		    System.out.println("Type 'log' to view product logs");
        System.out.println("Type 'new product' if you would you like to add a new product");
        String result = m.getSelection();
        try
        {
            if (!result.equals("q") && !result.equals("new product")) {
                int iSel = Integer.parseInt(result);//Item  selected
                currentItemName = itemList.get(iSel);
                //currentItem = itemList.get(iSel);
                //Now read the file and print the org.rit.swen440.presentation.items in the catalog
                System.out.println("You've selected: " + currentItemName);
            }
        }
        catch (Exception e)
        {
            //result = "q";
        }
        if (result.equals("q")){
          currentLevel-=2;
        }
        else if (result.equals("new product")){
            System.out.println("Please enter the following item details");
            System.out.println("Title: ");
            String title = m.getSelection();
            System.out.println("Description: ");
            String description = m.getSelection();
            System.out.println("SKU Code: ");
            int sku = Integer.parseInt(m.getSelection());
            System.out.println("Cost :");
            double cost = Double.parseDouble(m.getSelection());
            System.out.println("Item Count: ");
            int count = Integer.parseInt(m.getSelection());
            System.out.println("Reorder Threshold: ");
            int threshold = Integer.parseInt(m.getSelection());
            System.out.println("Reorder Amount: ");
            int amount = Integer.parseInt(m.getSelection());
            System.out.println("Please enter an available category from the following list: ");
            List<String> categories = controller.getCategories();
            for(int i = 0; i < categories.size(); i++) {
                System.out.println(categories.get(i));
            }
            System.out.println("Category: ");
            String category = m.getSelection();
            //call a controller funtion to insert into product table
            //make sure to normalize category into foreign key
            controller.addProduct(sku, count, threshold, amount, title, description, cost, category);
        }
		else if (result.equals("log")){
			List<String> logs = controller.getLogs();
			System.out.println();
			int i = 0;
			while(i < logs.size()){
				System.out.println("Product: " + logs.get(i) + " Amount: " + logs.get(i+1) + " Type: " + logs.get(i+2));
				i = i + 3;
			}
			System.out.println("");
			System.out.println("Press Enter to continue");
			m.getSelection();
		}
        else
        {
            //currentLevel++;//Or keep at same level?
            OrderQty(currentCategoryName, currentItemName, true);
        }
    }

    public void CustomerProductMenu()
    {
        menu m = new menu();


        List<String> itemList = controller.getProducts(currentCategoryName);
        List<String> l = new ArrayList<>();
        System.out.println("");
        for (String itm: itemList)
            l.add(controller.getProductInformation(currentCategoryName, itm, Controller.PRODUCT_FIELD.NAME)
             + "($" + controller.getProductInformation(currentCategoryName, itm, Controller.PRODUCT_FIELD.COST) + ")");

        m.loadMenu(l);
        m.addMenuItem("'q' to quit");
        System.out.println("The following items are available");
        m.printMenu();
        String result = m.getSelection();
        try
        {
            int iSel = Integer.parseInt(result);//Item  selected
            currentItemName = itemList.get(iSel);

            System.out.println("You've selected: " + currentItemName);
        }
        catch (Exception e)
        {
            result = "q";
        }
        if (result == "q")
            currentLevel-=2;
        else
        {
            OrderQty(currentCategoryName, currentItemName, false);
        }
    }


    public void OrderQty(String category, String item, boolean isSupplier)
    {
		if(isSupplier){
			System.out.println("Enter amount to add...");
		}
		else{
			System.out.println("Enter amount to buy...");
		}
        System.out.println(controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.NAME) +
                " availability: " + controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.INVENTORY));
        System.out.print(": ");
        menu m = new menu();
        String result = m.getSelection();
        //Boolean didBuy = false;
		try{
			if(isSupplier){
				controller.supplyItems(Integer.parseInt(result), controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.NAME));
                System.out.println("You added: " + result);
			}
			else {
                int stock = Integer.parseInt(controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.INVENTORY));
                if (stock - Integer.parseInt(result) < 0) {
                    System.out.println("Sorry that request exceeds available inventory.");
                } else {
                    controller.buyItems(Integer.parseInt(result), controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.NAME));
                    System.out.println("You ordered: " + result);
                }
            }
		}
		catch(NumberFormatException e){
			System.out.println("Please insert a number");
			return ;
		}

    }
}
