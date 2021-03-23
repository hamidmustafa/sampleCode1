
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.*;

public class Main {

    private static String productNameForCategory = "";
    private static String productNameForSales = "";
    private static String productCategory = "";
    private static String productSalePrice = "";
    private static String uniqueProductCategory = "";
    private static int productItemCounter = 0;
    private static double sumOfSales = 0.0;
    private static double averageSaleByCategory;
    private static BufferedReader productWithCategoryFileReader;
    private static BufferedReader productWithSalesFileReader;
    private static List<String> listOfProductsByUniqueCategory = new ArrayList<>();
    private static HashMap<String, Double> mapAverageSaleByCategory = new HashMap<>();
    private static List<Double> breakFastProductSalePriceList = new ArrayList<>();
    private static List<String> breakFastItemsList = new ArrayList<>();

    //Main method starts from here
    public static void main(String[] args) {
        try {
            for (int i = 0; i < getUniqueProductCategoryList().size(); i++) {
                productWithCategoryFileReader = new BufferedReader(new FileReader("products.tab"));
                String productAndCategories = productWithCategoryFileReader.readLine();
                while (productAndCategories != null && !productAndCategories.isEmpty()) {
                    String[] productAndCategorySplit = productAndCategories.split("\t");
                    productNameForCategory = productAndCategorySplit[0].trim();
                    productCategory = productAndCategorySplit[1].trim();
                    if (getUniqueProductCategoryList().get(i).equals("Breakfast") && productCategory.equals("Breakfast")) {
                        getBreakFastProductList(productNameForCategory);
                    }
                    if (productCategory.equals(getUniqueProductCategoryList().get(i)) && !listOfProductsByUniqueCategory.contains(productNameForCategory)) {
                        listOfProductsByUniqueCategory.add(productNameForCategory);
                    }
                    productAndCategories = productWithCategoryFileReader.readLine();
                }
                productWithCategoryFileReader.close();
                uniqueProductCategory = getUniqueProductCategoryList().get(i);

                productWithSalesFileReader = new BufferedReader(new FileReader("sales.tab"));
                String productAndSalePrice = productWithSalesFileReader.readLine();
                while (productAndSalePrice != null && !productAndSalePrice.isEmpty()) {
                    String[] productAndSalesSplit = productAndSalePrice.split("\t");
                    productNameForSales = productAndSalesSplit[0].trim();
                    productSalePrice = productAndSalesSplit[1].trim();
                    if (breakFastItemsList.contains(productNameForSales) && getUniqueProductCategoryList().get(i).equals("Breakfast")) {
                        getBreakFastSalesList(Double.parseDouble(productSalePrice));
                    }
                    if (listOfProductsByUniqueCategory.contains(productNameForSales)) {
                        productItemCounter++;
                        sumOfSales = sumOfSales + Double.parseDouble(productSalePrice);
                    }
                    productAndSalePrice = productWithSalesFileReader.readLine();
                }
                productWithSalesFileReader.close();

                averageSaleByCategory = findAverageOfSales(sumOfSales, productItemCounter);
                mapAverageSaleByCategory.put(uniqueProductCategory, averageSaleByCategory);
                // clearing list and resetting all these integer variables
                // to zero for next unique product Category iteration
                listOfProductsByUniqueCategory.clear();
                sumOfSales = 0.0;
                averageSaleByCategory = 0.0;
                productItemCounter = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double maxVal = getMinimumBreakFastSale();
        double minVal = getMaximumBreakFastSale();
        Collection collectionObj = mapAverageSaleByCategory.values();
        Collections collectionsObj = null;
        double highestAverageSale = (double) collectionsObj.max(collectionObj);
        String nameWithHighestAverageSale = getProductCategoryNameOfHighestAverageSale(highestAverageSale);
        //formatting sales' decimal values up to 2 decimal places.
        System.out.println("Maximum Sale in Category Breakfast is " + decimalNumberFormatter(maxVal));
        System.out.println("Minimum Sale in Category Breakfast is " + decimalNumberFormatter(minVal));
        System.out.println(decimalNumberFormatter(highestAverageSale) + " is the highest average sale price of product Category: " + nameWithHighestAverageSale);

    }

    //Getting all those product items from product list where category is 'Breakfast'
    private static List<String> getBreakFastProductList(String productItem) {

        breakFastItemsList.add(productItem);

        return breakFastItemsList;
    }

    // Getting list of all sales where category is 'Breakfast'
    private static List<Double> getBreakFastSalesList(double breakFastProductSale) {

        breakFastProductSalePriceList.add(breakFastProductSale);

        return breakFastProductSalePriceList;

    }

    // Getting minimum sale price value for category 'Breakfast'
    private static double getMinimumBreakFastSale() {

        double minVal = Collections.min(breakFastProductSalePriceList);

        return minVal;
    }

    // Getting maximum sale price value for category 'Breakfast'
    private static double getMaximumBreakFastSale() {

        double maxVal = Collections.max(breakFastProductSalePriceList);

        return maxVal;
    }

    // Getting category list with no repetition of same Category
    private static List<String> getUniqueProductCategoryList() {

        BufferedReader uniqueProductCategoryReader;
        List<String> productCategoryList = new ArrayList<>();
        try {
            uniqueProductCategoryReader = new BufferedReader(new FileReader("products.tab"));
            String productName_ = uniqueProductCategoryReader.readLine();
            while (productName_ != null && !productName_.isEmpty()) {
                String[] productNameAndCategorySplitter = productName_.split("\t");
                String productCategory = productNameAndCategorySplitter[1].trim();
                if (!productCategoryList.contains(productCategory)) {
                    productCategoryList.add(productCategory);
                }
                productName_ = uniqueProductCategoryReader.readLine();
            }
            uniqueProductCategoryReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productCategoryList;
    }

    // A utility method to find average of required sales
    private static double findAverageOfSales(Double sumOfSales, int sizeOfList) {

        double average = 0;
        average = sumOfSales / sizeOfList;

        return average;
    }

    // Getting highest average sale value
    private static String getProductCategoryNameOfHighestAverageSale(Double highestAverageSale) {

        String productCategoryNameOfHighestSale = "";
        for (Map.Entry<String, Double> entry : mapAverageSaleByCategory.entrySet()) {
            if (entry.getValue().equals(highestAverageSale)) {
                productCategoryNameOfHighestSale = entry.getKey();
            }
        }

        return productCategoryNameOfHighestSale;
    }

    // A utility method for formatting decimal number up to two places
    private static Double decimalNumberFormatter(double inputValue) {

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        String formattedValue = decimalFormat.format(inputValue);

        return Double.parseDouble(formattedValue);
    }

}