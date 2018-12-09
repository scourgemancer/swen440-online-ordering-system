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
      if (Objects.equals(s,"q")){
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
        System.out.println("\nThe following categories are availableries");
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
        System.out.println("The following supplier items are available");
        m.printMenu();
        String result = m.getSelection();
        try
        {
            int iSel = Integer.parseInt(result);//Item  selected
            currentItemName = itemList.get(iSel);
            //currentItem = itemList.get(iSel);
            //Now read the file and print the org.rit.swen440.presentation.items in the catalog
            System.out.println("You want item from the catalog: " + currentItemName);
        }
        catch (Exception e)
        {
            result = "q";
        }
        if (result == "q"){
          currentLevel-=2;
        }
        else
        {
            //currentLevel++;//Or keep at same level?
            OrderQty(currentCategoryName, currentItemName);
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

            System.out.println("You want item from the catalog: " + currentItemName);
        }
        catch (Exception e)
        {
            result = "q";
        }
        if (result == "q")
            currentLevel-=2;
        else
        {
            OrderQty(currentCategoryName, currentItemName);
        }
    }


    public void OrderQty(String category, String item)
    {
        System.out.println("Please select a quantity");
        System.out.println(controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.NAME) +
                " availability:" + controller.getProductInformation(category, item, Controller.PRODUCT_FIELD.INVENTORY));
        System.out.print(":");
        menu m = new menu();
        String result = m.getSelection();
        System.out.println("You ordered:" + result);
    }
}
